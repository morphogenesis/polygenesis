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
import java.util.List;

/**
 * Created on 2/26/14.
 */
public class Cloud {
	private final App p5;
	//	private final VerletPhysics2D physics = App.PSYS.getPhysics();
	private static Rect bounds;
	private final VerletPhysics2D physics = new VerletPhysics2D();
	public static ArrayList<VerletParticle2D> particles = new ArrayList<>();
	private final ArrayList<VerletSpring2D> mindists = new ArrayList<>();
	private final List<AttractionBehavior2D> attractors = new ArrayList<>();

	public Cloud(App p5) {
		this.p5 = p5;
		bounds = new Rect(10, 10, p5.width - 20, p5.height - 20);
		physics.setWorldBounds(bounds);
		physics.setDrag(Gui.physDrag);
	}

	public void draw() {
		if (Gui.isCloudUpdating) updateCloud();
		if (Gui.drawCloud) {
			p5.fill(0xff333333);
			for (VerletParticle2D v : particles) { p5.ellipse(v.x, v.y, 5, 5); }
			p5.noFill();
			p5.stroke(0xff222222);
			for (AttractionBehavior2D a : attractors) { Vec2D vb = a.getAttractor(); p5.ellipse(vb.x, vb.y, a.getRadius(), a.getRadius()); }
			for (VerletSpring2D s : mindists) {
				p5.line(s.a.x, s.a.y, s.b.x, s.b.y);
			}
		}
	}
	private void updateCloud() {
		for (VerletParticle2D p : particles) { p.setWeight(Gui.cloudVecWgt); }
		for (AttractionBehavior2D b : attractors) { b.setRadius(Gui.cloudBhvScl); b.setStrength(Gui.cloudBhvStr); }
		for (VerletSpring2D s : mindists) {s.setStrength(Gui.cloudMinStr); s.setRestLength(Gui.cloudMinScl);}
		physics.update();
	}

	public void addCloud() {
		Rect r = bounds.copy().scale(0.9f);
		Polygon2D polyRect = r.toPolygon2D().increaseVertexCount(40);
		for (Vec2D v : polyRect.vertices) {
			VerletParticle2D p = new VerletParticle2D(v, Gui.cloudVecWgt);
			AttractionBehavior2D a = new AttractionBehavior2D(p, Gui.cloudBhvScl, Gui.cloudBhvStr);
			particles.add(p);
			physics.addParticle(p);
			attractors.add(a);
			physics.addBehavior(a);
		}
	}
	public void addCloudMinDist() {
		clearCloudMin();
		for (VerletParticle2D va : particles) {
			for (VerletParticle2D vb : particles) {
				if (va != vb) {
					VerletSpring2D s = new VerletMinDistanceSpring2D(va, vb, Gui.cloudMinScl, Gui.cloudMinStr);
					mindists.add(s);
					physics.addSpring(s);
				}
			}
		}
	}
	public void clearCloudMin() {
		for (VerletSpring2D s : mindists) {
			physics.removeSpring(s);
		}
		mindists.clear();
	}

/*	public void addExtras(int cnt) {
		for (int i = 0; i < cnt; i++) {
			Vec2D e = new Vec2D(App.BOUNDS.getRandomPoint());
			extras.add(e);
			voidSites.add(e);
			App.PSYS.addAttractor(e);
		}
	}

	public void addPerim(int res) {
		for (int i = 0; i <PSys.getBounds().height; i += res) {
			Vec2D l = new Vec2D(PSys.getBounds().getLeft() + 20, i + App.BOUNDS.getTop());
			Vec2D r = new Vec2D(App.BOUNDS.getRight() - 20, i + App.BOUNDS.getTop());
			extras.add(l); extras.add(r);
			voidSites.add(l); voidSites.add(r);
			App.PSYS.addAttractor(l); App.PSYS.addAttractor(r);
		}
		for (int j = 0; j < App.BOUNDS.width; j += res) {
			Vec2D t = new Vec2D(j + App.BOUNDS.getLeft(), App.BOUNDS.getTop() + 20);
			Vec2D b = new Vec2D(j + App.BOUNDS.getLeft(), App.BOUNDS.getBottom() - 20);
			extras.add(t); extras.add(b);
			voidSites.add(t); voidSites.add(b);
			App.PSYS.addAttractor(t); App.PSYS.addAttractor(b);
		}
	}*/
}
