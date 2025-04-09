
package fi.soveltia.liferay.aitasks.internal.task.node;

import fi.soveltia.liferay.aitasks.task.node.AITaskNodeResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class AITaskNodeResponseImpl implements AITaskNodeResponse {

	public AITaskNodeResponseImpl(
		Map<String, Object> executionTrace, Map<String, Object> output) {

		_executionTrace = executionTrace;
		_output = output;
	}

	@Override
	public Map<String, Object> getExecutionTrace() {
		if (_executionTrace == null) {
			_executionTrace = new HashMap<>();
		}

		return _executionTrace;
	}

	@Override
	public Map<String, Object> getOutput() {
		if (_output == null) {
			_output = new HashMap<>();
		}

		return _output;
	}

	private Map<String, Object> _executionTrace;
	private Map<String, Object> _output;

}