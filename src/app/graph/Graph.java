package app.graph;

import app.core.App;
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
	private static XMLmap Map = new XMLmap();
	private static HashMap<Integer, Node> nodeIndex = new HashMap<>();
	private static HashMap<Integer, ArrayList<Node>> edgeIndex = new HashMap<>();
	public static ArrayList<Node> nodes;
	public static ArrayList<Edge> edges;
	public static float totalArea = 0;
	public Graph() {
		nodes = new ArrayList<>(); edges = new ArrayList<>();
	}
	public static void build() {
		Map = new XMLmap();
		Map.setNodes(nodes);
		Map.setEdges(edges);
		edgeIndex = new HashMap<>();
		nodeIndex = new HashMap<>();
		for (Node n : nodes) {totalArea += n.getSize(); n.setId(nodes.indexOf(n)); nodeIndex.put(n.getId(), n); }
		for (Edge e : edges) {
			ArrayList<Node> nlist = edgeIndex.get(e.getFrom());
			if (nlist == null) { nlist = new ArrayList<>(); edgeIndex.put(e.getFrom(), nlist); }
			nlist.add(nodeIndex.get(e.getTo()));
			System.out.println();
		} writeToXML();
	}

	public static void rebuild() {
		readFromXML();
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
		App.PSYS.reset();
		edgeIndex = new HashMap<>();
		nodeIndex = new HashMap<>();
		for (Node n : Map.getNodes()) {
			n.setParticle2D(new VerletParticle2D(n.getX(), n.getY()));
			n.setBehavior2D(new AttractionBehavior2D(n.getParticle2D(), n.getRadius(), -1));
			n.update();
			nodes.add(n);
			nodeIndex.put(n.getId(), n);
			App.PSYS.addParticle(n);
		} for (Edge e : Map.getEdges()) {
			e.setA(getNode(e.getFrom()));
			e.setB(getNode(e.getTo()));
			e.setSpring2D(new VerletSpring2D(e.getA().getParticle2D(), e.getB().getParticle2D(), e.getLength(), 0.001f));
			e.update();
			edges.add(e);
			App.PSYS.addSpring(e);
			ArrayList<Node> nlist = edgeIndex.get(e.getFrom());
			if (nlist == null) { nlist = new ArrayList<>(); edgeIndex.put(e.getFrom(), nlist); }
			nlist.add(nodeIndex.get(e.getTo()));
		}
		build();
	}
	private static void writeToXML() {
		File file = new File(App.filepath);
		File staticFile = new File(App.staticFilepath);
		try {
			JAXBContext jc = JAXBContext.newInstance(XMLmap.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(Map, System.out);
			m.marshal(Map, file);
			m.marshal(Map, staticFile);
		} catch (JAXBException e) { e.printStackTrace(); }
	}
	private static void readFromXML() {
		XMLmap map;
		try {
			JAXBContext jc = JAXBContext.newInstance(XMLmap.class);
			Unmarshaller um = jc.createUnmarshaller();
			map = (XMLmap) um.unmarshal(new File(App.staticFilepath));
			for (Node n : map.getNodes()) { System.out.println(n.getId() + "::" + n.getName() + "=>" + n.getSize()); }
			Map = map;
		} catch (JAXBException e) { e.printStackTrace(); }
	}
	public static void addNode(Node n) {
		nodes.add(n);
		App.PSYS.addParticle(n);
		build();
	}
	public static void addEdge(Edge e) {
		edges.add(e);
		App.PSYS.addSpring(e);
		build();
	}
	public static void removeNode(Node n) {
		int id = n.getId();
		int num = Node.getNumberOfGNodes();
		Node.setNumberOfGNodes(num - 1);
		nodes.remove(n);
		nodeIndex.remove(n.getId());
		App.PSYS.removeParticle(n);
		for (Node na : nodes) {
			int naId = na.getId();
			if (naId > id) {
				na.setId(naId - 1);
			}
		}
	}
	public static void removeEdges(ArrayList<Edge> edges) { for (Edge e : edges) { removeEdge(e); } }
	private static void removeEdge(Edge e) {
		edges.remove(e);
		edgeIndex.remove(e.getFrom());
		App.PSYS.removeSpring(e);
		build();
	}
	private static Edge getEdge(Node a, Node b) {
		for (Edge e : edges) { if ((e.getA() == a && e.getB() == b) || (e.getA() == b && e.getB() == a)) { return e; } }
		return null;
	}
	public static void addEdgeTwo(Edge s) {
		if (getEdge(s.getA(), s.getB()) == null) { addEdge(s); }
	}
	public static void update() {
		totalArea = 0;
		for (Node n : nodes) {
			totalArea += n.getSize();
			n.update();
		}
		for (Edge e : edges) e.update();
	}
	//	public static XMLmap getMap() { return Map; }
	private static Node getNode(int id) {return nodeIndex.get(id);}
//	public static void setMap(XMLmap Map) { Map = Map; }
}
