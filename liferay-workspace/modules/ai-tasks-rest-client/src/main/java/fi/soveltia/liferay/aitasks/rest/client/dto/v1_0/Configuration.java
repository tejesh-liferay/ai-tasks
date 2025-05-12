package fi.soveltia.liferay.aitasks.rest.client.dto.v1_0;

import fi.soveltia.liferay.aitasks.rest.client.dto.v1_0.Edge;
import fi.soveltia.liferay.aitasks.rest.client.dto.v1_0.Node;
import fi.soveltia.liferay.aitasks.rest.client.function.UnsafeSupplier;
import fi.soveltia.liferay.aitasks.rest.client.serdes.v1_0.ConfigurationSerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Petteri Karttunen
 * @generated
 */
@Generated("")
public class Configuration implements Cloneable, Serializable {

	public static Configuration toDTO(String json) {
		return ConfigurationSerDes.toDTO(json);
	}

	public Edge[] getEdges() {
		return edges;
	}

	public void setEdges(Edge[] edges) {
		this.edges = edges;
	}

	public void setEdges(
		UnsafeSupplier<Edge[], Exception> edgesUnsafeSupplier) {

		try {
			edges = edgesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Edge[] edges;

	public Node[] getNodes() {
		return nodes;
	}

	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}

	public void setNodes(
		UnsafeSupplier<Node[], Exception> nodesUnsafeSupplier) {

		try {
			nodes = nodesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Node[] nodes;

	public Boolean getTrace() {
		return trace;
	}

	public void setTrace(Boolean trace) {
		this.trace = trace;
	}

	public void setTrace(
		UnsafeSupplier<Boolean, Exception> traceUnsafeSupplier) {

		try {
			trace = traceUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean trace;

	@Override
	public Configuration clone() throws CloneNotSupportedException {
		return (Configuration)super.clone();
	}

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
		return ConfigurationSerDes.toJSON(this);
	}

}