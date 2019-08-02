package cs120.bdevaughn.texascounties.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import cs120.bdevaughn.texascounties.Coords2D;

public class Coors2DTests {

	@Test
	public void testGeneration() {
		float x = 15.0f,y = 30.0f;
		Coords2D c = new Coords2D(x,y);
		assertTrue(c.getX()==x);
		assertTrue(c.getY()==y);
	}

}
