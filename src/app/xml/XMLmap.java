package app.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created on 2/13/14.
 */
@XmlRootElement(name = "flowgraph")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlMap {

	@XmlElement(name = "node")
	private ArrayList<Node> nodes = new ArrayList<>();
	@XmlElement(name = "rel")
	private ArrayList<Edge> edges = new ArrayList<>();

	public void setNodes(ArrayList<Node> nodes) { this.nodes = nodes; }
	public void setEdges(ArrayList<Edge> edges) { this.edges = edges; }

	public ArrayList<Node> getNodes() { return nodes; }
	public ArrayList<Edge> getEdges() { return edges; }
}