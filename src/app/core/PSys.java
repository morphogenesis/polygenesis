package app.core;

import app.graph.Edge;
import app.graph.Graph;
import app.graph.Node;
import processing.core.PApplet;
import toxi.geom.Polygon2D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletMinDistanceSpring2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static util.Color.BLACK;
import static util.Color.GREY_DK;

public class PSys {
	public final List<AttractionBehavior2D> perimeter = new ArrayList<>();
	private final Rect bounds;
	private VerletPhysics2D physics = new VerletPhysics2D();
	private List<VerletSpring2D> springs = new ArrayList<>();
	private List<VerletSpring2D> minDistSprings = new ArrayList<>();
	private List<AttractionBehavior2D> behaviors = new ArrayList<>();
	private ArrayList<VerletParticle2D> particles = new ArrayList<>();
	private HashMap<String, String> info = new HashMap<>();
	private App p5;
	public PSys(App p5) {
		this.p5 = p5;
		bounds = new Rect(10, 10, p5.width - 20, p5.height - 20);
		physics.setWorldBounds(bounds);
		physics.setDrag(Gui.PHYS_DRAG);
	}
	public void draw() {
		p5.noFill(); p5.noStroke();
		if (Gui.updatePhysics) update();
		if (Gui.drawPhysSprings) {p5.stroke(0xff333333); p5.noFill(); for (VerletSpring2D s : springs) { p5.line(s.a.x, s.a.y, s.b.x, s.b.y); } }
		if (Gui.drawPhysMindist) {p5.stroke(0xff333333); p5.noFill(); for (VerletSpring2D s : minDistSprings) { p5.line(s.a.x, s.a.y, s.b.x, s.b.y); } }
		if (Gui.drawPhysParticles) { p5.stroke(BLACK); p5.fill(GREY_DK); for (VerletParticle2D a : physics.particles) { p5.ellipse(a.x, a.y, 3, 3); } }
		if (Gui.drawPhysWeights) { p5.stroke(BLACK); p5.fill(GREY_DK); for (VerletParticle2D a : physics.particles) { p5.ellipse(a.x, a.y, a.getWeight(), a.getWeight()); } }

		if (Gui.drawPhysBehaviors) {
			p5.stroke(0xff343434); p5.noFill();
			for (AttractionBehavior2D a : behaviors) { Vec2D vb = a.getAttractor(); p5.ellipse(vb.x, vb.y, a.getRadius(), a.getRadius()); }
			for (AttractionBehavior2D a : perimeter) { Vec2D vb = a.getAttractor(); p5.ellipse(vb.x, vb.y, a.getRadius(), a.getRadius()); }
		}

		if (Gui.drawPhysInfo) {
			p5.fill(0xff666666); p5.noStroke();
			p5.pushMatrix(); p5.translate(300, 30);
			for (String key : info.keySet()) {
				p5.translate(0, 10); p5.textAlign(PApplet.LEFT); p5.text(key, -50, 0);
				p5.textAlign(PApplet.RIGHT); p5.text(String.valueOf(info.get(key)), 80, 0);
			} p5.popMatrix();
		}
	}
	private void update() {
		physics.update();
		physics.setDrag(Gui.PHYS_DRAG);
		for (VerletSpring2D s : springs) { s.setStrength(Gui.springStrength); }
		for (VerletSpring2D s : minDistSprings) {s.setStrength(Gui.mindistStrength);}
		for (VerletParticle2D n : particles) { n.setWeight(Gui.particleWeight); }
		for (AttractionBehavior2D b : behaviors) {b.setStrength(Gui.behaviorStrength);}

		for (AttractionBehavior2D a : perimeter) { a.setRadius(Gui.behaviorScale); a.setStrength(Gui.behaviorStrength); }

		info.put("PSYS.springs : ", String.valueOf(physics.springs.size()));
		info.put("PSYS.particles phys: ", String.valueOf(physics.particles.size()));
		info.put("PSYS.behaviors phys: ", String.valueOf(physics.behaviors.size()));
		info.put("phys.springs : ", String.valueOf(springs.size()));
		info.put("phys.particles : ", String.valueOf(particles.size()));
		info.put("phys.behaviors : ", String.valueOf(behaviors.size()));
		info.put("w.iter : ", App.DF3.format(physics.getNumIterations()));
		info.put("w.drag : ", App.DF3.format(physics.getDrag()));
		info.put("x.getB scale : ", App.DF3.format(Gui.behaviorScale));
		info.put("x.p scale : ", App.DF3.format(Gui.particleScale));
		info.put("x.s scale : ", App.DF3.format(Gui.springScale));
	}
	public void addMinDist() {
		Graph g = App.GRAPH;
		for (Node na : g.nodes) {
			VerletParticle2D va = na.getParticle2D();
			for (Node nb : g.nodes) {
				VerletParticle2D vb = nb.getParticle2D();
				if ((na != nb) && (physics.getSpring(na.getParticle2D(), nb.getParticle2D()) == null)) {
					float len = (na.getRadius() + nb.getRadius());
					VerletSpring2D s = new VerletMinDistanceSpring2D(va, vb, len, .01f);
					minDistSprings.add(s);
					physics.addSpring(s);
				}
			}
		}
	}
	public void addParticle(Node n) {
		particles.add(n.getParticle2D());
		physics.addParticle(n.getParticle2D());
		behaviors.add(n.getBehavior2D());
		physics.addBehavior(n.getBehavior2D());
	}
	public void removeParticle(Node n) {
		particles.remove(n.getParticle2D());
		physics.removeParticle(n.getParticle2D());
		behaviors.remove(n.getBehavior2D());
		physics.removeBehavior(n.getBehavior2D());
	}
	public void addSpring(Edge e) {
		springs.add(e.getSpring2D());
		physics.addSpring(e.getSpring2D());
	}
	public void removeSpring(Edge e) {
		springs.remove(e.getSpring2D());
		physics.removeSpring(e.getSpring2D());
	}

	public void clearMinDist() { for (VerletSpring2D s : minDistSprings) { physics.springs.remove(s); } minDistSprings.clear();}
	public void reset() {springs.clear(); minDistSprings.clear(); particles.clear(); behaviors.clear(); physics.clear();}
	public void addPerim() {
		Rect r = new Rect(0, 0, App.WIDTH, App.HEIGHT);
		Polygon2D polyRect = r.toPolygon2D().increaseVertexCount((int) Gui.psys_perimRes);
		for (Vec2D v : polyRect.vertices) {
			VerletParticle2D p = new VerletParticle2D(v, 1);
			AttractionBehavior2D a = new AttractionBehavior2D(v, 200, -1);
//			p.lock();
			physics.addParticle(p);
			physics.addBehavior(a);
			perimeter.add(a);
		}
	}
	public VerletPhysics2D getPhysics() { return physics; }
	public Rect getBounds() { return bounds; }
	public float getDrag() {return physics.getDrag();}
	public ArrayList<VerletParticle2D> getParticles() { return particles; }
}

