
package fi.soveltia.liferay.aitasks.internal.task.request;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.MapUtil;

import fi.soveltia.liferay.aitasks.internal.task.node.condition.AITaskNodeConditionEvaluationResponse;
import fi.soveltia.liferay.aitasks.internal.task.node.condition.AITaskNodeConditionEvaluator;
import fi.soveltia.liferay.aitasks.internal.task.node.type.TriggerAITaskNode;
import fi.soveltia.liferay.aitasks.model.AITask;
import fi.soveltia.liferay.aitasks.rest.dto.v1_0.Configuration;
import fi.soveltia.liferay.aitasks.rest.dto.v1_0.Edge;
import fi.soveltia.liferay.aitasks.rest.dto.v1_0.Node;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeResponse;
import fi.soveltia.liferay.aitasks.task.request.AITaskRequest;
import fi.soveltia.liferay.aitasks.task.request.AITaskRequestExecutor;
import fi.soveltia.liferay.aitasks.task.response.AITaskResponse;
import fi.soveltia.liferay.aitasks.task.response.AITaskResponseBuilder;
import fi.soveltia.liferay.aitasks.task.response.AITaskResponseBuilderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(service = AITaskRequestExecutor.class)
public class AITaskRequestExecutorImpl implements AITaskRequestExecutor {

	public AITaskResponse execute(AITaskRequest aiTaskRequest) {
		AITask aiTask = aiTaskRequest.getAITask();

		if (!aiTask.getEnabled()) {
			return _aiTaskResponseBuilderFactory.builder(
			).addOutput(
				"error",
				StringBundler.concat(
					"The task ", aiTask.getExternalReferenceCode(),
					" is not enabled")
			).build();
		}

		Configuration configuration = Configuration.toDTO(
			aiTask.getConfigurationJSON());

		Map<String, TriggerAITaskNode> triggerAITaskNodes =
			_getTriggerAITaskNodes(configuration);

		if (triggerAITaskNodes.isEmpty()) {
			return _aiTaskResponseBuilderFactory.builder(
			).addOutput(
				"error",
				"No trigger nodes defined. Please check the configuration"
			).build();
		}

		return _executeTriggerNodes(
			aiTaskRequest, configuration, triggerAITaskNodes);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, AITaskNode.class, "ai.task.node.type");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private void _addExecutionTime(
		AITaskNodeResponse aiTaskNodeResponse, long startTime) {

		if (aiTaskNodeResponse == null) {
			return;
		}

		Map<String, Object> executionTrace =
			aiTaskNodeResponse.getExecutionTrace();

		executionTrace.put(
			"executionTime", (System.currentTimeMillis() - startTime) + "ms");
	}

	private void _addExecutionTrace(
		AITaskNodeResponse aiTaskNodeResponse,
		AITaskResponseBuilder aiTaskResponseBuilder, String id) {

		if (aiTaskNodeResponse == null) {
			return;
		}

		aiTaskResponseBuilder.addExecutionTrace(
			aiTaskNodeResponse.getExecutionTrace(), id);
	}

	private void _addOutput(
		AITaskNodeResponse aiTaskNodeResponse,
		AITaskResponseBuilder aiTaskResponseBuilder) {

		if (aiTaskNodeResponse == null) {
			return;
		}

		MapUtil.isNotEmptyForEach(
			aiTaskNodeResponse.getOutput(),
			(k, v) -> aiTaskResponseBuilder.addOutput(k, v));
	}

	private boolean _evaluateCondition(
		AITaskContext aiTaskContext,
		AITaskResponseBuilder aiTaskResponseBuilder, Node node, boolean trace) {

		if (node.getCondition() == null) {
			return true;
		}

		AITaskNodeConditionEvaluator aiTaskNodeConditionEvaluator =
			new AITaskNodeConditionEvaluator(aiTaskContext, trace);

		AITaskNodeConditionEvaluationResponse
			aiTaskNodeConditionEvaluationResponse =
				aiTaskNodeConditionEvaluator.evaluate(node.getCondition());

		if (trace) {
			aiTaskResponseBuilder.addExecutionTrace(
				HashMapBuilder.<String, Object>put(
					"condition",
					aiTaskNodeConditionEvaluationResponse.getExecutionTrace()
				).build(),
				node.getId());
		}

		return aiTaskNodeConditionEvaluationResponse.isValid();
	}

