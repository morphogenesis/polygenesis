package app.core;

import app.graph.Node;
import app.ui.Editor;
import controlP5.*;
import util.Color;

import static app.core.App.CP5;

public class Gui {
	public static Knob radiusSlider;
	public static Knob colorSlider;
	public static Knob capacitySlider;
	public static Group properties;
	public static Group generator;
	public static Group vorConfig;
	public static Group physConfig;
	public static Textfield nameTextfield;
	public static Accordion accordion;

	public static boolean isEditMode = true;

	public static boolean updateGraph;
	public static boolean drawGraphOutline = true;
	public static boolean drawGraphNodes = true;
	public static boolean drawGraphEdges = true;

	public static boolean updatePhysics = true;
	public static boolean drawPhysInfo;
	public static boolean drawPhysParticles = true;
	public static boolean drawPhysSprings = true;
	public static boolean drawPhysMindist;
	public static boolean drawPhysBehaviors;
	public static boolean drawPhysWeights;

	public static boolean updateVoronoi = true;
	public static boolean drawVoronoi;
	public static boolean drawVorPoly = true;
	public static boolean drawVorBez;
	public static boolean drawVorVec;
	public static boolean drawVorInfo;
	public static boolean offsetVoronoi = true;

	public static float world_scale = 10;
	public static float physDrag = 0.3f;
	public static float physPtclScale = 1;
	public static float physSprScale = 1;
	public static float physBhvScale = 2f;
	public static float physPtclWght = 1f;
	public static float physBhvStr = -0.3f;
	public static float physSprStr = 0.001f;
	public static float physMindistStr = 0.01f;

	public static float cloudBhvStr = -1;
	public static float cloudBhvScale = 100;
	public static float cloudPtclWght = .5f;
	public static float cloudMindistScale = 100;
	public static float cloudMindStr = -1;

	public static float vor_perimRes = 10;
	public static float vor_ringScale = 1;
	public static float vor_rectScale = 1;
	public static float vor_intersectorScale = 1;
	public static float vor_clipScale = 1;

	public static int editor_numClones = 1;
	public static int outlinerX = App.WIDTH - 200;
//	public static float particlePadding = 0.1f;
//	public static float psys_boundsScale;

