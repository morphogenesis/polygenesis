package app.core;

import app.graph.Editor;
import app.graph.Graph;
import app.metaball.Metaball;
import app.metaball.MetaballManager;
import app.metaball.Old_Metaball;
import app.metaball.Vector2D;
import app.phys.Cloud;
import app.phys.PSys;
import app.ui.Display;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.MouseEvent;
import toxi.geom.Vec2D;
import util.Color;
import util.MathUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App extends PApplet {
	public static final String staticFilepath = "./data/graphtest.xml";
	public static final int WIDTH = 1600;
	public static final DecimalFormat DF3 = new DecimalFormat("#.###");
	public static final DecimalFormat DF2 = new DecimalFormat("#.##");
	public static final DecimalFormat DF1 = new DecimalFormat("#.#");
	private static final String timestamp = new SimpleDateFormat("yyyy-MM-dd'v'HH").format(new Date());
	private static final String filename = "thesis_" + timestamp + ".xml";
	public static final String filepath = "./data/" + filename;
	private static final int HEIGHT = 1000;
	public static boolean RECORDING = false;
	public static boolean isShiftDown;
	public static PSys PSYS;
	public static Cloud CLOUD;
	public static Graph GRAPH;
	public static ControlP5 CP5;
	public static PFont pfont, bfont;
	private static Editor GEDIT;
	private static VoronoiDiagram VSYS;
	private final Display display = new Display(this);
	float fps = 0;
	private MetaballManager manager;
	Metaball target;

//	private Old_Metaball MBALL;

	public static void main(String[] args) {
		PApplet.main(new String[]{("app.core.App")});
		System.out.println("Current File  " + filepath);
	}
	public static void __rebelReload() {
		System.out.println("********************  rebelReload  ********************");
		System.out.println("Current File: " + filepath);
	}

	public void setup() {
		pfont = createFont("SourceCodePro", 10);
		bfont = createFont("SourceCodePro", 14);
		CP5 = new ControlP5(this);
		PSYS = new PSys(this);
		CLOUD = new Cloud(this);
		GRAPH = new Graph();
		GEDIT = new Editor();

		//  Metaball Manager
		manager = MetaballManager.getInstance(this);
		target = new Metaball(new Vector2D(mouseX, mouseY), 2);
		manager.addMetaball(target);
		//	MBALL = new Old_Metaball(this);

		VSYS = new VoronoiDiagram(this);
		Gui.init();
		size(WIDTH, HEIGHT, P2D);
		frameRate(60);
		smooth(16);
		colorMode(HSB, 360, 100, 100, 100);
		background(Color.BG);
		ellipseMode(RADIUS);
		textAlign(LEFT);
		textFont(pfont, 10);
		strokeWeight(1);
		noStroke();
		noFill();
	}

	public void draw() {
		background(Color.BG);
		noFill(); noStroke();
		VSYS.draw();
		PSYS.draw();
		CLOUD.draw();
		display.draw();

		//  Metaball
		target.position(new Vector2D(MathUtil.clamp(mouseX, 50, 850), MathUtil.clamp(mouseY, 50, 550)));
		manager.draw();
//		MBALL.update();

		drawStatusbar();
		if (RECORDING) { RECORDING = false; endRecord(); System.out.println("SVG EXPORTED SUCCESSFULLY"); }
		CP5.draw();
	}
	private void drawStatusbar() {
		Vec2D pos = new Vec2D(20, HEIGHT - 100);

		if (frameCount % 10 == 0) {fps = frameRate;}
		fill(0xffffffff);
		text(fps, pos.x, pos.y);
		noFill();
	}

	public void keyPressed() {
		if (key == CODED) {
			switch (keyCode) { case SHIFT: isShiftDown = true; break; }
		}
		if (key == TAB) {
			Gui.isEditMode = !Gui.isEditMode;
		}
		if (Gui.isEditMode) {
			switch (key) {
				case '1': Gui.drawVorPoly = !Gui.drawVorPoly; break;
				case '2': Gui.drawVorBez = !Gui.drawVorBez; break;
				case '3': Gui.drawVorVec = !Gui.drawVorVec; break;
				case '4': Gui.drawPhysInfo = !Gui.drawPhysInfo; Gui.drawVorInfo = !Gui.drawVorInfo; break;
				case 'a': Editor.createNewNode(Gui.nameTextfield.getStringValue(), Gui.radiusSlider.getValue(), mousePos()); Gui.toggleObjProperties(); break;
				case 'f': Editor.createNewEdge(); break;
				case 'x': Editor.deleteNode(); break;
				case 'v': Editor.deleteEdges(); break;
				case 'q': Editor.createNewBranch(Gui.capacitySlider.getValue(), true); break;
				case 'w': Editor.createNewBranch(Gui.capacitySlider.getValue(), false); break;
				case 'l': Editor.lockNode(); break;
				case 'm': Old_Metaball.isMetaUpdating = !Old_Metaball.isMetaUpdating; break;
//				case '6': MBALL.setDifferentialMethod("euler"); break;
//				case '7': MBALL.setDifferentialMethod("rk2"); break;
//				case 'o': if (!Editor.hasActiveNode()) VSYS.addCloud(50);
//				case 'p': if (!Editor.hasActiveNode()) VSYS.addExtras(9);
			}
		}
	}
	public void keyReleased() {
		if (key == CODED) { if (keyCode == SHIFT) { isShiftDown = false; } }
	}
	private Vec2D mousePos() {return new Vec2D(mouseX, mouseY);}
	public void mouseDragged() {
		if (mouseButton == RIGHT) Editor.mouseDragged(mousePos());
	}
	public void mouseMoved() {
		Editor.mouseMoved(mousePos());
	}
	public void mousePressed() {
		if (mouseButton == LEFT) {
			manager.addMetaball(new Vector2D(mouseX, mouseY));
		}
		if (mouseButton == RIGHT) {
			if (isShiftDown) {
				manager.removeMetaball(manager.getSize() - 1);
			} else { Editor.mousePressed(mousePos()); Gui.toggleObjProperties(); }
		}
	}
	public void mouseReleased() {
		if (mouseButton == RIGHT) Editor.mouseReleased();
	}
	public void mouseWheel(MouseEvent event) {
		float e = event.getCount();
//		if (e > 0) {System.out.println("-");} else if (e == 0) {System.out.println("0");} else if (e < 0) {System.out.println("+");}
		Editor.mouseWheel(e);
	}

	public void controlEvent(ControlEvent theEvent) {
		Gui.controlEvent(this, theEvent);
	}
}


