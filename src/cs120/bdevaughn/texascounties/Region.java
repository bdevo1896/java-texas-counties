package cs120.bdevaughn.texascounties;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.LinkedList;

/**
 * This is the base class for the County class and it holds onto a color handle, Polygon handle, a list of
 * points, and handles for the the minimum and maximum x-value and minimum and maximum y-value.
 * This class also contains a drawOn method to draw out the Polygon with the color it was assigned.
 * @author Bryce DeVaughn
 *
 */
public class Region {
	private LinkedList<Coords2D> points;//Contains a list of Coors2D objects that contain latitude and longitude values
	private Color color;//The color of the region
	private Polygon poly;//The Polygon representing the shape of the region
	private float minx,miny,maxx,maxy;//The boundaries of the region
	private boolean active;//If the region can be used


	public Region(LinkedList<Coords2D> points) {
		this.points = points;
		this.poly = new Polygon();
		color = Color.BLACK;
		active = true;
	}

	/**
	 * This method will set the points for the polygon.
	 */
	public void setPolygon(Point[] polyPoints){
		for(Point point: polyPoints){
			poly.addPoint(point.x, point.y);
		}
	}

	/**
	 * This method will set the boundary values (like the min x-value)
	 */
	public void setBoundaries(){
		/*
		 * Finding the minimum x-value.
		 */
		minx = points.getFirst().getX();
		for(Coords2D coord: getPoints()){
			float temp = coord.getX();
			if(temp<minx)
				minx = temp;
		}

		/*
		 * Finding the max x-value
		 */
		maxx = getPoints().get(0).getX();
		for(Coords2D coord: getPoints()){
			float temp = coord.getX();
			if(temp>maxx)
				maxx = temp;
		}

		/*
		 * Finding the min y-value
		 */
		miny = getPoints().get(0).getY();
		for(Coords2D coord: getPoints()){
			float temp = coord.getY();
			if(temp<miny)
				miny = temp;
		}

		/*
		 * Finding the max y-value
		 */
		maxy = getPoints().get(0).getY();
		for(Coords2D coord: getPoints()){
			float temp = coord.getY();
			if(temp>maxy)
				maxy = temp;
		}
	}

	/**
	 * This method will draw the polygon.
	 */
	public void drawOn(Graphics gfx){
		if(active){
			gfx.setColor(color);
			gfx.fillPolygon(poly);
		}else{
			gfx.setColor(Color.GRAY);
			gfx.fillPolygon(poly);
		}
	}


	public float getMinX(){
		return minx;
	}

	public float getMaxX(){
		return maxx;
	}

	public float getMinY(){
		return miny;
	}

	public float getMaxY(){		
		return maxy;
	}

	public LinkedList<Coords2D> getPoints() {
		return points;
	}

	public void setPoints(LinkedList<Coords2D> points) {
		this.points = points;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Polygon getPoly() {
		return poly;
	}

	public void setPoly(Polygon poly) {
		this.poly = poly;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}



}
