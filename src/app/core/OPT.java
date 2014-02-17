package app.core;

/**
 * Created on 2/14/14.
 */
public class OPT {
	public static boolean showInfo;
	public static boolean drawOutliner = true;
	public static boolean showNodes = true;
	public static boolean showEdges = true;
	public static boolean showParticles = true;
	public static boolean showSprings = true;
	public static boolean showMinDist;
	public static boolean showBehaviors;
	public static boolean showWeights;
	public static boolean isUpdating = true;
	public static boolean doClip;
	public static boolean UPDATE_PHYSICS = true;
	public static boolean UPDATE_VALUES = true;
	public static boolean UPDATE_FLOWGRAPH = true;

	public static float world_scale;
	public static float particleScale = 1;
	public static float particleWeight = 0.5f;
	public static float particlePadding = 0.1f;
	public static float behaviorScale = 2f;
	public static float behaviorStrength = -0.3f;
	public static float springScale = 1;
	public static float springStrength = 0.001f;
	public static float mindistStrength = 0.001f;
	public static float PHYS_DRAG;
	public static float psys_boundsScale;
	public static float psys_perimRes;

	public static void toggleOutliner() {drawOutliner = !drawOutliner;}
}
