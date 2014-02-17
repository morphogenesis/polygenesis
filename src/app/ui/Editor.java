package app.ui;

import app.core.App;
import app.graph.Edge;
import app.graph.Graph;
import app.graph.Node;
import processing.core.PApplet;
import toxi.geom.Circle;
import toxi.geom.Vec2D;

import java.util.ArrayList;

/**
 * Created on 2/13/14.
 */
public class Editor {
	public static Node activeNode;
	public static Node hoveredNode;
	public static ArrayList<Node> selectedNodes = new ArrayList<>();
	public static ArrayList<Node> lockedNodes = new ArrayList<>();
	public static ArrayList<Edge> adjacentEdges = new ArrayList<>();
	private App p5;
	private Graph graph;
	public Editor(App p5) {this.p5 = p5; this.graph = App.GRAPH;}

	public void createNewNode(String name, float size, Vec2D pos) {
		Node n = new Node(name, size, pos);
		System.out.println("number of nodes" + Node.getNumberOfGNodes());
		App.GRAPH.addNode(n);
	}
	public void createNewBranch(float num, boolean split) {
		if (hasActiveNode()) {
			Node parent = Editor.activeNode;
			float size = parent.getSize();
			for (int i = 1; i <= num; i++) {
				Vec2D pos = Vec2D.fromTheta(i * PApplet.TWO_PI / num).scaleSelf(size).addSelf(parent.getParticle2D());
				Node child = new Node(parent.getName() + i, size, pos);
				graph.addNode(child);
				graph.addEdge(new Edge(parent, child));
				selectedNodes.add(child);
			} if (split) parent.setSize(size / (num + 1));
		}
	}
	public void createNewEdge() {
		for (Node n : selectedNodes) {
			graph.addEdge(new Edge(activeNode, n));
		}
		/*if (selectedNodes.size() >= 2) {
			for (Node a : selectedNodes)
				Node na = selectedNodes.get(0);
			Node nb = selectedNodes.get(1);
			graph.addEdge(new Edge(na, nb));
		}*/
	}

	public static boolean hasActiveNode() {return activeNode != null; }

	void highlightNodeNearPosition(Vec2D mousePos) {
		hoveredNode = null; Circle c = new Circle(mousePos, 20);
		for (Node n : Graph.nodes) { if (c.containsPoint(n.getParticle2D())) { hoveredNode = n; break; } }
	}
	void selectNodeNearPosition(Vec2D mousePos) {
//		else { deselectNode(); }
		for (Node n : Graph.nodes) {
			Circle c = new Circle(n.getX(), n.getY(), 20);
			if (c.containsPoint(mousePos)) { setActiveNode(n); break; }
//			else { deselectNode(); }
		}
	}
	void setActiveNode(Node n) {
		if (selectedNodes.contains(n)) {selectedNodes.remove(n);}
		if ((App.isShiftDown) && (hasActiveNode())) { selectedNodes.add(activeNode); activeNode = n; selectedNodes.remove(activeNode); }
		else {clearSelection(); activeNode = n; }
		//		activeNode = n;//		selectedNodes.add(n);
		System.out.println("num" + n.getId());
		selectAdjacentEdges();
	}
	void deselectNode() { releaseNode(); activeNode = null; adjacentEdges.clear(); }
	void clearSelection() { selectedNodes.clear(); adjacentEdges.clear(); }

	void moveNode(Vec2D mousePos) { if (hasActiveNode()) { activeNode.getParticle2D().lock(); activeNode.getParticle2D().set(mousePos); } }
	void releaseNode() { if (hasActiveNode()) { if (!lockedNodes.contains(activeNode)) activeNode.getParticle2D().unlock(); } }

	void selectAdjacentEdges() {
		Node n = activeNode;
		deselectEdges();
		ArrayList<Edge> edges = new ArrayList<>();
		for (Edge e : Graph.edges) {
			if (e.getA() == n) { edges.add(e); System.out.println("[" + e.getA().getId() + "][" + e.getB().getId() + "]"); continue;}
			if (e.getB() == n) { edges.add(e); System.out.println("[" + e.getA().getId() + "][" + e.getB().getId() + "]");}
		} adjacentEdges.addAll(edges);
	}
	void deselectEdges() { adjacentEdges.clear(); }

	public void mouseMoved(Vec2D mousePos) {highlightNodeNearPosition(mousePos);}
	public void mousePressed(Vec2D mousePos) { selectNodeNearPosition(mousePos); }
	public void mouseDragged(Vec2D mousePos) { if (hasActiveNode()) moveNode(mousePos); }
	public void mouseReleased(Vec2D mousePos) {releaseNode(); }
	public void mouseWheel(float e) {
		if (hasActiveNode()) {
			float size = activeNode.getSize();
			float scale = 0.1f;
			if (size >= 3) {scale = 1;}
			if (size >= 20) {scale = 5;}
			if (size >= 50) {scale = 12;}
			if (size >= 100) {scale = 25;}
			if (e > 0) {activeNode.setSize(size - scale);}
			else if (e < 0) {activeNode.setSize(size + scale);}
			if (activeNode.getSize() <= 1) {activeNode.setSize(2);}
		}
	}

	public void deleteEdges() {graph.removeEdges(adjacentEdges); deselectEdges(); }
	public void lockNode() {
		for (Node n : selectedNodes) {
			if (lockedNodes.contains(n)) { lockedNodes.remove(n); n.getParticle2D().unlock(); }
			else { n.getParticle2D().lock(); lockedNodes.add(n); }
		}
	}
	public void deleteNode() {
		while (hasActiveNode()) {
			Node n = activeNode;
			ArrayList<Edge> rels = new ArrayList<>();
			for (Edge e : Graph.edges) {
				if (e.getA() == n) { rels.add(e); System.out.println("getA[" + e.getA().getId() + "] ==> getB[" + e.getB().getId() + "] <= MATCH"); continue;}
				if (e.getB() == n) { rels.add(e);/*System.out.println("getA[" + e.getA().getId() + "] ==> getB[" + e.getB().getId() + "] <= MATCH");*/}
			}
			graph.removeEdges(rels); graph.removeNode(n);
			clearSelection();
			deselectNode();
		}
	}
}
