package fi.soveltia.liferay.aitasks.internal.task.node.condition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class AITaskNodeConditionEvaluationResponse {

	public AITaskNodeConditionEvaluationResponse(
		Map<String, Object> executionTrace, boolean valid) {

		_executionTrace = executionTrace;
		_valid = valid;
	}

	public Map<String, Object> getExecutionTrace() {
		if (_executionTrace == null) {
			_executionTrace = new HashMap<>();
		}

		return _executionTrace;
	}

	public boolean isValid() {
		return _valid;
	}

	private Map<String, Object> _executionTrace;
	private boolean _valid;

}