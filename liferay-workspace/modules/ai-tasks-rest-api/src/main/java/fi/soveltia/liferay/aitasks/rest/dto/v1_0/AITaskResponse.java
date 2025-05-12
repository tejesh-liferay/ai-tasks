package fi.soveltia.liferay.aitasks.rest.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Generated;

import javax.validation.Valid;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petteri Karttunen
 * @generated
 */
@Generated("")
@GraphQLName("AITaskResponse")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "AITaskResponse")
public class AITaskResponse implements Serializable {

	public static AITaskResponse toDTO(String json) {
		return ObjectMapperUtil.readValue(AITaskResponse.class, json);
	}

	public static AITaskResponse unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(AITaskResponse.class, json);
	}

	@Schema
	@Valid
	public Object getExecutionTrace() {
		if (_executionTraceSupplier != null) {
			executionTrace = _executionTraceSupplier.get();

			_executionTraceSupplier = null;
		}

		return executionTrace;
	}

	public void setExecutionTrace(Object executionTrace) {
		this.executionTrace = executionTrace;

		_executionTraceSupplier = null;
	}

	@JsonIgnore
	public void setExecutionTrace(
		UnsafeSupplier<Object, Exception> executionTraceUnsafeSupplier) {

		_executionTraceSupplier = () -> {
			try {
				return executionTraceUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Object executionTrace;

	@JsonIgnore
	private Supplier<Object> _executionTraceSupplier;

	@Schema
	@Valid
	public Object getOutput() {
		if (_outputSupplier != null) {
			output = _outputSupplier.get();

			_outputSupplier = null;
		}

		return output;
	}

	public void setOutput(Object output) {
		this.output = output;

		_outputSupplier = null;
	}

	@JsonIgnore
	public void setOutput(
		UnsafeSupplier<Object, Exception> outputUnsafeSupplier) {

		_outputSupplier = () -> {
			try {
				return outputUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Object output;

	@JsonIgnore
	private Supplier<Object> _outputSupplier;

	@Schema
	public String getTook() {
		if (_tookSupplier != null) {
			took = _tookSupplier.get();

			_tookSupplier = null;
		}

		return took;
	}

	public void setTook(String took) {
		this.took = took;

		_tookSupplier = null;
	}

	@JsonIgnore
	public void setTook(UnsafeSupplier<String, Exception> tookUnsafeSupplier) {
		_tookSupplier = () -> {
			try {
				return tookUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String took;

	@JsonIgnore
	private Supplier<String> _tookSupplier;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AITaskResponse)) {
			return false;
		}

		AITaskResponse aiTaskResponse = (AITaskResponse)object;

		return Objects.equals(toString(), aiTaskResponse.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		Object executionTrace = getExecutionTrace();

		if (executionTrace != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"executionTrace\": ");

			if (executionTrace instanceof Map) {
				sb.append(
					JSONFactoryUtil.createJSONObject(
						(Map<?, ?>)executionTrace));
			}
			else if (executionTrace instanceof String) {
				sb.append("\"");
				sb.append(_escape((String)executionTrace));
				sb.append("\"");
			}
			else {
				sb.append(executionTrace);
			}
		}

		Object output = getOutput();

		if (output != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"output\": ");

			if (output instanceof Map) {
				sb.append(JSONFactoryUtil.createJSONObject((Map<?, ?>)output));
			}
			else if (output instanceof String) {
				sb.append("\"");
				sb.append(_escape((String)output));
				sb.append("\"");
			}
			else {
				sb.append(output);
			}
		}

		String took = getTook();

		if (took != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"took\": ");

			sb.append("\"");

			sb.append(_escape(took));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "fi.soveltia.liferay.aitasks.rest.dto.v1_0.AITaskResponse",
		name = "x-class-name"
	)
	public String xClassName;

	private static String _escape(Object object) {
		return StringUtil.replace(
			String.valueOf(object), _JSON_ESCAPE_STRINGS[0],
			_JSON_ESCAPE_STRINGS[1]);
	}

	private static boolean _isArray(Object value) {
		if (value == null) {
			return false;
		}

		Class<?> clazz = value.getClass();

		return clazz.isArray();
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(_escape(entry.getKey()));
			sb.append("\": ");

			Object value = entry.getValue();

			if (_isArray(value)) {
				sb.append("[");

				Object[] valueArray = (Object[])value;

				for (int i = 0; i < valueArray.length; i++) {
					if (valueArray[i] instanceof Map) {
						sb.append(_toJSON((Map<String, ?>)valueArray[i]));
					}
					else if (valueArray[i] instanceof String) {
						sb.append("\"");
						sb.append(valueArray[i]);
						sb.append("\"");
					}
					else {
						sb.append(valueArray[i]);
					}

					if ((i + 1) < valueArray.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof Map) {
				sb.append(_toJSON((Map<String, ?>)value));
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(value));
				sb.append("\"");
			}
			else {
				sb.append(value);
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static final String[][] _JSON_ESCAPE_STRINGS = {
		{"\\", "\"", "\b", "\f", "\n", "\r", "\t"},
		{"\\\\", "\\\"", "\\b", "\\f", "\\n", "\\r", "\\t"}
	};

	private Map<String, Serializable> _extendedProperties;

}