package app.core;

import app.graph.Graph;
import app.phys.PSys;
import app.xml.Node;
import processing.core.PApplet;
import toxi.geom.*;
import toxi.geom.mesh2d.Voronoi;

import java.util.ArrayList;
import java.util.List;

public class VoronoiDiagram {
	private final PApplet p5;
	//	private final ArrayList<Vec2D> extras = new ArrayList<>();
	private final PolygonClipper2D clipper = new SutherlandHodgemanClipper(PSys.getBounds());
	//	private final ArrayList<Vec2D> voidSites = new ArrayList<>();
	private Voronoi voronoi;
	public VoronoiDiagram(PApplet $p5) { this.p5 = $p5; }
	public static ArrayList<Vec2D> points = new ArrayList<>();

	public void draw() {
		if ((Gui.drawVoronoi) && (!Graph.nodes.isEmpty())) {
			voronoi = new Voronoi();
			for (Node n : Graph.nodes) { voronoi.addPoint(n.getParticle2D()); }
			if (!points.isEmpty()) for (Vec2D v : points) { if (v != null) voronoi.addPoint(v); }
//			voronoi.addPoints(points);
//			for (Vec2D v : PSys.attractorParticles) { voronoi.addPoint(v); }
//			for (Vec2D v : points) { voronoi.addPoint(v); }

			for (Polygon2D poly : voronoi.getRegions()) {
				poly = clipper.clipPolygon(poly);
				if (poly.vertices.size() < 3) return;
				for (Node n : Graph.nodes) {
					if (poly.containsPoint(n.getParticle2D())) {
						if (!poly.isClockwise()) poly.flipVertexOrder();
						if (Gui.isVorOffset) poly.offsetShape(Gui.setVorOffset);
						if (Gui.drawVorPoly) drawPoly(poly, 0xff222222, -1);
						if (Gui.drawVorBez) drawBezier(poly, 0xff666666, -1);
						if (Gui.drawVorVec) drawHandles(poly, 0xffeca860, -1);
						if (Gui.drawVorInfo) drawRegionInfo(poly, 0xff666666);
					}
				}
			}
			for (Vec2D v : voronoi.getSites()) { if (Gui.drawVorInfo) drawSiteInfo(v, 0xffffffff); }
		}
	}

	private void drawPoly(Polygon2D poly, int stroke, int fill) {
		if (fill == -1) { p5.noFill(); } else p5.fill(fill);
		if (stroke == -1) { p5.noStroke(); } else p5.stroke(stroke);
		for (Line2D l : poly.getEdges()) { p5.line(l.a.x, l.a.y, l.b.x, l.b.y); }
		p5.noStroke();
		p5.noFill();
	}

	private void drawBezier(Polygon2D poly, int stroke, int fill) {
		if (fill == -1) { p5.noFill(); } else p5.fill(fill);
		if (stroke == -1) { p5.noStroke(); } else p5.stroke(stroke);

		List<Vec2D> list = poly.vertices;
		Vec2D vA = list.get(0);
		Vec2D vZ = list.get(list.size() - 1);
		Vec2D origin = new Vec2D((vZ.x + vA.x) / 2, (vZ.y + vA.y) / 2);

		p5.beginShape();
		p5.vertex(origin.x, origin.y);
		for (int i = 0; i < list.size(); i++) {
			Vec2D vi = list.get(i);
			Vec2D vj = list.get((i + 1) % list.size());
			p5.bezierVertex(vi.x, vi.y, vi.x, vi.y, (vj.x + vi.x) / 2, (vj.y + vi.y) / 2);
		}
		p5.endShape(PApplet.CLOSE);

		p5.noFill();
		p5.noStroke();
	}

