package fi.soveltia.liferay.aitasks.internal.task.node;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.HashMapBuilder;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;

import fi.soveltia.liferay.aitasks.internal.task.node.type.ImageModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.util.PromptUtil;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseImageModelAITaskNode
	extends BaseAITaskNode implements ImageModelAITaskNode {

	@Override
	public AITaskNodeResponse execute(
		AITaskContext aiTaskContext, JSONObject jsonObject, String nodeId,
		boolean trace) {

		return _toImageAITaskNodeResponse(
			aiTaskContext, getImageModel(jsonObject), jsonObject, trace);
	}

	private Map<String, Object> _getExecutionTrace(
		Response<?> response, boolean trace) {

		if (!trace || (response == null)) {
			return null;
		}

		Map<String, Object> executionTrace = new HashMap<>();

		executionTrace.putAll(
			getExecutionTrace(response.finishReason(), response.tokenUsage()));

		if (response.content() instanceof List) {
			Map<String, Object> imageExecutionTraces = new HashMap<>();

			List<Image> images = (List<Image>)response.content();

			for (int i = 0; i < images.size(); i++) {
				imageExecutionTraces.put(
					String.valueOf(i), _getImageExecutionTrace(images.get(i)));
			}

			executionTrace.put("images", imageExecutionTraces);
		}
		else if (response.content() instanceof Image) {
			executionTrace.putAll(
				_getImageExecutionTrace((Image)response.content()));
		}

		return executionTrace;
	}

	private Map<String, Object> _getImageExecutionTrace(Image image) {
		return HashMapBuilder.<String, Object>put(
			"mimeType", image.mimeType()
		).put(
			"revisedPrompt", image.revisedPrompt()
		).build();
	}

	private AITaskNodeResponse _toImageAITaskNodeResponse(
		AITaskContext aiTaskContext, ImageModel imageModel,
		JSONObject jsonObject, boolean trace) {

		int numberOfImages = jsonObject.getInt("numberOfImages", 1);

		if (numberOfImages > 1) {
			return _toMultiImageAITaskNodeResponse(
				aiTaskContext, imageModel, jsonObject, numberOfImages, trace);
		}

		return _toSingleImageAITaskNodeResponse(
			aiTaskContext, imageModel, jsonObject, trace);
	}

	private Map<String, Object> _toImageEntry(Image image) {
		if (image.url() != null) {
			return HashMapBuilder.<String, Object>put(
				"url", image.url()
			).build();
		}

		return HashMapBuilder.<String, Object>put(
			"base64Data", image.base64Data()
		).build();
	}

	private AITaskNodeResponse _toMultiImageAITaskNodeResponse(
		AITaskContext aiTaskContext, ImageModel imageModel,
		JSONObject jsonObject, int numberOfImages, boolean trace) {

		Response<List<Image>> response = imageModel.generate(
			PromptUtil.getUserMessageString(aiTaskContext, jsonObject),
			numberOfImages);

		List<Image> images = response.content();

		List<Map<String, Object>> output = new ArrayList<>();

		for (Image image : images) {
			output.add(_toImageEntry(image));
		}

		return toAITaskNodeResponse(
			aiTaskContext, _getExecutionTrace(response, trace), jsonObject,
			trace, output);
	}

	private AITaskNodeResponse _toSingleImageAITaskNodeResponse(
		AITaskContext aiTaskContext, ImageModel imageModel,
		JSONObject jsonObject, boolean trace) {

		Response<Image> response = imageModel.generate(
			PromptUtil.getUserMessageString(aiTaskContext, jsonObject));

		return toAITaskNodeResponse(
			aiTaskContext, _getExecutionTrace(response, trace), jsonObject,
			trace, _toImageEntry(response.content()));
	}

}