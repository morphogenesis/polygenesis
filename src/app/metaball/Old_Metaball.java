package app.metaball;

import app.core.VoronoiDiagram;
import app.graph.Graph;
import app.xml.Node;
import processing.core.PApplet;
import toxi.geom.Polygon2D;
import toxi.geom.ReadonlyVec2D;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics3d.VerletParticle3D;

import java.util.ArrayList;

public class Old_Metaball {
	private PApplet p5;
	private ArrayList<MVec2D> MVec2Ds = new ArrayList<>();
	public static ArrayList<Vec2D> points = new ArrayList<>();
	public static boolean isMetaDynamic;
	public static boolean isMetaUpdating;
	public static boolean drawMetaLine;
	public static boolean drawMetaPnt;
	public static boolean drawMetaEdgePos;
	public static boolean drawMetaPos0;
	public static float borderStepSize = 0.01f;/*7.5*/
	public static float viscosity = 2;
	public static float viscosityNorm = 1.0f / viscosity;
	public static float minThreshold;
	public static float minSize = 1e32f;
	public static float threshold = 0.003f;
	public static float stepping = 20;
	public static float tracking = 0.25f;
	private Polygon2D poly;
	private String differentialMethod = "euler";
	public static int maxIter = 100;
	public static int maxPts = 25;
	public static int maxTrackIter;
	//	public static int maxTrackIter = 10;/*160*/
	public Old_Metaball(PApplet p5) {
		this.p5 = p5;
		poly = new Polygon2D();
	}

	public void addParticle(Vec2D v, float w) {
		MVec2D mv = new MVec2D(v);
		mv.setWeight(w);
		if (mv.getWeight() < minSize) minSize = mv.getWeight();
		minThreshold = (float) Math.pow(minSize / threshold, viscosityNorm);
		MVec2Ds.add(mv);
	}
	public void update() {
		MVec2Ds = new ArrayList<>();
		for (Node n : Graph.nodes) { addParticle(n.getParticle2D(), n.getSize()); }
		for (MVec2D mp : MVec2Ds) { if (mp.getWeight() < minSize) minSize = mp.getWeight(); minThreshold = (float) Math.pow(minSize / threshold, viscosityNorm); }
		draw();
	}

	public void draw() {
		if ((!MVec2Ds.isEmpty()) && (isMetaUpdating)) {

			for (MVec2D mp : MVec2Ds) {
				Vec2D borderPlusOne;
				if (isMetaDynamic) {
					borderPlusOne = new Vec2D(mp.x + p5.random(-tracking, tracking), mp.y + p5.random(-tracking, tracking));
				} else { borderPlusOne = new Vec2D(mp.x - tracking, mp.y +tracking); }
				mp.pos0 = trackTheBorder(mp.copy().getRotated(0.1f).getAbs());
//				mp.pos0 = trackTheBorder(borderPlusOne);
				mp.edgePos = mp.pos0;
				mp.isTracked = true;
			}

			poly = new Polygon2D();
			int loopIndex = 0;

			while (loopIndex < maxIter) {
				loopIndex++;
				points = new ArrayList<>();
				p5.noFill();
				for (MVec2D m : MVec2Ds) {
					if (!m.isTracked) continue;
					Vec2D old_pos = new Vec2D(m.edgePos);
					m.edgePos = rungeKutta2(m.edgePos, stepping);
					Vec2D temp = new Vec2D(stepOnceTowardsBorder(m.edgePos));
					/*p5.stroke(0xffff0000); p5.ellipse(old_pos.x, old_pos.y, 4, 4);
					p5.stroke(0xff00ff00); p5.ellipse(temp.x, temp.y, 4, 4);
					p5.stroke(0xff0000ff); p5.ellipse(temp.x, temp.y, 4, 4);*/
					p5.stroke(0xffff0000);
					p5.line(old_pos.x, old_pos.y, temp.x, temp.y);
//					m.edgePos = euler(temp, stepping);
//					m.edgePos = calc_differential(temp, stepping, differentialMethod);

//					if (drawMetaLine) {
//						p5.stroke(0xffffffff);
//						p5.line(m.x, m.y, m.edgePos.x, m.edgePos.y);/*p5.line(m.pos0.x, m.pos0.y, m.edgePos.x, m.edgePos.y);*/
//					}
//					if (loopIndex % 2 == 0) { points.add(m.edgePos); }					/*	if (points.size() > maxPts) points.remove(0);*/
//					VoronoiDiagram.points = points;
//					for (MVec2D mpb : MVec2Ds) {
//						float distanceSq = mpb.pos0.distanceToSquared(m.edgePos);
//						if ((mpb != m || loopIndex > 3) && (distanceSq < (stepping * stepping))) m.isTracked = false;
//					}//	isTracked(loopIndex, m);
					for (MVec2D ob : MVec2Ds) {
						Vec2D delta = new Vec2D(ob.pos0.x - m.edgePos.x, ob.pos0.y - m.edgePos.y);
						float distanceSq = (delta.x * delta.x) + (delta.y * delta.y);
						if ((ob != m || loopIndex > 3) && (distanceSq < (stepping * stepping))) {
							m.isTracked = false;
						}
						/*if ((ob != m) || loopIndex > 3) {
							if (ob.pos0.distanceTo(m.edgePos) < stepping) { m.isTracked = false; }
						}*/
					}
					float tracking = 0;
					for (MVec2D n : MVec2Ds) { if (n.isTracked) {tracking += 1;} }
					if (tracking == 0) {break;}
				}					/*drawMetaparticle();*/

			}

		}
	}

