package app.core;

import app.graph.Editor;
import app.graph.Graph;
import app.metaball.Metaball;
import app.phys.Cloud;
import app.phys.PSys;
import app.xml.Node;
import controlP5.*;
import org.philhosoft.p8g.svg.P8gGraphicsSVG;
import util.Color;

import static app.core.App.CP5;

public class Gui {
	public static Knob radiusSlider;
	private static Knob colorSlider;
	public static Knob capacitySlider;
	private static Group toolGroup;
	private static Group graphGroup;
	private static Group vorGroup;
	private static Group fileGroup;
	private static Group physGroup;
	private static Group cloudGroup;
	private static MultiList fileMenu;
	private static MultiList graphMenu;
	private static MultiList cloudMenu;
	private static MultiList physMenu;
	private static MultiList vorMenu;
	public static Textfield nameTextfield;
	private static Accordion accordion;

	public static final int outlinerX = App.WIDTH - 200;

	/**
	 * Graph
	 */
	public static boolean isGraphUpdating;
	public static boolean drawGraphList = true;
	public static boolean drawGraphNodes = true;
	public static boolean drawGraphEdges = true;
	public static boolean isEditMode = true;
//	public static int editor_numClones = 1;

	/**
	 * Physics
	 */
	public static boolean updatePhysics = true;
	public static boolean drawPhysInfo;
	public static boolean drawPhysVec = true;
	public static boolean drawPhysSpr = true;
	public static boolean drawPhysMin;
	public static boolean drawPhysBhv;
	public static boolean drawPhysWgt;

	public static float setWorldScl = 10;
	public static float physDrag = 0.3f;
	public static float physPtclScale = 1;
	public static float physSprScale = 1;
	public static float physBhvScale = 2f;
	public static float physPtclWght = 1f;
	public static float physBhvStr = -1f;
	public static float physSprStr = 0.01f;
	public static float physMindistStr = 0.01f;

	/**
	 * Voronoi
	 */
	public static boolean isVorUpdating = true;
	public static boolean isVorOffset = true;
	public static boolean drawVoronoi = true;
	public static boolean drawVorPoly = true;
	public static boolean drawVorBez;
	public static boolean drawVorVec;
	public static boolean drawVorInfo;
	public static float setVorPerimres = 10;
	public static float setVorClipscale = 1;
	public static float setVorOffset = -2;
	/*	private static float vor_ringScale = 1;	private static float vor_rectScale = 1;	private static float vor_intersectorScale = 1;*/

	/**
	 * Cloud
	 */
	public static float cloudBhvStr = -1;
	public static float cloudBhvScl = 100;
	public static float cloudVecWgt = .5f;
	public static float cloudMinScl = 100;
	public static float cloudMinStr = -1;
	public static boolean isCloudUpdating = true;
	public static boolean drawCloud = true;
	private static Group metaGroup;

