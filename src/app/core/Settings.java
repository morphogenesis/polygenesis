package app.core;

/**
 * Created on 2/14/14.
 */
public class Settings {
	public boolean showInfo;
	public boolean showOutliner = true;
	public boolean showNodes = true;
	public boolean showEdges = true;
	public boolean showParticles = true;
	public boolean showSprings = true;
	public boolean showMinDist;
	public boolean showBehaviors;
	public boolean showWeights;
	public boolean showVoronoi;
	public boolean showPolygons;
	public boolean showBezier;
	public boolean showVerts;
	public boolean isUpdating = true;
	public boolean doClip;
	public float particleScale = 1;
	public float particleWeight = 0.5f;
	public float particlePadding = 0.1f;
	public float behaviorScale = 2f;
	public float behaviorStrength = -0.3f;
	public float springScale = 1;
	public float springStrength = 0.001f;
	public float mindistStrength = 0.001f;
	public float PHYS_DRAG = 0.3f;
	public boolean UPDATE_VORONOI;
	public boolean UPDATE_PHYSICS = true;
	public boolean UPDATE_VALUES = true;
	public boolean UPDATE_FLOWGRAPH = true;

	public float worldBoundRes = 10;
	public float curBoundRes = 10;
	public float worldBoundOffset = 0;
	public float curBoundOffset = 0;


}
