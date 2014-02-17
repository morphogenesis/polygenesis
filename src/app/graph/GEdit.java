package app.graph;

import app.core.App;
import app.core.OPT;
import processing.core.PApplet;
import toxi.geom.Circle;
import toxi.geom.Vec2D;

import java.util.ArrayList;

/**
 * Created on 2/13/14.
 */
public class GEdit {
	private final app.graph.GMash GMash = new app.graph.GMash(this);
	public boolean isUpdating;
	private GNode activeNode;
	private GNode hoveredNode;
	private ArrayList<GNode> selectedNodes = new ArrayList<>();
	private ArrayList<GNode> lockedNodes = new ArrayList<>();
	private ArrayList<GEdge> adjacentEdges = new ArrayList<>();
	private App p5;
	private OPT X;
	private Graph graph;
	public GEdit(App p5) {this.p5 = p5; this.graph = App.GRAPH; this.X = App.CONF;}

	public void draw() {
		p5.noFill(); p5.noStroke();
		for (GEdge e : graph.edges) {
			e.update(OPT.springStrength, OPT.springScale);
			if (OPT.showEdges) {
				EdgeGfx fx = new EdgeGfx(p5, e);
				fx.draw();

				if (adjacentEdges.contains(e)) fx.drawActive();
			}
		}

		for (GNode n : graph.nodes) {
			n.update(OPT.particleWeight, OPT.behaviorScale, OPT.behaviorStrength);
			NodeTag t = new NodeTag(p5, n);
			if (OPT.drawOutliner) { t.drawListing(); }
			if (OPT.showNodes) {
				t.draw();
				if (OPT.showInfo) { t.drawNametag();}
				if (n == activeNode) t.drawActive();
				if (n == hoveredNode) t.drawHovered();
				if (selectedNodes.contains(n)) t.drawSelected();
			}
		}

		if (OPT.drawOutliner) { drawOutliner(); }
	}
	private void drawOutliner() {
		p5.noFill(); p5.noStroke();
		float totalSize = 0;
		int xx = App.WIDTH - 120;
		for (GNode n : graph.nodes) { totalSize += n.getSize(); }
		p5.noStroke();
		p5.fill(0xff999999);
		p5.text("NAME", 10, -2);
		p5.textAlign(PApplet.RIGHT);
		p5.text("AREA", 100, -2);
		p5.textFont(App.bfont, 14);
		p5.text("Total Area: " + App.DF1.format(totalSize) + " sq.m", xx + 100, 30);
		p5.textFont(App.pfont, 10);
	}
	private void selectNodeNearPosition(Vec2D mousePos) {
		if (!App.isShiftDown) clearSelection();
		else deselectNode();
		for (GNode n : graph.nodes) {
			Circle c = new Circle(n.getX(), n.getY(), 20);
			if (c.containsPoint(mousePos)) {
				setActiveNode(n);
				selectAdjacentEdges(n);
				break;
			} else deselectNode();
		}
	}

	public void removeActiveNode() {
		GMash.removeActiveNode();
	}
	public void removeActiveEdges() {
		GMash.removeActiveEdges();
	}
	private void deselectNode() {
		releaseNode();
		activeNode = null;
		adjacentEdges.clear();
	}
	private void deselectEdges() {
		adjacentEdges.clear();
	}
	private void releaseNode() {
		if (hasActiveNode()) { if (!lockedNodes.contains(activeNode)) activeNode.getParticle2D().unlock(); }
	}
	private void clearSelection() {
		selectedNodes.clear();
		adjacentEdges.clear();
	}
	private void selectAdjacentEdges(GNode n) {
		deselectEdges();
		ArrayList<GEdge> edges = new ArrayList<>();
		for (GEdge e : graph.edges) {
			if (e.getNodeA() == n) { edges.add(e); System.out.println("[" + e.getNodeA().getId() + "][" + e.getNodeB().getId() + "]"); continue;}
			if (e.getNodeB() == n) { edges.add(e); System.out.println("[" + e.getNodeA().getId() + "][" + e.getNodeB().getId() + "]");}
		} adjacentEdges.addAll(edges);
	}
	private void moveActiveNode(Vec2D mousePos) {
		if (hasActiveNode()) {
			activeNode.getParticle2D().lock();
			activeNode.getParticle2D().set(mousePos);
		}
	}
	private void highlightNodeNearPosition(Vec2D mousePos) {
		hoveredNode = null;

		for (GNode n : graph.nodes) {
			Circle c = new Circle(mousePos, 20);
			if (c.containsPoint(n.getParticle2D())) {
				hoveredNode = n;
				break;
			}
		}
	}
	protected void setActiveNode(GNode n) {
		this.activeNode = n;
		selectedNodes.add(n);
	}
	public void createBranch(float num, boolean split) {
		GMash.createBranch(num, split);
	}

