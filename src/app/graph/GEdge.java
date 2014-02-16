package app.graph;

import toxi.physics2d.VerletSpring2D;

import javax.xml.bind.annotation.*;

/**
 * Created on 2/13/14.
 */
@XmlRootElement(name = "rel")
@XmlAccessorType(XmlAccessType.FIELD)
public class GEdge {

	@XmlAttribute
	private int from;
	@XmlAttribute
	private int to;
	@XmlTransient
	private VerletSpring2D spring2D;
	@XmlTransient
	private float length;
	@XmlTransient
	private GNode nodeA;
	@XmlTransient
	private GNode nodeB;

/*
	public void draw(PApplet pg) {
		pg.stroke(0xff666666); pg.noFill();
		pg.line(getNodeA().getX(), getNodeA().getY(), getNodeB().getX(), getNodeB().getY());
	}
*/

	public void update(float strength, float scale) {
		setLength(getNodeA().getRadius() + getNodeB().getRadius());
		VerletSpring2D s = getSpring2D();
		s.setStrength(strength);
		s.setRestLength(getLength() * scale);
	}
	public GNode getNodeA() { return nodeA;/*Graph.getNode(from); */}
	public GNode getNodeB() { return nodeB;/*Graph.getNode(to);*/ }

	public void setLength(float length) { this.length = length; }
	public float getLength() { return length;/* getNodeA().getRadius() + getNodeB().getRadius(); */}
	public void setTo(int to) {this.to = to;}
	public void setFrom(int from) {this.from = from;}
	public int getFrom() { return from; }
	public int getTo() { return to; }
	public VerletSpring2D getSpring2D() { return spring2D; }
	public void setNodeA(GNode nA) { this.nodeA = nA; }

	public void setNodeB(GNode nB) { this.nodeB = nB;}
	public void setSpring2D(VerletSpring2D spring2D) { this.spring2D = spring2D; }
}
//	public GNode getnA() { return nA; }
//	public GNode getnB() { return nB; }