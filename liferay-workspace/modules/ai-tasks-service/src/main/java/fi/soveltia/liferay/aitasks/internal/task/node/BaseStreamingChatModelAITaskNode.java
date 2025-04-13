package fi.soveltia.liferay.aitasks.internal.task.node;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.HashMapBuilder;

import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;

import fi.soveltia.liferay.aitasks.configuration.AITasksConfigurationProvider;
import fi.soveltia.liferay.aitasks.internal.task.ai.service.AIServiceHelper;
import fi.soveltia.liferay.aitasks.internal.task.ai.service.ChatAssistant;
import fi.soveltia.liferay.aitasks.internal.task.ai.service.WithMemoryChatAssistant;
import fi.soveltia.liferay.aitasks.internal.task.chat.model.listener.ChatModelListenerProvider;
import fi.soveltia.liferay.aitasks.internal.task.node.type.StreamingChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.util.MemoryUtil;
import fi.soveltia.liferay.aitasks.internal.task.node.util.PromptUtil;
import fi.soveltia.liferay.aitasks.internal.task.response.AITaskTokenStreamHandlerImpl;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeResponse;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Reference;

/**
 * TODO
 *
 * @author Petteri Karttunen
 */
public abstract class BaseStreamingChatModelAITaskNode
	extends BaseAITaskNode implements StreamingChatModelAITaskNode {

	@Override
	public AITaskNodeResponse execute(
		AITaskContext aiTaskContext, JSONObject jsonObject, String nodeId,
		boolean trace) {

		String userMessage = PromptUtil.getUserMessage(
			aiTaskContext, jsonObject);

		if (userMessage == null) {
			return null;
		}

		String systemMessage = PromptUtil.getSystemMessage(
			aiTaskContext, jsonObject);

		boolean useChatMemory = jsonObject.getBoolean("useChatMemory");

		if (useChatMemory) {
			return _chatWithMemory(
				aiTaskContext, jsonObject, nodeId, systemMessage, trace,
				userMessage);
		}

		return _chat(jsonObject, nodeId, systemMessage, trace, userMessage);
	}

	public abstract StreamingChatLanguageModel getStreamingChatLanguageModel(
		JSONObject jsonObject);

	protected List<ChatModelListener> getChatModelListeners(
		JSONArray jsonArray) {

		List<ChatModelListener> chatModelListeners = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			ChatModelListener chatModelListener =
				chatModelListenerProvider.getChatModelListener(
					jsonArray.getString(i));

			if (chatModelListener != null) {
				chatModelListeners.add(chatModelListener);
			}
		}

		return chatModelListeners;
	}

	@Reference
	protected AIServiceHelper aiServiceHelper;

	@Reference
	protected AITasksConfigurationProvider aiTasksConfigurationProvider;

	@Reference
	protected ChatModelListenerProvider chatModelListenerProvider;

	private AITaskNodeResponse _chat(
		JSONObject jsonObject, String nodeId, String systemMessage,
		boolean trace, String userMessage) {

		ChatAssistant chatAssistant =
			(ChatAssistant)aiServiceHelper.createAssistant(
				ChatAssistant.class, jsonObject,
				getStreamingChatLanguageModel(jsonObject), systemMessage, true);

		return new AITaskNodeResponseImpl(
			null,
			HashMapBuilder.<String, Object>put(
				"stream",
				new AITaskTokenStreamHandlerImpl(
					nodeId, systemMessage, chatAssistant.stream(userMessage),
					trace, userMessage)
			).build());
	}

	private AITaskNodeResponse _chatWithMemory(
		AITaskContext aiTaskContext, JSONObject jsonObject, String nodeId,
		String systemMessage, boolean trace, String userMessage) {

		WithMemoryChatAssistant withMemoryChatAssistant =
			(WithMemoryChatAssistant)aiServiceHelper.createAssistant(
				WithMemoryChatAssistant.class, jsonObject,
				getStreamingChatLanguageModel(jsonObject), systemMessage, true);

		return new AITaskNodeResponseImpl(
			null,
			HashMapBuilder.<String, Object>put(
				"stream",
				new AITaskTokenStreamHandlerImpl(
					nodeId, systemMessage,
					withMemoryChatAssistant.stream(
						MemoryUtil.getMemoryId(aiTaskContext, nodeId),
						userMessage),
					trace, userMessage)
			).build());
	}

}