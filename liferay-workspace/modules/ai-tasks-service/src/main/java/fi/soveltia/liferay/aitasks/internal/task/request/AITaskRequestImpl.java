
package fi.soveltia.liferay.aitasks.internal.task.request;

import fi.soveltia.liferay.aitasks.model.AITask;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.request.AITaskRequest;

/**
 * @author Petteri Karttunen
 */
public class AITaskRequestImpl implements AITaskRequest {

	public AITaskRequestImpl(AITask aiTask, AITaskContext aiTaskContext) {
		_aiTask = aiTask;
		_aiTaskContext = aiTaskContext;
	}

	public AITask getAITask() {
		return _aiTask;
	}

	@Override
	public AITaskContext getAITaskContext() {
		return _aiTaskContext;
	}

	private AITaskRequestImpl() {
	}

	private AITask _aiTask;
	private AITaskContext _aiTaskContext;

}