	private void drawHandles(Polygon2D poly, int stroke, int fill) {
		if (fill == -1) { p5.noFill(); } else p5.fill(fill);
		if (stroke == -1) { p5.noStroke(); } else p5.stroke(stroke);
		for (Vec2D v : poly.vertices) {p5.ellipse(v.x, v.y, 4, 4);}
		p5.noStroke();
		p5.noFill();
	}
	private void drawRegionInfo(Polygon2D poly, int fill) {
		float x = poly.getCentroid().x;
		float y = poly.getCentroid().y;

		p5.fill(fill);
		p5.text("Vert: " + poly.getNumVertices(), x, y);
		p5.text("Area: " + (int) (poly.getArea() / (Gui.setWorldScl * Gui.setWorldScl)), x, y + 10);
		p5.text("Circ: " + (int) poly.getCircumference() / Gui.setWorldScl, x, y + 20);
		p5.noFill();
	}
	private void drawSiteInfo(Vec2D v, int fill) {
		int index = voronoi.getSites().indexOf(v);
		p5.fill(fill);
		p5.text(index, v.x + 10, v.y);
		p5.noFill();
	}

/*	public void addExtras(int cnt) {
		for (int i = 0; i < cnt; i++) {
			Vec2D e = new Vec2D(App.PSYS.getBounds().getRandomPoint());
			extras.add(e);
			voidSites.add(e);
			App.PSYS.addAttractor(e);
		}
	}*/

/*	public void addCloud(int res) {
		for (int i = 0; i < App.PSYS.getBounds().height; i += res) {
			Vec2D l = new Vec2D(App.PSYS.getBounds().getLeft() + 20, i + App.PSYS.getBounds().getTop());
			Vec2D r = new Vec2D(App.PSYS.getBounds().getRight() - 20, i + App.PSYS.getBounds().getTop());
			extras.add(l); extras.add(r);
			voidSites.add(l); voidSites.add(r);
			App.PSYS.addAttractor(l); App.PSYS.addAttractor(r);
		}
		for (int j = 0; j < App.PSYS.getBounds().width; j += res) {
			Vec2D t = new Vec2D(j + App.PSYS.getBounds().getLeft(), App.PSYS.getBounds().getTop() + 20);
			Vec2D b = new Vec2D(j + App.PSYS.getBounds().getLeft(), App.PSYS.getBounds().getBottom() - 20);
			extras.add(t); extras.add(b);
			voidSites.add(t); voidSites.add(b);
			App.PSYS.addAttractor(t); App.PSYS.addAttractor(b);
		}
	}*/

/*	private void drawSiteInfo(int fill) {
		p5.fill(fill);
		for (int i = 0; i < cellRegions.size(); i++) {
			Polygon2D region = cellRegions.get(i);
			int area = (int) (Math.abs(region.getArea()) / App.setWorldScl);
			Vec2D centroid = region.getCentroid();
			p5.textAlign(PConstants.CENTER);
			p5.text(area, centroid.x, centroid.y);
			p5.textAlign(PConstants.LEFT);
			p5.text(area, 1600, (10 * i) + 500);
		} p5.noFill();
	}*/
}

