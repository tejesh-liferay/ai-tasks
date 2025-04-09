package fi.soveltia.liferay.aitasks.internal.task.node.type;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.chat.StreamingChatLanguageModel;

import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;

/**
 * @author Petteri Karttunen
 */
public interface StreamingChatModelAITaskNode extends AITaskNode {

	public StreamingChatLanguageModel getStreamingChatLanguageModel(
		JSONObject jsonObject);

}