	private void _executeNode(
		AITaskContext aiTaskContext,
		AITaskResponseBuilder aiTaskResponseBuilder,
		Configuration configuration, Node node) {

		AITaskNode aiTaskNode = _serviceTrackerMap.getService(node.getType());

		if (aiTaskNode == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"AI task node of type " + node.getType() +
						" could not be found");
			}

			return;
		}

		long startTime = System.currentTimeMillis();

		AITaskNodeResponse aiTaskNodeResponse = aiTaskNode.execute(
			aiTaskContext,
			_jsonFactory.createJSONObject((Map)node.getParameters()),
			node.getId(), configuration.getTrace());

		if (configuration.getTrace()) {
			_addExecutionTrace(
				aiTaskNodeResponse, aiTaskResponseBuilder, node.getId());
			_addExecutionTime(aiTaskNodeResponse, startTime);
		}

		_addOutput(aiTaskNodeResponse, aiTaskResponseBuilder);

		List<Node> nextNodes = _getNextNodes(configuration, node.getId());

		if (nextNodes.isEmpty()) {
			return;
		}

		for (Node nextNode : nextNodes) {
			if (!_evaluateCondition(
					aiTaskContext, aiTaskResponseBuilder, nextNode,
					configuration.getTrace())) {

				continue;
			}

			_executeNode(
				aiTaskContext, aiTaskResponseBuilder, configuration, nextNode);
		}
	}

	private AITaskResponse _executeTriggerNodes(
		AITaskRequest aiTaskRequest, Configuration configuration,
		Map<String, TriggerAITaskNode> triggerAITaskNodes) {

		long startTime = System.currentTimeMillis();

		AITaskResponseBuilder aiTaskResponseBuilder =
			_aiTaskResponseBuilderFactory.builder();

		// TODO: For now, all the trees share the same context and output

		for (Map.Entry<String, TriggerAITaskNode> entry :
				triggerAITaskNodes.entrySet()) {

			Node node = _getNode(entry.getKey(), configuration.getNodes());

			TriggerAITaskNode triggerAITaskNode = entry.getValue();

			try {
				if (triggerAITaskNode.evaluate(
						aiTaskRequest.getAITaskContext(),
						_jsonFactory.createJSONObject(
							(Map)node.getParameters()))) {

					_executeNode(
						aiTaskRequest.getAITaskContext(), aiTaskResponseBuilder,
						configuration, node);
				}
			}
			catch (Exception exception) {
				_log.error(exception);

				aiTaskResponseBuilder.addExecutionTrace(
					HashMapBuilder.<String, Object>put(
						"exception", exception.toString()
					).build(),
					entry.getKey());

				aiTaskResponseBuilder.addOutput(
					"error",
					StringBundler.concat(
						"Something went wrong:\n", "```json\n",
						exception.getMessage(), "\n```\n"));
			}
		}

		if (configuration.getTrace()) {
			aiTaskResponseBuilder.took(
				(System.currentTimeMillis() - startTime) + "ms");
		}

		return aiTaskResponseBuilder.build();
	}

	private List<Node> _getNextNodes(Configuration configuration, String id) {
		List<Node> nextNodes = new ArrayList<>();

		if (configuration.getEdges() == null) {
			return nextNodes;
		}

		for (Edge edge : configuration.getEdges()) {
			if (Objects.equals(edge.getSource(), id)) {
				nextNodes.add(
					_getNode(edge.getTarget(), configuration.getNodes()));
			}
		}

		return nextNodes;
	}

	private Node _getNode(String id, Node[] nodes) {
		for (Node node : nodes) {
			if (Objects.equals(node.getId(), id)) {
				return node;
			}
		}

		return null;
	}

	private Map<String, TriggerAITaskNode> _getTriggerAITaskNodes(
		Configuration configuration) {

		Map<String, TriggerAITaskNode> triggerAITaskNodes = new HashMap<>();

		for (Node node : configuration.getNodes()) {
			AITaskNode aiTaskNode = _serviceTrackerMap.getService(
				node.getType());

			if ((aiTaskNode == null) ||
				!(aiTaskNode instanceof TriggerAITaskNode)) {

				continue;
			}

			triggerAITaskNodes.put(node.getId(), (TriggerAITaskNode)aiTaskNode);
		}

		return triggerAITaskNodes;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AITaskRequestExecutorImpl.class);

	@Reference
	private AITaskResponseBuilderFactory _aiTaskResponseBuilderFactory;

	@Reference
	private JSONFactory _jsonFactory;

	private ServiceTrackerMap<String, AITaskNode> _serviceTrackerMap;

}