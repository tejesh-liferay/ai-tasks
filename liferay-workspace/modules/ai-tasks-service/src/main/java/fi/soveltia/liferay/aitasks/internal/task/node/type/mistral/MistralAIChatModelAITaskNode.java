package fi.soveltia.liferay.aitasks.internal.task.node.type.mistral;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.type.ChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=mistralIChatModel", service = AITaskNode.class
)
public class MistralAIChatModelAITaskNode
	extends BaseChatModelAITaskNode implements ChatModelAITaskNode {

	@Override
	public ChatLanguageModel getChatLanguageModel(JSONObject jsonObject) {
		MistralAiChatModel.MistralAiChatModelBuilder builder =
			MistralAiChatModel.builder();

		SetterUtil.setNotBlankString(
			builder::apiKey, jsonObject.getString("apiKey"));
		SetterUtil.setNotBlankString(
			builder::baseUrl, jsonObject.getString("baseUrl"));
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
		SetterUtil.setNotNullInteger(builder::randomSeed, jsonObject, "seed");
		SetterUtil.setNotBlankString(
			builder::responseFormat, jsonObject.getString("responseFormat"));
		SetterUtil.setNotNullBoolean(
			builder::safePrompt, jsonObject, "safePrompt");
		SetterUtil.setNotNullDouble(
			builder::temperature, jsonObject, "temperature");
		SetterUtil.setNotNullDurationOfSeconds(
			builder::timeout, jsonObject, "timeout");
		SetterUtil.setNotNullDouble(builder::topP, jsonObject, "topP");

		return builder.build();
	}

}