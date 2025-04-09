
package fi.soveltia.liferay.aitasks.internal.task.node.condition;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import fi.soveltia.liferay.aitasks.rest.dto.v1_0.Condition;
import fi.soveltia.liferay.aitasks.rest.dto.v1_0.Contains;
import fi.soveltia.liferay.aitasks.rest.dto.v1_0.Equals;
import fi.soveltia.liferay.aitasks.rest.dto.v1_0.Exists;
import fi.soveltia.liferay.aitasks.rest.dto.v1_0.In;
import fi.soveltia.liferay.aitasks.rest.dto.v1_0.Range;
import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.context.AITaskContextParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Petteri Karttunen
 */
public class AITaskNodeConditionEvaluator {

	public AITaskNodeConditionEvaluator(
		AITaskContext aiTaskContext, boolean trace) {

		_aiTaskContext = aiTaskContext;
		_trace = trace;
	}

	public AITaskNodeConditionEvaluationResponse evaluate(Condition condition) {
		if (!_evaluate(condition)) {
			return new AITaskNodeConditionEvaluationResponse(
				_executionTrace, false);
		}

		return new AITaskNodeConditionEvaluationResponse(_executionTrace, true);
	}

	private boolean _contains(
		Object field, String fieldName, String id, Object value) {

		if (field == null) {
			return false;
		}

		if (field instanceof double[]) {
			double doubleValue = GetterUtil.getDouble(value);

			for (double d : (double[])field) {
				if (d == doubleValue) {
					return _withExecutionTrace(id, true, fieldName, value);
				}
			}
		}
		else if (field instanceof float[]) {
			float floatValue = GetterUtil.getFloat(value);

			for (float f : (float[])field) {
				if (f == floatValue) {
					return _withExecutionTrace(id, true, fieldName, value);
				}
			}
		}
		else if (field instanceof int[]) {
			int intValue = GetterUtil.getInteger(value);

			for (int i : (int[])field) {
				if (i == intValue) {
					return _withExecutionTrace(id, true, fieldName, value);
				}
			}
		}
		else if (field instanceof long[]) {
			long longValue = GetterUtil.getLong(value);

			for (long l : (long[])field) {
				if (l == longValue) {
					return _withExecutionTrace(id, true, fieldName, value);
				}
			}
		}
		else if (field instanceof String[]) {
			String stringValue = GetterUtil.getString(value);

			for (String s : (String[])field) {
				if (Objects.equals(s, stringValue)) {
					return _withExecutionTrace(id, true, fieldName, value);
				}
			}
		}
		else if (field instanceof String) {
			String stringField = (String)field;

			return _withExecutionTrace(
				id, stringField.contains(GetterUtil.getString(value)),
				fieldName, value);
		}

		return _withExecutionTrace(id, false, fieldName, value);
	}

	private boolean _evaluate(Condition condition) {
		if (!_evaluateAllConditions(condition.getAllConditions()) ||
			!_evaluateAnyConditions(condition.getAnyConditions()) ||
			!_evaluateContains(condition.getContains()) ||
			!_evaluateEquals(condition.getEquals()) ||
			!_evaluateExists(condition.getExists()) ||
			!_evaluateIn(condition.getIn()) ||
			!_evaluateNot(condition.getNot()) ||
			!_evaluateRange(condition.getRange())) {

			return false;
		}

		return true;
	}

	private boolean _evaluateAllConditions(Condition[] conditions) {
		if (conditions == null) {
			return true;
		}

		for (Condition condition : conditions) {
			if (!_evaluate(condition)) {
				return false;
			}
		}

		return true;
	}

	private boolean _evaluateAnyConditions(Condition[] conditions) {
		if (conditions == null) {
			return true;
		}

		for (Condition condition : conditions) {
			if (_evaluate(condition)) {
				return true;
			}
		}

		return false;
	}

	private boolean _evaluateContains(Contains contains) {
		if (contains == null) {
			return true;
		}

		if (_contains(
				_getField(contains.getField()), contains.getField(),
				contains.getId(), contains.getValue())) {

			return _withExecutionTrace(
				contains.getId(), true, contains.getField(),
				contains.getValue());
		}

		return _withExecutionTrace(
			contains.getId(), false, contains.getField(), contains.getValue());
	}

