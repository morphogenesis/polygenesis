package app.core;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.geom.Polygon2D;
import toxi.geom.PolygonClipper2D;
import toxi.geom.SutherlandHodgemanClipper;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import java.util.ArrayList;
import java.util.List;

public class VoronoiDiagram {
	protected PApplet p5;
	protected PolygonClipper2D clipper;
	protected int refreshRate = 0;
	protected ArrayList<Vec2D> cellSites, voidSites;
	protected ArrayList<Polygon2D> cellRegions, voidRegions;
	ArrayList<Vec2D> extras = new ArrayList<>();

	public VoronoiDiagram(PApplet $p5) {
		this.p5 = $p5;
		clipper = new SutherlandHodgemanClipper(App.PSYS.getBounds());
		cellSites = new ArrayList<>();
		voidSites = new ArrayList<>();
		cellRegions = new ArrayList<>();
		voidRegions = new ArrayList<>();
	}

	public void draw() {
		if (Gui.drawVoronoi) {
			if (Gui.updateVoronoi) update();
			if (Gui.drawVorPoly) {for (Polygon2D vp : voidRegions) drawPoly(vp, 0xffffffff);}
			if (Gui.drawVorPoly) {for (Polygon2D p : cellRegions) drawPoly(p, 0xff666666);}
			if (Gui.drawVorVec) { for (Polygon2D p : cellRegions) drawHandles(p, 0xffeca860);}
			if (Gui.drawVorInfo) {drawInfo(0xff666666);}
		}
	}

	private void update() {
//		if (refreshRate++ % Gui.VOR_REFRESH == 0) {
		Voronoi voronoi = new Voronoi();
		for (VerletParticle2D v : App.PSYS.getPhysics().particles) { voronoi.addPoint(v); }
		for (AttractionBehavior2D a : App.PSYS.attractors) { voronoi.addPoint(a.getAttractor()); }
		cellRegions.clear(); voidRegions.clear();
		for (Polygon2D poly : voronoi.getRegions()) {
			poly = clipper.clipPolygon(poly);
			if (poly.vertices.size() < 3) return;
			if (Gui.drawVoronoi) for (Polygon2D v : voronoi.getRegions()) { drawPoly(v, 0xffffffff); }
//			if (Gui.drawVoronoi) for (Vec2D v : cellSites) { if ((poly.containsPoint(v))) cellRegions.add(poly); }
//			if (Gui.drawVoronoi) for (Vec2D v : voidSites) { if ((poly.containsPoint(v))) voidRegions.add(poly); }
		}
//		}
	}

	private void drawPoly(Polygon2D poly, int color) {
		List<Vec2D> pts = poly.vertices;
		int count = pts.size();
		p5.stroke(color);
		p5.beginShape();
		p5.vertex((pts.get(count - 1).x + pts.get(0).x) / 2, (pts.get(count - 1).y + pts.get(0).y) / 2);
		for (int i = 0; i < count; i++) {
			p5.bezierVertex(pts.get(i).x, pts.get(i).y, pts.get(i).x, pts.get(i).y, (pts.get((i + 1) % count).x + pts.get(i).x) / 2, (pts.get((i + 1) % count).y + pts.get(i).y) / 2);
		} p5.endShape(PApplet.CLOSE);
		p5.noStroke();
	}

	private void drawInfo(int color) {
		p5.fill(color);
		for (int i = 0; i < cellRegions.size(); i++) {
			Polygon2D region = cellRegions.get(i);
			int area = (int) (Math.abs(region.getArea()) / App.world_scale);
			Vec2D centroid = region.getCentroid();
			p5.textAlign(PConstants.CENTER);
			p5.text(area, centroid.x, centroid.y);
			p5.textAlign(PConstants.LEFT);
			p5.text(area, 1600, (10 * i) + 500);
		} p5.noFill();
	}

	private void drawHandles(Polygon2D poly, int color) {
		p5.stroke(color);
		for (Vec2D v : poly.vertices) {p5.ellipse(v.x, v.y, 4, 4);}
		p5.noStroke();
	}

	public void addExtras(int cnt) {
		for (int i = 0; i < cnt; i++) {
			Vec2D e = new Vec2D(App.PSYS.getBounds().getRandomPoint());
			extras.add(e);
			voidSites.add(e);
			App.PSYS.addAttractor(e);
		}
	}

	public void addPerim(int res) {
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
	}

	public void addVoids(ArrayList<Vec2D> v) { voidSites.addAll(v); }

	public void addVoid(Vec2D v) {voidSites.add(v);}

	public void addCells(ArrayList<Vec2D> cells) { cellSites.addAll(cells); }

	public void addCell(Vec2D point) { cellSites.add(point); }
}
