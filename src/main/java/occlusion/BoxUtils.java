package occlusion;

public class BoxUtils {

	/**
	 * 
	 * @param a a rectangle
	 * @param b a rectangle
	 * @return true iff there is area in the overlapping region (intersection) of a and b
	 */
	public static boolean hasOverlap(Box a, Box b) {
		boolean horizontalOverlap = a.x+a.w>b.x && a.x<b.x+b.w;
		boolean verticalOverlap =   a.y+a.h>b.y && a.y<b.y+b.h;
		return horizontalOverlap && verticalOverlap;
	}
	
	public static boolean isValid(Box box) {
		return box.w>0 && box.h>0;
	}
}
