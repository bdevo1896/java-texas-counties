package cs120.bdevaughn.texascounties;
/**
 * An instance of this class will hold the world coordinates of a point, that will be converted to 
 * pixel coordinates later. 
 * @author Bryce DeVaughn
 *
 */
public class Coords2D {
	
	private float x;
	private float y;

	public Coords2D(float x,float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	

}
