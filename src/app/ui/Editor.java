package app.ui;

import app.core.App;
import app.core.Gui;
import app.graph.Edge;
import app.graph.Graph;
import app.graph.Node;
import processing.core.PApplet;
import toxi.geom.Circle;
import toxi.geom.Rect;
import toxi.geom.Vec2D;

import java.util.ArrayList;

/**
 * Created on 2/13/14.
 */
public class Editor {
	public static Node activeNode;
	public static Node hoveredNode;
	public static final ArrayList<Node> selectedNodes = new ArrayList<>();
	public static final ArrayList<Node> lockedNodes = new ArrayList<>();
	public static final ArrayList<Edge> adjacentEdges = new ArrayList<>();
	public Editor() {}

	public static void createNewNode(String name, float size, Vec2D pos) {
		Node n = new Node(name, size, pos);
		System.out.println("number of nodes" + Node.getNumberOfGNodes());
		Graph.addNode(n);
	}
	public static void createNewBranch(float num, boolean split) {
		if (hasActiveNode()) {
			Node parent = activeNode;
			float size = parent.getSize();
			for (int i = 1; i <= num; i++) {
				Vec2D pos = Vec2D.fromTheta(i * PApplet.TWO_PI / num).scaleSelf(size).addSelf(parent.getParticle2D());
				Node child = new Node(parent.getName() + i, size, pos);
				Graph.addNode(child);
				Graph.addEdge(new Edge(parent, child));
				selectedNodes.add(child);
			} if (split) parent.setSize(size / (num + 1));
		}
	}
	public static void createNewEdge() {
		for (Node n : selectedNodes) {
			Edge e = new Edge(n, activeNode);
			Graph.addEdgeTwo(e);
		}
	}

	public static boolean hasActiveNode() {return activeNode != null; }

	private static void highlightNodeNearPosition(Vec2D mousePos) {
		hoveredNode = null;
		Circle c = new Circle(mousePos, 20);

		for (Node n : Graph.nodes) {
			Rect r = new Rect(App.WIDTH - 200, 50 + (n.getId() * 14), 160, 12);
			if (c.containsPoint(n.getParticle2D())) { hoveredNode = n; break; } else if ((Gui.drawGraphOutline) && (r.containsPoint(mousePos))) { hoveredNode = n; break; }
		}
	}
	private static void selectNodeNearPosition(Vec2D mousePos) {
		if (!App.isShiftDown) { clearSelection(); } else if (hasActiveNode()) {selectedNodes.add(activeNode); deselectNode(); }
		for (Node n : Graph.nodes) {
			Rect r = new Rect(App.WIDTH - 200, 50 + (n.getId() * 14), 160, 12);
			Circle c = new Circle(n.getX(), n.getY(), 20);
			if (c.containsPoint(mousePos)) { setActiveNode(n); break; } else if ((Gui.drawGraphOutline) && (r.containsPoint(mousePos))) { setActiveNode(n); break; } else { deselectNode(); }
		}
	}
	private static void setActiveNode(Node n) {
		activeNode = n;
		System.out.println("num" + n.getId());
		selectAdjacentEdges();
	}
	private static void deselectNode() { releaseNode(); activeNode = null; adjacentEdges.clear(); }
	private static void clearSelection() { selectedNodes.clear(); adjacentEdges.clear(); }

	private static void moveNode(Vec2D mousePos) { activeNode.getParticle2D().lock(); activeNode.getParticle2D().set(mousePos); }
	private static void releaseNode() { if (hasActiveNode()) { if (!lockedNodes.contains(activeNode)) activeNode.getParticle2D().unlock(); } }

	private static void selectAdjacentEdges() {
		Node n = activeNode;
		deselectEdges();
		ArrayList<Edge> edges = new ArrayList<>();
		for (Edge e : Graph.edges) {
			if (e.getA() == n) { edges.add(e); System.out.println("[" + e.getA().getId() + "][" + e.getB().getId() + "]"); continue;}
			if (e.getB() == n) { edges.add(e); System.out.println("[" + e.getA().getId() + "][" + e.getB().getId() + "]");}
		} adjacentEdges.addAll(edges);
	}
	private static void deselectEdges() { adjacentEdges.clear(); }

	public static void mouseMoved(Vec2D mousePos) {highlightNodeNearPosition(mousePos);}
	public static void mousePressed(Vec2D mousePos) { selectNodeNearPosition(mousePos); }
	public static void mouseDragged(Vec2D mousePos) { if ((hasActiveNode()) && (Gui.isEditMode)) moveNode(mousePos); }
	public static void mouseReleased() {releaseNode(); }
	public static void mouseWheel(float e) {
		if (hasActiveNode()) {
			float size = activeNode.getSize();
			float scale = 0.1f;
			if (size >= 3) {scale = 1;}
			if (size >= 20) {scale = 5;}
			if (size >= 50) {scale = 12;}
			if (size >= 100) {scale = 25;}
			if (e > 0) {activeNode.setSize(size - scale);} else if (e < 0) {activeNode.setSize(size + scale);}
			if (activeNode.getSize() <= 1) {activeNode.setSize(2);}
		} for (Node n : selectedNodes) {
			n.setSize(activeNode.getSize());
		}
	}

	public static void deleteEdges() {Graph.removeEdges(adjacentEdges); deselectEdges(); }
	public static void lockNode() {
		if (lockedNodes.contains(activeNode)) { lockedNodes.remove(activeNode); activeNode.getParticle2D().unlock(); } else { activeNode.getParticle2D().lock(); lockedNodes.add(activeNode); }
	}
	public static void deleteNode() {
		while (hasActiveNode()) {
			Node n = activeNode;
			ArrayList<Edge> rels = new ArrayList<>();
			for (Edge e : Graph.edges) {
				if (e.getA() == n) { rels.add(e); System.out.println("getA[" + e.getA().getId() + "] ==> getB[" + e.getB().getId() + "] <= MATCH"); continue;}
				if (e.getB() == n) { rels.add(e);/*System.out.println("getA[" + e.getA().getId() + "] ==> getB[" + e.getB().getId() + "] <= MATCH");*/}
			}
			Graph.removeEdges(rels); Graph.removeNode(n);
			clearSelection();
			deselectNode();
		}
	}
}
