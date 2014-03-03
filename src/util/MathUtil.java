package util;

public class MathUtil {

    public static float PI = 3.14159265f;
    public static float HALF_PI = 1.57079633f;
    public static float PI_OVER_180 = 0.0174532925f;
    public static float PI_UNDER_180 = 57.2957795f;

    public static float map(float value, float fromLow, float fromHigh, float toLow, float toHigh) {
        return (value - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
    }

    public static float strictMap(float value, float fromLow, float fromHigh, float toLow, float toHigh) {
        float result = (value - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
        return result < toLow ? toLow : (result > toHigh ? toHigh : result);
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : (value > max ? max : value);
    }

    public static float angleBetween(float x, float y, float u, float v, boolean radians) { // removed " = false" from after "radians" //
        return radians ? (float) Math.atan2(v - y, u - x) : degrees((float) Math.atan2(v - y, u - x));
    }

    public static float degrees(float value) {
        return value * PI_UNDER_180;
    }

    public static float radians(float value) {
        return value * PI_OVER_180;
    }
}
