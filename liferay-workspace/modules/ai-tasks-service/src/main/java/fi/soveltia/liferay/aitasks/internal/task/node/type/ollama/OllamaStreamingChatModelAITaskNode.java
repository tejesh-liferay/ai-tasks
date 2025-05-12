
package fi.soveltia.liferay.aitasks.internal.task.node.type.ollama;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseStreamingChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.type.StreamingChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=ollamaStreamingChatModel",
	service = AITaskNode.class
)
public class OllamaStreamingChatModelAITaskNode
	extends BaseStreamingChatModelAITaskNode
	implements StreamingChatModelAITaskNode {

	@Override
	public StreamingChatLanguageModel getStreamingChatLanguageModel(
		JSONObject jsonObject) {

		OllamaStreamingChatModel.OllamaStreamingChatModelBuilder builder =
			OllamaStreamingChatModel.builder();

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