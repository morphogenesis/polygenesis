package app.core;

import processing.core.PApplet;
import toxi.color.ReadonlyTColor;
import toxi.geom.*;
import toxi.geom.mesh2d.Voronoi;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;

import java.util.ArrayList;
import java.util.List;

import static util.Color.VOR_VERTS;

public class VoronoiDiagram {
	private App p5;
	private PolygonClipper2D clipper;
	private Voronoi voronoi;
	//	private ArrayList<VerletParticle2D> sites;
	private ArrayList<Polygon2D> cells;
	private ArrayList<Polygon2D> voids;
	private PSys psys;
	private ArrayList<VerletParticle2D> particles;
	private VerletPhysics2D physics;

	public VoronoiDiagram(App p5) {
		this.p5 = p5;
		this.psys = App.PSYS;
		this.physics = App.PSYS.getPhysics();
		this.particles = App.PSYS.getParticles();

//		this.sites = psys.getPhysics().particles;
		this.clipper = new SutherlandHodgemanClipper(psys.getBounds().copy());
		this.voronoi = new Voronoi();
		this.cells = new ArrayList<>();
		this.voids = new ArrayList<>();
	}
	private ArrayList<Vec2D> getVecs() {
		ArrayList<Vec2D> vecs = new ArrayList<>();
		for (Vec2D v : particles) {
			Vec2D vi = new Vec2D((int) v.x, (int) v.y);
			vecs.add(vi);
		}
		return vecs;
	}
	private List<Vec2D> getPerim() {
		List<Vec2D> vecs = new ArrayList<>();

		Rect r = getClipRect().copy().scale(Gui.vor_boundsScale);
		Polygon2D polyRect = r.toPolygon2D().increaseVertexCount((int) Gui.vor_perimRes);
		vecs.addAll(polyRect.vertices);

		Circle c = physics.getCurrentBounds().scale(Gui.vor_perimScale).getBoundingCircle();
		Polygon2D polyCircle = c.toPolygon2D((int) Gui.vor_perimRes);
		vecs.addAll(polyCircle.vertices);

		drawPerim(r, c);
		return vecs;
	}

	private Rect getClipRect() {
		Rect r = new Rect(0, 0, App.WIDTH, App.HEIGHT);
		r.scale(Gui.vor_clipScale);
		return r;
	}

	public void draw() {
		if (Gui.showVoronoi) {
			if (Gui.UPDATE_VORONOI) { update(); }
			p5.noFill(); p5.noStroke();
			if (Gui.showPolygons) { p5.stroke(0xff333333); for (Polygon2D p : voronoi.getRegions()) { App.GFX.polygon2D(p); } }
			p5.noFill(); p5.noStroke();
			if (Gui.showVerts) { p5.stroke(VOR_VERTS); for (Polygon2D poly : cells) { for (Vec2D vec : poly.vertices) { App.GFX.circle(vec, 2); } } }
			if (OPT.showInfo) { drawInfo(); }
		}
	}

	private void update() {
		voronoi = new Voronoi();

		voronoi.addPoints(getVecs());
		if (getPerim() != null) voronoi.addPoints(getPerim());
		cells = new ArrayList<>();
		voids = new ArrayList<>();
		clipper = new SutherlandHodgemanClipper(getClipRect());
		for (Polygon2D p : voronoi.getRegions()) {
			p = clipper.clipPolygon(p);
			if (Gui.showBezier) {
				for (Vec2D v : particles) {
					if (p.containsPoint(v)) {p5.stroke(0xffe5cdab); p5.fill(0xff444243); break; }
					p5.stroke(0xffeaeaea); p5.fill(0xffbfbbba);
				} drawBezier(p);
			}
		}
	}
	private void drawInfo() {
		p5.fill(0xff000000);
		p5.noStroke();
		int index = 0;
		for (Polygon2D p : voronoi.getRegions()) {
			if (!p.isClockwise()) p.flipVertexOrder();
			Vec2D centroid = p.getCentroid();
			float x = centroid.x;
			float y = centroid.y;
			index++;
			int numVerts = p.getNumVertices();
			int circumference = (int) p.getCircumference();
			int area = (int) p.getArea();
			boolean isConvex = p.isConvex();
			boolean isClockwise = p.isClockwise();

			p5.text("[" + index + "] [" + isClockwise + "] [" + isConvex + "]", x, y);
			p5.text("verts: " + numVerts, x, y + 10);
			p5.text("circumference: " + circumference, x, y + 20);
			p5.text("area: " + area, x, y + 30);
		}
	}
	private void drawBezier(Polygon2D p) {

		List<Vec2D> v = p.vertices;
		int j = v.size();
		if (j >= 3) {
			p5.beginShape();
			p5.vertex((v.get(j - 1).x + v.get(0).x) / 2, (v.get(j - 1).y + v.get(0).y) / 2);
			for (int i = 0; i < j; i++) { p5.bezierVertex(v.get(i).x, v.get(i).y, v.get(i).x, v.get(i).y, (v.get((i + 1) % j).x + v.get(i).x) / 2, (v.get((i + 1) % j).y + v.get(i).y) / 2); }
			p5.endShape(PApplet.CLOSE);
		}
	}
	void drawPerim(Rect r, Circle c) {
		p5.noFill(); p5.stroke(0, 10);
		p5.rect(r.getTopLeft().x, r.getTopLeft().y, r.width, r.height);
		p5.ellipse(c.x, c.y, c.getRadius(), c.getRadius());
		p5.stroke(0, 100);
	}
	class ColoredPolygon extends Polygon2D {
		ReadonlyTColor col;

		public ColoredPolygon(ReadonlyTColor col) {
			this.col = col;
		}
	}
}
//		}
//		for (Polygon2D p : voronoi.getRegions()) {
//			p5.noStroke();p5.noFill();
//			if (cells.contains(p)) { p5.stroke(0xffe5cdab); p5.fill(0xff444243); } if (voids.contains(p)) {
//		p5.stroke(0xffeaeaea); p5.fill(0xffbfbbba);
//			}

//		Polygon2D boundPoly = new Polygon2D();
//		for (VerletParticle2D p : particles) {			boundPoly.add(p);		}
//		boundPoly.smooth(0.01f, 0.05f).increaseVertexCount((int) vorBndsNumPts).offsetShape((int) vorBndsOffset);
