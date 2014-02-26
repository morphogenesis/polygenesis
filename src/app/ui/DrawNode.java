package app.ui;

import app.core.App;
import app.core.Gui;
import app.graph.Editor;
import app.graph.Graph;
import app.xml.Node;
import processing.core.PApplet;

/**
 * Created on 2/26/14.
 */
public class DrawNode {
	private final App p5;
	private final Node n;
	private final float nY;
	private final float nX;
	private final float nR;
	private final int nCol;
	private final int nId;
	private static final float oX = Gui.outlinerX;
	private final float oY;
	private final float areaPercentage;
	private final String name;
	private final float size;

	public DrawNode(App p5, Node n) {
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

	public void draw() {
		drawNode(nR, nCol, -1);
		drawInfo();
		if (Editor.selectedNodes.contains(n)) { drawNode(nR - 1, nCol, -1); drawNode(nR - 3, 180, 40); }
		if (n == Editor.activeNode) { drawNode(nR - 1, nCol, -1); drawNode(nR - 3, 360, 20); }
		if (n == Editor.hoveredNode) { drawNode(nR - 1, 180, -1); }
		if (Editor.lockedNodes.contains(n)) {drawNode(10, 0, nCol);}
		if (Gui.drawGraphList) { drawDatablock(); }
		drawNode(5, 20, nCol);
	}
	private void drawInfo() {
		p5.fill(nCol);
		p5.textAlign(PApplet.CENTER);
		p5.text("[" + nId + "] " + (int) size, nX, nY - 10);
		p5.textAlign(PApplet.LEFT);
		p5.noFill();
	}

	private void drawNode(float size, int stroke, int fill) {
		if (fill == -1) { p5.noFill(); } else if (fill == nCol) { p5.fill(fill, 100, 100, 50); } else p5.fill(fill, 50);
		if (stroke == -1) { p5.noStroke(); } else if (stroke == nCol) { p5.stroke(stroke, 100, 100, 100); } else p5.stroke(stroke);
		p5.ellipse(nX, nY, size, size);
		p5.noFill(); p5.noStroke();
	}

	public void drawWire() {
		if ((Editor.selectedNodes.contains(n)) || (Editor.activeNode == n)) {
			float h = 12;
			float w = 150;
			float xx = nX + nR;
			float yy = nY - nR;

			/** Wire */
			p5.stroke(0xff888888);
			p5.line(nX, nY, xx + h, yy - h);
			p5.line(xx + w + h, yy - h, oX - 100, yy - h);
			p5.line(oX - 100, yy - h, oX - 20, oY + 7);
			p5.line(oX - 20, oY + 7, oX - 8, oY + 7);

			/** Tag */
			p5.fill(0xff888888);
			p5.quad(xx, yy, xx + h, yy - h, xx + w + h, yy - h, xx + w, yy);
			p5.noStroke();

			/** Node */
			p5.fill(0xffffffff);
			p5.ellipse(nX, nY, 4, 4);

			/** Text */
			p5.fill(0xff000000);
			p5.textAlign(PApplet.LEFT);
			p5.text(nId, xx + 15, yy - 3);
			p5.text(name, xx + 35, yy - 3);
			p5.textAlign(PApplet.RIGHT);
			p5.text(App.DF1.format(size) + "sq.m", xx + w - 10, yy - 3);
			p5.textAlign(PApplet.LEFT);
			p5.noFill();
		}
	}

	private void drawDatablock() {
		/** Dot */
		p5.fill(0xff333333);
		p5.stroke(nCol, 100, 100);
		p5.ellipse(oX - 8, oY + 7, 3, 3);

		/** Bar */
		if (nId % 2 == 0) { p5.fill(0xff2b2b2b); } else { p5.fill(0xff333333); }
		p5.noStroke();
		p5.rect(oX, oY + 1, 200, 12);
		p5.stroke(0xff666666);
		p5.line(oX + 120, oY + 1, oX + 120, oY + 12);

		/** Text */
		p5.fill(0xffaeaeae);
		p5.textAlign(PApplet.LEFT);
		p5.text(name, oX, oY + 10);
		p5.text(App.DF1.format(size) + "sq.m", oX + 124, oY + 10);
		p5.textAlign(PApplet.RIGHT);
		p5.text(App.DF1.format(areaPercentage) + "%", oX + 190, oY + 10);
		p5.textAlign(PApplet.LEFT);
	}
}
