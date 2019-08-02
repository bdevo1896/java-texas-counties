package cs120.bdevaughn.texascounties.tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import org.junit.Test;

import cs120.bdevaughn.texascounties.Coords2D;
import cs120.bdevaughn.texascounties.County;

public class CountyTests {

	@Test
	public void testGeneration() {
		LinkedList<Coords2D> points = new LinkedList<Coords2D>();
		points.add(new Coords2D(15f,30f));
		points.add(new Coords2D(20f,28f));
		points.add(new Coords2D(10f,35f));
		points.add(new Coords2D(12f,30f));
		points.add(new Coords2D(17f,20f));
		
		County c = new County(points,1,1234,500123,"Collin");
		c.setBoundaries();
		
		assertTrue(c.getPID()==1);
		assertTrue(c.getFIPSSD()==1234);
		assertTrue(c.getFIPSCD()==500123);
		assertTrue("Collin".equals(c.getName()));
		assertTrue(c.getPoints().size()==5);
		assertTrue(c.getMinX()==10f);
		assertTrue(c.getMaxX()==20f);
		assertTrue(c.getMinY()==20f);
		assertTrue(c.getMaxY()==35f);
		System.out.println("R: "+c.getColor().getRed()+" G: "+c.getColor().getGreen()+" B: "+c.getColor().getBlue());
		assertTrue(c.getColor()+"",c.getColor()!= Color.BLACK && c.getColor() != null);
		
	}

}
