package app.core;

import processing.core.PApplet;
import toxi.color.ColorRange;
import toxi.color.ReadonlyTColor;
import toxi.color.TColor;
import toxi.geom.*;
import toxi.geom.mesh2d.Voronoi;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;

import java.util.ArrayList;
import java.util.List;

import static util.Color.VOR_TXT;
import static util.Color.VOR_VERTS;

public class VoronoiDiagram {
	private App p5;
	private Settings X;
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
		this.X = App.CONF;
		this.psys = App.PSYS;
		this.physics = App.PSYS.getPhysics();
		this.particles = App.PSYS.getPhysics().particles;
//		this.sites = psys.getPhysics().particles;
		this.clipper = new SutherlandHodgemanClipper(psys.getBounds().copy());
		this.voronoi = new Voronoi();
		this.cells = new ArrayList<>();
		this.voids = new ArrayList<>();
	}

	public void draw() {
		if (X.showVoronoi) {
			if (X.UPDATE_VORONOI) {
				update();
			}
			p5.noFill(); p5.noStroke();
			if (X.showPolygons) { p5.stroke(0xff333333); for (Polygon2D p : voronoi.getRegions()) { App.GFX.polygon2D(p); } }
			p5.noFill(); p5.noStroke();
			if (X.showVerts) { p5.stroke(VOR_VERTS); for (Polygon2D poly : cells) { for (Vec2D vec : poly.vertices) { App.GFX.circle(vec, 2); } } }
			if (X.showInfo) { drawInfo(); }
		}
	}
	List<Vec2D> getWorldBoundVerts() {
		Rect r = physics.getWorldBounds().copy();
		Polygon2D p = r.toPolygon2D().increaseVertexCount((int) X.worldBoundRes).offsetShape((int) X.worldBoundOffset);
		return p.vertices;
	}
	List<Vec2D> getCurrBoundVerts() {
		Circle bounds = physics.getCurrentBounds().getBoundingCircle();
		p5.noFill(); p5.stroke(0xff444243);
		p5.ellipse(bounds.x, bounds.y, bounds.getRadius(), bounds.getRadius());
		Polygon2D boundPoly = bounds.toPolygon2D((int) X.curBoundRes).offsetShape((int) X.curBoundOffset);
		return boundPoly.vertices;
	}

	private void update() {

		voronoi = new Voronoi();
		voronoi.addPoints(particles);
		voronoi.addPoints(getWorldBoundVerts());
		voronoi.addPoints(getCurrBoundVerts());
		cells = new ArrayList<>();
		voids = new ArrayList<>();
		clipper = new SutherlandHodgemanClipper(physics.getWorldBounds().copy().scale(2));
		for (Polygon2D p : voronoi.getRegions()) {
			p = clipper.clipPolygon(p);
			if (X.showBezier) {
				for (Vec2D v : particles) {
					if (p.containsPoint(v)) {p5.stroke(0xffe5cdab); p5.fill(0xff444243); break; }
					p5.stroke(0xffeaeaea); p5.fill(0xffbfbbba);
				} drawBezier(p);
			}
		}
	}
	private void drawInfo() {
		p5.fill(VOR_TXT);
		p5.noStroke();
		for (Polygon2D p : voronoi.getRegions()) {
			p5.text(p.getNumVertices() + "." + cells.indexOf(p), p.getCentroid().x, p.getCentroid().y);
			float c = p.getCircumference();
		}
	}
	private void drawBezier(Polygon2D p) {

		List<Vec2D> v = p.vertices;
		int j = v.size();
		p5.beginShape();
		p5.vertex((v.get(j - 1).x + v.get(0).x) / 2, (v.get(j - 1).y + v.get(0).y) / 2);
		for (int i = 0; i < j; i++) { p5.bezierVertex(v.get(i).x, v.get(i).y, v.get(i).x, v.get(i).y, (v.get((i + 1) % j).x + v.get(i).x) / 2, (v.get((i + 1) % j).y + v.get(i).y) / 2); }
		p5.endShape(PApplet.CLOSE);
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
//		boundPoly.smooth(0.01f, 0.05f).increaseVertexCount((int) X.vorBndsNumPts).offsetShape((int) X.vorBndsOffset);
