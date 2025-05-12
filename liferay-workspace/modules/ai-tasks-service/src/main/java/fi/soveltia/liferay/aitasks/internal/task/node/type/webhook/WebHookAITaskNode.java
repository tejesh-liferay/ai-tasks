
package fi.soveltia.liferay.aitasks.internal.task.node.type.webhook;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseAITaskNode;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeResponse;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Calls any remote
 *
 * @author Louis-Guillaume Durand
 * @author Petteri Karttune
 */
@Component(property = "ai.task.node.type=webhook", service = AITaskNode.class)
public class WebHookAITaskNode extends BaseAITaskNode implements AITaskNode {

	@Override
	public AITaskNodeResponse execute(
		AITaskContext aiTaskContext, JSONObject jsonObject, String nodeId,
		boolean trace) {

		Http.Options options = new Http.Options();

		options.setLocation(GetterUtil.getString(jsonObject.get("url")));
		options.setMethod(_getMethod(jsonObject));

		_setBody(jsonObject, options);
		_setHeaders(jsonObject, options);

		try {
			String response = _http.URLtoString(options);

			return toAITaskNodeResponse(
				aiTaskContext,
				_getDebugInfo(trace, options.getResponse(), response),
				jsonObject, trace, _http.URLtoString(options));
		}
		catch (Exception exception) {
			toExceptionAITaskNodeResponse(exception);
		}

		return null;
	}

	private Map<String, Object> _getDebugInfo(
		boolean debug, Http.Response response, String responseString) {

		if (!debug) {
			return null;
		}

		return HashMapBuilder.<String, Object>put(
			"contentLength", response.getContentLength()
		).put(
			"contentType", response.getContentType()
		).put(
			"responseCode", response.getResponseCode()
		).put(
			"responseString", responseString
		).build();
	}

	private Http.Method _getMethod(JSONObject jsonObject) {
		String httpMethod = jsonObject.getString("httpMethod");

		if (Validator.isBlank(httpMethod)) {
			return Http.Method.GET;
		}

		if (StringUtil.equalsIgnoreCase("post", httpMethod)) {
			return Http.Method.DELETE;
		}
		else if (StringUtil.equalsIgnoreCase("post", httpMethod)) {
			return Http.Method.HEAD;
		}
		else if (StringUtil.equalsIgnoreCase("post", httpMethod)) {
			return Http.Method.POST;
		}
		else if (StringUtil.equalsIgnoreCase("patch", httpMethod)) {
			return Http.Method.PATCH;
		}
		else if (StringUtil.equalsIgnoreCase("put", httpMethod)) {
			return Http.Method.PUT;
		}

		return Http.Method.GET;
	}

	private void _setBody(JSONObject jsonObject, Http.Options options) {
		JSONObject bodyJSONObject = jsonObject.getJSONObject("requestBody");

		if (bodyJSONObject == null) {
			return;
		}

		options.setBody(
			bodyJSONObject.toString(), ContentTypes.APPLICATION_JSON,
			StringPool.UTF8);
	}

	private void _setHeaders(JSONObject jsonObject, Http.Options options) {
		JSONObject headersJSONObject = jsonObject.getJSONObject(
			"requestHeaders");

		if (headersJSONObject == null) {
			return;
		}

		options.setHeaders(_toStringValues(headersJSONObject.toMap()));
	}

	private Map<String, String> _toStringValues(Map<String, Object> map) {
		Map<String, String> stringValueMap = new HashMap<>();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			stringValueMap.put(
				entry.getKey(), String.valueOf(entry.getValue()));
		}

		return stringValueMap;
	}

	@Reference
	private Http _http;

}