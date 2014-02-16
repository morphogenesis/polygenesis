package app.graph;

import app.core.App;

import static util.Color.SELECTED;

/**
 * Created on 2/15/14.
 */
public class EdgeGfx {
	App p5;
	GEdge e;
	float ax;
	float ay;
	float bx;
	float by;
	public EdgeGfx(App p5, GEdge e) {
		this.p5 = p5;
		this.e = e;
		this.ax = e.getNodeA().getX();
		this.ay = e.getNodeA().getY();
		this.bx = e.getNodeB().getX();
		this.by = e.getNodeB().getY();
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
