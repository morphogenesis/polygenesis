package app.ui;

import app.core.App;
import app.graph.Edge;

import static util.Color.SELECTED;

/**
 * Created on 2/26/14.
 */
class DrawEdge {
	private final App p5;
	final Edge e;
	private final float ax;
	private final float ay;
	private final float bx;
	private final float by;
	DrawEdge(App p5, Edge e) {
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
