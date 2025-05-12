package fi.soveltia.liferay.aitasks.internal.task.node.tool;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringUtil;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import fi.soveltia.liferay.aitasks.internal.task.node.type.ImageModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.util.ImageUtil;
import fi.soveltia.liferay.aitasks.spi.task.tool.AITaskTool;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.tool.name=imageGenerator", service = AITaskTool.class
)
public class ImageGeneratorAITaskTool implements AITaskTool {

	@Override
	public Object getExecutor(JSONObject jsonObject) {
		return new Executor(jsonObject);
	}

	public class Executor {

		public Executor(JSONObject jsonObject) {
			_jsonObject = jsonObject;
		}

		@Tool("Generates an image")
		public String generateImage(
				@P("The text to create the image for") String text)
			throws Exception {

			String type = _jsonObject.getString("type");

			if (type == null) {
				throw new IllegalArgumentException(
					"Image model type is not defined");
			}

			if (StringUtil.equals(type, "googleImagen")) {
				return ImageUtil.generateImage(
					googleImagenAITaskNode.getImageModel(_jsonObject), text);
			}
			else if (StringUtil.equals(type, "openAIImageModel")) {
				return ImageUtil.generateImage(
					openAIImageModelAITaskNode.getImageModel(_jsonObject),
					text);
			}

			throw new IllegalArgumentException("Unknown image model");
		}

		private final JSONObject _jsonObject;

	}

	@Reference(target = "(ai.task.node.type=googleImagen)")
	protected ImageModelAITaskNode googleImagenAITaskNode;

	@Reference(target = "(ai.task.node.type=openAIImageModel)")
	protected ImageModelAITaskNode openAIImageModelAITaskNode;

}