package fi.soveltia.liferay.aitasks.rest.internal.resource.v1_0;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MapUtil;

import fi.soveltia.liferay.aitasks.model.AITask;
import fi.soveltia.liferay.aitasks.rest.dto.v1_0.AITaskRequest;
import fi.soveltia.liferay.aitasks.rest.dto.v1_0.AITaskResponse;
import fi.soveltia.liferay.aitasks.rest.resource.v1_0.AITaskResponseResource;
import fi.soveltia.liferay.aitasks.service.AITaskService;
import fi.soveltia.liferay.aitasks.spi.task.context.AITaskContextContributor;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.request.AITaskRequestBuilder;
import fi.soveltia.liferay.aitasks.task.request.AITaskRequestBuilderFactory;
import fi.soveltia.liferay.aitasks.task.request.AITaskRequestExecutor;
import fi.soveltia.liferay.aitasks.task.response.AITaskTokenStreamHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Petteri Karttunen
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/ai-task-response.properties",
	scope = ServiceScope.PROTOTYPE, service = AITaskResponseResource.class
)
public class AITaskResponseResourceImpl extends BaseAITaskResponseResourceImpl {

	public AITaskResponse postGenerateExternalReferenceCode(
			String externalReferenceCode, AITaskRequest aiTaskRequest)
		throws Exception {

		return _toDTO(
			_aiTaskRequestExecutor.execute(
				_createAITaskRequest(externalReferenceCode, aiTaskRequest)));
	}

	@Override
	public Response postStreamExternalReferenceCode(
			String externalReferenceCode, AITaskRequest aiTaskRequest)
		throws Exception {

		fi.soveltia.liferay.aitasks.task.response.AITaskResponse
			aiTaskResponse = _aiTaskRequestExecutor.execute(
				_createAITaskRequest(externalReferenceCode, aiTaskRequest));

		Map<String, Object> output = aiTaskResponse.getOutput();

		for (Map.Entry<String, Object> entry : output.entrySet()) {
			if (entry.getValue() instanceof AITaskTokenStreamHandler) {
				return _stream(
					(AITaskTokenStreamHandler)entry.getValue(),
					aiTaskResponse.getExecutionTrace());
			}
		}

		return Response.ok(
		).build();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, AITaskContextContributor.class,
			"ai.task.context.contributor.name");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private AITaskContext _createAITaskContext(AITask aiTask, Object input) {
		AITaskContext aiTaskContext = new AITaskContext(
			aiTask.getExternalReferenceCode(), contextCompany.getCompanyId(),
			input, contextAcceptLanguage.getPreferredLocale(),
			contextUser.getUserId());

		for (AITaskContextContributor aiTaskContextContributor :
				_serviceTrackerMap.values()) {

			aiTaskContextContributor.contribute(
				aiTaskContext, contextHttpServletRequest,
				contextAcceptLanguage.getPreferredLocale(), contextUser);
		}

		return aiTaskContext;
	}

	private fi.soveltia.liferay.aitasks.task.request.AITaskRequest
			_createAITaskRequest(
				String externalReferenceCode, AITaskRequest aiTaskRequest)
		throws Exception {

		AITaskRequestBuilder aiTaskRequestBuilder =
			_aiTaskRequestBuilderFactory.builder();

		AITask aiTask = _aiTaskService.getAITaskByExternalReferenceCode(
			contextCompany.getCompanyId(), externalReferenceCode);

		aiTaskRequestBuilder.aiTask(aiTask);
		aiTaskRequestBuilder.aiTaskContext(
			_createAITaskContext(aiTask, aiTaskRequest.getInput()));

		return aiTaskRequestBuilder.build();
	}

	private Response _stream(
		AITaskTokenStreamHandler aiTaskTokenStreamHandler,
		Map<String, Map<String, Object>> executionTrace) {

		StreamingOutput streamingOutput = new StreamingOutput() {

			@Override
			public void write(OutputStream outputStream) {
				Writer writer = new BufferedWriter(
					new OutputStreamWriter(outputStream));

				CountDownLatch countDownLatch = new CountDownLatch(1);

				aiTaskTokenStreamHandler.onPartialResponse(
					partialResponse -> _writeResponse(partialResponse, writer)
				).onError(
					error -> _log.error(error)
				).onCompleteResponse(
					chatResponse -> {
						_writeExecutionTrace(
							chatResponse, executionTrace, writer);
						countDownLatch.countDown();
					}
				).start();

				try {
					countDownLatch.await();
				}
				catch (InterruptedException interruptedException) {
					throw new RuntimeException(interruptedException);
				}
			}

		};

		return Response.ok(
			streamingOutput
		).type(
			MediaType.TEXT_PLAIN
		).build();
	}

	private AITaskResponse _toDTO(
		fi.soveltia.liferay.aitasks.task.response.AITaskResponse
			aiTaskResponse) {

		return new AITaskResponse() {
			{
				if (aiTaskResponse.getExecutionTrace() != null) {
					setExecutionTrace(aiTaskResponse::getExecutionTrace);
				}

				setOutput(aiTaskResponse::getOutput);
				setTook(aiTaskResponse::getTook);
			}
		};
	}

	private void _writeExecutionTrace(
		String chatResponse, Map<String, Map<String, Object>> executionTrace,
		Writer writer) {

		if (MapUtil.isEmpty(executionTrace)) {
			return;
		}

		try {
			JSONObject chatResponseJSONObject = _jsonFactory.createJSONObject(
				chatResponse);

			JSONObject executionTraceJSONObject = _jsonFactory.createJSONObject(
				executionTrace);

			for (String nodeId : chatResponseJSONObject.keySet()) {
				JSONObject aiTaskNodeExecutionTraceJSONObject =
					executionTraceJSONObject.getJSONObject(nodeId);

				if (aiTaskNodeExecutionTraceJSONObject != null) {
					Set<String> keySet = chatResponseJSONObject.keySet();

					keySet.forEach(
						key -> aiTaskNodeExecutionTraceJSONObject.put(
							key, chatResponseJSONObject.get(key)));
				}
				else {
					executionTraceJSONObject.put(
						nodeId, chatResponseJSONObject.get(nodeId));
				}
			}

			writer.write(
				"executionTrace:" + executionTraceJSONObject.toString(3));
			writer.flush();
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	private void _writeResponse(String token, Writer writer) {
		try {
			writer.write(token);
			writer.write("\n##");
			writer.flush();
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AITaskResponseResourceImpl.class);

	@Reference
	private AITaskRequestBuilderFactory _aiTaskRequestBuilderFactory;

	@Reference
	private AITaskRequestExecutor _aiTaskRequestExecutor;

	@Reference
	private AITaskService _aiTaskService;

	@Reference
	private JSONFactory _jsonFactory;

	private ServiceTrackerMap<String, AITaskContextContributor>
		_serviceTrackerMap;

}