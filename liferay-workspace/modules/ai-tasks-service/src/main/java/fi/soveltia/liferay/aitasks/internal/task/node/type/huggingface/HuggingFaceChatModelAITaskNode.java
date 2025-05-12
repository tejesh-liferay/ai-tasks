
package fi.soveltia.liferay.aitasks.internal.task.node.type.huggingface;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.type.ChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=huggingFaceChatModel",
	service = AITaskNode.class
)
public class HuggingFaceChatModelAITaskNode
	extends BaseChatModelAITaskNode implements ChatModelAITaskNode {

	@Override
	public ChatLanguageModel getChatLanguageModel(JSONObject jsonObject) {
		HuggingFaceChatModel.Builder builder = HuggingFaceChatModel.builder();

		SetterUtil.setNotBlankString(
			builder::accessToken, jsonObject.getString("accessToken"));
		SetterUtil.setNotBlankString(
			builder::baseUrl, jsonObject.getString("baseUrl"));
		SetterUtil.setNotNullInteger(
			builder::maxNewTokens, jsonObject, "maxNewTokens");
		SetterUtil.setNotBlankString(
			builder::modelId, jsonObject.getString("modelId"));
		SetterUtil.setNotNullBoolean(
			builder::returnFullText, jsonObject, "returnFullText");
		SetterUtil.setNotNullDouble(
			builder::temperature, jsonObject, "temperature");
		SetterUtil.setNotNullDurationOfSeconds(
			builder::timeout, jsonObject, "timeout");
		SetterUtil.setNotNullBoolean(
			builder::waitForModel, jsonObject, "waitForModel");

		return builder.build();
	}

}