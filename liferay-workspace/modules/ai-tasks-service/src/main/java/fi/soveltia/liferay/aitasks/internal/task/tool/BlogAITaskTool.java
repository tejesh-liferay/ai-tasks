package fi.soveltia.liferay.aitasks.internal.task.tool;

import com.liferay.blogs.constants.BlogsConstants;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.blogs.service.BlogsEntryService;
import com.liferay.document.library.kernel.service.persistence.DLFileEntryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GroupThreadLocal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import com.liferay.upload.UniqueFileNameProvider;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;
import fi.soveltia.liferay.aitasks.internal.task.util.ImageModelUtil;
import fi.soveltia.liferay.aitasks.spi.task.tool.AITaskTool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(property = "ai.task.tool.name=blog", service = AITaskTool.class)
public class BlogAITaskTool implements AITaskTool {

	@Override
	public Object getExecutor(JSONObject configurationJSONObject) {
		return new Executor(configurationJSONObject);
	}

	public class Executor {

		private final JSONObject _jsonObject;

		public Executor(JSONObject jsonObject) {
			_jsonObject = jsonObject;
		}

		@Tool("Creates a new blogs entry")
		public BlogsEntry createBlogsEntry(
				@P("The content for the blogs entry") String content,
				@P("Create a cover image for the blogs entry?") Boolean createCoverImage,
				@P("The subtitle for the blogs entry") String subtitle,
				@P("The title for the blogs entry") String title)
				throws Exception {

			_validateParameters(content, subtitle, title);

			try {
				ServiceContext serviceContext =
						ServiceContextThreadLocal.getServiceContext();

				ImageSelector coverImageSelector = null;

				if (createCoverImage) {
					coverImageSelector = _createImageSelector(title);
				}

				Calendar calendar = Calendar.getInstance();
				return blogsEntryService.addEntry(
						title, subtitle, _createDescription(content), content,
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH),
						calendar.get(Calendar.YEAR),
						calendar.get(Calendar.HOUR_OF_DAY),
						calendar.get(Calendar.MINUTE), true, true, new String[0],
						null, coverImageSelector, null, serviceContext);
			} catch (Exception portalException) {
				log.error(portalException);

				throw portalException;
			}
		}

		private void _validateParameters(String content, String subtitle, String title) {

			if (Validator.isBlank(content)) {
				throw new IllegalArgumentException("Content cannot be blank");
			} else if (Validator.isBlank(subtitle)) {
				throw new IllegalArgumentException("Subtitle cannot be blank");
			} else if (Validator.isBlank(title)) {
				throw new IllegalArgumentException("Title cannot be blank");
			}
		}

		private ImageSelector _createImageSelector(String title) {

			JSONObject jsonObject = _jsonObject.getJSONObject("imageGenerator");

			if (jsonObject == null) {
				return null;
			}

			String imageBase64 = ImageModelUtil.generateImage(jsonObject, title, jsonObject.getString("type"));

			if (Validator.isBlank(imageBase64)) {
				return null;
			}

			return new ImageSelector(
					Base64.getDecoder(
					).decode(
							imageBase64
					),
					StringBundler.concat(_sanitizeFilename(title), ".", "png"),
					"image/png", StringPool.BLANK);


		}

		private String _createDescription(String content) {
			if (content.length() <= DESCRIPTION_LENGTH) {
				return content;
			}

			return content.substring(0, DESCRIPTION_LENGTH);
		}

		private String _sanitizeFilename(String inputName) {
			return inputName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
		}
	}

	protected static final int DESCRIPTION_LENGTH = 400;

	protected static final Log log = LogFactoryUtil.getLog(
		BlogAITaskTool.class);

	@Reference
	protected BlogsEntryService blogsEntryService;

}