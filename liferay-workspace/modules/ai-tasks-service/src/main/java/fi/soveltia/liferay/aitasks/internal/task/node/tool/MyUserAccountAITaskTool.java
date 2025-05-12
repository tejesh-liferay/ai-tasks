package fi.soveltia.liferay.aitasks.internal.task.node.tool;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.UserService;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.util.Validator;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import fi.soveltia.liferay.aitasks.spi.task.tool.AITaskTool;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.tool.name=myUserAccount", service = AITaskTool.class
)
public class MyUserAccountAITaskTool implements AITaskTool {

	@Override
	public Object getExecutor(JSONObject jsonObject) {
		return new Executor();
	}

	public class Executor {

		@Tool("Updates my email address")
		public String updateMyEmailAddress(
				@P("The new email address of the user") String emailAddress)
			throws PortalException {

			return _updateUser(emailAddress, null, null, null);
		}

		@Tool("Updates my first name")
		public String updateMyFirstName(
				@P("The new first name of the user") String firstName)
			throws PortalException {

			return _updateUser(null, firstName, null, null);
		}

		@Tool("Updates my job title")
		public String updateMyJobTitle(
				@P("The new job title of the user") String jobTitle)
			throws PortalException {

			return _updateUser(null, null, jobTitle, null);
		}

		@Tool("Updates my last name")
		public String updateMyLastName(
				@P("The new last name of the user") String lastName)
			throws PortalException {

			return _updateUser(null, null, null, lastName);
		}

		private String _updateUser(
				String emailAddress, String firstName, String jobTitle,
				String lastName)
			throws PortalException {

			try {
				User user = UserServiceUtil.getCurrentUser();

				if (user.isGuestUser()) {
					return null;
				}

				if (!Validator.isBlank(emailAddress)) {
					user.setEmailAddress(emailAddress);
				}

				if (!Validator.isBlank(firstName)) {
					user.setFirstName(firstName);
				}

				if (!Validator.isBlank(jobTitle)) {
					user.setJobTitle(jobTitle);
				}

				if (!Validator.isBlank(lastName)) {
					user.setLastName(lastName);
				}

				user = UserLocalServiceUtil.updateUser(user);

				return user.toString();
			}
			catch (PortalException portalException) {
				log.error(portalException);

				throw portalException;
			}
		}

	}

	protected static final Log log = LogFactoryUtil.getLog(
		MyUserAccountAITaskTool.class);

	@Reference
	protected UserService userService;

}