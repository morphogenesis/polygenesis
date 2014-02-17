package app.graph;

import app.core.App;
import app.core.OPT;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;
import util.Color;

import javax.xml.bind.annotation.*;

/**
 * Created on 2/13/14.
 */
@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.FIELD)
public class GNode {
	@XmlAttribute
	private int id;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private float size;
	@XmlAttribute
	private int occupancy;
	@XmlAttribute
	private int color;
	@XmlAttribute
	private float x;
	@XmlAttribute
	private float y;
	@XmlTransient
	private float radius;
	@XmlTransient
	private VerletParticle2D particle2D;
	@XmlTransient
	private AttractionBehavior2D behavior2D;
	public void draw(App p5) {
		float r = (float) ((Math.sqrt(getSize() / Math.PI)) * OPT.particleScale * App.world_scale);
		setRadius(r);
		String name = getName();
		int size = (int) getSize();
		int id = getId();
		float x = getX();
		float y = getY();
		p5.stroke(Color.NORMAL); p5.fill(Color.NORMAL_FILL); p5.ellipse(x, y, (int) radius, (int) radius);
		p5.stroke(Color.BLACK); p5.fill(Color.GREY); p5.ellipse(x, y, 3, 3);
		p5.noFill();
		p5.stroke(0xffffffff);
		p5.ellipse(x, y, r, r);
	}
	public void update(float weight, float scale, float strength) {
		float r = (float) ((Math.sqrt(getSize() / Math.PI)) * OPT.particleScale * App.world_scale);
		setRadius(r);
		VerletParticle2D v = getParticle2D();
		v.setWeight(weight);
		setX(v.x);
		setY(v.y);
		setId(Graph.getMap().getNodes().indexOf(this));
		AttractionBehavior2D b = getBehavior2D();
		b.setRadius(getRadius() * scale);
		b.setStrength(strength);
	}

	public float getX() { return x; }
	public void setX(float x) { this.x = x; }
	public float getY() { return y; }
	public void setY(float y) { this.y = y; }
	public int getId() { return id; }
	public void setId(int id) {this.id = id;}
	public String getName() { return name; }
	public void setName(String name) {this.name = name;}
	public float getSize() { return size; }
	public void setSize(float size) {this.size = size;}
	public VerletParticle2D getParticle2D() { return particle2D; }
	public void setParticle2D(VerletParticle2D particle2D) { this.particle2D = particle2D;}
	public AttractionBehavior2D getBehavior2D() { return behavior2D; }
	public void setBehavior2D(AttractionBehavior2D behavior2D) { this.behavior2D = behavior2D; }
	public float getRadius() { return radius; }
	public void setRadius(float radius) {this.radius = radius;}
	public int getOccupancy() { return occupancy; }
	public void setOccupancy(int occupancy) { this.occupancy = occupancy; }
	public int getColor() { return color; }
	public void setColor(int color) { this.color = color; }
}
/*	public void drawTag(App p5) {
		int index = Graph.getMap().getNodes().indexOf(this);
		int offset = 50 + (index * 12);

		p5.fill(0xff333333);
		p5.stroke(0xff999999);
		p5.rect(240, y, 120, 16);
		p5.textAlign(PApplet.CENTER);
		p5.fill(0xffffffff);
		p5.text(name +" "+ index, 300, y + 12);
	}
	public void drawTagID(App p5) {
		p5.fill(0xff333333);
		p5.stroke(0xff444444);
		p5.line(350, y, x, y);
		p5.stroke(0xff999999);
		p5.rect(364, y, 16, 16);
		p5.textAlign(PApplet.CENTER);
		p5.fill(color, 100, 100);
		p5.text(id, 372, y + 12);
	}*/