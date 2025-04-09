
package fi.soveltia.liferay.aitasks.internal.task.node.type.ollama;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.ollama.OllamaChatModel;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.type.ChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=ollamaChatModel", service = AITaskNode.class
)
public class OllamaChatModelAITaskNode
	extends BaseChatModelAITaskNode implements ChatModelAITaskNode {

	@Override
	public ChatLanguageModel getChatLanguageModel(JSONObject jsonObject) {
		OllamaChatModel.OllamaChatModelBuilder builder =
			OllamaChatModel.builder();

		SetterUtil.setNotBlankString(
			builder::baseUrl, jsonObject.getString("baseUrl"));

		if (jsonObject.has("chatModelListeners")) {
			builder.listeners(
				getChatModelListeners(
					jsonObject.getJSONArray("chatModelListeners")));
		}

		SetterUtil.setNotNullJSONObjectAsStringMap(
			builder::customHeaders, jsonObject, "customHeaders");
		SetterUtil.setNotBlankString(
			builder::format, jsonObject.getString("format"));
		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logRequests");
		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logResponses");
		SetterUtil.setNotNullInteger(
			builder::maxRetries, jsonObject, "maxRetries");
		SetterUtil.setNotBlankString(
			builder::modelName, jsonObject.getString("modelName"));
		SetterUtil.setNotNullInteger(builder::numCtx, jsonObject, "numCtx");
		SetterUtil.setNotNullInteger(
			builder::numPredict, jsonObject, "numPredict");
		SetterUtil.setNotNullDouble(
			builder::repeatPenalty, jsonObject, "repeatPenalty");

		String responseFormat = jsonObject.getString("responseFormat");

		if (responseFormat != null) {
			if (responseFormat.equalsIgnoreCase("json")) {
				builder.responseFormat(ResponseFormat.JSON);
			}
			else {
				builder.responseFormat(ResponseFormat.TEXT);
			}
		}

		SetterUtil.setNotNullInteger(builder::seed, jsonObject, "seed");
		SetterUtil.setNotNullJSONArrayAsStringList(
			builder::stop, jsonObject, "stop");
		SetterUtil.setNotNullDouble(
			builder::temperature, jsonObject, "temperature");
		SetterUtil.setNotNullDurationOfSeconds(
			builder::timeout, jsonObject, "timeout");
		SetterUtil.setNotNullInteger(builder::topK, jsonObject, "topK");
		SetterUtil.setNotNullDouble(builder::topP, jsonObject, "topP");

		return builder.build();
	}

}