/*
	public void draw() {
		if (Gui.drawVoronoi) {
			voronoi = new Voronoi();
			for (Node n : Graph.nodes) { voronoi.addPoint(n.getParticle2D()); }
//			for (VerletParticle2D v : App.PSYS.getPhysics().particles) { voronoi.addPoint(v); }
//			for (AttractionBehavior2D a : App.PSYS.attractors) { voronoi.addPoint(a.getAttractor()); }
//			cellRegions.clear(); voidRegions.clear();
			for (int i = 0; i <= voronoi.getRegions().size(); i++) {
				Polygon2D poly = voronoi.getRegions().get(i);
				poly = clipper.clipPolygon(poly);
				if (poly.vertices.size() < 3) return;
				if (!poly.isClockwise()) poly.flipVertexOrder();
				poly.offsetShape(-2);
//				poly.smooth(0.01f,0.05f);
				if (Gui.drawVorPoly) drawPoly(poly, 0xff444444, -1);
				if (Gui.drawVorBez) drawBezier(poly, 0xff666666, -1);
				if (Gui.drawVorVec) drawHandles(poly, 0xffeca860, -1);
				if (Gui.drawVorInfo) drawDetailInfo(poly, i, 360);
			}
		for (Polygon2D poly : voronoi.getRegions()) {
				int index = voronoi.getRegions().indexOf(poly);
				poly = clipper.clipPolygon(poly);
				if (poly.vertices.size() < 3) return;
				if (!poly.isClockwise()) poly.flipVertexOrder();
				poly.offsetShape(-2);
//				poly.smooth(0.01f,0.05f);
				if (Gui.drawVorPoly) drawPoly(poly, 0xff444444, -1);
				if (Gui.drawVorBez) drawBezier(poly, 0xff666666, -1);
				if (Gui.drawVorVec) drawHandles(poly, 0xffeca860, -1);
				if (Gui.drawVorInfo) drawDetailInfo(poly,index, 360);
			}
if (Gui.drawVorInfo) drawInfo(0xff666666);
		}
		}

* */

	/*	private void drawBezier(Polygon2D poly, int stroke, int fill) {
		p5.fill(fill); if (fill == -1) p5.noFill();
		p5.stroke(stroke); if (stroke == -1) p5.noStroke();

		List<Vec2D> list = poly.vertices;
		int il = list.size();
		p5.beginShape();
		p5.vertex((list.get(il - 1).x + list.get(0).x) / 2, (list.get(il - 1).y + list.get(0).y) / 2);
		for (int i = 0; i < il; i++) { p5.bezierVertex(list.get(i).x, list.get(i).y, list.get(i).x, list.get(i).y, (list.get((i + 1) % il).x + list.get(i).x) / 2, (list.get((i + 1) % il).y + list.get(i).y) / 2); }
		p5.endShape(PApplet.CLOSE);

		p5.noFill(); p5.noStroke();
	}*/
//	public void addVoids(ArrayList<Vec2D> v) { voidSites.addAll(v); }
//	public void addVoid(Vec2D v) {voidSites.add(v);}
//	public void addCells(ArrayList<Vec2D> cells) { cellSites.addAll(cells); }
//	public void addCell(Vec2D point) { cellSites.add(point); }
/*	private void drawBezierG(Polygon2D poly, int stroke, int fill) {
		p5.fill(fill); if (fill == -1) p5.noFill();
		p5.stroke(stroke); if (stroke == -1) p5.noStroke();
		ToxiclibsSupport gfx = App.GFX;
		poly.increaseVertexCount(40);
		poly.smooth(.5f, .5f);
		gfx.polygon2D(poly);
		gfx.polygon2D(poly);
	}
	float MAX_IMPACT = 0.5f;*/
/*
	private void drawBezierC(Polygon2D poly, int stroke, int fill) {
		p5.fill(fill); if (fill == -1) p5.noFill();
		p5.stroke(stroke); if (stroke == -1) p5.noStroke();
		Vec2D[] points = new Vec2D[poly.vertices.size()];
		p5.beginShape();
		for (int i = 0; i < poly.vertices.size(); i++) { points[i] = new Vec2D(poly.vertices.get(i)); p5.vertex(points[i].x, points[i].y); }
		p5.endShape();
		p5.stroke(0);
		for (int i = 0; i < points.length; i++) { p5.ellipse(points[i].x, points[i].y, 5, 5); }
		float tight = (p5.mouseX - p5.height / 2.0f) / (p5.height / 2.0f) * MAX_IMPACT;
		Spline2D spline = new Spline2D(points, null, tight);
		LineStrip2D vertices = spline.toLineStrip2D(32);
		p5.beginShape();
		for (Vec2D v : vertices) { p5.vertex(v.x, v.y); }
		p5.endShape();
	}
*/