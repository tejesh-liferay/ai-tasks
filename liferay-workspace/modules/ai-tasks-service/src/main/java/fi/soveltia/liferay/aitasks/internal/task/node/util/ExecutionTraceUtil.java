package fi.soveltia.liferay.aitasks.internal.task.node.util;

import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;

import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.tool.ToolExecution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class ExecutionTraceUtil {

	public static Map<String, Object> getExecutionTrace(
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

	public static Map<String, Object> getExecutionTrace(Result<?> result) {
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

	private static Object _stringify(List<ToolExecution> toolExecutions) {
		List<String> list = new ArrayList<>();

		for (ToolExecution toolExecution : toolExecutions) {
			list.add(toolExecution.toString());
		}

		return list;
	}

}