	public void addNodeAtCursor(String name, float size, Vec2D pos) {
		GMash.addNodeAtCursor(name, size, pos);
	}

	public void addEdgeToSelection() {
		GMash.addEdgeToSelection();
	}

	/*	private GEdge newEdge(GNode from, GNode to) {
			return GMash.newEdge(from, to);
		}
		private GNode newNode(String name, float size, Vec2D pos) {
			return GMash.newNode(name, size, pos);
		}
		private GNode newNode(String name, float size, Vec2D pos, int color) {
			return GMash.newNode(name, size, pos, color);
		}*/
	public boolean hasActiveNode() {return activeNode != null; }
	public GNode getActiveNode() { return activeNode; }
	public ArrayList<GNode> getSelectedNodes() { return selectedNodes; }
	public void mousePressed(Vec2D mousePos) { selectNodeNearPosition(mousePos); }
	public void mouseDragged(Vec2D mousePos) { if (hasActiveNode()) moveActiveNode(mousePos); }
	public void mouseReleased(Vec2D mousePos) {releaseNode(); }
	public void mouseMoved(Vec2D mousePos) {highlightNodeNearPosition(mousePos);}
	public void mouseWheel(float e) {
		if (hasActiveNode()) {
			float size = activeNode.getSize();
			float scale = 0.1f;
			if (size >= 3) {scale = 1;}
			if (size >= 20) {scale = 5;}
			if (size >= 100) {scale = 50;}
			if (e > 0) {activeNode.setSize(size - scale);} else if (e < 0) {activeNode.setSize(size + scale);}
			if (activeNode.getSize() <= 1) {activeNode.setSize(2);}
		}
	}
	public void freezeNode() {
		if (hasActiveNode()) {
			if (lockedNodes.contains(activeNode)) {
				lockedNodes.remove(activeNode); activeNode.getParticle2D().unlock();
			} else { activeNode.getParticle2D().lock(); lockedNodes.add(activeNode); }
		}
	}
	public void setName(String name) { if (hasActiveNode()) activeNode.setName(name); }
	public void setOccupancy(float occupancy) {
		if (hasActiveNode()) { activeNode.setOccupancy((int) occupancy); }
		if (App.CP5.isMouseOver()) { for (GNode g : selectedNodes) {g.setOccupancy((int) occupancy);} }
	}
	public void setColor(float color) {
		if (hasActiveNode()) activeNode.setColor((int) color);
		if (App.CP5.isMouseOver()) { for (GNode g : selectedNodes) {g.setColor((int) color);} }
	}
	public void setSize(float size) {
		if (hasActiveNode()) activeNode.setSize(size);
		if (App.CP5.isMouseOver()) { for (GNode g : selectedNodes) {g.setSize(size);} }
	}
	public ArrayList<GEdge> getAdjacentEdges() {
		return adjacentEdges;
	}
	public Graph getGraph() {
		return graph;
	}
	public OPT getX() {
		return X;
	}
}
		/*GNode n = new GNode();
		n.setId(graph.nodes.size());
		n.setName($name);
		n.setSize($size);
		n.setX($pos.x);
		n.setY($pos.y);
		VerletParticle2D v = new VerletParticle2D($pos);
		AttractionBehavior2D getNodeA = new AttractionBehavior2D(v, n.getRadius(), -1);
		n.setParticle2D(v);
		n.setBehavior2D(getNodeA);*/
//		graph.addNode(n);

