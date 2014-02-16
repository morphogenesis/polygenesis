package app.graph;

import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import java.util.ArrayList;

public class GMash {
	private final app.graph.GEdit GEdit;
	public GMash(app.graph.GEdit GEdit) { this.GEdit = GEdit; }
	public void removeActiveNode() {
		while (GEdit.hasActiveNode()) {
			GNode n = GEdit.getActiveNode();
			ArrayList<GEdge> rels = new ArrayList<GEdge>();
			for (GEdge e : GEdit.getGraph().edges) {
				if (e.getNodeA() == n) {
					rels.add(e);
					System.out.println("getNodeA[" + e.getNodeA().getId() + "] ==> getNodeB[" + e.getNodeB().getId() + "] <= MATCH");
					continue;
				}
				if (e.getNodeB() == n) {
					rels.add(e);
					System.out.println("getNodeA[" + e.getNodeA().getId() + "] ==> getNodeB[" + e.getNodeB().getId() + "] <= MATCH");
				}
			}
			for (GEdge e : rels) {
				GEdit.getGraph().removeEdge(e);
			}
			GEdit.getGraph().removeNode(n);
			GEdit.getGraph().build();
			GEdit.getSelectedNodes().clear();
			GEdit.setActiveNode(null);
		}
	}
	public void removeActiveEdges() {
		for (GEdge e : GEdit.getAdjacentEdges()) {
			GEdit.getGraph().removeEdge(e);
			GEdit.getGraph().build();
		}
		GEdit.getAdjacentEdges().clear();
	}
	public void createBranch(float num, boolean split) {
		if (GEdit.hasActiveNode()) {
			GNode pNode = GEdit.getActiveNode();
			String name = pNode.getName();
			int col = pNode.getColor();
			Vec2D pPos = pNode.getParticle2D();
			float size = pNode.getSize();
			if (split) {size = pNode.getSize() / (num + 1);}
			pNode.setSize(size);
			for (int i = 1; i <= num; i++) {
				Vec2D pos = Vec2D.fromTheta(i * PApplet.TWO_PI / num).scaleSelf(size).addSelf(pPos);
				GNode n = newNode(name + i, size, pos, col);
				GEdit.getGraph().addNode(n);
				GEdit.getSelectedNodes().add(n);
				GEdge e = newEdge(pNode, n);
				GEdit.getGraph().addEdge(e);
			}
		}
	}
	public void addNodeAtCursor(String name, float size, Vec2D pos) {
		GNode n = newNode(name, size, pos);
		GEdit.getGraph().addNode(n);
	}
	public void addEdgeToSelection() {
		if (GEdit.getSelectedNodes().size() >= 2) {
			GNode na = GEdit.getSelectedNodes().get(0);
			GNode nb = GEdit.getSelectedNodes().get(1);
			GEdge e = newEdge(na, nb);
			GEdit.getGraph().addEdge(e);
			GEdit.getSelectedNodes().add(na);
			GEdit.getSelectedNodes().add(nb);
		}
	}
	GEdge newEdge(GNode from, GNode to) {
		GEdge e = new GEdge();
		e.setFrom(from.getId());
		e.setTo(to.getId());
		e.setNodeA(from);
		e.setNodeB(to);
		e.setLength(from.getRadius() + to.getRadius());
		e.setSpring2D(new VerletSpring2D(from.getParticle2D(), to.getParticle2D(), e.getLength() * GEdit.getX().springScale, GEdit.getX().springStrength));
		return e;
	}
	GNode newNode(String name, float size, Vec2D pos) {
		return newNode(name, size, pos, 100);
	}
	GNode newNode(String name, float size, Vec2D pos, int color) {
		GNode n = new GNode();
		n.setId(GEdit.getGraph().nodes.size());
		n.setName(name);
		n.setSize(size);
		n.setX(pos.x);
		n.setY(pos.y);
		n.setColor(color);
		n.setParticle2D(new VerletParticle2D(pos));
		n.setBehavior2D(new AttractionBehavior2D(n.getParticle2D(), n.getRadius(), -1));
		return n;
	}
}