	public static void controlEvent(App app, ControlEvent theEvent) {
		if (!theEvent.isGroup()) {
			float theValue = theEvent.getController().getValue();
//			System.out.println(theEvent.getController().getName() + "=>" + theValue);
			switch (theEvent.getController().getName()) {
				/** File */
				case "file_quit": System.out.println("[quit]"); app.exit(); break;
				case "file_open": Graph.rebuild(); break;
				case "file_save": Graph.build(); break;
				case "file_print": app.beginRecord(P8gGraphicsSVG.SVG, "./out/svg/print-###.svg"); App.RECORDING = true; break;
				/** Graph */
				case "drawGraphList": drawGraphList = !drawGraphList; break;
				case "drawGraphNodes": drawGraphNodes = !drawGraphNodes; break;
				case "drawGraphEdges": drawGraphEdges = !drawGraphEdges; break;
				case "setWorldScl": setWorldScl = theValue; break;
				case "doClearGraph": App.PSYS = new PSys(app); App.GRAPH = new Graph(); break;
				case "isGraphUpdating": isGraphUpdating = !isGraphUpdating; break;
				/** Physics */
				case "drawPhysInfo": drawPhysInfo = !drawPhysInfo; break;
				case "drawPhysVec": drawPhysVec = !drawPhysVec; break;
				case "drawPhysSpr": drawPhysSpr = !drawPhysSpr; break;
				case "drawPhysMin": drawPhysMin = !drawPhysMin; break;
				case "drawPhysWgt": drawPhysWgt = !drawPhysWgt; break;
				case "drawPhysBhv": drawPhysBhv = !drawPhysBhv; break;
				case "isPhysUpdating": updatePhysics = !updatePhysics; break;
				case "doAddMindist": App.PSYS.addMinDist(); break;
				case "doClearMindist": App.PSYS.clearMinDist(); break;
				case "doClearPhysics": App.PSYS.reset(); break;
				case "setPhysDrag": physDrag = theValue; break;
				case "setPhysVecScl": physPtclScale = theValue; break;
				case "setPhysVecWgt": physPtclWght = theValue; break;
				case "setPhysSprScl": physSprScale = theValue; break;
				case "setPhysSprStr": physSprStr = theValue; break;
				case "setPhysBhvScl": physBhvScale = theValue; break;
				case "setPhysBhvStr": physBhvStr = theValue; break;
				case "setPhysMinStr": physMindistStr = theValue; break;
				/** Voronoi */
				case "drawVoronoi": drawVoronoi = !drawVoronoi; break;
				case "drawVorPoly": drawVorPoly = !drawVorPoly; break;
				case "drawVorBez": drawVorBez = !drawVorBez; break;
				case "drawVorVec": drawVorVec = !drawVorVec; break;
				case "drawVorInfo": drawVorInfo = !drawVorInfo; break;
				case "isVorUpdating": isVorUpdating = !isVorUpdating; break;
				case "isVorOffset": isVorOffset = !isVorOffset; break;
				case "setVorPerimres": setVorPerimres = theValue; break;
				case "setVorClipscale": setVorClipscale = theValue; break;
				case "setVorOffset": setVorOffset = theValue; break;
				/** Cloud */
				case "doAddCloud": Cloud.addCloud(); break;
				case "doAddCloudPerim": Cloud.addPerimeterVecs(50); break;
				case "doAddCloudRand": Cloud.addRandomVecs(9); break;
				case "doAddCloudMind": Cloud.addCloudMinDist(); break;
				case "setCloudVecWgt": cloudVecWgt = theValue; break;
				case "setCloudBhvScl": cloudBhvScl = theValue; break;
				case "setCloudBhvStr": cloudBhvStr = theValue; break;
				case "setCloudMinStr": cloudMinStr = theValue; break;
				case "setCloudMinScl": cloudMinScl = theValue; break;
				case "isCloudUpdating": isCloudUpdating = !isCloudUpdating; break;
				case "drawCloud": drawCloud = !drawCloud; break;

				case "isMetaUpdating": Metaball.isMetaUpdating = !Metaball.isMetaUpdating; break;
				case "isMetaDynamic": Metaball.isMetaDynamic = !Metaball.isMetaDynamic; break;
				case "drawMetaLine": Metaball.drawMetaLine = !Metaball.drawMetaLine; break;
				case "drawMetaPnt": Metaball.drawMetaPnt = !Metaball.drawMetaPnt; break;
				case "drawMetaEdgePos": Metaball.drawMetaEdgePos = !Metaball.drawMetaEdgePos; break;
				case "drawMetaPos0": Metaball.drawMetaPos0 = !Metaball.drawMetaPos0; break;
				case "setMetaVisc": Metaball.viscosity = theValue; break;
				case "setMetaThresh": Metaball.threshold = theValue; break;
				case "setMetaStep": Metaball.stepping = theValue; break;
				case "setMetaTrack": Metaball.tracking = theValue; break;
				case "setMetaMaxIter": Metaball.maxIter = (int) theValue; break;
				case "setMetaMaxPts": Metaball.maxPts = (int) theValue; break;
				case "setMetaMaxTrackIter": Metaball.maxTrackIter = (int) theValue; break;
				case "setMetaBorderStep": Metaball.borderStepSize = theValue; break;

				default: if (theEvent.getController() != fileMenu) {
					System.out.println("Missing ControlEvent [" + theEvent.getController().getName() + "=" + theValue + "]");
				} break;
			}
		}
	}
	public static void init() {
		CP5.enableShortcuts();
		CP5.setAutoDraw(false);
		CP5.setFont(App.pfont, 10);
		CP5.setAutoSpacing(4, 8);
		CP5.setColorBackground(Color.CP5_BG).setColorForeground(Color.CP5_FG).setColorActive(Color.CP5_ACT);
		CP5.setColorCaptionLabel(Color.CP5_CAP).setColorValueLabel(Color.CP5_VAL);

		initControllers();
		initMenus();
		guiStyles();
		/** Accordion */
		accordion = CP5.addAccordion("acc").setPosition(0, 0).setWidth(220).setCollapseMode(Accordion.MULTI).setMinItemHeight(32);
		accordion.addItem(physGroup).addItem(vorGroup).addItem(metaGroup).addItem(cloudGroup).addItem(toolGroup);
		accordion.open(0, 1, 2);
	}

