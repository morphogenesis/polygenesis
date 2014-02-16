package app.graph;

import app.core.App;
import app.core.Settings;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created on 2/13/14.
 */
public class Graph {
	private static XMLmap Map;
	private static HashMap<Integer, GNode> nodeIndex = new HashMap<>();
	private static HashMap<Integer, ArrayList<GNode>> edgeIndex = new HashMap<>();
	public ArrayList<GNode> nodes;
	public ArrayList<GEdge> edges;

	public Graph() {
		Map = new XMLmap();
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
//		XMLtool.unmarshal();
	}

	public void build() {
		Map = new XMLmap();
		Map.setNodes(nodes);
		Map.setEdges(edges);
		edgeIndex = new HashMap<>();
		nodeIndex = new HashMap<>();
		for (GNode n : nodes) { nodeIndex.put(n.getId(), n); }
		for (GEdge e : edges) {
			ArrayList<GNode> nlist = edgeIndex.get(e.getFrom());
			if (nlist == null) {
				nlist = new ArrayList<>();
				edgeIndex.put(e.getFrom(), nlist);
			}
			nlist.add(nodeIndex.get(e.getTo()));
			System.out.println();
		} marshal();
	}
	public static void marshal() {
		try {
			File file = new File(App.filepath);
			JAXBContext jc = JAXBContext.newInstance(XMLmap.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(Graph.getMap(), System.out);
			m.marshal(Graph.getMap(), file);
//			m.marshal(Graph.getMap(), new File("./data/graphtest.xml"));
		} catch (JAXBException e) { e.printStackTrace(); }
	}

	//	/************************************************* XML READER ******************************************/
	public static XMLmap unmarshal() {
		XMLmap map = new XMLmap();
		try {
			JAXBContext jc = JAXBContext.newInstance(XMLmap.class);
			Unmarshaller m = jc.createUnmarshaller();
			map = (XMLmap) m.unmarshal(new File(App.filepath));
//			map = (XMLmap) m.unmarshal(new File("./data/graphtest.xml"));
			for (GNode n : map.getNodes()) { System.out.println(n.getId() + "::" + n.getName() + "=>" + n.getSize()); }
			setMap(map);
		} catch (JAXBException e) { e.printStackTrace(); } return map;
	}
	public void rebuild() {
		unmarshal();
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
		App.PSYS.reset();
		edgeIndex = new HashMap<>();
		nodeIndex = new HashMap<>();
		Settings X = App.CONF;
		for (GNode n : Map.getNodes()) {
			n.setParticle2D(new VerletParticle2D(n.getX(), n.getY()));
			n.setBehavior2D(new AttractionBehavior2D(n.getParticle2D(), n.getRadius(), -1));
			n.update(X.particleWeight, X.behaviorScale, X.behaviorStrength);
			nodes.add(n);
			nodeIndex.put(n.getId(), n);
			App.PSYS.addParticle(n);
		} for (GEdge e : Map.getEdges()) {
			e.setSpring2D(new VerletSpring2D(e.getNodeA().getParticle2D(), e.getNodeB().getParticle2D(), e.getLength(), 0.001f));
			e.update(X.springStrength, X.springScale);
			edges.add(e);
			App.PSYS.addSpring(e);
			ArrayList<GNode> nlist = edgeIndex.get(e.getFrom());
			if (nlist == null) { nlist = new ArrayList<>(); edgeIndex.put(e.getFrom(), nlist); }
			nlist.add(nodeIndex.get(e.getTo()));
		}
		build();
	}

	public void addNode(GNode n) {
		nodes.add(n);
		App.PSYS.addParticle(n);
		build();
	}
	public void addEdge(GEdge e) {
		edges.add(e);
		App.PSYS.addSpring(e);
		build();
	}
	public void removeNode(GNode n) {
		nodes.remove(n);
		nodeIndex.remove(n.getId());
		App.PSYS.removeParticle(n);
	}
	public void removeEdge(GEdge e) {
		edges.remove(e);
		edgeIndex.remove(e.getFrom());
		App.PSYS.removeSpring(e);
	}
	public void addNodes(ArrayList<GNode> nodes) { for (GNode n : nodes) { addNode(n); }}
	public void addEdges(ArrayList<GEdge> edges) { for (GEdge e : edges) {addEdge(e);} }

	public static XMLmap getMap() { return Map; }
	public static void setMap(XMLmap Map) { Graph.Map = Map; }
	public HashMap<Integer, ArrayList<GNode>> getEdgeIndex() { return edgeIndex; }
	public HashMap<Integer, GNode> getNodeIndex() { return nodeIndex; }
	public void setNodes(ArrayList<GNode> nodes) { this.nodes = nodes; }
	public void setEdges(ArrayList<GEdge> edges) { this.edges = edges; }
	public static GNode getNode(int id) {return nodeIndex.get(id);}
	public void reset() {
		nodes.clear(); edges.clear(); build();
	}
}
