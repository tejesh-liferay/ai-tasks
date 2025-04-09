package fi.soveltia.liferay.aitasks.rest.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
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
@GraphQLName("Configuration")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "Configuration")
public class Configuration implements Serializable {

	public static Configuration toDTO(String json) {
		return ObjectMapperUtil.readValue(Configuration.class, json);
	}

	public static Configuration unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(Configuration.class, json);
	}

	@Schema
	@Valid
	public Edge[] getEdges() {
		if (_edgesSupplier != null) {
			edges = _edgesSupplier.get();

			_edgesSupplier = null;
		}

		return edges;
	}

	public void setEdges(Edge[] edges) {
		this.edges = edges;

		_edgesSupplier = null;
	}

	@JsonIgnore
	public void setEdges(
		UnsafeSupplier<Edge[], Exception> edgesUnsafeSupplier) {

		_edgesSupplier = () -> {
			try {
				return edgesUnsafeSupplier.get();
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
	protected Edge[] edges;

	@JsonIgnore
	private Supplier<Edge[]> _edgesSupplier;

	@Schema
	@Valid
	public Node[] getNodes() {
		if (_nodesSupplier != null) {
			nodes = _nodesSupplier.get();

			_nodesSupplier = null;
		}

		return nodes;
	}

	public void setNodes(Node[] nodes) {
		this.nodes = nodes;

		_nodesSupplier = null;
	}

	@JsonIgnore
	public void setNodes(
		UnsafeSupplier<Node[], Exception> nodesUnsafeSupplier) {

		_nodesSupplier = () -> {
			try {
				return nodesUnsafeSupplier.get();
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
	protected Node[] nodes;

	@JsonIgnore
	private Supplier<Node[]> _nodesSupplier;

	@Schema
	public Boolean getTrace() {
		if (_traceSupplier != null) {
			trace = _traceSupplier.get();

			_traceSupplier = null;
		}

		return trace;
	}

	public void setTrace(Boolean trace) {
		this.trace = trace;

		_traceSupplier = null;
	}

	@JsonIgnore
	public void setTrace(
		UnsafeSupplier<Boolean, Exception> traceUnsafeSupplier) {

		_traceSupplier = () -> {
			try {
				return traceUnsafeSupplier.get();
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
	protected Boolean trace;

	@JsonIgnore
	private Supplier<Boolean> _traceSupplier;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Configuration)) {
			return false;
		}

		Configuration configuration = (Configuration)object;

		return Objects.equals(toString(), configuration.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		Edge[] edges = getEdges();

		if (edges != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"edges\": ");

			sb.append("[");

			for (int i = 0; i < edges.length; i++) {
				sb.append(String.valueOf(edges[i]));

				if ((i + 1) < edges.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		Node[] nodes = getNodes();

		if (nodes != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"nodes\": ");

			sb.append("[");

			for (int i = 0; i < nodes.length; i++) {
				sb.append(String.valueOf(nodes[i]));

				if ((i + 1) < nodes.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		Boolean trace = getTrace();

		if (trace != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"trace\": ");

			sb.append(trace);
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "fi.soveltia.liferay.aitasks.rest.dto.v1_0.Configuration",
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