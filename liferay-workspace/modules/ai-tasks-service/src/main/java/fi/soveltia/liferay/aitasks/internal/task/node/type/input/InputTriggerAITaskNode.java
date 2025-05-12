package fi.soveltia.liferay.aitasks.internal.task.node.type.input;

import com.liferay.portal.kernel.json.JSONObject;

import fi.soveltia.liferay.aitasks.internal.task.node.type.TriggerAITaskNode;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=inputTrigger", service = AITaskNode.class
)
public class InputTriggerAITaskNode implements TriggerAITaskNode {

	@Override
	public boolean evaluate(
		AITaskContext aiTaskContext, JSONObject jsonObject) {

		if (aiTaskContext.getInput() != null) {
			return true;
		}

		return false;
	}

	@Override
	public AITaskNodeResponse execute(
		AITaskContext aiTaskContext, JSONObject jsonObject, String nodeId,
		boolean trace) {

		return null;
	}

}