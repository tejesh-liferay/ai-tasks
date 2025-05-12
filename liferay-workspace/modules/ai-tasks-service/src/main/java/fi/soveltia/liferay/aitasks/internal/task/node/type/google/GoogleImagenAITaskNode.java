package fi.soveltia.liferay.aitasks.internal.task.node.type.google;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;

import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.vertexai.VertexAiImageModel;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseImageModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.type.ImageModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=googleImagen",
	service = ImageModelAITaskNode.class
)
public class GoogleImagenAITaskNode
	extends BaseImageModelAITaskNode implements ImageModelAITaskNode {

	@Override
	public ImageModel getImageModel(JSONObject jsonObject) {
		VertexAiImageModel.Builder builder = VertexAiImageModel.builder();

		SetterUtil.setNotBlankString(
			builder::endpoint, jsonObject.getString("endpoint"));
		SetterUtil.setNotNullInteger(
			builder::guidanceScale, jsonObject, "guidanceScale");
		SetterUtil.setNotBlankString(
			builder::language, jsonObject.getString("language"));
		SetterUtil.setNotBlankString(
			builder::location, jsonObject.getString("location"));
		SetterUtil.setNotBlankString(
			builder::location, jsonObject.getString("location"));
		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logRequests");
		SetterUtil.setNotNullInteger(
			builder::maxRetries, jsonObject, "maxRetries");
		SetterUtil.setNotBlankString(
			builder::modelName, jsonObject.getString("model"));
		SetterUtil.setNotBlankString(
			builder::negativePrompt, jsonObject.getString("negativePrompt"));
		SetterUtil.setNotBlankString(
			builder::project, jsonObject.getString("project"));
		SetterUtil.setNotBlankString(
			builder::publisher, jsonObject.getString("publisher"));
		SetterUtil.setNotNullInteger(
			builder::sampleImageSize, jsonObject, "sampleImageSize");

		VertexAiImageModel.ImageStyle imageStyle = _getVertexAiImageStyle(
			jsonObject);

		if (imageStyle != null) {
			builder.sampleImageStyle(imageStyle);
		}

		SetterUtil.setNotNullLong(builder::seed, jsonObject, "seed");

		return builder.build();
	}

	private VertexAiImageModel.ImageStyle _getVertexAiImageStyle(
		JSONObject jsonObject) {

		String imageStyle = jsonObject.getString("imageStyle");

		if (Validator.isBlank(imageStyle)) {
			return null;
		}

		if (imageStyle.equals("cyberpunk")) {
			return VertexAiImageModel.ImageStyle.CYBERPUNK;
		}
		else if (imageStyle.equals("digitalArt")) {
			return VertexAiImageModel.ImageStyle.DIGITAL_ART;
		}
		else if (imageStyle.equals("landscape")) {
			return VertexAiImageModel.ImageStyle.LANDSCAPE;
		}
		else if (imageStyle.equals("photograph")) {
			return VertexAiImageModel.ImageStyle.PHOTOGRAPH;
		}
		else if (imageStyle.equals("popArt")) {
			return VertexAiImageModel.ImageStyle.POP_ART;
		}
		else if (imageStyle.equals("sketch")) {
			return VertexAiImageModel.ImageStyle.SKETCH;
		}
		else if (imageStyle.equals("watercolor")) {
			return VertexAiImageModel.ImageStyle.WATERCOLOR;
		}

		throw new IllegalArgumentException("Invalid image style " + imageStyle);
	}

}