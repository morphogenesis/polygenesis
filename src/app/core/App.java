package app.core;

import app.graph.Graph;
import app.ui.Display;
import app.ui.Editor;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.MouseEvent;
import toxi.geom.Vec2D;
import util.Color;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//import util.xml.XMLflowgraph;

public class App extends PApplet {
	//	private static App P5;
	private static final String timestamp = new SimpleDateFormat("yyyy-MM-dd'v'HH").format(new Date());
	private static final String filename = "thesis_" + timestamp + ".xml";
	public static final String filepath = "./data/" + filename;
	public static final String staticFilepath = "./data/graphtest.xml";
	public static final int WIDTH = 1600;
	private static final int HEIGHT = 1000;
	public static final DecimalFormat DF3 = new DecimalFormat("#.###");
	public static final DecimalFormat DF2 = new DecimalFormat("#.##");
	public static final DecimalFormat DF1 = new DecimalFormat("#.#");
	public static boolean RECORDING = false;
	public static boolean isShiftDown;
	//	private static boolean isCtrlDown;
//	public static float ZOOM = 1;
	//	public static float world_scale = 10;
//	private static ToxiclibsSupport GFX;
	public static PSys PSYS;
	public static Cloud CLOUD;
	public static Graph GRAPH;
	private static Editor GEDIT;
	private static VoronoiDiagram VSYS;
	public static ControlP5 CP5;
	public static PFont pfont, bfont;
	private final Display display = new Display(this);

	public static void main(String[] args) {
		PApplet.main(new String[]{("app.core.App")});
		System.out.println("Current File  " + filepath);
	}
	public static void __rebelReload() {
		System.out.println("********************  rebelRyeload  ********************");
		System.out.println("Current File: " + filepath);
		Gui.initGUI();
	}
	public void setup() {
//		P5 = this;
		pfont = createFont("SourceCodePro", 10);
		bfont = createFont("SourceCodePro", 14);
//		GFX = new ToxiclibsSupport(this);
		CP5 = new ControlP5(this);
		PSYS = new PSys(this);
		CLOUD = new Cloud(this);
		GRAPH = new Graph();
		GEDIT = new Editor();
		VSYS = new VoronoiDiagram(this);
		Gui.initGUI();
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
		if (RECORDING) { RECORDING = false; endRecord(); System.out.println("SVG EXPORTED SUCCESSFULLY"); }
		CP5.draw();
	}

	public void controlEvent(ControlEvent theEvent) {
		Gui.controlEvent(this, theEvent);
	}
	public void keyPressed() {
		if (key == TAB) Gui.isEditMode = !Gui.isEditMode;

		if (key == CODED) {
			switch (keyCode) {
				case SHIFT: isShiftDown = true; break;
//				case CONTROL: isCtrlDown = true; break;
			}
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
//				case 'o': if (!Editor.hasActiveNode()) VSYS.addPerim(50);
//				case 'p': if (!Editor.hasActiveNode()) VSYS.addExtras(9);
			}
		}
	}
	public void keyReleased() {
		if (key == CODED) {
			if (keyCode == SHIFT) { isShiftDown = false; }
//			if (keyCode == CONTROL) { isCtrlDown = false; }
		}
	}
	public void mouseDragged() {
		if (mouseButton == RIGHT) Editor.mouseDragged(mousePos());
	}
	public void mouseMoved() {
		Editor.mouseMoved(mousePos());
	}
	Vec2D mousePos() {return new Vec2D(mouseX, mouseY);}
	public void mousePressed() {
		if (mouseButton == RIGHT) { Editor.mousePressed(mousePos()); Gui.toggleObjProperties(); }
	}
	public void mouseReleased() {
		if (mouseButton == RIGHT) Editor.mouseReleased();
	}
	public void mouseWheel(MouseEvent event) {
		float e = event.getCount();
//		if (e > 0) {System.out.println("-");} else if (e == 0) {System.out.println("0");} else if (e < 0) {System.out.println("+");}
		Editor.mouseWheel(e);
	}
}


