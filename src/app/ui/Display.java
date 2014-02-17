package app.ui;

import app.core.App;
import app.core.Gui;
import app.graph.Edge;
import app.graph.Graph;
import app.graph.Node;
import processing.core.PApplet;
import util.Color;

import static util.Color.SELECTED;

/**
 * Created on 2/17/14.
 */
public class Display {
	App p5;

	public Display(App p5) { this.p5 = p5; }

	public void draw() {
		drawEdges();
		drawNodes();
		if (Gui.drawGraphOutline) { drawOutliner(); }
	}

	private void drawEdges() {
		p5.noFill();
		p5.noStroke();
		for (Edge e : Graph.edges) {
			e.update();
			if (Gui.drawGraphEdges) {
				EdgeGfx fx = new EdgeGfx(p5, e);
				fx.draw();

				if (Editor.adjacentEdges.contains(e)) fx.drawActive();
			}
		}
	}

	private void drawNodes() {
		p5.noFill();
		p5.noStroke();
		for (Node n : Graph.nodes) {
			n.update();
			NodeTag t = new NodeTag(p5, n);
			if (Gui.drawGraphOutline) { t.drawListing(); }
			if (Gui.drawGraphNodes) {
				t.draw();
				if (Gui.drawPhysInfo) { t.drawNametag();}
				if (n == Editor.activeNode) t.drawActive();
				if (n == Editor.hoveredNode) t.drawHovered();
				if (Editor.selectedNodes.contains(n)) t.drawSelected();
			}
		}
	}

	public void drawOutliner() {
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

	public static class NodeTag {
		App p5;
		Node n;
		float y;
		float x;
		float r;
		public NodeTag(App p5, Node n) {
			this.p5 = p5;
			this.n = n;
			this.x = n.getX();
			this.y = n.getY();
			this.r = n.getRadius();
		}
		public void draw() {
			p5.noFill();
			p5.stroke(n.getColor(), 100, 100, 100); p5.strokeWeight(1); p5.ellipse(x, y, r + 1, r + 1);
			p5.stroke(0xff999999); p5.ellipse(x, y, r, r);
			p5.stroke(Color.BLACK); p5.fill(n.getColor(), 100, 100); p5.ellipse(x, y, 5, 5);
		}
		public void drawNametag() {
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
		public void drawListing() {
			int xPos = App.WIDTH - 200;
			int yPos = 50 + (n.getId() * 14);
			p5.noFill();
			//		p5.stroke(0xff444444);
			//		p5.line(x + r, y, xPos - 100, y);
			//		p5.line(xPos - 100, y, xPos - 20, yPos + 7);
			//		p5.line(xPos - 20, yPos + 7, xPos, yPos + 7);
			//		p5.bezier(xPos, yPos, xPos-100, yPos, xPos, y, x+r, y);
			p5.noStroke();
			if (n.getId() % 2 == 0) { p5.fill(0xff2b2b2b); }
			else { p5.fill(0xff333333); }
			p5.rect(xPos - 10, yPos + 1, 160, 12);

			p5.fill(0xff333333);
			p5.stroke(n.getColor(), 100, 100);
			p5.ellipse(xPos - 14, yPos + 7, 3, 3);
			p5.noStroke();
			p5.fill(0xff666666);
			p5.rect(xPos + 100, yPos + 1, 1, 12);
			p5.fill(0xff999999);
			p5.textAlign(PApplet.LEFT);
			p5.text(n.getName(), xPos, yPos + 10);
			//		p5.textAlign(PApplet.RIGHT);
			p5.text(App.DF1.format(n.getSize()), xPos + 114, yPos + 10);
			//		p5.text(n.getId(), xPos + 110, yPos + 10);
		}
		public void drawHovered() {
			p5.noFill(); p5.stroke(30, 100, 100);
			p5.ellipse(x, y, r, r);
		}
		public void drawActive() {
			p5.noFill(); p5.stroke(30, 100, 100, 100); p5.strokeWeight(2);
			p5.ellipse(x, y, r + 4, r + 3); p5.strokeWeight(1);
		}
		public void drawSelected() {
			int xPos = App.WIDTH - 200;
			int yPos = 50 + (n.getId() * 14);
			p5.noFill(); p5.stroke(0xff444444);
			p5.line(x + r, y, x + (r * 2), y);
			p5.line(xPos - 100, y, xPos - 30, yPos + 7);
			p5.line(xPos - 30, yPos + 7, xPos - 14, yPos + 7);
			// p5.bezier(xPos, yPos, (xPos+x)/2, yPos, xPos, y, x+r, y);
			p5.noStroke();

			p5.noFill(); p5.stroke(SELECTED);
			p5.ellipse(x, y, r + 3, r + 3);
		}
	}

	public static class EdgeGfx {
		App p5;
		Edge e;
		float ax;
		float ay;
		float bx;
		float by;
		public EdgeGfx(App p5, Edge e) {
			this.p5 = p5;
			this.e = e;
			this.ax = e.getA().getX();
			this.ay = e.getA().getY();
			this.bx = e.getB().getX();
			this.by = e.getB().getY();
		}
		public void draw() {
			p5.noFill();
			p5.stroke(0xff666666);
			p5.line(ax, ay, bx, by);
		}
		public void drawActive() {
			p5.noFill();
			p5.stroke(SELECTED);
			p5.line(ax, ay, bx, by);
		}
	}
}
