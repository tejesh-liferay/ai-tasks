package fi.soveltia.liferay.aitasks.task.request;

import fi.soveltia.liferay.aitasks.model.AITask;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Petteri Karttunen
 */
@ProviderType
public interface AITaskRequestBuilder {

	public AITaskRequestBuilder aiTask(AITask aiTask);

	public AITaskRequestBuilder aiTaskContext(AITaskContext aiTaskContext);

	public AITaskRequest build();

}