	public static void controlEvent(App app, ControlEvent theEvent) {
		if (!theEvent.isGroup()) {
			float theValue = theEvent.getController().getValue();
			System.out.println(theEvent.getController().getName() + "=>" + theValue);
			switch (theEvent.getController().getName()) {
				case "file_quit": System.out.println("[quit]"); app.exit(); break;
				case "file_open": App.GRAPH.rebuild(); break;
				case "file_save": App.GRAPH.build(); break;
//				case "file_print": app.beginRecord(P8gGraphicsSVG.SVG, "./out/svg/print-###.svg"); App.RECORDING = true; break;

				case "view_nodes": drawGraphNodes = !drawGraphNodes; break;
				case "view_edges": drawGraphEdges = !drawGraphEdges; break;
//				case "view_outliner": drawGraphOutline = !drawGraphOutline; break;
				case "view_particles": drawPhysParticles = !drawPhysParticles; break;
				case "view_springs": drawPhysSprings = !drawPhysSprings; break;
				case "view_minDist": drawPhysMindist = !drawPhysMindist; break;
				case "view_weights": drawPhysWeights = !drawPhysWeights; break;
				case "view_behaviors": drawPhysBehaviors = !drawPhysBehaviors; break;
				case "view_voronoi": drawVoronoi = !drawVoronoi; break;
				case "view_vorInfo": drawVorInfo = !drawVorInfo; break;
				case "view_physInfo": drawPhysInfo = !drawPhysInfo; break;

				case "view_outline": drawGraphOutline = !drawGraphOutline; break;

				case "run_physics": updatePhysics = !updatePhysics; break;
				case "run_flowgraph": updateGraph = !updateGraph; break;
				case "run_voronoi": updateVoronoi = !updateVoronoi; break;

				case "edit_addMinDist": App.PSYS.addMinDist(); break;
				case "edit_addCloudMinDist": App.PSYS.addCloudMinDist(); break;
				case "edit_addPerim": App.PSYS.addPerim(); break;
				case "edit_rebuildMinD": App.PSYS.clearMinDist(); App.PSYS.addMinDist(); break;
				case "edit_clearMinD": App.PSYS.clearMinDist(); break;
				case "edit_clearPhys": App.PSYS.reset(); break;
//				case "edit_clearAll": App.PSYS = new PSys(app); App.GRAPH.reset(); break;
				case "world_scale": world_scale = theValue; break;
				case "verlet_drag": physDrag = theValue; break;
				case "particle_scale": physPtclScale = theValue; break;
				case "mindist_strength": physMindistStr = theValue; break;
				case "particle_strength": physPtclWght = theValue; break;
				case "behavior_scale": physBhvScale = theValue; break;
				case "behavior_strength": physBhvStr = theValue; break;
				case "spring_scale": physSprScale = theValue; break;
				case "spring_strength": physSprStr = theValue; break;
				case "vor_perimRes": vor_perimRes = theValue; break;
				case "vor_clipScale": vor_clipScale = theValue; break;
				case "vor_ringScale": vor_ringScale = theValue; break;
				case "vor_rectScale": vor_rectScale = theValue; break;
				case "vor_intersectorScale": vor_intersectorScale = theValue; break;
/*				case "cloudBhvScale": cloudBhvScale = theValue; break;
				case "cloudBhvStr": cloudBhvStr = theValue; break;
				case "cloudMindStr": cloudMindStr = theValue; break;
				case "cloudMindistScale": cloudMindistScale = theValue; break;
				case "cloudPtclWght": cloudPtclWght = theValue; break;*/
			}
		}
	}
	public static void initGUI(App app) {
		CP5.enableShortcuts();
		CP5.setAutoDraw(false);
		CP5.setFont(App.pfont, 10);
		CP5.setAutoSpacing(4, 8);
		CP5.setColorBackground(Color.CP5_BG).setColorForeground(Color.CP5_FG).setColorActive(Color.CP5_ACT);
		CP5.setColorCaptionLabel(Color.CP5_CAP).setColorValueLabel(Color.CP5_VAL);
		initGUImenu();
		initGuiPhysProperties(app);
		voronoiOptions();
		initGuiObProperties(app);
		editorOptions();
		initViewOptions();
		initGUI_styles();
		accordion = CP5.addAccordion("acc").setPosition(0, 92).setWidth(220).setCollapseMode(Accordion.MULTI);
		accordion.addItem(physConfig).addItem(vorConfig).addItem(generator).addItem(properties);
		accordion.open(0, 1);
	}
	private static void initViewOptions() {
		CP5.addButton("view_outline").setPosition(App.WIDTH - 200, 0);
	}
	public static void initGuiPhysProperties(App app) {
		physConfig = CP5.addGroup("VERLET PHYSICS SETTINGS").setBackgroundHeight(236);
		CP5.begin(10, 10);
		CP5.addSlider("world_scale").setRange(1, 20).setDecimalPrecision(0).linebreak().setGroup(physConfig);
		CP5.addSlider("verlet_drag").setValue(0.32f).setRange(0.1f, 1).setDecimalPrecision(2).linebreak().setGroup(physConfig);
		CP5.addSlider("particle_scale").setValue(physPtclScale).setRange(0.5f, 2).setDecimalPrecision(1).linebreak().setGroup(physConfig);
		CP5.addSlider("spring_scale").setValue(physSprScale).setRange(0.5f, 2).setDecimalPrecision(1).linebreak().setGroup(physConfig);
		CP5.addSlider("behavior_scale").setValue(physBhvScale).setRange(0, 3).setDecimalPrecision(1).linebreak().setGroup(physConfig);
		CP5.addSlider("particle_strength").setValue(physPtclWght).setRange(0.1f, 3).setDecimalPrecision(1).linebreak().setGroup(physConfig);
		CP5.addSlider("behavior_strength").setValue(physBhvStr).setRange(-3f, 0).setDecimalPrecision(2).linebreak().setGroup(physConfig);
		CP5.addSlider("spring_strength").setValue(physSprStr).setRange(0.001f, 0.05f).setDecimalPrecision(3).linebreak().setGroup(physConfig);
		CP5.addSlider("mindist_strength").setValue(physMindistStr).setRange(0.001f, 0.05f).setDecimalPrecision(2).linebreak().setGroup(physConfig);
/*		CP5.addSlider("cloudBhvScale").setValue(cloudBhvScale).setRange(10, 500).setDecimalPrecision(0).linebreak().setGroup(physConfig);
		CP5.addSlider("cloudBhvStr").setValue(cloudBhvStr).setRange(-1f, 1).setDecimalPrecision(2).linebreak().setGroup(physConfig);
		CP5.addSlider("cloudPtclWght").setValue(cloudPtclWght).setRange(.1f, 2).setDecimalPrecision(2).linebreak().setGroup(physConfig);
		CP5.addSlider("cloudMindStr").setValue(cloudMindStr).setRange(.01f, 5).setDecimalPrecision(2).linebreak().setGroup(physConfig);
		CP5.addSlider("cloudMindistScale").setValue(cloudMindistScale).setRange(20, 500).setDecimalPrecision(2).linebreak().setGroup(physConfig);*/
		CP5.end();
	}
	private static void voronoiOptions() {
		vorConfig = CP5.addGroup("VORONOI SETTINGS").setBackgroundHeight(164);
		CP5.begin(10, 10);
		CP5.addSlider("vor_perimRes").setValue(vor_perimRes).setRange(4, 20).setDecimalPrecision(1).setGroup(vorConfig).linebreak();
		CP5.addSlider("vor_ringScale").setValue(vor_ringScale).setRange(.1f, 10).setDecimalPrecision(1).setGroup(vorConfig).linebreak();
		CP5.addSlider("vor_rectScale").setValue(vor_rectScale).setRange(.01f, 2).setDecimalPrecision(2).setGroup(vorConfig).linebreak();
		CP5.addSlider("vor_intersectorScale").setValue(vor_intersectorScale).setRange(.01f, 2).setDecimalPrecision(2).setGroup(vorConfig).linebreak();
		CP5.addSlider("vor_clipScale").setValue(vor_clipScale).setRange(.01f, 2).setDecimalPrecision(2).setGroup(vorConfig).linebreak();
		CP5.end();
	}
	public static void initGuiObProperties(App app) {
		properties = CP5.addGroup("OBJECT_PROPERTIES").setBackgroundHeight(200);
		CP5.begin(0, 0);
		radiusSlider = CP5.addKnob("setSize").setCaptionLabel("Size").setRange(0, 500).setPosition(10, 30).setDecimalPrecision(1).setGroup(properties);
		radiusSlider.setValue(50); radiusSlider.addListener(new radiusSliderListener()); radiusSlider.hide();
		colorSlider = CP5.addKnob("setColor").setCaptionLabel("Color").setRange(0, 100).setPosition(80, 30).setDecimalPrecision(0).setGroup(properties);
		colorSlider.setValue(50); colorSlider.addListener(new colorSliderListener()); colorSlider.hide();
		capacitySlider = CP5.addKnob("setCapacity").setCaptionLabel("Capacity").setRange(1, 200).setPosition(150, 30).setDecimalPrecision(0).setGroup(properties);
		capacitySlider.setValue(1); capacitySlider.addListener(new capacitySliderListener()); capacitySlider.hide();
		nameTextfield = CP5.addTextfield("setName").setCaptionLabel("Unique Datablock ID Name").setPosition(0, 0);
		nameTextfield.setStringValue("untitled"); nameTextfield.addListener(new nameTextfieldListener()); nameTextfield.hide();
		CP5.end();
	}
	private static void editorOptions() {
		generator = CP5.addGroup("RECURSIVE GRAPH GENERATOR").setBackgroundHeight(140);
		CP5.begin(10, 10);
		CP5.addNumberbox("ITER_A").setPosition(10, 14).linebreak();
		CP5.addNumberbox("ITER_B").setPosition(10, 38).linebreak();
		CP5.addNumberbox("ITER_C").setPosition(10, 62).linebreak();
		CP5.addNumberbox("ITER_D").setPosition(10, 86).linebreak();
		CP5.addNumberbox("ITER_E").setPosition(10, 110).linebreak();
		CP5.end();
	}
	public static void initGUImenu() {
		MultiList mainMenu = CP5.addMultiList("myList", 90, 0, 130, 24);
		MultiListButton file;
		file = mainMenu.add("File", 1);
		file.setWidth(130).setHeight(20);
		file.add("file_quit", 11).setCaptionLabel("Quit");
		file.add("file_open", 12).setCaptionLabel("Open XML");
		file.add("file_save", 13).setCaptionLabel("Save XML");
		file.add("file_print", 14).setCaptionLabel("Print SVG");
		file.add("file_loadDef", 15).setCaptionLabel("Load Defaults");
		file.add("file_saveDef", 16).setCaptionLabel("Save Defaults");
		MultiListButton view;
		view = mainMenu.add("View", 2);
		view.setWidth(130).setHeight(20);
		view.add("view_physInfo", 26).setCaptionLabel("Info");
		view.add("view_vorInfo", 26).setCaptionLabel("Info");
		view.add("view_outliner", 25).setCaptionLabel("Outliner");
		view.add("view_voronoi", 24).setCaptionLabel("Voronoi");
		view.add("view_nodes", 21).setCaptionLabel("Nodes");
		view.add("view_edges", 25).setCaptionLabel("Edges");
		view.add("view_particles", 22).setCaptionLabel("Particles");
		view.add("view_springs", 23).setCaptionLabel("Springs");
		view.add("view_minDist", 23).setCaptionLabel("MinDist");
		view.add("view_weights", 23).setCaptionLabel("Weights");
		view.add("view_behaviors", 23).setCaptionLabel("Behaviors");
		MultiListButton run;
		run = mainMenu.add("Run", 3);
		run.setWidth(130).setHeight(20);
		run.add("run_physics", 31).setCaptionLabel("Run Physics");
		run.add("run_voronoi", 32).setCaptionLabel("Run Voronoi");
		run.add("run_updateVals", 33).setCaptionLabel("Update Values");
		run.add("run_flowgraph", 33).setCaptionLabel("Update FlowGraph");
		MultiListButton edit;
		edit = mainMenu.add("Edit", 4);
		edit.setWidth(130).setHeight(20);
		edit.add("edit_addMinDist", 41).setCaptionLabel("Add MinDist");
		edit.add("edit_addCloudMinDist", 41).setCaptionLabel("Add CloudMinDist");
		edit.add("edit_addPerim", 41).setCaptionLabel("Add Perim");
		edit.add("edit_rebuildMinD", 42).setCaptionLabel("Rebuild MinDist");
		edit.add("edit_clearMinD", 43).setCaptionLabel("Clear MinDist");
		edit.add("edit_clearPhys", 44).setCaptionLabel("Reset Physics");
		edit.add("edit_clearAll", 45).setCaptionLabel("Clear");
	}
	public static void initGUI_styles() {
		for (Button b : CP5.getAll(Button.class)) {
			b.setSize(130, 22);
			b.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setFont(App.pfont);
		}
		for (Numberbox n : CP5.getAll(Numberbox.class)) {
			n.setSize(200, 16);
			n.setRange(0, 10);
			n.setDirection(Controller.HORIZONTAL);
			n.setMultiplier(0.05f);
			n.setDecimalPrecision(0);
			n.getCaptionLabel().align(ControlP5.RIGHT, ControlP5.CENTER);
			n.getValueLabel().align(ControlP5.LEFT, ControlP5.CENTER);
			n.setGroup(generator);
		}
		for (Knob k : CP5.getAll(Knob.class)) {
			k.setRadius(30);
			k.setDragDirection(Knob.HORIZONTAL);
			k.setGroup(properties);
		}
		for (Textfield t : CP5.getAll(Textfield.class)) {
			t.setSize(120, 12);
			t.setAutoClear(false);
			t.getCaptionLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).getStyle().setPaddingTop(4);
			t.setColor(0xff000000);
			t.setColorBackground(0xffffffff);
//			t.setGroup(properties);
			t.getValueLabel().align(ControlP5.LEFT, ControlP5.CENTER);
			t.setColorForeground(0xffffffff);
			t.getCaptionLabel().hide();
		}
		for (Slider s : CP5.getAll(Slider.class)) {
			s.setSize(170, 16);
			s.showTickMarks(false);
			s.setHandleSize(12);
			s.setSliderMode(Slider.FLEXIBLE);
			s.getValueLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER).getStyle().setPaddingLeft(4);
			s.getCaptionLabel().align(ControlP5.RIGHT, ControlP5.CENTER).getStyle().setPaddingRight(4);
		}
		for (Group g : CP5.getAll(Group.class)) {
			g.setBackgroundColor(Color.CP5_GRP);
			g.setBarHeight(32);/*.setWidth(220)*/
			g.getCaptionLabel().align(ControlP5.LEFT, ControlP5.CENTER).getStyle().setPaddingLeft(4);
		}
	}
	static void toggleObjProperties() {
		if (Editor.hasActiveNode()) {
			radiusSlider.setValue(Editor.activeNode.getSize());
//			colorSlider.setValue(Editor.activeNode.getColor());
			capacitySlider.setValue(Editor.activeNode.getOccupancy());
			nameTextfield.setValue(Editor.activeNode.getName());
			radiusSlider.show();
			colorSlider.show();
			capacitySlider.show();
			nameTextfield.show();
			accordion.open(3);
			nameTextfield.setPosition(outlinerX, 50 + Editor.activeNode.getId() * 14);
		} else {
			radiusSlider.hide();
			colorSlider.hide();
			capacitySlider.hide();
			nameTextfield.hide();
			accordion.close(3);
		}
	}

	static class nameTextfieldListener implements ControlListener {
		String name;
		public void controlEvent(ControlEvent e) { name = e.getController().getStringValue(); if (Editor.hasActiveNode()) Editor.activeNode.setName(name); }
	}

	static class colorSliderListener implements ControlListener {
		float color;
		public void controlEvent(ControlEvent e) {
			color = e.getController().getValue();
//			if (App.CP5.isMouseOver()) { for (Node n : Editor.selectedNodes) {n.setColor((int) color);} }
		}
	}

	static class capacitySliderListener implements ControlListener {
		float capacity;
		public void controlEvent(ControlEvent e) {
			capacity = e.getController().getValue(); if (App.CP5.isMouseOver()) { for (Node g : Editor.selectedNodes) {g.setOccupancy((int) capacity);} }
		}
	}

	static class radiusSliderListener implements ControlListener {
		float size;
		public void controlEvent(ControlEvent e) {
			size = e.getController().getValue();
			if (App.CP5.isMouseOver()) { for (Node g : Editor.selectedNodes) {g.setSize(size);} }
		}
	}
}
