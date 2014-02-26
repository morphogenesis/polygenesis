package app.ui;

import app.core.App;
import app.core.Gui;
import app.graph.Edge;
import app.graph.Graph;
import app.graph.Node;
import processing.core.PApplet;

import java.util.ArrayList;

import static util.Color.SELECTED;

/**
 * Created on 2/17/14.
 */
public class Display {
	App p5;

	public Display(App p5) { this.p5 = p5; }

	public void draw() {
		App.GRAPH.update();
		if (Gui.drawGraphNodes) { drawEdges();}
		if (Gui.drawGraphEdges) { drawNodes(); }
		if (Gui.drawGraphOutline) { drawOutliner(); }
		if (Gui.drawVoronoi) { drawVoronoi(); }
		drawStatusBar();
	}

	private void drawEdges() {
		ArrayList<EdgeGfx> egfx = new ArrayList<>();
		for (Edge e : Graph.edges) { egfx.add(new EdgeGfx(p5, e)); }
		for (EdgeGfx fx : egfx) { fx.draw(); }
		for (EdgeGfx fx : egfx) { if (Editor.adjacentEdges.contains(fx.e)) fx.drawActive(); }
	}

	private void drawNodes() {
		ArrayList<NodeGfx> ngfx = new ArrayList<>();
		for (Node n : Graph.nodes) { n.update(); ngfx.add(new NodeGfx(p5, n)); }
		for (NodeGfx nt : ngfx) { nt.draw(); }
		for (NodeGfx nt : ngfx) { nt.drawWire(); }
	}

	private void drawOutliner() {
		int xO = Gui.outlinerX;
		String totalArea = App.DF1.format(Graph.totalArea);
		p5.noStroke();
		p5.fill(0xffeaeaea);
		p5.text("NAME", 10, -2);
		p5.text("AREA", 100, -2);
		p5.textFont(App.bfont, 14);
		p5.text("Total Area: " + totalArea + " sq.m", xO, 40);
		p5.textFont(App.pfont, 10);
		p5.noFill();
	}
	private void drawVoronoi() {

	}
	private void drawStatusBar() {
		if (Gui.isEditMode) {
			p5.fill(0xffff0000);
			p5.text("Edit Mode", App.WIDTH / 2, 20);
			p5.noFill();
		}
	}

	private static class NodeGfx {
		private App p5;
		private Node n;
		private final float nY;
		private final float nX;
		private final float nR;
		private final int nCol;
		private final int nId;
		private static final int oX = Gui.outlinerX;
		private final int oY;
		private float areaPercentage;
		private final String name;
		private final float size;

		public NodeGfx(App p5, Node n) {
			this.p5 = p5;
			this.n = n;
			this.nX = n.getX();
			this.nY = n.getY();
			this.nR = n.getRadius();
			this.nId = n.getId();
			this.name = n.getName();
			this.size = n.getSize();
			this.oY = 50 + (n.getId() * 14);
			this.nCol = (Graph.nodes.size() / 2 + (360 / (Graph.nodes.size()) * n.getId()));
			this.areaPercentage = (n.getSize() / Graph.totalArea) * 100;
		}
		private void draw() {
			drawNode(nR, nCol, -1);

			if (Editor.selectedNodes.contains(n)) { drawNode(nR - 1, nCol, -1); drawNode(nR - 3, 180, 40); }
			if (n == Editor.activeNode) { drawNode(nR - 1, nCol, -1); drawNode(nR - 3, 360, 20); }
			if (n == Editor.hoveredNode) { drawNode(nR - 1, 180, -1); }
			if (Editor.lockedNodes.contains(n)) {drawNode(10, 0, nCol);}
			if (Gui.drawGraphOutline) { drawDatablock(); }
			drawNode(5, 20, nCol);
		}

		private void drawNode(float size, int stroke, int fill) {
			if (fill == -1) { p5.noFill(); } else if (fill == nCol) { p5.fill(fill, 100, 100, 100); } else p5.fill(fill);
			p5.stroke(stroke);
			if (stroke == -1) { p5.noStroke(); } else if (stroke == nCol) p5.stroke(stroke, 100, 100, 100);
			p5.ellipse(nX, nY, size, size);
			p5.noFill(); p5.noStroke();
		}

		private void drawWire() {
			if ((Editor.selectedNodes.contains(n)) || (Editor.activeNode == n)) {

				/** Wire */
				p5.stroke(0xff444444);
				p5.line(nX, nY, nX + nR + 12, nY - nR - 12);
				p5.line(nX + nR + 12, nY - nR - 12, oX - 100, nY - nR - 12);
				p5.line(oX - 100, nY - nR - 12, oX - 20, oY + 7);
				p5.line(oX - 20, oY + 7, oX, oY + 7);
				p5.noStroke();

				/** Tag */
				float h = 12;
				float w = 180;
				float xx = nX + nR;
				float yy = nY - nR;
				p5.fill(0xff444444);
				p5.quad(xx, yy, xx + h, yy - h, xx + w + h, yy - h, xx + w, yy);

				/** Text */
				float tY = nY - nR - 3;
				p5.fill(0xff222222);
				p5.textAlign(PApplet.LEFT);
				p5.text(name, xx + 20, tY);
				p5.text(App.DF1.format(size), xx + 114, tY);
				p5.text(nId, xx + 150, tY);
				p5.noFill();
			}
		}

		private void drawDatablock() {
			/** Dot */
			p5.fill(0xff333333);
			p5.stroke(nCol, 100, 100);
			p5.ellipse(-8, oY + 7, 3, 3);

			/** Bar */
			if (nId % 2 == 0) { p5.fill(0xff2b2b2b); } else { p5.fill(0xff333333); }
			p5.noStroke();
			p5.rect(oX, oY + 1, 200, 12);
			p5.stroke(0xff666666);
			p5.line(+120, oY + 1, +120, oY + 12);

			/** Text */
			p5.fill(0xffaeaeae);
			p5.textAlign(PApplet.LEFT);
			p5.text(name, oX, oY + 10);
			p5.text(App.DF1.format(size) + "sq.m", +124, oY + 10);
			p5.textAlign(PApplet.RIGHT);
			p5.text(App.DF1.format(areaPercentage) + "%", +190, oY + 10);
			p5.textAlign(PApplet.LEFT);
		}
	}

	private static class EdgeGfx {
		App p5;
		Edge e;
		float ax;
		float ay;
		float bx;
		float by;
		private EdgeGfx(App p5, Edge e) {
			this.p5 = p5;
			this.e = e;
			this.ax = e.getA().getX();
			this.ay = e.getA().getY();
			this.bx = e.getB().getX();
			this.by = e.getB().getY();
		}
		private EdgeGfx draw() {
			p5.noFill();
			p5.stroke(0xff666666);
			p5.line(ax, ay, bx, by);
			return this;
		}
		private void drawActive() {
			p5.noFill();
			p5.stroke(SELECTED);
			p5.line(ax, ay, bx, by);
		}
	}
}