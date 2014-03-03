package app.metaball;

import processing.core.PApplet;
import toxi.geom.Line2D;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;

import java.util.ArrayList;

public class MetaballManager {

	private static MetaballManager instance = null;
	private static float viscosity = 2.0f;
	private static float threshold = 0.0006f;
	private static float resolution = 10.0f;
	private static int maxSteps = 400;
	private static boolean lock;
	private final PApplet p5;
	private ArrayList<Metaball> _metaballs;
	private ArrayList<Polygon2D> outlines;
	private float minStrength;

	public MetaballManager(PApplet p5) {
		this.p5 = p5;
		if (lock) {
			throw new Error("Error: Instantiation failed. Use app.core.MetaballManager.getInstance() instead of new.");
		} else {
			_metaballs = new ArrayList<>();
			minStrength = Metaball.MIN_STRENGTH;
		}
	}
	public void draw() {
		if (_metaballs.size() > 0) {
			run();
			p5.noFill();
			for (Polygon2D p : getOutlines()) {
				p5.stroke(0xff3333cc);
				for (Vec2D v : p.vertices) { p5.ellipse(v.x, v.y, 5, 5); }
				p5.stroke(0xffcc3333);
				for (Line2D l : p.getEdges()) { p5.line(l.a.x, l.a.y, l.b.x, l.b.y); }
				p5.noStroke();
			}
			p5.fill(0xff000000);
			p5.text("Outlines: " + outlines.size(), 20, 20);
		}
	}
	public ArrayList<Polygon2D> getOutlines() {
		return outlines;
	}
	public void run() {
		outlines = new ArrayList<>();
		Polygon2D _outline = new Polygon2D();
		Vector2D seeker = new Vector2D();
		int i;

		for (Metaball metaball : _metaballs) {
			metaball.tracking = false;
			seeker.copy(metaball.position());
			i = 0;
//			while ((stepToEdge(seeker.add(new Vector2D(1, 1))) > threshold) && (++i < 50)) { }
			while ((stepToEdge(seeker) > threshold) && (++i < 50)) { }
			metaball.edge.copy(seeker);
		}

		int edgeSteps = 0;
		Metaball current = untrackedMetaball();
		seeker.copy(current.edge);
		_outline.add(seeker.x, seeker.y);

		while (current != null && edgeSteps < maxSteps) {
			rk2(seeker, resolution);
			_outline.add(seeker.x, seeker.y);
			for (Metaball metaball : _metaballs) {
				if (seeker.dist(metaball.edge) < (resolution * 0.9f)) {
					seeker.copy(metaball.edge);
					_outline.add(seeker.x, seeker.y);
					current.tracking = true;
					if (metaball.tracking) {
						current = untrackedMetaball();
						if (current != null) {
							seeker.copy(current.edge);
							addOutline(_outline);
							_outline = new Polygon2D();
							_outline.add(seeker.x, seeker.y);
						}
					} else { current = metaball; } break;
				}
			} ++edgeSteps;
		} addOutline(_outline);
	}
	private void addOutline(Polygon2D outline) {
		outlines.add(outline.removeDuplicates(resolution));
	}
	/**
	 * Differential Method
	 */
	private void rk2(Vector2D v, float h) {
		Vector2D t1 = calc_normal(v).getPerpLeft();
		t1.multiply(h * 0.5f);
		Vector2D t2 = calc_normal(Vector2D.add(v, t1)).getPerpLeft();
		t2.multiply(h);
		v.add(t2);
	}
	private float stepToEdge(Vector2D seeker) {
		float force = calc_force(seeker);
		float stepsize;
		stepsize = (float) Math.pow(minStrength / threshold, 1 / viscosity) - (float) Math.pow(minStrength / force, 1 / viscosity) + 0.01f;
		seeker.add(calc_normal(seeker).multiply(stepsize));
		return force;
	}
	private Vector2D calc_normal(Vector2D v) {
		Vector2D force = new Vector2D();
		Vector2D radius;
		for (Metaball metaball : _metaballs) {
			radius = Vector2D.subtract(metaball.position(), v);
			if (radius.getLengthSq() == 0) { continue; }
			radius.multiply(-viscosity * metaball.strength() * (1 / (float) Math.pow(radius.getLengthSq(), (2 + viscosity) * 0.5f)));
			force.add(radius);
		} return force.norm();
	}
	// --------------------------------------------------------------------------------
	// Helpers
	// --------------------------------------------------------------------------------
	private float calc_force(Vector2D v) {
		float force = 0.0f;
		for (Metaball metaball : _metaballs) {
			force += metaball.strengthAt(v, viscosity);
		} return force;
	}
	private Metaball untrackedMetaball() {
		for (Metaball metaball : _metaballs) {
			if (!metaball.tracking) { return metaball; }
		} return null;
	}
	/**
	 * Build Ops
	 */
	public void addMetaball(Vector2D pos) {
		Metaball m = new Metaball(pos, p5.random(1, 4));
		addMetaball(m);
	}
	public void addMetaball(Metaball metaball) {
		minStrength = Math.min(metaball.strength(), minStrength);
		_metaballs.add(metaball);
		MetaballManager.maxSteps = _metaballs.size() * 400;
	}
	public static MetaballManager getInstance(PApplet p5) {
		if (instance == null) {
			instance = new MetaballManager(p5);
			lock = true;
		} return instance;
	}
	public int getSize() {
		return _metaballs.size();
	}
	public void removeMetaball(Metaball metaball) {
		int index = _metaballs.indexOf(metaball);
		removeMetaball(index);
	}
	public void removeMetaball(int index) {
		if (index < 0) {System.out.println("Metaball not found.");}
		_metaballs.remove(index);
	}
}
