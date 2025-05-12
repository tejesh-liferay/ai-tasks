package fi.soveltia.liferay.aitasks.internal.task.node;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.HashMapBuilder;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.service.Result;

import fi.soveltia.liferay.aitasks.configuration.AITasksConfigurationProvider;
import fi.soveltia.liferay.aitasks.internal.task.ai.service.AIServiceHelper;
import fi.soveltia.liferay.aitasks.internal.task.ai.service.ChatAssistant;
import fi.soveltia.liferay.aitasks.internal.task.ai.service.WithMemoryChatAssistant;
import fi.soveltia.liferay.aitasks.internal.task.chat.model.listener.ChatModelListenerProvider;
import fi.soveltia.liferay.aitasks.internal.task.node.type.ChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.util.ExecutionTraceUtil;
import fi.soveltia.liferay.aitasks.internal.task.node.util.MemoryUtil;
import fi.soveltia.liferay.aitasks.internal.task.node.util.PromptUtil;
import fi.soveltia.liferay.aitasks.internal.web.cache.ChatModelAITaskNodeWebCacheItem;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseChatModelAITaskNode
	extends BaseAITaskNode implements ChatModelAITaskNode {

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

		return _chat(
			aiTaskContext, jsonObject, nodeId, systemMessage, trace,
			userMessage);
	}

	public abstract ChatLanguageModel getChatLanguageModel(
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
		AITaskContext aiTaskContext, JSONObject jsonObject, String nodeId,
		String systemMessage, boolean trace, String userMessage) {

		ChatAssistant chatAssistant =
			(ChatAssistant)aiServiceHelper.createAssistant(
				ChatAssistant.class, jsonObject,
				getChatLanguageModel(jsonObject), systemMessage, true);

		Result<?> result = _getResult(
			aiTaskContext, chatAssistant, jsonObject, nodeId,
			() -> chatAssistant.chat(userMessage), userMessage);

		return toAITaskNodeResponse(
			aiTaskContext,
			_getExecutionTrace(result, systemMessage, trace, userMessage),
			jsonObject, trace, result);
	}

	private AITaskNodeResponse _chatWithMemory(
		AITaskContext aiTaskContext, JSONObject jsonObject, String nodeId,
		String systemMessage, boolean trace, String userMessage) {

		WithMemoryChatAssistant withMemoryChatAssistant =
			(WithMemoryChatAssistant)aiServiceHelper.createAssistant(
				WithMemoryChatAssistant.class, jsonObject,
				getChatLanguageModel(jsonObject), systemMessage, true);

		Result<?> result = _getResult(
			aiTaskContext, withMemoryChatAssistant, jsonObject, nodeId,
			() -> withMemoryChatAssistant.chat(
				MemoryUtil.getMemoryId(aiTaskContext, nodeId), userMessage),
			userMessage);

		return toAITaskNodeResponse(
			aiTaskContext,
			_getExecutionTrace(result, systemMessage, trace, userMessage),
			jsonObject, trace, result);
	}

	private Map<String, Object> _getExecutionTrace(
		Result<?> result, String systemMessage, boolean trace,
		String userMessage) {

		if ((result == null) || !trace) {
			return null;
		}

		return HashMapBuilder.<String, Object>put(
			"systemMessage", systemMessage
		).put(
			"userMessage", userMessage
		).putAll(
			ExecutionTraceUtil.getExecutionTrace(result)
		).build();
	}

	private Result<?> _getResult(
		AITaskContext aiTaskContext, Object chatAssistant,
		JSONObject jsonObject, String nodeId, Supplier<Result<?>> supplier,
		String userMessage) {

		if (jsonObject.getBoolean("useCache")) {
			String json = jsonObject.toString();

			return (Result<String>)ChatModelAITaskNodeWebCacheItem.get(
				aiTasksConfigurationProvider.getCompanyConfiguration(
					aiTaskContext.getCompanyId()),
				chatAssistant, MemoryUtil.getMemoryId(aiTaskContext, nodeId),
				String.valueOf(json.hashCode()), userMessage);
		}

		return supplier.get();
	}

}