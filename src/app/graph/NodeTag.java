package app.graph;

import app.core.App;
import processing.core.PApplet;
import util.Color;

import static util.Color.SELECTED;

/**
 * Created on 2/15/14.
 */
public class NodeTag {
	App p5;
	GNode n;
	float y;
	float x;
	float r;
	public NodeTag(App p5, GNode n) {
		this.p5 = p5;
		this.n = n;
		this.x = n.getX();
		this.y = n.getY();
		this.r = n.getRadius();
	}
	public void draw() {
		p5.noFill();
		p5.stroke(n.getColor(), 100, 100, 50); p5.strokeWeight(1); p5.ellipse(x, y, r + 1, r + 1);
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
		if (n.getId() % 2 == 0) p5.fill(0xff2b2b2b);
		else p5.fill(0xff333333);
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
		p5.ellipse(x, y, r + 3, r + 3);
	}
	public void drawActive() {
		p5.noFill(); p5.stroke(30, 100, 100);
		p5.ellipse(x, y, r + 3, r + 3);
	}
	public void drawSelected() {
		int xPos = App.WIDTH - 200;
		int yPos = 50 + (n.getId() * 14);
		p5.noFill(); p5.stroke(0xff444444);
				p5.line(x + r, y,x + (r*2), y);
		p5.line(xPos - 100, y, xPos - 30, yPos + 7);
		p5.line(xPos - 30, yPos + 7, xPos-14, yPos + 7);
// p5.bezier(xPos, yPos, (xPos+x)/2, yPos, xPos, y, x+r, y);
		p5.noStroke();

		p5.noFill(); p5.stroke(SELECTED);
		p5.ellipse(x, y, r + 3, r + 3);

	}
}

/*	if (n == activeNode) p5.fill(ACTIVE);
		else if (selectedNodes.contains(n)) p5.fill(SELECTED);
		else*///		p5.beginShape();
//		p5.vertex(xPos-300, y);
//		p5.bezierVertex(xPos, y, xPos - 200, yPos + 7, xPos, yPos+ 7);
//
//		p5.endShape();
//		p5.line(x + r, y, xPos - 100, y + 7);
//		p5.line(xPos - 100, y, xPos - 20, yPos + 7);
/*		p5.beginShape();
		p5.curveVertex(x + r, y);
		p5.curveVertex(xPos - 100, y);
		p5.curveVertex(xPos - 20, yPos + 7);
		p5.curveVertex(xPos, yPos + 7);
		p5.endShape();*/
//		p5.bezierDetail(100);
//		p5.bezier(x + r, y, xPos, y, x + r, yPos + 7, xPos, yPos + 7);
// 		p5.curve(xPos - 300, y, xPos - 200, yPos + 7,xPos - 100, yPos + 7, xPos, yPos + 7);
