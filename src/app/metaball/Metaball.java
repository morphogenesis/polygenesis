package app.metaball;

import processing.core.PApplet;
import toxi.geom.ReadonlyVec2D;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

import java.util.ArrayList;

public class Metaball {
	private PApplet p5;
	private ArrayList<MVec2D> MVec2Ds = new ArrayList<>();
	private ArrayList<Vec2D> points = new ArrayList<>();
	private float viscosity = 2;
	private float viscosityNorm = 1.0f / viscosity;
	private float minSize = 1e32f;
	private float minThreshold;
	private float threshold = 0.003f;
	private float stepping = 20;
	private float lineDistance = 5;
	private boolean isRunning;

	public Metaball(PApplet p5) {
		this.p5 = p5;
	}
	public void addParticles(ArrayList<MVec2D> mplist) {
		for (MVec2D mp : mplist) {
			float weight = 10;
			mp.setWeight(weight);
			if (mp.getWeight() < minSize) minSize = mp.getWeight();
			minThreshold = (float) Math.pow(minSize / threshold, viscosityNorm);
			MVec2Ds.add(mp);
		}
	}
	public void draw() {
		if ((!MVec2Ds.isEmpty()) && (isRunning)) {
			for (MVec2D mp : MVec2Ds) resetMetaParticle(mp);
			int loopIndex = 0;
			while (loopIndex < 200) {
				loopIndex++;
				for (MVec2D m : MVec2Ds) {
					if (!m.isTracked) continue;
					Vec2D temp = stepOnceTowardsBorder(m.edgePos, calcForce(m.edgePos));
					m.edgePos = rungeKutta2(temp, stepping);
					p5.stroke(0xffffffff); p5.line(temp.x, temp.y, m.edgePos.x, m.edgePos.y);
					points.add(m.edgePos);
					if (points.size() > 500) points.remove(0);
					isTracked(loopIndex, m);
				}
			}

			for (Vec2D a : points) { p5.stroke(0xffff00ff); p5.point(a.x, a.y); }
			for (MVec2D mp : MVec2Ds) {
				p5.stroke(0xff444444); p5.line(mp.edgePos.x, mp.edgePos.y, mp.pos0.x, mp.pos0.y);
				p5.stroke(0xff4cc3c7); p5.ellipse(mp.pos0.x, mp.pos0.y, 4, 4);
			}
		}
	}
	private void isTracked(int loopIndex, MVec2D m) {
		for (MVec2D mpb : MVec2Ds) {
			float distanceSq = mpb.pos0.distanceToSquared(m.edgePos);
			if ((mpb != m || loopIndex > 3) && (distanceSq < (stepping * stepping))) m.isTracked = false;
		}
	}

	private void resetMetaParticle(MVec2D mp) {
		Vec2D borderPlusOne;
		boolean dynamic = false;
		float tracking = 0.25f;
		if (dynamic) borderPlusOne = new Vec2D(mp.x + p5.random(-tracking, tracking), mp.y + p5.random(-tracking, tracking));
		else borderPlusOne = new Vec2D(mp.x - tracking, mp.y - tracking);
		mp.pos0 = trackTheBorder(borderPlusOne);
		mp.edgePos = mp.pos0;
		mp.isTracked = true;
	}
	public Vec2D stepOnceTowardsBorder(Vec2D pos, float forceAtPoint) {
		Vec2D np = calcNormal(pos);
		float borderStepSize = 0.01f;
		float stepsize = minThreshold - (float) Math.pow(minSize / forceAtPoint, viscosityNorm) + borderStepSize;
		return new Vec2D(pos.x + np.x * stepsize, pos.y + np.y * stepsize);
	}
	public Vec2D trackTheBorder(Vec2D borderPlusOne) {
		float force = calcForce(borderPlusOne);
		int iters = 0;
		while (force > threshold) {
			force = calcForce(borderPlusOne);
			borderPlusOne = stepOnceTowardsBorder(borderPlusOne, force);
			iters++;
			int maxIterations = 10;
			if (iters > maxIterations) break;
		} return borderPlusOne;
	}
	public float calcForce(Vec2D pos) {
		float forceAtPoint = 0.0f;
		for (MVec2D a : MVec2Ds) {
			Vec2D tmp = new Vec2D(a.x - pos.x, a.y - pos.y);
			float div = (float) Math.pow((float) Math.sqrt(tmp.x * tmp.x + tmp.y * tmp.y), viscosity);
			if (div != 0) forceAtPoint += a.getWeight() / div;
			else forceAtPoint += 1e32f;
		} return forceAtPoint;
	}
	public Vec2D rungeKutta2(Vec2D pos, float h) {
		Vec2D t1 = calcTangent(pos);
		Vec2D t2 = calcTangent(new Vec2D(pos.x + t1.x * h / 2, pos.y + t1.y * h / 2));
		return new Vec2D(pos.x + h * t2.x, pos.y + h * t2.y);
	}
	public Vec2D calcTangent(Vec2D pos) {
		Vec2D np = this.calcNormal(pos);
		//noinspection SuspiciousNameCombination
		return new Vec2D(-np.y, np.x);
	}
	public Vec2D calcNormal(Vec2D pos) {
		Vec2D np = new Vec2D(0, 0);
		for (MVec2D a : MVec2Ds) {
			Vec2D fromPointToBall = new Vec2D(a.x - pos.x, a.y - pos.y);
			float centerDist = (float) Math.sqrt(fromPointToBall.x * fromPointToBall.x + fromPointToBall.y * fromPointToBall.y);
			float rDiv = 1.0f / (float) Math.pow(centerDist, 2.0f + viscosity);
			np.x += -viscosity * a.getWeight() * fromPointToBall.x * rDiv;
			np.y += -viscosity * a.getWeight() * fromPointToBall.y * rDiv;
		} float rLen = 1.0f / (float) Math.sqrt(np.x * np.x + np.y * np.y);
		return new Vec2D(np.x * rLen, np.y * rLen);
	}
	/**
	 * Created on 2/14/14.
	 */
	public static class MVec2D extends Vec2D {
		public Vec2D pos0;
		public Vec2D edgePos;
		public Boolean isTracked;
		public float weight;

		public MVec2D(ReadonlyVec2D v) {
			super(v);
		}
		public MVec2D(VerletParticle2D p) {
			super(p);
			pos0 = new Vec2D(this);
			edgePos = new Vec2D(this);
			weight = 1;
		}
		public void setWeight(float weight) {
			this.weight = weight;
		}
		public float getWeight() { return weight; }
	}
}