	private void drawMetaparticle() {
		if (drawMetaLine) {
			for (MVec2D m : MVec2Ds) {
				p5.stroke(0xff00ffff); p5.line(m.edgePos.x, m.edgePos.y, m.pos0.x, m.pos0.y);
				p5.stroke(0xffff00ff); p5.line(m.x, m.y, m.edgePos.x, m.edgePos.y);
			}
		}
		if (drawMetaPnt) { for (Vec2D a : points) { p5.fill(0xffff0000); p5.ellipse(a.x, a.y, 4, 4); } }
		if (drawMetaPos0) { for (MVec2D mp : MVec2Ds) { p5.fill(0xff00ff00); p5.ellipse(mp.pos0.x, mp.pos0.y, 6, 6); } }
		if (drawMetaEdgePos) { for (MVec2D mp : MVec2Ds) { p5.fill(0xff0000ff); p5.ellipse(mp.edgePos.x, mp.edgePos.y, 8, 8); } }

//		p5.stroke(0xffff0000); p5.ellipse(mp.x, mp.y, mp.weight, mp.weight);
//		if (drawMetaPos0) { p5.stroke(0xffff66ff); p5.ellipse(mp.pos0.x, mp.pos0.y, 4, 4); }
//		if (drawMetaEdgePos) { p5.strokeWeight(2); p5.stroke(0xff66ffff); p5.point(mp.edgePos.x, mp.edgePos.y); p5.strokeWeight(1); p5.text(MVec2Ds.indexOf(mp), mp.x, mp.y); }
//		if (drawMetaLine) {p5.stroke(0xffffff66); p5.line(mp.edgePos.x, mp.edgePos.y, mp.pos0.x, mp.pos0.y); }
		p5.noStroke();
	}

	private void isTracked(int loopIndex, MVec2D m) {
		for (MVec2D mpb : MVec2Ds) {
			float distanceSq = mpb.pos0.distanceToSquared(m.edgePos);
			if ((mpb != m || loopIndex > 3) && (distanceSq < (stepping * stepping))) m.isTracked = false;
		}
	}

	private void resetMetaParticle(MVec2D mp) {

	}
	public Vec2D stepOnceTowardsBorder(Vec2D pos) {
		float force = calcForce(pos);
		Vec2D np = calcNormal(pos);
		float stepsize = minThreshold - (float) Math.pow(minSize / force, viscosityNorm) + borderStepSize;
		return new Vec2D(pos.x + np.x * stepsize, pos.y + np.y * stepsize);
	}
	public Vec2D trackTheBorder(Vec2D pos) {
		float force = 9999999;
		int iters = 0;
		while (force > threshold) {
			force = calcForce(pos);
			pos = stepOnceTowardsBorder(pos);
			p5.stroke(0xff4cc3c7);
			p5.fill(0xffffffff);
			p5.ellipse(pos.x, pos.y, 6, 6);
			iters++; if (iters > maxTrackIter) break;
		} return pos;
	}
	public float calcForce(Vec2D pos) {
		float forceAtPoint = 0.0f;
		for (MVec2D a : MVec2Ds) {
			Vec2D tmp = new Vec2D(a.x - pos.x, a.y - pos.y);
			float div = (float) Math.pow((float) Math.sqrt(tmp.x * tmp.x + tmp.y * tmp.y), viscosity);
			if (div != 0) { forceAtPoint += a.getWeight() / div; } else forceAtPoint += 1e32f;
		} return forceAtPoint;
	}

	public Vec2D calc_differential(Vec2D pos, float h, String method) {
		switch (method) {
			case "rk2":
				Vec2D t1 = calcTangent(pos);
				Vec2D t2 = calcTangent(new Vec2D(pos.x + t1.x * h / 2, pos.y + t1.y * h / 2));
				return new Vec2D(pos.x + h * t2.x, pos.y + h * t2.y);
			case "euler":
				return new Vec2D((pos.x + h * calcTangent(pos).x), (pos.y + h * calcTangent(pos).y));
			default:
				return new Vec2D((pos.x + h * calcTangent(pos).x), (pos.y + h * calcTangent(pos).y));
		}
	}
	public Vec2D rungeKutta2(Vec2D pos, float h) {
		Vec2D t1 = calcTangent(pos);
		Vec2D t2 = calcTangent(new Vec2D(pos.x + t1.x * h / 2, pos.y + t1.y * h / 2));
		return new Vec2D(pos.x + h * t2.x, pos.y + h * t2.y);
	}
	public Vec2D euler(Vec2D pos, float h) {
		return new Vec2D((pos.x + h * calcTangent(pos).x), (pos.y + h * calcTangent(pos).y));
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

	public void setDifferentialMethod(String method) { this.differentialMethod = method; }

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
