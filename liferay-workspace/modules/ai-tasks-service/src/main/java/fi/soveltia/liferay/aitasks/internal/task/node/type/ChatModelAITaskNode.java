package fi.soveltia.liferay.aitasks.internal.task.node.type;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.chat.ChatLanguageModel;

import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;

/**
 * @author Petteri Karttunen
 */
public interface ChatModelAITaskNode extends AITaskNode {

	public ChatLanguageModel getChatLanguageModel(JSONObject jsonObject);

}