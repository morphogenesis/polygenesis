package app.phys;

import app.core.App;
import app.core.Gui;
import toxi.geom.Polygon2D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletMinDistanceSpring2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import java.util.ArrayList;

/**
 * Created on 2/26/14.
 */
public class Cloud {
	private final App p5;
	private static Rect bounds = PSys.getBounds();
	private static VerletPhysics2D physics = App.PSYS.getPhysics();
	public static ArrayList<VerletParticle2D> particles = new ArrayList<>();
	private static ArrayList<VerletSpring2D> mindists = new ArrayList<>();
	private static ArrayList<AttractionBehavior2D> cloudAttractors = new ArrayList<>();
	public static ArrayList<Vec2D> voidSites = new ArrayList<>();

	public Cloud(App p5) {
		this.p5 = p5;
	}

	public void draw() {
		if (Gui.isCloudUpdating) updateCloud();
		if (Gui.drawCloud) {
			p5.fill(0xff333333);
			for (VerletParticle2D v : particles) { p5.ellipse(v.x, v.y, 5, 5); }
			p5.noFill();
			p5.stroke(0xff222222);
			for (AttractionBehavior2D a : cloudAttractors) { Vec2D vb = a.getAttractor(); p5.ellipse(vb.x, vb.y, a.getRadius(), a.getRadius()); }
			for (VerletSpring2D s : mindists) {
				p5.line(s.a.x, s.a.y, s.b.x, s.b.y);
			}
		}
	}
	private static void updateCloud() {
//		for (VerletParticle2D p : particles) { p.setWeight(Gui.cloudVecWgt); }
//		for (AttractionBehavior2D b : cloudAttractors) { b.setRadius(Gui.cloudBhvScl); b.setStrength(Gui.cloudBhvStr); }
//		for (VerletSpring2D s : mindists) {s.setStrength(Gui.cloudMinStr); s.setRestLength(Gui.cloudMinScl);}
	}

	public static void addCloud() {
		Rect r = bounds.copy().scale(0.9f);
		Polygon2D polyRect = r.toPolygon2D().increaseVertexCount(40);
		for (Vec2D v : polyRect.vertices) {
			VerletParticle2D p = new VerletParticle2D(v, Gui.cloudVecWgt);
			AttractionBehavior2D a = new AttractionBehavior2D(p, Gui.cloudBhvScl, Gui.cloudBhvStr);
			PSys.attractorParticles.add(p);
			PSys.attractors.add(a);
			App.PSYS.getPhysics().addParticle(p);
			App.PSYS.getPhysics().addBehavior(a);

//			particles.add(p);
//			cloudAttractors.add(a);

		}
	}

	public static void addCloudMinDist() {
		clearCloudMin();
		for (VerletParticle2D va : PSys.attractorParticles) {
			for (VerletParticle2D vb : PSys.attractorParticles) {
				if (va != vb) {
					VerletSpring2D s = new VerletMinDistanceSpring2D(va, vb, Gui.cloudMinScl, Gui.cloudMinStr);
					PSys.attractorMin.add(s);
					PSys.physics.addSpring(s);
				}
			}
		}
	}
	public static void clearCloudMin() {
		for (VerletSpring2D s : PSys.attractorMin) {
			PSys.physics.removeSpring(s);
		} PSys.attractorMin.clear();
	}

	public static void addRandomVecs(int count) {

		for (int i = 0; i < count; i++) {
			Vec2D e = new Vec2D(PSys.getBounds().getRandomPoint());
			voidSites.add(e);
			addAttractor(e);
		}
	}

	public static void addPerimeterVecs(int res) {
		for (int i = 0; i < PSys.bounds.height; i += res) {
			addAttractor(new Vec2D(PSys.bounds.getLeft() + 20, i + PSys.bounds.getTop()));
			addAttractor(new Vec2D(PSys.bounds.getRight() - 20, i + PSys.bounds.getTop()));
		}
		for (int j = 0; j < PSys.bounds.width; j += res) {
			addAttractor(new Vec2D(j + PSys.bounds.getLeft(), PSys.bounds.getTop() + 20));
			addAttractor(new Vec2D(j + PSys.bounds.getLeft(), PSys.bounds.getBottom() - 20));
		}
	}
	private static void addAttractor(Vec2D pos) {
		voidSites.add(pos);
		VerletParticle2D p = new VerletParticle2D(pos);
		AttractionBehavior2D a = new AttractionBehavior2D(p, 200, 1f);

		PSys.attractorParticles.add(p);
		PSys.attractors.add(a);
		PSys.physics.addParticle(p);
		PSys.physics.addBehavior(a);

		clearCloudMin();
		for (int i = 1; i < PSys.attractorParticles.size(); i++) {
			VerletParticle2D pi = PSys.attractorParticles.get(i);
			for (int j = 0; j < i; j++) {
				VerletParticle2D pj = PSys.attractorParticles.get(j);
				VerletMinDistanceSpring2D s = new VerletMinDistanceSpring2D(pi, pj, 100, 0.1f);
				PSys.attractorMin.add(s);
				PSys.physics.addSpring(s);
			}
		}
	}
}
