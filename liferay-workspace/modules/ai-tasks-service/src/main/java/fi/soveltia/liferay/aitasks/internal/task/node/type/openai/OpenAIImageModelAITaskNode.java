package fi.soveltia.liferay.aitasks.internal.task.node.type.openai;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseImageModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.type.ImageModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=openAIImageModel",
	service = ImageModelAITaskNode.class
)
public class OpenAIImageModelAITaskNode
	extends BaseImageModelAITaskNode implements ImageModelAITaskNode {

	@Override
	public ImageModel getImageModel(JSONObject jsonObject) {
		OpenAiImageModel.OpenAiImageModelBuilder builder =
			OpenAiImageModel.builder();

		SetterUtil.setNotBlankString(
			builder::apiKey, jsonObject.getString("apiKey"));
		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logRequests");
		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logResponses");
		SetterUtil.setNotNullInteger(
			builder::maxRetries, jsonObject, "maxRetries");
		SetterUtil.setNotBlankString(
			builder::modelName, jsonObject.getString("model"));
		SetterUtil.setNotBlankString(
			builder::quality, jsonObject.getString("quality"));
		SetterUtil.setNotBlankString(
			builder::responseFormat, jsonObject.getString("responseFormat"));
		SetterUtil.setNotBlankString(
			builder::size, jsonObject.getString("size"));
		SetterUtil.setNotBlankString(
			builder::style, jsonObject.getString("style"));
		SetterUtil.setNotBlankString(
			builder::user, jsonObject.getString("user"));

		return builder.build();
	}

}