	private static void initControllers() {
		physGroup = CP5.addGroup("physics_group").setBackgroundHeight((9 * 24) + 24);
		vorGroup = CP5.addGroup("voronoi_group").setBackgroundHeight((3 * 24) + 24);
		metaGroup = CP5.addGroup("metaball_group").setBackgroundHeight((8 * 24) + 24);
		cloudGroup = CP5.addGroup("cloud_group").setBackgroundHeight((5 * 24) + 24);
		toolGroup = CP5.addGroup("tool_group").setBackgroundHeight(140);

		CP5.begin(10, 12);
		CP5.addSlider("setWorldScl").setValue(setWorldScl).setRange(1, 20).setDecimalPrecision(0).setGroup(physGroup).linebreak();
		CP5.addSlider("setPhysDrag").setValue(physDrag).setRange(0.1f, 1).setDecimalPrecision(2).linebreak().setGroup(physGroup);
		CP5.addSlider("setPhysVecScl").setValue(physPtclScale).setRange(0.5f, 2).setDecimalPrecision(1).linebreak().setGroup(physGroup);
		CP5.addSlider("setPhysVecWgt").setValue(physPtclWght).setRange(0.1f, 3).setDecimalPrecision(1).linebreak().setGroup(physGroup);
		CP5.addSlider("setPhysSprScl").setValue(physSprScale).setRange(0.5f, 2).setDecimalPrecision(1).linebreak().setGroup(physGroup);
		CP5.addSlider("setPhysSprStr").setValue(physSprStr).setRange(0.001f, 0.05f).setDecimalPrecision(3).linebreak().setGroup(physGroup);
		CP5.addSlider("setPhysBhvScl").setValue(physBhvScale).setRange(0, 3).setDecimalPrecision(1).linebreak().setGroup(physGroup);
		CP5.addSlider("setPhysBhvStr").setValue(physBhvStr).setRange(-3f, 0).setDecimalPrecision(2).linebreak().setGroup(physGroup);
		CP5.addSlider("setPhysMinStr").setValue(physMindistStr).setRange(0.001f, 0.05f).setDecimalPrecision(2).linebreak().setGroup(physGroup);
		CP5.end();
		CP5.begin(10, 12);
		CP5.addSlider("setVorClipscale").setValue(setVorClipscale).setRange(.01f, 2).setDecimalPrecision(1).setGroup(vorGroup).linebreak();
		CP5.addSlider("setVorPerimres").setValue(setVorPerimres).setRange(4, 20).setDecimalPrecision(1).setGroup(vorGroup).linebreak();
		CP5.addSlider("setVorOffset").setValue(setVorOffset).setRange(-10, 10).setDecimalPrecision(0).setGroup(vorGroup).linebreak();
		CP5.end();
		CP5.begin(10, 12);
		CP5.addSlider("setMetaVisc").setValue(Metaball.viscosity).setRange(1, 3).setDecimalPrecision(1).setGroup(metaGroup).linebreak();
		CP5.addSlider("setMetaThresh").setValue(Metaball.threshold).setRange(0.0f, 0.03f).setDecimalPrecision(3).setGroup(metaGroup).linebreak();
		CP5.addSlider("setMetaStep").setValue(Metaball.stepping).setRange(.01f, 50).setDecimalPrecision(0).setGroup(metaGroup).linebreak();
		CP5.addSlider("setMetaTrack").setValue(Metaball.tracking).setRange(0.001f, 2).setDecimalPrecision(3).setGroup(metaGroup).linebreak();
		CP5.addSlider("setMetaMaxIter").setValue(Metaball.maxIter).setRange(2, 1000).setDecimalPrecision(0).setGroup(metaGroup).linebreak();
		CP5.addSlider("setMetaMaxPts").setValue(Metaball.maxPts).setRange(2, 50).setDecimalPrecision(0).setGroup(metaGroup).linebreak();
		CP5.addSlider("setMetaMaxTrackIter").setValue(Metaball.maxTrackIter).setRange(1, 1000).setDecimalPrecision(0).setGroup(metaGroup).linebreak();
		CP5.addSlider("setMetaBorderStep").setValue(Metaball.borderStepSize).setRange(0.001f, 50).setDecimalPrecision(3).setGroup(metaGroup).linebreak();
		CP5.end();
		CP5.begin(10, 12);
		CP5.addSlider("setCloudVecWgt").setValue(cloudVecWgt).setRange(.1f, 3).setDecimalPrecision(2).setGroup(cloudGroup).linebreak();
		CP5.addSlider("setCloudBhvScl").setValue(cloudBhvScl).setRange(0, 300).setDecimalPrecision(0).setGroup(cloudGroup).linebreak();
		CP5.addSlider("setCloudBhvStr").setValue(cloudBhvStr).setRange(-3f, 1).setDecimalPrecision(2).setGroup(cloudGroup).linebreak();
		CP5.addSlider("setCloudMinStr").setValue(cloudMinStr).setRange(.001f, 0.05f).setDecimalPrecision(2).setGroup(cloudGroup).linebreak();
		CP5.addSlider("setCloudMinScl").setValue(cloudMinScl).setRange(.1f, 2).setDecimalPrecision(2).setGroup(cloudGroup).linebreak();
		CP5.end();
		CP5.begin(0, 0);
		radiusSlider = CP5.addKnob("setSize").setCaptionLabel("Size").addListener(new radiusSliderListener()).setRange(0, 500).setValue(50).setPosition(10, 30).setDecimalPrecision(1).setGroup(toolGroup).hide();
		colorSlider = CP5.addKnob("setType").setCaptionLabel("Type").addListener(new colorSliderListener()).setPosition(80, 30).setRange(0, 360).setValue(10).setDecimalPrecision(0).setGroup(toolGroup).hide();
		capacitySlider = CP5.addKnob("setCapacity").setCaptionLabel("Capacity").addListener(new capacitySliderListener()).setPosition(150, 30).setRange(1, 200).setValue(1).setDecimalPrecision(0).setGroup(toolGroup).hide();
		CP5.end();

		/** Outliner */
		nameTextfield = CP5.addTextfield("setName").setCaptionLabel("Name").addListener(new nameTextfieldListener()).setPosition(0, 0).setStringValue("untitled").hide();
	}
	private static void initMenus() {
		fileMenu = CP5.addMultiList("myList", 220, 0, 130, 24);
		MultiListButton file_options = fileMenu.add("fileButton", 1).setWidth(130).setHeight(20); file_options.setCaptionLabel("File");
		file_options.add("file_quit", 1).setCaptionLabel("Quit");
		file_options.add("file_open", 1).setCaptionLabel("Open XML");
		file_options.add("file_save", 1).setCaptionLabel("Save XML");
		file_options.add("file_print", 1).setCaptionLabel("Print SVG");
		file_options.add("file_loadDef", 1).setCaptionLabel("Load Defaults");
		file_options.add("file_saveDef", 1).setCaptionLabel("Save Defaults");
		MultiListButton graph_options = fileMenu.add("graphButton", 2).setWidth(130).setHeight(20); graph_options.setCaptionLabel("Graph");
		graph_options.add("drawGraphNodes", 1).setCaptionLabel("Draw Nodes");
		graph_options.add("drawGraphEdges", 1).setCaptionLabel("Draw Edges");
		graph_options.add("drawGraphList", 1).setCaptionLabel("Draw Outline");
		graph_options.add("isGraphUpdating", 1).setCaptionLabel("Is Updating");
		graph_options.add("doClearGraph", 1).setCaptionLabel("Clear Graph");
		MultiListButton physics_options = fileMenu.add("physicsButton", 3).setWidth(130).setHeight(20); physics_options.setCaptionLabel("Physics");
		physics_options.add("drawPhysInfo", 1).setCaptionLabel("Draw Info");
		physics_options.add("drawPhysVec", 1).setCaptionLabel("Draw Particles");
		physics_options.add("drawPhysSpr", 1).setCaptionLabel("Draw Springs");
		physics_options.add("drawPhysMin", 1).setCaptionLabel("Draw MinDist");
		physics_options.add("drawPhysWgt", 1).setCaptionLabel("Draw Weights");
		physics_options.add("drawPhysBhv", 1).setCaptionLabel("Draw Behaviors");
		physics_options.add("isPhysUpdating", 1).setCaptionLabel("Is Updating");
		physics_options.add("doAddMindist", 1).setCaptionLabel("Add MinDist");
		physics_options.add("doClearMindist", 1).setCaptionLabel("Clear MinDist");
		physics_options.add("doClearPhysics", 1).setCaptionLabel("Clear Physics");
		MultiListButton voronoi_options = fileMenu.add("voronoiButton", 5).setWidth(130).setHeight(20); voronoi_options.setCaptionLabel("Voronoi");
		voronoi_options.add("drawVorPoly", 1).setCaptionLabel("Draw Polygons");
		voronoi_options.add("drawVorBez", 1).setCaptionLabel("Draw Bezier");
		voronoi_options.add("drawVorVec", 1).setCaptionLabel("Draw Handles");
		voronoi_options.add("drawVorInfo", 1).setCaptionLabel("Draw Info");
		voronoi_options.add("drawVorCol", 1).setCaptionLabel("Draw Info");
		voronoi_options.add("isVorOffset", 1).setCaptionLabel("Is Offset");
		voronoi_options.add("isVorUpdating", 1).setCaptionLabel("Is Updating");
		MultiListButton metaball_options = fileMenu.add("metaballButton", 6).setWidth(130).setHeight(20); metaball_options.setCaptionLabel("Metaball");
		metaball_options.add("isMetaUpdating", 1).setCaptionLabel("Is Updating");
		metaball_options.add("isMetaDynamic", 1).setCaptionLabel("Is Dynamic");
		metaball_options.add("drawMetaLine", 1).setCaptionLabel("Draw Lines");
		metaball_options.add("drawMetaPnt", 1).setCaptionLabel("Draw Points");
		metaball_options.add("drawMetaEdgePos", 1).setCaptionLabel("Draw EdgePos");
		metaball_options.add("drawMetaPos0", 1).setCaptionLabel("Draw Pos0");
		MultiListButton cloud_options = fileMenu.add("cloudButton", 4).setWidth(130).setHeight(20); cloud_options.setCaptionLabel("Cloud");
		cloud_options.add("drawCloud", 1).setCaptionLabel("Draw Cloud");
		cloud_options.add("doAddCloud", 1).setCaptionLabel("Add Cloud");
		cloud_options.add("doAddCloudPerim", 1).setCaptionLabel("Add Perimeter");
		cloud_options.add("doAddCloudRand", 1).setCaptionLabel("Add Random");
		cloud_options.add("doAddCloudMind", 1).setCaptionLabel("Add MinDist");
		cloud_options.add("isCloudUpdating", 1).setCaptionLabel("Is Updating");
	}
	private static void guiStyles() {
		for (Button b : CP5.getAll(Button.class)) {
			b.setSize(130, 22);
			b.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setFont(App.pfont);
		} for (Slider s : CP5.getAll(Slider.class)) {
			s.setSize(200, 20);
			s.showTickMarks(false);
			s.setHandleSize(12);
			s.setSliderMode(Slider.FLEXIBLE);
			s.getValueLabel().align(ControlP5.LEFT, ControlP5.CENTER).getStyle().setPaddingLeft(4);
			s.getCaptionLabel().align(ControlP5.RIGHT, ControlP5.CENTER).getStyle().setPaddingRight(4);
		} for (Knob k : CP5.getAll(Knob.class)) {
			k.setRadius(30);
			k.setDragDirection(Knob.HORIZONTAL);
		} for (Textfield t : CP5.getAll(Textfield.class)) {
			t.setSize(120, 12);
			t.setAutoClear(false);
			t.getCaptionLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).getStyle().setPaddingTop(4);
			t.setColor(0xff000000);
			t.setColorBackground(0xffffffff);
			t.getValueLabel().align(ControlP5.LEFT, ControlP5.CENTER);
			t.setColorForeground(0xffffffff);
			t.getCaptionLabel().hide();
		} for (Group g : CP5.getAll(Group.class)) {
			g.setBackgroundColor(Color.CP5_GRP);
			g.setBarHeight(32);
			g.getCaptionLabel().align(ControlP5.LEFT, ControlP5.CENTER).getStyle().setPaddingLeft(4);
		}
	}
	static void toggleObjProperties() {
		if (Editor.hasActiveNode()) {
			radiusSlider.setValue(Editor.activeNode.getSize());
			colorSlider.setValue(Editor.activeNode.getType());
			capacitySlider.setValue(Editor.activeNode.getOccupancy());
			nameTextfield.setValue(Editor.activeNode.getName());
			radiusSlider.show();
			colorSlider.show();
			capacitySlider.show();
			nameTextfield.show();
			accordion.open(6);
			nameTextfield.setPosition(outlinerX, 50 + Editor.activeNode.getId() * 14);
		} else {
			radiusSlider.hide();
			colorSlider.hide();
			capacitySlider.hide();
			nameTextfield.hide();
			accordion.close(6);
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
			if (App.CP5.isMouseOver()) { if (Editor.hasActiveNode()) Editor.activeNode.setType((int) color); for (Node n : Editor.selectedNodes) {n.setType((int) color);} }
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

/*	private static void editorOptions() {
		generator = CP5.addGroup("RECURSIVE GRAPH GENERATOR").setBackgroundHeight(140);
		CP5.begin(10, 10);
		CP5.addNumberbox("ITER_A").setPosition(10, 14).linebreak();
		CP5.addNumberbox("ITER_B").setPosition(10, 38).linebreak();
		CP5.addNumberbox("ITER_C").setPosition(10, 62).linebreak();
		CP5.addNumberbox("ITER_D").setPosition(10, 86).linebreak();
		CP5.addNumberbox("ITER_E").setPosition(10, 110).linebreak();
		CP5.end();
	}*/

/*	for (Numberbox n : CP5.getAll(Numberbox.class)) {
		n.setSize(200, 16);
		n.setRange(0, 10);
		n.setDirection(Controller.HORIZONTAL);
		n.setMultiplier(0.05f);
		n.setDecimalPrecision(0);
		n.getCaptionLabel().align(ControlP5.RIGHT, ControlP5.CENTER);
		n.getValueLabel().align(ControlP5.LEFT, ControlP5.CENTER);
		n.setGroup(generator);
	}*/

/*	private static void initGuiChart() {
		myChart = CP5.addChart("hello").setPosition(0, App.HEIGHT - 200).setSize(200, 200).setRange(-20, 20).setView(Chart.BAR);
		// use Chart.LINE, Chart.PIE, Chart.AREA, Chart.BAR_CENTERED
		myChart.getColor().setBackground(0xffffffff);
		myChart.addDataSet("world");
		myChart.setColors("world", 0xffff00ff, 0xffff0000);
		myChart.setData("world", new float[4]);
		myChart.setStrokeWeight(1.5f);
		myChart.addDataSet("earth");
		myChart.setColors("earth", 0xffffffff, 0xff00ff00);
		myChart.updateData("earth", 1, 2, 10, 3);
	}*/
//		view.add("view_vorInfo", 1).setCaptionLabel("Vor Info");
//		view.add("view_outliner", 1).setCaptionLabel("Outliner");
//		view.add("view_voronoi", 1).setCaptionLabel("Voronoi");
//		view.add("view_nodes", 1).setCaptionLabel("Nodes");
//		view.add("view_edges", 1).setCaptionLabel("Edges");
//		run.add("run_voronoi", 1).setCaptionLabel("Run Voronoi");
//		run.add("run_flowgraph", 1).setCaptionLabel("Update FlowGraph");
//		CP5.addSlider("vor_ringScale").setValue(vor_ringScale).setRange(.1f, 10).setDecimalPrecision(1).setGroup(vorGroup).linebreak();
//		CP5.addSlider("vor_rectScale").setValue(vor_rectScale).setRange(.01f, 2).setDecimalPrecision(2).setGroup(vorGroup).linebreak();
//		CP5.addSlider("vor_intersectorScale").setValue(vor_intersectorScale).setRange(.01f, 2).setDecimalPrecision(2).setGroup(vorGroup).linebreak();
/*physics_options.add("edit_rebuildMinD", 1).setCaptionLabel("Rebuild MinDist");*/
				/*case "vor_ringScale": vor_ringScale = theValue; break; case "vor_rectScale": vor_rectScale = theValue; break;				case "vor_intersectorScale": vor_intersectorScale = theValue; break;*/
			/*	case "edit_rebuildMinD": App.PSYS.rebuildMinDist(); break;*/
//	private static void initGuiProperties() {	}private static void physicsButtons() { }	private static void physicsSliders() { }	private static void voronoiButtons() { }	private static void voronoiSliders() { }	private static void initGuiCloud() { }
