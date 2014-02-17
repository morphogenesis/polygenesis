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
import toxi.processing.ToxiclibsSupport;
import util.Color;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//import util.xml.XMLflowgraph;

public class App extends PApplet {
	public static final String timestamp = new SimpleDateFormat("yyyy-MM-dd'v'HH").format(new Date());
	public static final String filename = "thesis_" + timestamp + ".xml";
	public static final String filepath = "./data/" + filename;
	public static final String staticFilepath = "./data/graphtest.xml";
	public static final int WIDTH = 1600, HEIGHT = 1000;
	public static final DecimalFormat DF3 = new DecimalFormat("#.###");
	public static final DecimalFormat DF2 = new DecimalFormat("#.##");
	public static final DecimalFormat DF1 = new DecimalFormat("#.#");
	//	public static String xmlFilePath = "./data/flowgraph_test_lg.xml";
	public static boolean RECORDING = false;
	public static boolean isShiftDown;
	public static boolean isCtrlDown;
	public static float ZOOM = 1;
	public static float world_scale = 10;
	public static ToxiclibsSupport GFX;
	public static PSys PSYS;
	public static Graph GRAPH;
	public static Editor GEDIT;
	public static VoronoiDiagram VSYS;
	public static ControlP5 CP5;
	public static PFont pfont, bfont;
	public Vec2D mousePos() {return new Vec2D(mouseX, mouseY);}
	Display display = new Display(this);
	public static void main(String[] args) {
		PApplet.main(new String[]{("app.core.App")});
		System.out.println("Current File  " + filepath);
	}
	public static void __rebelReload() {
		System.out.println("********************  rebelRyeload  ********************");
		System.out.println("Current File: " + filepath);
	}

	public void setup() {
		pfont = createFont("SourceCodePro", 10);
		bfont = createFont("SourceCodePro", 14);
		GFX = new ToxiclibsSupport(this);
		CP5 = new ControlP5(this);
		PSYS = new PSys(this);
		GRAPH = new Graph();
		GEDIT = new Editor(this);
		VSYS = new VoronoiDiagram(this);
		Gui.initGUI(this);
		size(WIDTH, HEIGHT, P2D);
		frameRate(60);
		smooth(16);
		colorMode(HSB, 360, 100, 100);
//		background(0xffffffff);
		background(Color.BG);
		ellipseMode(RADIUS);
		textAlign(LEFT);
		textFont(pfont, 10);
		strokeWeight(1);
		noStroke();
		noFill();
	}
	public void draw() {
//		background(0xffffffff);
		background(Color.BG);
		noFill(); noStroke();
		VSYS.draw();
		display.draw();
		PSYS.draw();
		if (RECORDING) { RECORDING = false; endRecord(); System.out.println("SVG EXPORTED SUCCESSFULLY"); }
		CP5.draw();
	}

	public void mouseMoved() {
		GEDIT.mouseMoved(mousePos());
	}
	public void mousePressed() {
		if (mouseButton == RIGHT) { GEDIT.mousePressed(mousePos()); Gui.toggleObjProperties(); }
	}
	public void mouseDragged() {
		if (mouseButton == RIGHT) GEDIT.mouseDragged(mousePos());
	}
	public void mouseReleased() {
		if (mouseButton == RIGHT) GEDIT.mouseReleased(mousePos());
	}
	public void mouseWheel(MouseEvent event) {
		float e = event.getCount();
//		if (e > 0) {System.out.println("-");} else if (e == 0) {System.out.println("0");} else if (e < 0) {System.out.println("+");}
		GEDIT.mouseWheel(e);
	}
	public void keyPressed() {
		if (key == CODED) {
			if (keyCode == SHIFT) { isShiftDown = true; }
			if (keyCode == CONTROL) {isCtrlDown = true;}
		}
		switch (key) {
			case '1': Gui.drawVorPoly = !Gui.drawVorPoly; break;
			case '2': Gui.drawVorBez = !Gui.drawVorBez; break;
			case '3': Gui.drawVorVec = !Gui.drawVorVec; break;
			case '4': Gui.drawPhysInfo = !Gui.drawPhysInfo; break;
			case 'c': Gui.doClip = !Gui.doClip; break;
			case 'a': GEDIT.createNewNode(Gui.nameTextfield.getStringValue(), Gui.radiusSlider.getValue(), mousePos()); Gui.toggleObjProperties(); break;
			case 'f': GEDIT.createNewEdge(); break;
			case 'x': GEDIT.deleteNode(); break;
			case 'z': GEDIT.deleteEdges(); break;

			case 'q': GEDIT.createNewBranch(Gui.capacitySlider.getValue(), true); break;
			case 'w': GEDIT.createNewBranch(Gui.capacitySlider.getValue(), false); break;
			case 'l': GEDIT.lockNode(); break;
		}
	}
	public void keyReleased() {
		if (key == CODED && keyCode == SHIFT) { isShiftDown = false; }
	}

	public void controlEvent(ControlEvent theEvent) {
		Gui.controlEvent(this, theEvent);
	}
}


