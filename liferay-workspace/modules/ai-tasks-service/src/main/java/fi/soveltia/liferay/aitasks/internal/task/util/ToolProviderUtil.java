package fi.soveltia.liferay.aitasks.internal.task.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.StringUtil;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;

import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;

import java.time.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petteri Karttunen
 */
public class ToolProviderUtil {

	public static ToolProvider getToolProvider(JSONObject jsonObject) {
		JSONObject mcpToolProviderJSONObject = jsonObject.getJSONObject("mcp");

		if (mcpToolProviderJSONObject == null) {
			return null;
		}

		return _getMCPToolProvider(mcpToolProviderJSONObject);
	}

	private static McpTransport _getHttpMcpTransport(JSONObject jsonObject) {
		HttpMcpTransport.Builder builder = new HttpMcpTransport.Builder();

		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logRequests");
		SetterUtil.setNotNullBoolean(
			builder::logResponses, jsonObject, "logResponses");
		SetterUtil.setNotBlankString(
			builder::sseUrl, jsonObject.getString("sseUrl"));

		if (jsonObject.has("timeout")) {
			builder.timeout(Duration.ofSeconds(jsonObject.getLong("timeout")));
		}

		return builder.build();
	}

	private static ToolProvider _getMCPToolProvider(JSONObject jsonObject) {
		JSONArray clientsJSONArray = jsonObject.getJSONArray("clients");

		if (JSONUtil.isEmpty(clientsJSONArray)) {
			return null;
		}

		List<McpClient> mcpClients = new ArrayList<>();

		for (int i = 0; i < clientsJSONArray.length(); i++) {
			JSONObject clientJSONObject = clientsJSONArray.getJSONObject(i);

			JSONObject transportJSONObject = clientJSONObject.getJSONObject(
				"transport");

			String type = transportJSONObject.getString("type");

			McpTransport mcpTransport = null;

			if (StringUtil.equals(type, "http")) {
				mcpTransport = _getHttpMcpTransport(transportJSONObject);
			}
			else if (StringUtil.equals(type, "stdio")) {
				mcpTransport = _getStdioMcpTransport(transportJSONObject);
			}

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

	private static McpTransport _getStdioMcpTransport(JSONObject jsonObject) {
		StdioMcpTransport.Builder builder = new StdioMcpTransport.Builder();

		builder.command(
			JSONUtil.toStringList(jsonObject.getJSONArray("command")));

		JSONObject environmentJSONObject = jsonObject.getJSONObject(
			"environment");

		if (environmentJSONObject != null) {
			builder.environment(JSONUtil.toStringMap(environmentJSONObject));
		}

		SetterUtil.setNotNullBoolean(
			builder::logEvents, jsonObject, "logEvents");

		return builder.build();
	}

}