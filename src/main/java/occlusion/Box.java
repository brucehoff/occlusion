package occlusion;

/*
 * A bounding box with upper left corner (x, y) and size (width, height) = (w, h)
 */
public class Box {
	public int x;
	public int y;
	public int w;
	public int h;
	
	public Box(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	@Override
	public String toString() {
		return "Box [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + h;
		result = prime * result + w;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Box other = (Box) obj;
		if (h != other.h)
			return false;
		if (w != other.w)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
