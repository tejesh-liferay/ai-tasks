package fi.soveltia.liferay.aitasks.internal.task.node;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.CamelCaseUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;

import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.context.AITaskContextParameter;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseAITaskNode {

	protected Map<String, Object> getExecutionTrace(
		FinishReason finishReason, TokenUsage tokenUsage) {

		Map<String, Object> executionTrace = new HashMap<>();

		if (finishReason != null) {
			executionTrace.put("finishReason", finishReason.name());
		}

		if (tokenUsage != null) {
			executionTrace.put(
				"tokenUsage",
				HashMapBuilder.put(
					"inputTokenCount", tokenUsage.inputTokenCount()
				).put(
					"outputTokenCount", tokenUsage.outputTokenCount()
				).put(
					"totalTokenCount", tokenUsage.totalTokenCount()
				).build());
		}

		return executionTrace;
	}

	protected Map<String, Object> getExecutionTrace(Result<?> result) {
		if (result == null) {
			return null;
		}

		Map<String, Object> executionTrace = new HashMap<>();

		executionTrace.put("content", result.content());

		List<ToolExecution> toolExecutions = result.toolExecutions();

		if (ListUtil.isNotEmpty(toolExecutions)) {
			executionTrace.put("toolExecutions", _stringify(toolExecutions));
		}

		executionTrace.putAll(
			getExecutionTrace(result.finishReason(), result.tokenUsage()));

		return executionTrace;
	}

	protected AITaskNodeResponse toAITaskNodeResponse(
		AITaskContext aiTaskContext, Map<String, Object> executionTrace,
		JSONObject jsonObject, boolean trace, Object value) {

		if (value == null) {
			return new AITaskNodeResponseImpl(executionTrace, null);
		}

		if (value instanceof Result) {
			Result<?> result = (Result)value;

			value = result.content();
		}

		Map<String, Object> output = new HashMap<>();

		if (value instanceof String) {
			String stringValue = StringUtil.trim((String)value);

			String think = _getThink(stringValue);

			if (think != null) {
				output.put("think", think);

				value = stringValue.split("</think>")[1];
			}
		}

		String taskContextOutputParameterName = jsonObject.getString(
			"taskContextOutputParameterName");

		if ((aiTaskContext == null) ||
			Validator.isBlank(taskContextOutputParameterName)) {

			String outputParameterName = jsonObject.getString(
				"outputParameterName");

			if (!Validator.isBlank(outputParameterName)) {
				output.put(outputParameterName, value);
			}

			return new AITaskNodeResponseImpl(executionTrace, output);
		}

		aiTaskContext.addAITaskContextParameter(
			new AITaskContextParameter(
				CamelCaseUtil.normalizeCamelCase(
					taskContextOutputParameterName),
				toStringValue(value), value),
			taskContextOutputParameterName);

		if (trace) {
			executionTrace.put(
				"taskContextOutputParameterName",
				taskContextOutputParameterName);
			executionTrace.put("taskContextOutputParameterValue", value);
		}

		return new AITaskNodeResponseImpl(executionTrace, null);
	}

	protected AITaskNodeResponse toExceptionAITaskNodeResponse(
		Exception exception) {

		return new AITaskNodeResponseImpl(
			HashMapBuilder.<String, Object>put(
				"exception", exception.getLocalizedMessage()
			).build(),
			null);
	}

	protected AITaskNodeResponse toStreamingAITaskNodeResponse(
		JSONObject jsonObject, TokenStream tokenStream) {

		return new AITaskNodeResponseImpl(
			null,
			HashMapBuilder.<String, Object>put(
				jsonObject.getString("outputParameterName", "text"), tokenStream
			).build());
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

	private String _getThink(String value) {
		Document document = Jsoup.parse(value);

		Elements elements = document.select("think");

		if (elements.size() == 1) {
			Element element = elements.get(0);

			return element.text();
		}

		return null;
	}

	private Object _stringify(List<ToolExecution> toolExecutions) {
		List<String> list = new ArrayList<>();

		for (ToolExecution toolExecution : toolExecutions) {
			list.add(toolExecution.toString());
		}

		return list;
	}

}