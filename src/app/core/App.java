package app.core;

import app.graph.GEdit;
import app.graph.Graph;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.MouseEvent;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;

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
	public static OPT CONF;
	public static PSys PSYS;
	public static Graph GRAPH;
	public static GEdit GEDIT;
	public static VoronoiDiagram VSYS;
	public static ControlP5 CP5;
	public static PFont pfont, bfont;
	public Vec2D mousePos() {return new Vec2D(mouseX, mouseY);}

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
		CONF = new OPT();
		CP5 = new ControlP5(this);
		PSYS = new PSys(this);
		GRAPH = new Graph();
		GEDIT = new GEdit(this);
		VSYS = new VoronoiDiagram(this);
		Gui.initGUI(this);
		size(WIDTH, HEIGHT, P2D);
		frameRate(60);
		smooth(16);
		colorMode(HSB, 100, 100, 100);
		background(0xffffffff);
		ellipseMode(RADIUS);
		textAlign(LEFT);
		textFont(pfont, 10);
		strokeWeight(1);
		noStroke();
		noFill();
	}
	public void draw() {
		background(0xffffffff); noFill(); noStroke();
		VSYS.draw();
		GEDIT.draw();
		PSYS.draw();
		if (RECORDING) { RECORDING = false; endRecord(); System.out.println("SVG EXPORTED SUCCESSFULLY"); }
		CP5.draw();
	}

	public void mouseMoved() {
		GEDIT.mouseMoved(mousePos());
	}
	public void mousePressed() {
		if (mouseButton == RIGHT) { GEDIT.mousePressed(mousePos()); toggleObjProperties(); }
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
			System.out.println(g);
			if (keyCode == SHIFT) { isShiftDown = true; }
			if (keyCode == CONTROL) {isCtrlDown = true;}
		}
		switch (key) {
			case '1': Gui.showPolygons = !Gui.showPolygons; break;
			case '2': Gui.showBezier = !Gui.showBezier; break;
			case '3': Gui.showVerts = !Gui.showVerts; break;
			case '4': OPT.showInfo = !OPT.showInfo; break;
			case 'c': CONF.doClip = !CONF.doClip; break;
			case 'a': GEDIT.addNodeAtCursor(Gui.nameTextfield.getStringValue(), Gui.radiusSlider.getValue(), mousePos()); toggleObjProperties(); break;
			case 'f': GEDIT.addEdgeToSelection(); break;
			case 'x': GEDIT.removeActiveNode(); break;
			case 'z': GEDIT.removeActiveEdges(); break;

			case 'q': GEDIT.createBranch(Gui.capacitySlider.getValue(), true); break;
			case 'w': GEDIT.createBranch(Gui.capacitySlider.getValue(), false); break;
			case 'l': GEDIT.freezeNode(); break;
		}
	}
	public void keyReleased() {
		if (key == CODED && keyCode == SHIFT) { isShiftDown = false; }
	}

	private void toggleObjProperties() {
		if (GEDIT.hasActiveNode()) {
			Gui.radiusSlider.setValue(GEDIT.getActiveNode().getSize());
			Gui.colorSlider.setValue(GEDIT.getActiveNode().getColor());
			Gui.capacitySlider.setValue(GEDIT.getActiveNode().getOccupancy());
			Gui.nameTextfield.setValue(GEDIT.getActiveNode().getName());
			Gui.radiusSlider.show();
			Gui.colorSlider.show();
			Gui.capacitySlider.show();
			Gui.nameTextfield.show();
			Gui.accordion.open(2);
		} else {
			Gui.radiusSlider.hide();
			Gui.colorSlider.hide();
			Gui.capacitySlider.hide();
			Gui.nameTextfield.hide();
			Gui.accordion.close(2);
		}
	}
	public void controlEvent(ControlEvent theEvent) {
		Gui.controlEvent(theEvent);
	}
}


