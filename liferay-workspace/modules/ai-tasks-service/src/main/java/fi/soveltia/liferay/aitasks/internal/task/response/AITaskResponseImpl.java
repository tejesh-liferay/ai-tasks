

package fi.soveltia.liferay.aitasks.internal.task.response;

import fi.soveltia.liferay.aitasks.task.response.AITaskResponse;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class AITaskResponseImpl implements AITaskResponse {

	public AITaskResponseImpl(
		Map<String, Map<String, Object>> executionTrace,
		Map<String, Object> output, String took) {

		_executionTrace = executionTrace;
		_output = output;
		_took = took;
	}

	@Override
	public Map<String, Map<String, Object>> getExecutionTrace() {
		return _executionTrace;
	}

	@Override
	public Map<String, Object> getOutput() {
		return _output;
	}

	@Override
	public String getTook() {
		return _took;
	}

	private final Map<String, Map<String, Object>> _executionTrace;
	private final Map<String, Object> _output;
	private final String _took;

}