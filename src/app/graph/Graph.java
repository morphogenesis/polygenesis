package app.graph;

import app.core.App;
import app.xml.Edge;
import app.xml.Node;
import app.xml.XmlMap;
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
	private static XmlMap Map = new XmlMap();
	private static HashMap<Integer, Node> nodeIndex = new HashMap<>();
	private static HashMap<Integer, ArrayList<Node>> edgeIndex = new HashMap<>();
	public static ArrayList<Node> nodes;
	public static ArrayList<Edge> edges;
	public static float totalArea = 0;
	public Graph() {
		nodes = new ArrayList<>(); edges = new ArrayList<>();
	}
	public static void build() {
		Map = new XmlMap();
		Map.setNodes(nodes);
		Map.setEdges(edges);
		edgeIndex = new HashMap<>();
		nodeIndex = new HashMap<>();
		for (Node n : nodes) {totalArea += n.getSize(); n.setId(nodes.indexOf(n)); nodeIndex.put(n.getId(), n); }
		for (Edge e : edges) {
			ArrayList<Node> nlist = edgeIndex.get(e.getFrom());
			if (nlist == null) { nlist = new ArrayList<>(); edgeIndex.put(e.getFrom(), nlist); }
			nlist.add(nodeIndex.get(e.getTo()));
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
//			n.update();
			if (n.getId() == 0) { Editor.activeNode = n; Editor.lockNode(); }
			nodes.add(n);
			nodeIndex.put(n.getId(), n);
			App.PSYS.addParticle(n);
		}
		update();
		for (Edge e : Map.getEdges()) {
			e.setA(getNode(e.getFrom()));
			e.setB(getNode(e.getTo()));
			e.setSpring2D(new VerletSpring2D(e.getA().getParticle2D(), e.getB().getParticle2D(), e.getLength(), 0.001f));
//			e.update();
			edges.add(e);
			App.PSYS.addSpring(e);
			ArrayList<Node> nlist = edgeIndex.get(e.getFrom());
			if (nlist == null) { nlist = new ArrayList<>(); edgeIndex.put(e.getFrom(), nlist); }
			nlist.add(nodeIndex.get(e.getTo()));
		}
		update();
		build();
	}
	private static void writeToXML() {
		File file = new File(App.filepath);
		File staticFile = new File(App.staticFilepath);
		System.out.println("Writing XML file..." + file.getPath());
		System.out.println("Writing static XML file..." + staticFile.getPath());
		try {
			JAXBContext jc = JAXBContext.newInstance(XmlMap.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(Map, System.out);
			m.marshal(Map, file);
			m.marshal(Map, staticFile);
		} catch (JAXBException e) { e.printStackTrace(); }
		System.out.println("Finished writing to XML file.");
	}
	private static void readFromXML() {
		System.out.println("Reading from XML file...");
		XmlMap map;
		try {
			JAXBContext jc = JAXBContext.newInstance(XmlMap.class);
			Unmarshaller um = jc.createUnmarshaller();
			map = (XmlMap) um.unmarshal(new File(App.staticFilepath));
			if (map.getNodes() != null) for (Node n : map.getNodes()) { System.out.println("N [" + n.getId() + "] " + n.getName()); }
			if (map.getEdges() != null) for (Edge e : map.getEdges()) { System.out.println("E [" + e.getFrom() + " <-> " + e.getTo() + "]"); }
			Map = map;
		} catch (JAXBException e) { e.printStackTrace(); }
		System.out.println("Finished reading from XML file.");
	}
	public static void update() {
		totalArea = 0;
		for (Node n : nodes) { n.update(); totalArea += n.getSize(); }
		for (Edge e : edges) e.update();
	}
	public static void addNode(Node n) {
		nodes.add(n);
		App.PSYS.addParticle(n);
	}

	public static void addEdge(Edge e) {
		if (getEdge(e.getA(), e.getB()) == null) {
			edges.add(e);
			App.PSYS.addSpring(e);
			build();
		}
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
	private static Node getNode(int id) {
		return nodeIndex.get(id);
	}
}
//	public static XmlMap getMap() { return Map; }
//	public static void setMap(XmlMap Map) { Map = Map; }
/*	public static void addEdge(Edge s) {
		if (getEdge(s.getA(), s.getB()) == null) { buildEdge(s); }
	}*/