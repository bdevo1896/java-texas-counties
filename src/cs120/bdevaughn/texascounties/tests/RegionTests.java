package cs120.bdevaughn.texascounties.tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.LinkedList;

import org.junit.Test;

import cs120.bdevaughn.texascounties.Coords2D;
import cs120.bdevaughn.texascounties.Region;

public class RegionTests {

	@Test
	public void testGeneration() {
		LinkedList<Coords2D> points = new LinkedList<Coords2D>();
		points.add(new Coords2D(15f,30f));
		points.add(new Coords2D(20f,28f));
		points.add(new Coords2D(10f,35f));
		points.add(new Coords2D(12f,30f));
		points.add(new Coords2D(17f,20f));
		
		Region r = new Region(points);
		r.setBoundaries();
		assertTrue(r.getPoints().size()==5);
		assertTrue(r.getMinX()==10f);
		assertTrue(r.getMaxX()==20f);
		assertTrue(r.getMinY()==20f);
		assertTrue(r.getMaxY()==35f);
		assertTrue(r.getColor().toString(),r.getColor()== Color.BLACK);
		assertTrue(r.isActive());
	}

}
