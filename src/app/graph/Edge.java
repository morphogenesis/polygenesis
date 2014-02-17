package app.graph;

import app.core.Gui;
import toxi.physics2d.VerletSpring2D;

import javax.xml.bind.annotation.*;

/**
 * Created on 2/13/14.
 */
@XmlRootElement(name = "rel")
@XmlAccessorType(XmlAccessType.FIELD)
public class Edge {

	@XmlAttribute
	private int from;
	@XmlAttribute
	private int to;
	@XmlTransient
	private Node a;
	@XmlTransient
	private Node b;
	@XmlTransient
	private VerletSpring2D spring2D;

	public Edge() {
//		this.a=Graph.getNode(getFrom());
//		this.b=Graph.getNode(getTo());
//		this.spring2D=new VerletSpring2D(getA().getParticle2D(), getB().getParticle2D(), 100 ,  0.001f);
	}
	public Edge(Node a, Node b) {
		this.a = a;
		this.b = b;
		this.from = (a.getId());
		this.to = (b.getId());
		this.spring2D = new VerletSpring2D(a.getParticle2D(), b.getParticle2D(), getLength() * Gui.physSprScale, Gui.physSprStr);
	}

	public void update() {
		spring2D.setStrength(Gui.physSprStr);
		spring2D.setRestLength(getLength() * Gui.physSprScale);
	}
	public VerletSpring2D getSpring2D() { return spring2D; }
	public void setSpring2D(VerletSpring2D spring2D) { this.spring2D = spring2D; }
	public float getLength() { return a.getRadius() + b.getRadius();}
	public Node getB() { return b; }
	public void setB(Node nB) { this.b = nB;}
	public Node getA() { return a;}
	public void setA(Node nA) { this.a = nA; }
	public int getFrom() { return from; }
	public void setFrom(int from) {this.from = from;}
	public int getTo() { return to; }
	public void setTo(int to) {this.to = to;}
}
