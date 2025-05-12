package fi.soveltia.liferay.aitasks.internal.task.ai.service;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;

import fi.soveltia.liferay.aitasks.configuration.AITasksConfigurationProvider;
import fi.soveltia.liferay.aitasks.internal.task.chat.memory.ChatMemoryStoreProvider;
import fi.soveltia.liferay.aitasks.internal.task.node.tool.AIToolsProvider;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(service = AIServiceHelper.class)
public class AIServiceHelperImpl implements AIServiceHelper {

	@Override
	public Object createAssistant(
		Class<?> clazz, JSONObject jsonObject, Object model,
		String systemMessage, boolean useChatMemory) {

		AiServices<?> builder = AiServices.builder(clazz);

		if (model instanceof ChatLanguageModel) {
			builder.chatLanguageModel((ChatLanguageModel)model);
		}
		else if (model instanceof StreamingChatLanguageModel) {
			builder.streamingChatLanguageModel(
				(StreamingChatLanguageModel)model);
		}

		if (!Validator.isBlank(systemMessage)) {
			builder.systemMessageProvider(memoryId -> systemMessage);
		}

		if (useChatMemory) {
			setChatMemoryProvider(builder, jsonObject);
		}

		_setToolProvider(builder, jsonObject);

		_setTools(builder, jsonObject);

		_setWebSearchRetrievalAugmentor(builder, jsonObject);

		return builder.build();
	}

	protected void setChatMemoryProvider(
		AiServices<?> builder, JSONObject jsonObject) {

		builder.chatMemoryProvider(
			memoryId -> MessageWindowChatMemory.builder(
			).id(
				memoryId
			).maxMessages(
				jsonObject.getInt("memoryMaxMessages", 10)
			).chatMemoryStore(
				chatMemoryStoreProvider.getChatMemoryStore()
			).build());
	}

	@Reference
	protected AITasksConfigurationProvider aiTasksConfigurationProvider;

	@Reference
	protected AIToolsProvider aiToolsProvider;

	@Reference
	protected ChatMemoryStoreProvider chatMemoryStoreProvider;

	private McpTransport _getHttpMcpTransport(JSONObject jsonObject) {
		HttpMcpTransport.Builder builder = new HttpMcpTransport.Builder();

		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logRequests");
		SetterUtil.setNotNullBoolean(
			builder::logResponses, jsonObject, "logResponses");
		SetterUtil.setNotBlankString(
			builder::sseUrl, jsonObject.getString("sseUrl"));
		SetterUtil.setNotNullDurationOfSeconds(
			builder::timeout, jsonObject, "timeout");

		return builder.build();
	}

	private ToolProvider _getMCPToolProvider(JSONObject jsonObject) {
		JSONArray clientsJSONArray = jsonObject.getJSONArray("clients");

		if (JSONUtil.isEmpty(clientsJSONArray)) {
			return null;
		}

		List<McpClient> mcpClients = new ArrayList<>();

		for (int i = 0; i < clientsJSONArray.length(); i++) {
			JSONObject clientJSONObject = clientsJSONArray.getJSONObject(i);

			McpTransport mcpTransport = _getMcpTransport(
				clientJSONObject.getJSONObject("transport"));

			if (mcpTransport != null) {
				mcpClients.add(
					new DefaultMcpClient.Builder(
					).transport(
						mcpTransport
					).build());
			}
		}

		if (mcpClients.isEmpty()) {
			return null;
		}

		McpToolProvider.Builder builder = McpToolProvider.builder(
		).mcpClients(
			mcpClients
		);

		SetterUtil.setNotNullBoolean(
			builder::failIfOneServerFails, jsonObject, "failIfOneServerFails");

		return builder.build();
	}

	private McpTransport _getMcpTransport(JSONObject jsonObject) {
		String type = jsonObject.getString("type");

		if (StringUtil.equals(type, "http")) {
			return _getHttpMcpTransport(jsonObject);
		}
		else if (StringUtil.equals(type, "stdio")) {
			return _getStdioMcpTransport(jsonObject);
		}

		return null;
	}

	private McpTransport _getStdioMcpTransport(JSONObject jsonObject) {
		StdioMcpTransport.Builder builder = new StdioMcpTransport.Builder();

		builder.command(
			JSONUtil.toStringList(jsonObject.getJSONArray("command")));

		SetterUtil.setNotNullJSONObjectAsStringMap(
			builder::environment, jsonObject, "environment");
		SetterUtil.setNotNullBoolean(
			builder::logEvents, jsonObject, "logEvents");

		return builder.build();
	}

	private ToolProvider _getToolProvider(JSONObject jsonObject) {
		JSONObject mcpToolProviderJSONObject = jsonObject.getJSONObject("mcp");

		if (mcpToolProviderJSONObject == null) {
			return null;
		}

		return _getMCPToolProvider(mcpToolProviderJSONObject);
	}

	private void _setToolProvider(
		AiServices<?> builder, JSONObject jsonObject) {

		JSONObject toolProviderJSONObject = jsonObject.getJSONObject(
			"toolProvider");

		if (toolProviderJSONObject == null) {
			return;
		}

		ToolProvider toolProvider = _getToolProvider(toolProviderJSONObject);

		if (toolProvider != null) {
			builder.toolProvider(toolProvider);
		}
	}

	private void _setTools(AiServices<?> builder, JSONObject jsonObject) {
		JSONObject toolsJSONObject = jsonObject.getJSONObject("tools");

		if (toolsJSONObject == null) {
			return;
		}

		List<Object> aiTaskTools = new ArrayList<>();

		for (String key : toolsJSONObject.keySet()) {
			Object aiTaskTool = aiToolsProvider.getTool(
				toolsJSONObject.getJSONObject(key), key);

			if (aiTaskTool != null) {
				aiTaskTools.add(aiTaskTool);
			}
		}

		if (aiTaskTools.isEmpty()) {
			return;
		}

		builder.tools(aiTaskTools);
	}

	private void _setWebSearchRetrievalAugmentor(
		AiServices<?> builder, JSONObject jsonObject) {

		JSONObject webSearchRetrievalAugmentorJSONObject =
			jsonObject.getJSONObject("webSearchRetrievalAugmentor");

		if (webSearchRetrievalAugmentorJSONObject == null) {
			return;
		}

		List<ContentRetriever> contentRetrievers = new ArrayList<>();

		for (String key : webSearchRetrievalAugmentorJSONObject.keySet()) {

			// TODO

		}

		if (contentRetrievers.isEmpty()) {
			return;
		}

		builder.retrievalAugmentor(
			DefaultRetrievalAugmentor.builder(
			).queryRouter(
				new DefaultQueryRouter(
					contentRetrievers.toArray(new ContentRetriever[0]))
			).build());
	}

}