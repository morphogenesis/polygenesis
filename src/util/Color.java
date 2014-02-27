package util;

public final class Color {
	//	DEFAULTS
	public static final int GREY_DK = 0xff333333;
	public static final int BG = 0xff393939;
	public static final int BLACK = 0xff000000;
	public static final int ORANGE = 0xffeca860;
	public static final int WHITE = 0xffffffff;
	public static final int BG_MENUS = 0xff171717;
	public static final int BG_TEXT = 0xff666666;
	public static final int GRID = 0xff464646;
	public static final int FACES = 0xff313131;
	public static final int GREY = 0xff666666;
	public static final int GREY_LT = 0xff999999;
	public static final int TXT = 0xffffffff;
	public static final int RED = 0xffbf3c3c;
	//	NODES
	public static final int NODE_TXT = 0xffffffff;
	public static final int NODE_F = 0xffbf3c3c;
	public static final int NODE_S = 0xff333333;
	public static final int NODE_SEL = 0xffbf3c3c;
	//	VORONOI
	public static final int VOR_TXT = 0xffffffff;
	public static final int VOR_VERTS = 0xffeca860;
	public static final int VOR_CELLS = 0xff666666;
	public static final int VOR_VOIDS = 0xff252525;
	//	PARTICLES
	public static final int PHYS_TXT = 0xffffffff;
	public static final int PHYS_PTCL = 0xff666666;
	public static final int PHYS_SPR = 0xff999999;
	public static final int PHYS_ATTR = 0xff444444;
	//	CP5
	public static final int CP5_CAP = 0xffc2c2c2;
	public static final int CP5_VAL = 0xffcccccc;
	public static final int CP5_ACT = 0xffe6e6e6;
	public static final int CP5_FG = 0xffbf3c3c;
	public static final int CP5_BG = 0xff2b2b2b;
	public static final int CP5_GRP = 0xff222222;
	public static final int CP5_BLUE = 0xff557fc1;
	public static final int NORMAL = 0xff666666;
	public static final int NORMAL_FILL = 0xff444444;
	public static final int ACTIVE = 0xffffaa40;
	public static final int ACTIVE_FILL = 0xffFFFFFF;
	public static final int SELECTED = 0xfff15800;
	public static final int SELECTED_FILL = 0xffcccccc;
	public static final int FROZEN = 0xffffffff;
	public static final int FROZEN_FILL = 0xff888888;
	public static final int PROXY = 0xff88ffff;
	public static final int GROUP = 0xff55bb55;

	public static String hsvToRgb(float hue, float saturation, float value) {
		int h = (int) (hue * 6);
		float f = hue * 6 - h;
		float p = value * (1 - saturation);
		float q = value * (1 - f * saturation);
		float t = value * (1 - (1 - f) * saturation);

		switch (h) {
			case 0: return rgbToString(value, t, p);
			case 1: return rgbToString(q, value, p);
			case 2: return rgbToString(p, value, t);
			case 3: return rgbToString(p, q, value);
			case 4: return rgbToString(t, p, value);
			case 5: return rgbToString(value, p, q);
			default: throw new RuntimeException("Error converting HSV to RGB.Input was " + hue + ", " + saturation + ", " + value);
		}
	}

	public static String rgbToString(float r, float g, float getNodeB) {
		String rs = Integer.toHexString((int) (r * 256));
		String gs = Integer.toHexString((int) (g * 256));
		String bs = Integer.toHexString((int) (getNodeB * 256));
		return rs + gs + bs;
	}
}
