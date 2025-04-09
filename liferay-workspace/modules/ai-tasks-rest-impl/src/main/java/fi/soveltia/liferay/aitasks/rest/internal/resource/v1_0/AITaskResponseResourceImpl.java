package fi.soveltia.liferay.aitasks.rest.internal.resource.v1_0;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

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

import javax.ws.rs.core.Response;

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
		String externalReferenceCode, AITaskRequest aiTaskRequest) {

		// TODO

		return null;
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

	@Reference
	private AITaskRequestBuilderFactory _aiTaskRequestBuilderFactory;

	@Reference
	private AITaskRequestExecutor _aiTaskRequestExecutor;

	@Reference
	private AITaskService _aiTaskService;

	private ServiceTrackerMap<String, AITaskContextContributor>
		_serviceTrackerMap;

}