	private boolean _evaluateEquals(Equals equals) {
		if (equals == null) {
			return true;
		}

		Object field = _getField(equals.getField());

		if (field == null) {
			return false;
		}

		Object value = equals.getValue();

		if (field instanceof Double) {
			if (_withExecutionTrace(
					equals.getId(),
					GetterUtil.getDouble(field) == GetterUtil.getDouble(value),
					equals.getField(), value)) {

				return true;
			}

			return false;
		}
		else if (field instanceof Double[]) {
			return _withExecutionTrace(
				equals.getId(),
				Arrays.equals(
					GetterUtil.getDoubleValues(field),
					GetterUtil.getDoubleValues(value)),
				equals.getField(), value);
		}
		else if (field instanceof Float) {
			if (_withExecutionTrace(
					equals.getId(),
					GetterUtil.getFloat(field) == GetterUtil.getFloat(value),
					equals.getField(), value)) {

				return true;
			}

			return false;
		}
		else if (field instanceof Float[]) {
			return _withExecutionTrace(
				equals.getId(),
				Arrays.equals(
					GetterUtil.getFloatValues(field),
					GetterUtil.getFloatValues(value)),
				equals.getField(), value);
		}
		else if (field instanceof Integer) {
			if (_withExecutionTrace(
					equals.getId(),
					GetterUtil.getInteger(field) == GetterUtil.getInteger(
						value),
					equals.getField(), value)) {

				return true;
			}

			return false;
		}
		else if (field instanceof Integer[]) {
			return _withExecutionTrace(
				equals.getId(),
				Arrays.equals(
					GetterUtil.getIntegerValues(field),
					GetterUtil.getIntegerValues(value)),
				equals.getField(), value);
		}
		else if (field instanceof Long) {
			if (_withExecutionTrace(
					equals.getId(),
					GetterUtil.getLong(field) == GetterUtil.getLong(value),
					equals.getField(), value)) {

				return true;
			}

			return false;
		}
		else if (field instanceof Long[]) {
			return _withExecutionTrace(
				equals.getId(),
				Arrays.equals(
					GetterUtil.getLongValues(field),
					GetterUtil.getLongValues(value)),
				equals.getField(), value);
		}
		else if (field instanceof String) {
			return _withExecutionTrace(
				equals.getId(),
				StringUtil.equals(
					GetterUtil.getString(field), GetterUtil.getString(value)),
				equals.getField(), value);
		}
		else if (field instanceof String[]) {
			return _withExecutionTrace(
				equals.getId(),
				Arrays.equals(
					GetterUtil.getStringValues(field),
					GetterUtil.getStringValues(value)),
				equals.getField(), value);
		}

		return _withExecutionTrace(
			equals.getId(), false, equals.getField(), value);
	}

	private boolean _evaluateExists(Exists exists) {
		if (exists == null) {
			return true;
		}

		if (Objects.isNull(_getField(exists.getField()))) {
			return _withExecutionTrace(
				exists.getId(), false, exists.getField(), null);
		}

		return _withExecutionTrace(
			exists.getId(), true, exists.getField(), null);
	}

	private boolean _evaluateIn(In in) {
		if (in == null) {
			return true;
		}

		if (_contains(
				in.getValue(), in.getField(), in.getId(),
				_getField(in.getField()))) {

			return _withExecutionTrace(
				in.getId(), true, in.getField(), in.getValue());
		}

		return _withExecutionTrace(
			in.getId(), false, in.getField(), in.getValue());
	}

	private boolean _evaluateNot(Condition condition) {
		if (condition == null) {
			return true;
		}

		if (_evaluate(condition)) {
			return false;
		}

		return true;
	}

	private boolean _evaluateRange(Range range) {
		if (range == null) {
			return true;
		}

		throw new UnsupportedOperationException("Not implemented");
	}

	private Object _getField(String field) {
		if (Validator.isBlank(field)) {
			return true;
		}

		field = field.replaceAll("\\{\\{", "");
		field = field.replaceAll("\\}\\}", "");

		if (field.startsWith("input")) {
			return _aiTaskContext.getIfMapParameter(field);
		}

		if (field.startsWith("taskContext")) {
			String fieldName = field.substring("taskContext".length() + 1);

			AITaskContextParameter aiTaskContextParameter =
				_aiTaskContext.getAITaskContextParameter(fieldName);

			if (aiTaskContextParameter != null) {
				return aiTaskContextParameter.getValue();
			}
		}

		return null;
	}

	private boolean _withExecutionTrace(
		String id, boolean evaluationResult, String field, Object value) {

		if (_trace) {
			_executionTrace.put(
				id,
				HashMapBuilder.<String, Object>put(
					"field", field
				).put(
					"result", evaluationResult
				).put(
					"value", value
				).build());
		}

		return evaluationResult;
	}

	private AITaskContext _aiTaskContext;
	private Map<String, Object> _executionTrace = new HashMap<>();
	private boolean _trace;

}