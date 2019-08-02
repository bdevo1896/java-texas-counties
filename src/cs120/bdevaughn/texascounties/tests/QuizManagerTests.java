package cs120.bdevaughn.texascounties.tests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Polygon;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import cs120.bdevaughn.texascounties.Coords2D;
import cs120.bdevaughn.texascounties.County;
import cs120.bdevaughn.texascounties.QuizManager;
import cs120.bdevaughn.texascounties.Region;

public class QuizManagerTests {

	@Test
	public void testGeneration() throws IOException {
		QuizManager q = new QuizManager();
		q.setDimension(1000,1000);
		
		assertTrue(q.getCounties()!=null);
		assertTrue(q.getExclusionZones()!=null);
		
	}
	
	@Test
	public void readCounties() throws FileNotFoundException, IOException{
		QuizManager q = new QuizManager();
		q.setDimension(1000,1000);
		
		q.readCounties("co48_d00a.txt");
		
		assertTrue(q.getCounties().size()!=0);
		
		for(int i = 0; i < q.getCounties().size(); i++){
			County c = q.getCounties().get(i);
			assertTrue(q.getCounties().get(i).getPID()+"",q.getCounties().get(i).getPID() == i+1);
		}

	}
	
	@Test
	public void testReadCoordinates() throws IOException{
		QuizManager q = new QuizManager();
		q.setDimension(1000,1000);
		
		q.readCounties("co48_d00a.txt");
		q.readCoordinates("co48_d00.txt");
		ArrayList<County> counties = new ArrayList<County>(q.getCounties());
		ArrayList<Region> zones = new ArrayList<Region>(q.getExclusionZones());
		
		assertTrue(counties.size()!=0);
		assertTrue(zones.size()!=0);
		
		for(County c: counties){
			assertTrue(c.getPID()+"",c.getPoints()!=null);
			assertTrue(c.getPID()+"",c.getPoints().getFirst()!=null);
		}
		
		for(Region e: zones){
			assertTrue(e.getPoints()!=null);
			assertTrue(e.getPoints().getFirst()!=null);
		}
	}
	
	@Test
	public void testMakePolygons() throws IOException {
		QuizManager q = new QuizManager();
		q.setDimension(1000,1000);
		q.readCounties("co48_d00a.txt");
		q.readCoordinates("co48_d00.txt");
		q.makePolygons();
		ArrayList<County> tempLC = new ArrayList<County>(q.getCounties());
		ArrayList<Region> tempLR = new ArrayList<Region>(q.getExclusionZones());
		
		for(County c: tempLC){
			assertTrue(c.getPoly()!=null);
			assertTrue(c.getPID()+"",c.getPoly().npoints != 0);
		}
		
		for(Region r: tempLR){
			assertTrue(r.getPoly()!=null);
			assertTrue(r.getPoly().npoints!=0);
		}
	}
	
	@Test
	public void testFindCountyById() throws IOException {
		QuizManager q = new QuizManager();
		q.setDimension(1000,1000);
		q.readCounties("co48_d00a.txt");
		q.readCoordinates("co48_d00.txt");
		
		int id = 1;
		County c = q.findCountyById(id);
		
		assertTrue(c!=null);
		assertTrue(c.getPID()+"",c.getPID()==id);

	}
	
	@Test
	public void testToPixel(){
		QuizManager q = new QuizManager();
		q.setDimension(1000,1000);
		LinkedList<Coords2D> points = new LinkedList<Coords2D>();
		points.add(new Coords2D(5f,-5f));
		points.add(new Coords2D(5f,5f));
		points.add(new Coords2D(7.5f,0f));
		points.add(new Coords2D(10f,5f));
		points.add(new Coords2D(10f,-5f));
		
		County c = new County(points,1,265,4,"Collin");
		c.setBoundaries();
		
		q.getCounties().add(c);
		q.setBoundaries();
		System.out.println(q.getMinX());
		System.out.println(q.getMaxX());
		System.out.println(q.getMinY());
		System.out.println(q.getMaxY());
		
		LinkedList<Point> newPoints = new LinkedList<Point>();
		for(Coords2D coord: points){
			newPoints.add(q.toPixels(coord.getX(), coord.getY()));
		}
		/*
		 * Point 1
		 */
		assertTrue(newPoints.get(0).getX()+"",newPoints.get(0).getX()==0.0);
		assertTrue(newPoints.get(0).getY()+"",newPoints.get(0).getY()==1000.0);
		
		/*
		 * Point 2
		 */
		assertTrue(newPoints.get(1).getX()+"",newPoints.get(1).getX()==0.0);
		assertTrue(newPoints.get(1).getY()+"",newPoints.get(1).getY()==0.0);
		
		/*
		 * Point 3
		 */
		assertTrue(newPoints.get(2).getX()+"",newPoints.get(2).getX()==500.0);
		assertTrue(newPoints.get(2).getY()+"",newPoints.get(2).getY()==500.0);
		
		/*
		 * Point 4
		 */
		assertTrue(newPoints.get(3).getX()+"",newPoints.get(3).getX()==1000.0);
		assertTrue(newPoints.get(3).getY()+"",newPoints.get(3).getY()==0.0);
		
		/*
		 * Point 5
		 */
		assertTrue(newPoints.get(4).getX()+"",newPoints.get(4).getX()==1000.0);
		assertTrue(newPoints.get(4).getY()+"",newPoints.get(4).getY()==1000.0);
		
		
		
		
	}
	
	@Test
	public void testFindCountyWithPoint() throws IOException{
		QuizManager q = new QuizManager();
		q.setDimension(1000,1000);
		q.readCounties("co48_d00a.txt");
		q.readCoordinates("co48_d00.txt");
		q.setBoundaries();
		q.makePolygons();
		Point p = new Point(500,500);
		County testC = q.findCounty((int)p.getX(),(int)p.getY());
		
		assertTrue("X: "+p.getX()+" Y: "+p.getY(),testC!=null);
		
	}
	
	@Test
	public void testRandomCounty() throws FileNotFoundException, IOException{
		QuizManager q = new QuizManager();
		q.setDimension(1000,1000);
		q.readCounties("co48_d00a.txt");
		q.readCoordinates("co48_d00.txt");
		
		for(int i = 0; i < 10; i++){
			County[] list3 = q.randomCounty(4, 1);
			assertTrue(list3.length==4);
			for(County c: list3){
				assertTrue(c!=q.getCounties().get(0) && c!=null);
			}
		}

	}
	
	@Test
	public void testBoundaries() throws IOException{
		QuizManager q = new QuizManager();
		q.setDimension(1000,1000);
		q.readCounties("co48_d00a.txt");
		q.readCoordinates("co48_d00.txt");
		q.setBoundaries();
		
		for(County c: new ArrayList<County>(q.getCounties())){
			assertTrue(c.getMinX()!=0.0);
			assertTrue(c.getMaxX()!=0.0);
			assertTrue(c.getMinY()!=0.0);
			assertTrue(c.getMaxY()!=0.0);
		}
		
		for(Region r: new ArrayList<Region>(q.getExclusionZones())){
			assertTrue(r.getMinX()!=0.0);
			assertTrue(r.getMaxX()!=0.0);
			assertTrue(r.getMinY()!=0.0);
			assertTrue(r.getMaxY()!=0.0);
		}
	}

}
