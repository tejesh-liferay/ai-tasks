package fi.soveltia.liferay.aitasks.internal.task.tool;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;
import fi.soveltia.liferay.aitasks.internal.task.util.ImageModelUtil;
import fi.soveltia.liferay.aitasks.internal.util.PromptUtil;
import fi.soveltia.liferay.aitasks.spi.task.tool.AITaskTool;
import org.osgi.service.component.annotations.Component;

import java.net.URI;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
@Component(property = "ai.task.tool.name=imageGenerator", service = AITaskTool.class)
public class ImageGeneratorAITaskTool implements AITaskTool {

    @Override
    public Object getExecutor(JSONObject configurationJSONObject) {
        return new Executor(configurationJSONObject);
    }

    public class Executor {

        private final JSONObject _jsonObject;

        public Executor(JSONObject jsonObject) {
            _jsonObject = jsonObject;
        }

        @Tool("Generates an image")
        public String generateImage(
                @P("The text to create the image for") String text)
                throws Exception {

            String type = _jsonObject.getString("type");

            if (type == null) {
                throw new IllegalArgumentException("Type is not defined");
            }

            if (StringUtil.equals(type, "googleImagen")) {
                return _toGoogleImagenImage(text);
            } else if (StringUtil.equals(type, "openAIImageModel")) {
                return _toOpenAIImageModelImage(text);
            }

            throw new IllegalArgumentException("Unknown image model");
        }

        private String _toOpenAIImageModelImage(String text) {
            return _generate(ImageModelUtil.getOpenAIImageModel(_jsonObject), text);
        }

        private String _toGoogleImagenImage(String text) {
            return _generate(ImageModelUtil.getVertexAiImageModel(_jsonObject), text);
        }

        private String _generate(ImageModel imageModel, String text) {

            Response<Image> response = imageModel.generate(text);

            Image image = response.content();

            if (image.url() != null) {

                URI uri = image.url();

                return uri.toString();
            }

           return  image.base64Data();
        }
    }
}