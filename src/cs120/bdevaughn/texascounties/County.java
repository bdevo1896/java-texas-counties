package cs120.bdevaughn.texascounties;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

/**
 * An instance of this class will hold all the attributes of a county. These attributes are the name, id, 
 * fips state code, and a fips county code.
 * @author Bryce DeVaughn
 *
 */
public class County extends Region {
	
	private int pid,fipssd,fipscd;//int values for the polygon id, state code, and county code
	private String name;//To hold the name of the county

	public County(LinkedList<Coords2D> points,int pid,int fipssd, int fipscd, String name) {
		super(points);
		this.pid = pid;
		this.fipssd = fipssd;
		this.fipscd = fipscd;
		this.name = name;
		Random rand = new Random();
		Color randColor = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
		this.setColor(randColor);
	}
	
	@Override
	public void drawOn(Graphics gfx){
		super.drawOn(gfx);
	}

	public int getPID() {
		return pid;
	}

	public void setPID(int pid) {
		this.pid = pid;
	}

	public int getFIPSSD() {
		return fipssd;
	}

	public void setFIPSSD(int fipssd) {
		this.fipssd = fipssd;
	}

	public int getFIPSCD() {
		return fipscd;
	}

	public void setFIPSCD(int fipscd) {
		this.fipscd = fipscd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
