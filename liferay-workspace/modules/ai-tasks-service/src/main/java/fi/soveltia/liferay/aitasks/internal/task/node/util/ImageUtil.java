package fi.soveltia.liferay.aitasks.internal.task.node.util;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;

import java.net.URI;

/**
 * @author Petteri Karttunen
 */
public class ImageModelUtil {

	public static String generateImage(ImageModel imageModel, String text) {
		Response<Image> response = imageModel.generate(text);

		Image image = response.content();

		if (image.url() != null) {
			URI uri = image.url();

			return uri.toString();
		}

		return image.base64Data();
	}

}