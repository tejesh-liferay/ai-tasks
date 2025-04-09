package fi.soveltia.liferay.aitasks.spi.task.node;

import com.liferay.portal.kernel.json.JSONObject;

import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeResponse;

/**
 * @author Petteri Karttunen
 */
public interface AITaskNode {

	public AITaskNodeResponse execute(
		AITaskContext aiTaskContext, JSONObject jsonObject, String nodeId,
		boolean trace);

}