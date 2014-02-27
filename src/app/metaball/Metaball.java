package app.metaball;

import app.graph.Graph;
import app.xml.Node;
import processing.core.PApplet;
import toxi.geom.ReadonlyVec2D;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

import java.util.ArrayList;

public class Metaball {
	private PApplet p5;
	private ArrayList<MVec2D> MVec2Ds = new ArrayList<>();
	private ArrayList<Vec2D> points = new ArrayList<>();
	public static float viscosity = 2;
	public static float viscosityNorm = 1.0f / viscosity;
	public static float minSize = 1e32f;
	public static float minThreshold;
	public static float threshold = 0.003f;
	public static float stepping = 20;
	//	public static float lineDistance = 5;
	public static boolean isMetaDynamic;
	public static boolean isMetaUpdating;
	public static boolean drawMetaLine;
	public static boolean drawMetaPnt;
	public static boolean drawMetaEdgePos;
	public static boolean drawMetaPos0;
	public static float tracking = 0.25f;
	public static int maxIter = 200;
	public static int maxPts = 500;
	public static int maxTrackIter = 10;
	public static float borderStepSize = 0.01f;

	public Metaball(PApplet p5) {
		this.p5 = p5;
	}
	public void addParticle(Vec2D v, float w) {
		MVec2D mv = new MVec2D(v);
		mv.setWeight(w / 2);
		MVec2Ds.add(mv);
	}
	public void update() {
		MVec2Ds = new ArrayList<>();
		for (Node n : Graph.nodes) {
			addParticle(n.getParticle2D(), n.getRadius());
		}
		for (MVec2D mp : MVec2Ds) {

			if (mp.getWeight() < minSize) minSize = mp.getWeight();
			minThreshold = (float) Math.pow(minSize / threshold, viscosityNorm);
		}
		draw();
	}

	public void draw() {
		if ((!MVec2Ds.isEmpty()) && (isMetaUpdating)) {
			for (MVec2D mp : MVec2Ds) resetMetaParticle(mp);
			int loopIndex = 0;
			while (loopIndex < maxIter) {
				loopIndex++;
				for (MVec2D m : MVec2Ds) {
					if (!m.isTracked) continue;
					Vec2D temp = stepOnceTowardsBorder(m.edgePos, calcForce(m.edgePos));
					m.edgePos = rungeKutta2(temp, stepping);
					if (drawMetaLine) {
						p5.stroke(0xffffffff);
						p5.line(temp.x, temp.y, m.edgePos.x, m.edgePos.y);
					}
					points.add(m.edgePos);
					if (points.size() > maxPts) points.remove(0);
					isTracked(loopIndex, m);
				}
			}
			if (drawMetaPnt) { for (Vec2D a : points) { p5.stroke(360, 100); p5.ellipse(a.x, a.y, 2, 2); } }
			if (drawMetaPos0) { for (MVec2D mp : MVec2Ds) { p5.stroke(0xff4cc3c7); p5.ellipse(mp.pos0.x, mp.pos0.y, 4, 4); } }
			if (drawMetaEdgePos) { for (MVec2D mp : MVec2Ds) { p5.stroke(0xff888888); p5.ellipse(mp.edgePos.x, mp.edgePos.y, 4, 4); } }
			if (drawMetaLine) { for (MVec2D mp : MVec2Ds) { p5.stroke(0xffffff00); p5.line(mp.edgePos.x, mp.edgePos.y, mp.pos0.x, mp.pos0.y); } }
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
		if (isMetaDynamic) { borderPlusOne = new Vec2D(mp.x + p5.random(-tracking, tracking), mp.y + p5.random(-tracking, tracking)); } else borderPlusOne = new Vec2D(mp.x - tracking, mp.y - tracking);
		mp.pos0 = trackTheBorder(borderPlusOne);
		mp.edgePos = mp.pos0;
		mp.isTracked = true;
	}
	public Vec2D stepOnceTowardsBorder(Vec2D pos, float forceAtPoint) {
		Vec2D np = calcNormal(pos);
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
			if (iters > maxTrackIter) break;
		} return borderPlusOne;
	}
	public float calcForce(Vec2D pos) {
		float forceAtPoint = 0.0f;
		for (MVec2D a : MVec2Ds) {
			Vec2D tmp = new Vec2D(a.x - pos.x, a.y - pos.y);
			float div = (float) Math.pow((float) Math.sqrt(tmp.x * tmp.x + tmp.y * tmp.y), viscosity);
			if (div != 0) { forceAtPoint += a.getWeight() / div; } else forceAtPoint += 1e32f;
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
