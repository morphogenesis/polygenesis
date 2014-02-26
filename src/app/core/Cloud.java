package app.core;

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
	private final VerletPhysics2D physics = App.PSYS.getPhysics();
	private final Rect bounds = PSys.getBounds();
	public static ArrayList<VerletParticle2D> particles = new ArrayList<>();
	private final ArrayList<VerletSpring2D> mindists = new ArrayList<>();
	private final List<AttractionBehavior2D> attractors = new ArrayList<>();

	public Cloud(App p5) { this.p5 = p5; }

	public void draw() {
		if (Gui.updateCloud) updateCloud();
		if (Gui.drawCloud) {
			p5.stroke(0xff343434); p5.noFill();
			for (AttractionBehavior2D a : attractors) {
				Vec2D vb = a.getAttractor();
				p5.ellipse(vb.x, vb.y, a.getRadius(), a.getRadius());
			}
		}
	}
	private void updateCloud() {
		for (VerletParticle2D p : particles) { p.setWeight(Gui.cloudPtclWght); }
		for (AttractionBehavior2D b : attractors) { b.setRadius(Gui.cloudBhvScale); b.setStrength(Gui.cloudBhvScale); }
		for (VerletSpring2D s : mindists) {s.setStrength(Gui.cloudMindStr); s.setRestLength(Gui.cloudMindistScale);}
	}

	public void addPerim() {
		Rect r = bounds.copy().scale(0.9f);
		Polygon2D polyRect = r.toPolygon2D().increaseVertexCount(40);
		for (Vec2D v : polyRect.vertices) {
			VerletParticle2D p = new VerletParticle2D(v, Gui.cloudPtclWght);
			particles.add(p);
			physics.addParticle(p);
			AttractionBehavior2D a = new AttractionBehavior2D(v, Gui.cloudBhvScale, Gui.cloudBhvStr);
			attractors.add(a);
			physics.addBehavior(a);
		}
	}
	public void addCloudMinDist() {
		for (VerletParticle2D va : particles) {
			for (VerletParticle2D vb : particles) {
				if (va != vb) {
					VerletSpring2D s = new VerletMinDistanceSpring2D(va, vb, Gui.cloudMindistScale, Gui.cloudMindStr);
					mindists.add(s);
					physics.addSpring(s);
				}
			}
		}
	}
}
