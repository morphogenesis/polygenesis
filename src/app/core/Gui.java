package app.core;

import controlP5.*;
import util.Color;

import java.io.Serializable;

import static app.core.App.CP5;

public class Gui implements Serializable {
	public static Knob radiusSlider;
	public static Knob colorSlider;
	public static Knob capacitySlider;
	public static Group properties;
	public static Group generator;
	public static Group vorConfig;
	public static Group physConfig;
	public static Textfield nameTextfield;
	public static Accordion accordion;
	public static float psys_perimRad = 100;
	public static float psys_perimRes = 100;
	public static float vor_perimRes = 10;
	public static float vor_perimScale = 1;
	public static float vor_boundsScale = 1;
	public static float vor_clipScale = 1;
	public static boolean showVoronoi;
	public static boolean showPolygons;
	public static boolean showBezier;
	public static boolean showVerts;
	public static boolean UPDATE_VORONOI = true;
	App app;
	public Gui(App app) { this.app = app; }
	public static void initGUI(App app) {
		CP5.enableShortcuts();
		CP5.setAutoDraw(false);
		CP5.setFont(App.pfont, 10);
		CP5.setAutoSpacing(4, 8);
		CP5.setColorBackground(Color.CP5_BG).setColorForeground(Color.CP5_FG).setColorActive(Color.CP5_ACT);
		CP5.setColorCaptionLabel(Color.CP5_CAP).setColorValueLabel(Color.CP5_VAL);
		initGUImenu();
		initGuiPhysProperties(app);
		initGuiObProperties(app);
		initGUI_styles();
		accordion = CP5.addAccordion("acc").setPosition(0, 92).setWidth(220).setCollapseMode(Accordion.MULTI);
		accordion.addItem(physConfig).addItem(vorConfig).addItem(generator).addItem(properties);
		accordion.open(0, 1);
	}
	public static void initGuiPhysProperties(App app) {
		physConfig = CP5.addGroup("VERLET PHYSICS SETTINGS").setBackgroundHeight(340);
		CP5.begin(10, 8);
		CP5.addSlider("world_scale").setRange(1, 20).setDecimalPrecision(0).linebreak().setGroup(physConfig);
		CP5.addSlider("verlet_drag").setValue(0.32f).setRange(0.1f, 1).setDecimalPrecision(2).linebreak().setGroup(physConfig);
		CP5.addSlider("particle_scale").setValue(OPT.particleScale).setRange(0.5f, 2).setDecimalPrecision(1).linebreak().setGroup(physConfig);
		CP5.addSlider("spring_scale").setValue(OPT.springScale).setRange(0.5f, 2).setDecimalPrecision(1).linebreak().setGroup(physConfig);
		CP5.addSlider("behavior_scale").setValue(OPT.behaviorScale).setRange(0, 7).setDecimalPrecision(1).linebreak().setGroup(physConfig);
		CP5.addSlider("particle_strength").setValue(OPT.particleWeight).setRange(0.1f, 5).setDecimalPrecision(1).linebreak().setGroup(physConfig);
		CP5.addSlider("behavior_strength").setValue(OPT.behaviorStrength).setRange(-5f, 0).setDecimalPrecision(2).linebreak().setGroup(physConfig);
		CP5.addSlider("spring_strength").setValue(OPT.springStrength).setRange(0.001f, 0.05f).setDecimalPrecision(3).linebreak().setGroup(physConfig);
		CP5.addSlider("mindist_strength").setValue(OPT.mindistStrength).setRange(0.001f, 0.05f).setDecimalPrecision(2).linebreak().setGroup(physConfig);
		CP5.end();
		vorConfig = CP5.addGroup("VORONOI SETTINGS").setBackgroundHeight(340);
		CP5.begin(10, 15);
		CP5.addSlider("psys_perimRes").setValue(psys_perimRes).setRange(50, 300).setDecimalPrecision(0).setGroup(vorConfig).linebreak();
		CP5.addSlider("psys_perimRad").setValue(psys_perimRad).setRange(10, 300).setDecimalPrecision(0).setGroup(vorConfig).linebreak();
		CP5.addSlider("vor_perimRes").setValue(vor_perimRes).setRange(4, 20).setDecimalPrecision(1).setGroup(vorConfig).linebreak();
		CP5.addSlider("vor_perimScale").setValue(vor_perimScale).setRange(.1f, .9f).setDecimalPrecision(1).setGroup(vorConfig).linebreak();
		CP5.addSlider("vor_boundsScale").setValue(vor_boundsScale).setRange(1, 2).setDecimalPrecision(2).setGroup(vorConfig).linebreak();
		CP5.addSlider("vor_clipScale").setValue(vor_clipScale).setRange(1, 2).setDecimalPrecision(2).setGroup(vorConfig).linebreak();

		CP5.end();
	}
	public static void initGuiObProperties(App app) {
		properties = CP5.addGroup("OBJECT_PROPERTIES").setBackgroundHeight(200).setBarHeight(32).setWidth(220);
		CP5.begin(0, 0);
		radiusSlider = CP5.addKnob("setSize").setCaptionLabel("Size").setRange(0, 500).setPosition(10, 30).setDecimalPrecision(1).setGroup(properties);
		radiusSlider.setValue(50);
		radiusSlider.addListener(new radiusSliderListener());
		radiusSlider.hide();
		colorSlider = CP5.addKnob("setColor").setCaptionLabel("Color").setRange(0, 100).setPosition(80, 30).setDecimalPrecision(0).setGroup(properties);
		colorSlider.setValue(50);
		colorSlider.addListener(new colorSliderListener());
		colorSlider.hide();
		capacitySlider = CP5.addKnob("setCapacity").setCaptionLabel("Capacity").setRange(1, 200).setPosition(150, 30).setDecimalPrecision(0).setGroup(properties);
		capacitySlider.setValue(1);
		capacitySlider.addListener(new capacitySliderListener());
		capacitySlider.hide();
		nameTextfield = CP5.addTextfield("setName").setCaptionLabel("Unique Datablock ID Name").setPosition(40, 140).setGroup(properties);
		nameTextfield.setStringValue("untitled");
		nameTextfield.addListener(new nameTextfieldListener());
		nameTextfield.hide();
		CP5.end();
		generator = CP5.addGroup("RECURSIVE GRAPH GENERATOR").setBackgroundHeight(140).setBarHeight(32).setWidth(220);
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
			t.setSize(140, 22);
			t.setAutoClear(false);
			t.getCaptionLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).getStyle().setPaddingTop(4);
			t.setGroup(properties);
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
			g.getCaptionLabel().align(ControlP5.LEFT, ControlP5.CENTER).getStyle().setPaddingLeft(4);
		}
	}
	public static void controlEvent(ControlEvent theEvent) {
		if (!theEvent.isGroup()) {
			float theValue = theEvent.getController().getValue();
			System.out.println(theEvent.getController().getName() + "=>" + theValue);
			switch (theEvent.getController().getName()) {
//				case "file_quit": System.out.println("[quit]"); app.exit(); break;
				case "file_open": App.GRAPH.rebuild(); break;
				case "file_save": App.GRAPH.build(); break;
//				case "file_print": app.beginRecord(P8gGraphicsSVG.SVG, "./out/svg/print-###.svg"); App.RECORDING = true; break;
				case "view_nodes": OPT.showNodes = !OPT.showNodes; break;
				case "view_edges": OPT.showEdges = !OPT.showEdges; break;
				case "view_outliner": OPT.toggleOutliner(); break;
				case "view_particles": OPT.showParticles = !OPT.showParticles; break;
				case "view_springs": OPT.showSprings = !OPT.showSprings; break;
				case "view_minDist": OPT.showMinDist = !OPT.showMinDist; break;
				case "view_weights": OPT.showWeights = !OPT.showWeights; break;
				case "view_behaviors": OPT.showBehaviors = !OPT.showBehaviors; break;
				case "view_physInfo": OPT.showInfo = !OPT.showInfo; break;
				case "run_physics": OPT.isUpdating = !OPT.isUpdating; break;
				case "run_flowgraph": App.GEDIT.isUpdating = !App.GEDIT.isUpdating; break;

				case "view_voronoi": showVoronoi = !showVoronoi; break;
				case "run_voronoi": UPDATE_VORONOI = !UPDATE_VORONOI; break;

				case "edit_addMinDist": App.PSYS.addMinDist(); break;
				case "edit_addPerim": App.PSYS.addPerim(); break;
				case "edit_rebuildMinD": App.PSYS.clearMinDist(); App.PSYS.addMinDist(); break;
				case "edit_clearMinD": App.PSYS.clearMinDist(); break;
				case "edit_clearPhys": App.PSYS.reset(); break;
//				case "edit_clearAll": App.PSYS = new PSys(app); App.GRAPH.reset(); break;
				case "world_scale": OPT.world_scale = theValue; break;
				case "verlet_drag": OPT.PHYS_DRAG = theValue; break;
				case "particle_scale": OPT.particleScale = theValue; break;
				case "mindist_strength": OPT.mindistStrength = theValue; break;
				case "particle_strength": OPT.particleWeight = theValue; break;
				case "behavior_scale": OPT.behaviorScale = theValue; break;
				case "behavior_strength": OPT.behaviorStrength = theValue; break;
				case "spring_scale": OPT.springScale = theValue; break;
				case "spring_strength": OPT.springStrength = theValue; break;
				case "vor_perimRes": vor_perimRes = theValue; break;
				case "vor_perimScale": vor_perimScale = theValue; break;
				case "vor_boundsScale": vor_boundsScale = theValue; break;
				case "psys_perimRes": psys_perimRes = theValue; break;
				case "psys_perimRad": psys_perimRad = theValue; break;
			}
		}
	}
	static class nameTextfieldListener implements ControlListener {
		String name;
		public void controlEvent(ControlEvent e) { name = e.getController().getStringValue(); App.GEDIT.setName(name); }
	}

	static class colorSliderListener implements ControlListener {
		float color;
		public void controlEvent(ControlEvent e) { color = e.getController().getValue(); App.GEDIT.setColor(color); }
	}

	static class capacitySliderListener implements ControlListener {
		float capacity;
		public void controlEvent(ControlEvent e) { capacity = e.getController().getValue(); App.GEDIT.setOccupancy(capacity); }
	}

	static class radiusSliderListener implements ControlListener {
		float size;
		public void controlEvent(ControlEvent e) { size = e.getController().getValue(); App.GEDIT.setSize(size); }
	}
}