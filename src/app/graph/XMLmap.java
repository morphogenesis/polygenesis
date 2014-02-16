package app.graph;

import app.graph.GEdge;
import app.graph.GNode;

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
public class XMLmap {

	@XmlElement(name = "node")
	private ArrayList<GNode> nodes = new ArrayList<>();
	@XmlElement(name = "rel")
	private ArrayList<GEdge> edges = new ArrayList<>();

	public void setNodes(ArrayList<GNode> nodes) { this.nodes = nodes; }
	public void setEdges(ArrayList<GEdge> edges) { this.edges = edges; }


	public ArrayList<GNode> getNodes() { return nodes; }
	public ArrayList<GEdge> getEdges() { return edges; }
}
/*	@XmlTransient
	private HashMap<Integer, ArrayList<Node>> relationIndex = new HashMap<>();
	@XmlTransient
	private HashMap<Integer, Node> nodeIndex = new HashMap<>();*/

//	public void setRelationIndex(HashMap<Integer, ArrayList<Node>> relationIndex) {this.relationIndex = relationIndex;}
//	public void setNodeIndex(HashMap<Integer, Node> nodeIndex) {this.nodeIndex = nodeIndex;}
//	public HashMap<Integer, ArrayList<Node>> getRelationIndex() { return relationIndex; }
//	public HashMap<Integer, Node> getNodeIndex() { return nodeIndex; }