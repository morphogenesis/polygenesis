package app.ui;

import app.core.App;
import app.core.Gui;
import app.graph.Edge;
import app.graph.Graph;
import app.graph.Node;
import processing.core.PApplet;

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
	}

	private void drawEdges() {
		p5.noFill();
		p5.noStroke();
		for (Edge e : Graph.edges) {
			EdgeGfx gfx = new EdgeGfx(p5, e);
			gfx.draw();
			if (Editor.adjacentEdges.contains(e)) gfx.drawActive();
		}
	}

	private void drawNodes() {
		p5.noFill();
		p5.noStroke();
		for (Node n : Graph.nodes) {
			n.update();
			NodeGfx t = new NodeGfx(p5, n);
			if (Gui.drawGraphOutline) { t.drawListing(); }
			t.draw();
			if (Gui.drawPhysInfo) { t.drawNametag();}
			if (n == Editor.activeNode) t.drawActive();
			if (n == Editor.hoveredNode) t.drawHovered();
			if (Editor.selectedNodes.contains(n)) t.drawSelected();
			if (Editor.lockedNodes.contains(n)) t.drawLocked();
		}
	}

	private void drawOutliner() {
		p5.noFill(); p5.noStroke();
		float totalSize = 0;
		int xx = App.WIDTH - 120;
		for (Node n : Graph.nodes) { totalSize += n.getSize(); }
		p5.noStroke();
		p5.fill(0xff999999);
		p5.text("NAME", 10, -2);
		p5.textAlign(PApplet.RIGHT);
		p5.text("AREA", 100, -2);
		p5.textFont(App.bfont, 14);
		p5.text("Total Area: " + App.DF1.format(totalSize) + " sq.m", xx + 100, 30);
		p5.textFont(App.pfont, 10);
	}

	private static class NodeGfx {
		App p5;
		Node n;
		float y;
		float x;
		float r;
		int xMax;
		float xPos;
		int yPos;
		public NodeGfx(App p5, Node n) {
			this.p5 = p5;
			this.n = n;
			this.x = n.getX();
			this.y = n.getY();
			this.r = n.getRadius();
			this.xMax = App.WIDTH - 200;
			this.xPos = x + r;
			this.yPos = 50 + (n.getId() * 14);
		}
		private void draw() {
			p5.noFill(); p5.stroke(n.getColor(), 100, 100, 100);
			p5.ellipse(x, y, r + 1, r + 1);
//			p5.stroke(0xff999999); p5.strokeWeight(1);
//			p5.ellipse(x, y, r, r);
			p5.fill(n.getColor(), 100, 100, 100); p5.stroke(0xff1d1d1d);
			p5.ellipse(x, y, 5, 5);
		}
		private void drawNametag() {
//			float x = r * App.cos(theta);

			p5.fill(0xff333333);
			p5.stroke(0xff999999);
			p5.rect(240, n.getY(), 120, 16);
			p5.textAlign(PApplet.CENTER);
			p5.fill(0xffffffff);
			p5.text(n.getName() + " " + n.getId(), 300, n.getY() + 12);
			p5.fill(0xff333333);
			p5.stroke(0xff444444);
			p5.line(350, y, x, y);
			p5.stroke(0xff999999);
			p5.rect(364, y, 16, 16);
			p5.textAlign(PApplet.CENTER);
			p5.fill(n.getColor(), 100, 100);
			p5.text(n.getId(), 372, y + 12);
		}
		private void drawListing() {

			p5.noFill();
			p5.stroke(0xff444444);
			p5.line(x + r, y, xMax - 100, y);
			p5.line(xMax - 100, y, xMax - 20, yPos + 7);
			p5.line(xMax - 20, yPos + 7, xMax, yPos + 7);
			//		p5.bezier(xMax, yPos, xMax-100, yPos, xMax, y, x+r, y);
			p5.noStroke();
			if (n.getId() % 2 == 0) { p5.fill(0xff2b2b2b); }
			else { p5.fill(0xff333333); }
			p5.rect(xMax - 10, yPos + 1, 160, 12);

			p5.fill(0xff333333);
			p5.stroke(n.getColor(), 100, 100);
			p5.ellipse(xMax - 14, yPos + 7, 3, 3);
			p5.noStroke();
			p5.fill(0xff666666);
			p5.rect(xMax + 100, yPos + 1, 1, 12);
			p5.fill(0xff999999);
			p5.textAlign(PApplet.LEFT);
			p5.text(n.getName(), xMax, yPos + 10);
			p5.text(App.DF1.format(n.getSize()), xMax + 114, yPos + 10);
			p5.text(n.getId(), xMax + 150, yPos + 10);
			//		p5.textAlign(PApplet.RIGHT);
			//		p5.text(n.getId(), xMax + 110, yPos + 10);
		}
		private void drawHovered() {
//			int yPos = 50 + (n.getId() * 14);
			p5.noFill(); p5.stroke(0xffffffff);
			p5.ellipse(x, y, r, r);
			p5.fill(0xff333333);
			p5.stroke(n.getColor(), 100, 100);
			p5.ellipse(xPos - 14, y + 7, 3, 3);
		}
		private void drawActive() {

			p5.fill(360, 50); p5.stroke(360);
			p5.ellipse(x, y, r + 4, r + 4);
			p5.fill(0xffff0000); p5.stroke(0xffff0000);
			p5.noStroke();
			p5.fill(0xff666666);
			p5.rect(xMax + 100, y + 1, 1, 12);
			p5.fill(0xff999999);
			p5.textAlign(PApplet.LEFT);
			p5.text(n.getName(), xPos + 20, y + 10);
			p5.text(App.DF1.format(n.getSize()), xPos + 114, y + 10);
			p5.text(n.getId(), xPos + 150, y + 10);
		}
		private void drawSelected() {
			p5.fill(360, 25); p5.stroke(SELECTED);
			p5.ellipse(x, y, r - 4, r - 4);
			p5.strokeWeight(1);
		}
		private void drawLocked() {
			p5.fill(0, 50); p5.stroke(360);
			p5.ellipse(x, y, r - 8, r - 8);
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