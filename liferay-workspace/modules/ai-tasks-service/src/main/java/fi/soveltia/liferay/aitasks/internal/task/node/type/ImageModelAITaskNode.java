package fi.soveltia.liferay.aitasks.internal.task.node.type;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.image.ImageModel;

import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;

/**
 * @author Petteri Karttunen
 */
public interface ImageModelAITaskNode extends AITaskNode {

	public ImageModel getImageModel(JSONObject jsonObject);

}