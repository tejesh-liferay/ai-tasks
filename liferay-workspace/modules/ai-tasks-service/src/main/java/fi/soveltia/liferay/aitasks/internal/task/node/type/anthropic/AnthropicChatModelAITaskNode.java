package fi.soveltia.liferay.aitasks.internal.task.node.type.anthropic;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.type.ChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=anthropicChatModel",
	service = AITaskNode.class
)
public class AnthropicChatModelAITaskNode
	extends BaseChatModelAITaskNode implements ChatModelAITaskNode {

	@Override
	public ChatLanguageModel getChatLanguageModel(JSONObject jsonObject) {
		AnthropicChatModel.AnthropicChatModelBuilder builder =
			AnthropicChatModel.builder();

		SetterUtil.setNotBlankString(
			builder::apiKey, jsonObject.getString("apiKey"));
		SetterUtil.setNotBlankString(
			builder::baseUrl, jsonObject.getString("baseUrl"));
		SetterUtil.setNotBlankString(
			builder::beta, jsonObject.getString("beta"));
		SetterUtil.setNotNullBoolean(
			builder::cacheSystemMessages, jsonObject, "cacheSystemMessages");
		SetterUtil.setNotNullBoolean(
			builder::cacheTools, jsonObject, "cacheTools");

		JSONArray chatModelListenersJSONArray = jsonObject.getJSONArray(
			"chatModelListeners");

		if (chatModelListenersJSONArray != null) {
			builder.listeners(
				getChatModelListeners(chatModelListenersJSONArray));
		}

		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logRequests");
		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logResponses");
		SetterUtil.setNotNullInteger(
			builder::maxRetries, jsonObject, "maxRetries");
		SetterUtil.setNotNullInteger(
			builder::maxTokens, jsonObject, "maxTokens");
		SetterUtil.setNotBlankString(
			builder::modelName, jsonObject.getString("modelName"));
		SetterUtil.setNotNullJSONArrayAsStringList(
			builder::stopSequences, jsonObject, "stopSequences");
		SetterUtil.setNotNullDouble(
			builder::temperature, jsonObject, "temperature");
		SetterUtil.setNotNullDurationOfSeconds(
			builder::timeout, jsonObject, "timeout");
		SetterUtil.setNotNullInteger(builder::topK, jsonObject, "topK");
		SetterUtil.setNotNullDouble(builder::topP, jsonObject, "topP");
		SetterUtil.setNotBlankString(
			builder::version, jsonObject.getString("version"));

		return builder.build();
	}

}