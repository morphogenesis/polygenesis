package app.core;

import processing.core.PApplet;
import toxi.geom.*;
import toxi.geom.mesh2d.Voronoi;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import java.util.ArrayList;
import java.util.List;

import static util.Color.VOR_VERTS;

public class VoronoiDiagram {
	private App p5;
	private PolygonClipper2D clipper;
	private Voronoi voronoi;
	private ArrayList<VerletParticle2D> particles;
	private VerletPhysics2D physics;
	ArrayList<Vec2D> rand;
	public VoronoiDiagram(App p5) {
		this.p5 = p5;
		this.physics = App.PSYS.getPhysics();
		this.particles = App.PSYS.getParticles();
		this.clipper = new SutherlandHodgemanClipper(App.PSYS.getBounds().copy());
		this.voronoi = new Voronoi();
		rand = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			Vec2D vi = new Vec2D(getClipRect().getRandomPoint());
			rand.add(vi);
		}
	}

	private Rect getClipRect() {
		Rect r = new Rect(0, 0, App.WIDTH, App.HEIGHT);
		r.scale(Gui.vor_clipScale);
		p5.noFill(); p5.stroke(0xffff00ff);
		p5.rect(r.getTopLeft().x, r.getTopLeft().y, r.width, r.height);
		p5.noStroke();
		return r;
	}

	private Polygon2D frame() {
		Rect r = getClipRect().copy().scale(Gui.vor_rectScale);
		return r.toPolygon2D().increaseVertexCount((int) Gui.vor_perimRes);
	}
	private Polygon2D intersectingPoly() {
		Polygon2D p = new Polygon2D();
		for (Vec2D v : particles) { p.add(v); }
		return p.toPolygon2D();
	}

	private Polygon2D ring() {
		Circle c = physics.getCurrentBounds().scale(Gui.vor_ringScale).getBoundingCircle();
		return c.toPolygon2D((int) Gui.vor_perimRes);
	}
	private List<Vec2D> vecs() {
		ArrayList<Vec2D> verts = new ArrayList<>();
		for (Vec2D v : rand) {
			for (AttractionBehavior2D a : App.PSYS.behaviors) {
				Circle c = new Circle(a.getAttractor(), a.getRadius());
				if (c.containsPoint(v)) v.constrain(ring());
				verts.add(v);
			}
		} return verts;
	}
	public void draw() {
		if (Gui.drawVoronoi) {
			if (Gui.updateVoronoi) {
				voronoi = new Voronoi();
				voronoi.addPoints(particles);
				voronoi.addPoints(vecs());
				clipper = new SutherlandHodgemanClipper(getClipRect());
			}
			p5.noFill(); p5.noStroke();
			for (Polygon2D p : voronoi.getRegions()) {
				p = clipper.clipPolygon(p);
				if (p.intersectsPolygon(intersectingPoly())) {
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
			drawExtras();
		}
	}
	private void drawExtras() {
		p5.stroke(0xffffffff);
		p5.noFill();
		App.GFX.polygon2D(frame());
		App.GFX.polygon2D(ring());
		p5.noFill();
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