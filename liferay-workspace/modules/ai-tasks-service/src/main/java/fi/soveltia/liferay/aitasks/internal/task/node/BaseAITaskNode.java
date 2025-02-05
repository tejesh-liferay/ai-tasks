package fi.soveltia.liferay.aitasks.internal.task.node;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.CamelCaseUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.Result;

import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.context.AITaskContextParameter;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseAITaskNode {

	protected Map<String, Object> getCommonDebugInfo(
		FinishReason finishReason, TokenUsage tokenUsage) {

		Map<String, Object> debugInfo = new HashMap<>();

		if (finishReason != null) {
			debugInfo.put("finishReason", finishReason.name());
		}

		if (tokenUsage != null) {
			debugInfo.put("inputTokenCount", tokenUsage.inputTokenCount());
			debugInfo.put("outputTokenCount", tokenUsage.outputTokenCount());
			debugInfo.put("totalTokenCount", tokenUsage.totalTokenCount());
		}

		return debugInfo;
	}

	protected AITaskNodeResponse toAITaskNodeResponse(
		AITaskContext aiTaskContext, boolean debug,
		Map<String, Object> debugInfo, JSONObject jsonObject, Object value) {

		if (value == null) {
			return new AITaskNodeResponseImpl(debugInfo, null);
		}

		if (value instanceof Result) {
			Result<?> result = (Result)value;

			value = result.content();
		}

		if (value instanceof String) {
			value = StringUtil.trim((String)value);
		}

		String taskContextOutputParameterName = jsonObject.getString(
			"taskContextOutputParameterName");

		if ((aiTaskContext == null) ||
			Validator.isBlank(taskContextOutputParameterName)) {

			return new AITaskNodeResponseImpl(
				debugInfo,
				HashMapBuilder.put(
					jsonObject.getString("outputParameterName", "text"), value
				).build());
		}

		aiTaskContext.addAITaskContextParameter(
			new AITaskContextParameter(
				CamelCaseUtil.normalizeCamelCase(
					taskContextOutputParameterName),
				toStringValue(value), value),
			taskContextOutputParameterName);

		if (debug) {
			debugInfo.put(
				"taskContextOutputParameterName",
				taskContextOutputParameterName);
			debugInfo.put("taskContextOutputParameterValue", value);
		}

		return new AITaskNodeResponseImpl(debugInfo, null);
	}

	protected AITaskNodeResponse toExceptionAITaskNodeResponse(
		Exception exception) {

		return new AITaskNodeResponseImpl(
			HashMapBuilder.<String, Object>put(
				"exception", exception.getLocalizedMessage()
			).build(),
			null);
	}

	protected String toStringValue(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return (String)value;
		}

		return String.valueOf(value);
	}

}