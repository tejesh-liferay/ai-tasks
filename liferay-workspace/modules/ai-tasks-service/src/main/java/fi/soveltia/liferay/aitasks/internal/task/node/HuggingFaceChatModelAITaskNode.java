
package fi.soveltia.liferay.aitasks.internal.task.node;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;

import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeInformation;

import java.time.Duration;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=huggingFaceChatModel",
	service = AITaskNode.class
)
public class HuggingFaceChatModelAITaskNode
	extends BaseChatModelAITaskNode implements AITaskNode {

	@Override
	public AITaskNodeInformation getAITaskNodeInformation() {
		return new AITaskNodeInformation("huggingFaceChatModel", "input");
	}

	protected ChatLanguageModel getChatLanguageModel(JSONObject jsonObject) {
		HuggingFaceChatModel.Builder builder = HuggingFaceChatModel.builder();

		SetterUtil.setNotBlankString(
			builder::accessToken, jsonObject.getString("accessToken"));
		SetterUtil.setNotBlankString(
			builder::baseUrl, jsonObject.getString("baseUrl"));
		SetterUtil.setNotNullInteger(
			builder::maxNewTokens, jsonObject, "maxNewTokens");
		SetterUtil.setNotBlankString(
			builder::modelId, jsonObject.getString("modelName"));

		builder.returnFullText(jsonObject.getBoolean("returnFullText"));
		builder.waitForModel(jsonObject.getBoolean("waitForModel"));

		SetterUtil.setNotNullDouble(
			builder::temperature, jsonObject, "temperature");

		if (jsonObject.has("timeout")) {
			builder.timeout(Duration.ofSeconds(jsonObject.getInt("timeout")));
		}

		return builder.build();
	}

}