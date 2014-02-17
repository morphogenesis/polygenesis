package app.core;

import processing.core.PApplet;
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
	private ArrayList<Polygon2D> cells;
	private ArrayList<VerletParticle2D> particles;
	private VerletPhysics2D physics;

	public VoronoiDiagram(App p5) {
		this.p5 = p5;
		this.physics = App.PSYS.getPhysics();
		this.particles = App.PSYS.getParticles();
		this.clipper = new SutherlandHodgemanClipper(App.PSYS.getBounds().copy());
		this.voronoi = new Voronoi();
		this.cells = new ArrayList<>();
	}
	private ArrayList<Vec2D> getVecs() {
		ArrayList<Vec2D> vecs = new ArrayList<>();
		for (Vec2D v : particles) {
			Vec2D vi = new Vec2D((int) v.x, (int) v.y);
			vecs.add(vi);
		}
		return vecs;
	}
	private Rect getClipRect() {
		Rect r = new Rect(0, 0, App.WIDTH, App.HEIGHT);
		r.scale(Gui.vor_clipScale);
		p5.noFill(); p5.stroke(0xffff00ff);
		p5.rect(r.getTopLeft().x, r.getTopLeft().y, r.width, r.height);
		p5.noStroke();
		return r;
	}
/*	private List<Vec2D> getPerim() {
		List<Vec2D> vecs = new ArrayList<>();

		Rect r = getClipRect().copy().scale(Gui.vor_rectScale);
		Circle c = physics.getCurrentBounds().scale(Gui.vor_ringScale).getBoundingCircle();

		Polygon2D polyRect = r.toPolygon2D().increaseVertexCount((int) Gui.vor_perimRes);
		Polygon2D polyCircle = c.toPolygon2D((int) Gui.vor_perimRes);

		vecs.addAll(polyRect.vertices);
		vecs.addAll(polyCircle.vertices);

		drawPerim(r, c);
		return vecs;
	}*/

	private Polygon2D frame() {

		Rect r = getClipRect().copy().scale(Gui.vor_rectScale);
		return r.toPolygon2D().increaseVertexCount((int) Gui.vor_perimRes);
	}
	private Polygon2D activePolies() {
		Polygon2D p = new Polygon2D();
		for (Vec2D v : particles) { p.add(v); }
		return p.toPolygon2D();
	}

	private Polygon2D ring() {
		Circle c = physics.getCurrentBounds().scale(Gui.vor_ringScale).getBoundingCircle();
		return c.toPolygon2D((int) Gui.vor_perimRes);
	}
	/*
		private Rect intersectorOld() {
			Rect r = getClipRect().copy().scale(Gui.vor_intersectorScale);
			return r;
		}
	*/
/*	List<Vec2D> perims() {
		List<Vec2D> vecs = new ArrayList<>();
*//*		vecs.addAll(ring().vertices);*//*
		Polygon2D poly = new Polygon2D();
		int num = particles.size();
		Circle c = physics.getCurrentBounds().getBoundingCircle();
		float radius = physics.getCurrentBounds().getBoundingCircle().getRadius();
		for (int i = 0; i < num; i++) {
			Vec2D v = particles.get(i);
			poly.add(Vec2D.fromTheta((float) i / num * App.TWO_PI).scaleSelf(Gui.vor_ringScale * radius).addSelf(c.x, c.y));
//			poly.add(Vec2D.fromTheta(v.heading()*//*float) i / num * App.TWO_PI*//*).scaleSelf(Gui.vor_ringScale*//* * v.distanceTo(c)*//*).addSelf(v.x, v.y));
			poly.add(v.copy().scaleSelf(Gui.vor_ringScale).addSelf(Gui.vor_ringScale,Gui.vor_ringScale));
		}
		poly.smooth(0.5f, 0.05f);
		vecs.addAll(poly.vertices);
		p5.stroke(360, 100, 100, 100); p5.noFill();
		App.GFX.polygon2D(poly);

		return vecs;
	}*/

	/*	private Polygon2D goodPoly() {
			Polygon2D cPoly = ring().copy(). (Gui.vor_intersectorScale);
			return cPoly;
		}*/
	public void draw() {
		if (Gui.drawVoronoi) {
			if (Gui.updateVoronoi) {
				voronoi = new Voronoi();
				voronoi.addPoints(particles);
				voronoi.addPoints(frame().vertices);
				voronoi.addPoints(ring().vertices);
//				if (getPerim() != null) voronoi.addPoints(getPerim());
				cells = new ArrayList<>();
				clipper = new SutherlandHodgemanClipper(getClipRect());
			}
			p5.noFill(); p5.noStroke();
			for (Polygon2D p : voronoi.getRegions()) {
				p = clipper.clipPolygon(p);
				if (p.intersectsPolygon(activePolies())) {

					if (Gui.drawVorPoly) {p5.stroke(0xffe5cdab); p5.fill(330, 3, 27, 100); App.GFX.polygon2D(p); }
					if (Gui.drawVorBez) {
						p5.stroke(0xffeaeaea); p5.fill(0xff686463);
						for (Vec2D v : particles) {
							if (p.containsPoint(v)) {
								p5.stroke(0xffe5cdab); p5.fill(0xff444243); drawBezier(p);
								break;
							}
						}
					}
				} if (Gui.drawVorVec) { p5.stroke(VOR_VERTS); p5.noFill(); for (Vec2D vec : p.vertices) { App.GFX.circle(vec, 2); } }
			}
			p5.fill(0xff000000); p5.noStroke();
			if (Gui.drawPhysInfo) { drawInfo(); }
			drawExtrashit();
		}
	}
	private void drawExtrashit() {
		p5.stroke(0xffffffff);
		p5.noFill();
		App.GFX.polygon2D(frame());
		App.GFX.polygon2D(ring());
		/*for (Vec2D v : perims()) {
			p5.fill(360, 100, 100, 100);
			p5.ellipse(v.x, v.y, 6, 6);
		}*/
		p5.noFill();
//		App.GFX.polygon2D(goodPoly());
	}
	private void drawPerim(Rect r, Circle c) {
		p5.noFill(); p5.stroke(0, 10);
		p5.rect(r.getTopLeft().x, r.getTopLeft().y, r.width, r.height);
		p5.ellipse(c.x, c.y, c.getRadius(), c.getRadius());
		p5.stroke(0, 100);
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

	private void drawInfo() {

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
}

/*	private void update() {

	}
	private void drawVoronoi() {

	}*/

//			if (Gui.drawVorPoly) { p5.stroke(0xffe5cdab); p5.fill(330, 3, 27, 100); for (Polygon2D p : voronoi.getRegions()) { App.GFX.polygon2D(p); } }
		/*	p5.noFill(); p5.noStroke();
			if (Gui.drawVorVec) {
				p5.stroke(VOR_VERTS);
				for (Polygon2D poly : cells) { for (Vec2D vec : poly.vertices) { App.GFX.circle(vec, 2); } }
			}*/