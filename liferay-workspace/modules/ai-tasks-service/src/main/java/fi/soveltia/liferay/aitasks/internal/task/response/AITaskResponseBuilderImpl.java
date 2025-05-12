

package fi.soveltia.liferay.aitasks.internal.task.response;

import fi.soveltia.liferay.aitasks.task.response.AITaskResponse;
import fi.soveltia.liferay.aitasks.task.response.AITaskResponseBuilder;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(service = AITaskResponseBuilder.class)
public class AITaskResponseBuilderImpl implements AITaskResponseBuilder {

	@Override
	public AITaskResponseBuilder addExecutionTrace(
		Map<String, Object> executionTrace, String id) {

		if (_executionTrace == null) {
			_executionTrace = new HashMap<>();
		}

		if (_executionTrace.containsKey(id)) {
			Map<String, Object> map = _executionTrace.get(id);

			if (map == null) {
				map = new HashMap<>();
			}

			map.putAll(executionTrace);
		}
		else {
			_executionTrace.put(id, executionTrace);
		}

		return this;
	}

	@Override
	public AITaskResponseBuilder addOutput(String key, Object value) {
		if (_output == null) {
			_output = new HashMap<>();
		}

		_output.put(key, value);

		return this;
	}

	@Override
	public AITaskResponse build() {
		return new AITaskResponseImpl(_executionTrace, _output, _took);
	}

	@Override
	public AITaskResponseBuilder took(String took) {
		_took = took;

		return this;
	}

	private Map<String, Map<String, Object>> _executionTrace;
	private Map<String, Object> _output;
	private String _took;

}