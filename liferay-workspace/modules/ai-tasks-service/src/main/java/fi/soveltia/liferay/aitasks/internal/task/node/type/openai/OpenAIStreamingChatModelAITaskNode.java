package fi.soveltia.liferay.aitasks.internal.task.node.type.openai;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseStreamingChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.type.StreamingChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=openAIStreamingChatModel",
	service = AITaskNode.class
)
public class OpenAIStreamingChatModelAITaskNode
	extends BaseStreamingChatModelAITaskNode
	implements StreamingChatModelAITaskNode {

	@Override
	public StreamingChatLanguageModel getStreamingChatLanguageModel(
		JSONObject jsonObject) {

		OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder =
			OpenAiStreamingChatModel.builder();

		SetterUtil.setNotBlankString(
			builder::apiKey, jsonObject.getString("apiKey"));
		SetterUtil.setNotBlankString(
			builder::baseUrl, jsonObject.getString("baseUrl"));
		SetterUtil.setNotNullJSONObjectAsStringMap(
			builder::customHeaders, jsonObject, "customHeaders");
		SetterUtil.setNotNullDouble(
			builder::frequencyPenalty, jsonObject, "frequencyPenalty");

		if (jsonObject.has("chatModelListeners")) {
			builder.listeners(
				getChatModelListeners(
					jsonObject.getJSONArray("chatModelListeners")));
		}

		if (jsonObject.has("logitBias")) {
			builder.logitBias(
				_toLogitBias(jsonObject.getJSONObject("logitBias")));
		}

		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logRequests");
		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logResponses");
		SetterUtil.setNotNullInteger(
			builder::maxTokens, jsonObject, "maxTokens");
		SetterUtil.setNotBlankString(
			builder::modelName, jsonObject.getString("modelName"));
		SetterUtil.setNotBlankString(
			builder::organizationId, jsonObject.getString("organizationId"));
		SetterUtil.setNotNullBoolean(
			builder::parallelToolCalls, jsonObject, "parallelToolCalls");
		SetterUtil.setNotNullDouble(
			builder::presencePenalty, jsonObject, "presencePenalty");
		SetterUtil.setNotBlankString(
			builder::responseFormat, jsonObject.getString("responseFormat"));
		SetterUtil.setNotNullInteger(builder::seed, jsonObject, "seed");
		SetterUtil.setNotNullJSONArrayAsStringList(
			builder::stop, jsonObject, "stop");
		SetterUtil.setNotNullDouble(
			builder::temperature, jsonObject, "temperature");
		SetterUtil.setNotNullDurationOfSeconds(
			builder::timeout, jsonObject, "timeout");
		SetterUtil.setNotNullDouble(builder::topP, jsonObject, "topP");
		SetterUtil.setNotBlankString(
			builder::user, jsonObject.getString("user"));

		return builder.build();
	}

	private Map<String, Integer> _toLogitBias(JSONObject jsonObject) {
		Map<String, Integer> map = new HashMap<>();

		for (String key : jsonObject.keySet()) {
			map.put(key, jsonObject.getInt(key));
		}

		return map;
	}

}