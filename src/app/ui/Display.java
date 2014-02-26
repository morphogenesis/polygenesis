package app.ui;

import app.core.App;
import app.core.Gui;
import app.graph.Editor;
import app.graph.Graph;
import app.xml.Edge;
import app.xml.Node;

import java.util.ArrayList;

/**
 * Created on 2/17/14.
 */
public class Display {
	private final App p5;

	public Display(App p5) { this.p5 = p5; }

	public void draw() {
		Graph.update();
		if (Gui.drawGraphEdges) { drawEdges();}
		if (Gui.drawGraphNodes) { drawNodes(); }
		if (Gui.drawGraphList) { drawOutliner(); }
//		if (Gui.drawVoronoi) { drawVoronoi(); }
		drawStatusBar();
	}

	private void drawEdges() {
		ArrayList<DrawEdge> egfx = new ArrayList<>();
		for (Edge e : Graph.edges) { egfx.add(new DrawEdge(p5, e)); }
		for (DrawEdge fx : egfx) { fx.draw(); }
		for (DrawEdge fx : egfx) { if (Editor.adjacentEdges.contains(fx.e)) fx.drawActive(); }
	}

	private void drawNodes() {
		ArrayList<DrawNode> ngfx = new ArrayList<>();
		for (Node n : Graph.nodes) { n.update(); ngfx.add(new DrawNode(p5, n)); }
		for (DrawNode nt : ngfx) { nt.draw(); }
		for (DrawNode nt : ngfx) { nt.drawWire(); }
	}

	private void drawOutliner() {
		int xO = Gui.outlinerX;
		String totalArea = App.DF1.format(Graph.totalArea);
		p5.fill(0xffeaeaea);
		p5.text("NAME", 10, -2);
		p5.text("AREA", 100, -2);
		p5.textFont(App.bfont, 14);
		p5.text("Total Area: " + totalArea + " sq.m", xO, 40);
		p5.textFont(App.pfont, 10);
		p5.noFill();
	}

	private void drawStatusBar() {
		if (Gui.isEditMode) {
			p5.fill(0xffff0000);
			p5.text("Edit Mode", App.WIDTH / 2, 20);
			p5.noFill();
		}
	}
}