package app.graph;

import app.core.App;
import app.core.Gui;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import javax.xml.bind.annotation.*;

/**
 * Created on 2/13/14.
 */
@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.FIELD)
public class Node {
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
	private VerletParticle2D particle2D;
	@XmlTransient
	private AttractionBehavior2D behavior2D;
	@XmlTransient
	private static int numberOfGNodes = 0;

	public Node() {
		this.particle2D = new VerletParticle2D(this.x, this.y);
		this.behavior2D = new AttractionBehavior2D(this.particle2D, getRadius() * Gui.behaviorScale, -1);
		++numberOfGNodes;
	}

	public Node(String name, float size, Vec2D pos) {
		this.id = ++numberOfGNodes;
		this.name = name;
		this.size = size;
		this.x = pos.x;
		this.y = pos.y;
		this.color = (int) (360 / numberOfGNodes) * this.id;
		this.particle2D = new VerletParticle2D(pos);
		this.behavior2D = new AttractionBehavior2D(this.particle2D, getRadius() * Gui.behaviorScale, -1);
	}
	public void update() {
		particle2D.setWeight(Gui.particleWeight);
		x = particle2D.x;
		y = particle2D.y;
		behavior2D.setRadius(getRadius() * Gui.behaviorScale);
		behavior2D.setStrength(Gui.behaviorStrength);
		color = (int) (360 / numberOfGNodes) * this.id;
	}
	public static int getNumberOfGNodes() {return numberOfGNodes;}
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
	public float getRadius() { return (float) ((Math.sqrt(this.size / Math.PI)) * Gui.particleScale * App.world_scale); }
	public int getOccupancy() { return occupancy; }
	public void setOccupancy(int occupancy) { this.occupancy = occupancy; }
	public int getColor() { return color; }
	public void setColor(int color) { this.color = color; }
}