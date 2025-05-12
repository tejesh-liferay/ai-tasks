package fi.soveltia.liferay.aitasks.internal.task.node.type;

import com.liferay.portal.kernel.json.JSONObject;

import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;

/**
 * @author Petteri Karttunen
 */
public interface TriggerAITaskNode extends AITaskNode {

	public boolean evaluate(AITaskContext aiTaskContext, JSONObject jsonObject);

}