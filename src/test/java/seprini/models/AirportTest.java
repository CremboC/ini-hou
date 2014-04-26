/**
 * 
 */
package seprini.models;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Leslie
 * 
 */
public class AirportTest {

	Airport airport = new Airport(100, 200, true);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link seprini.models.Airport#additionalDraw(com.badlogic.gdx.graphics.g2d.SpriteBatch)}
	 * .
	 */
	@Test
	public void testAdditionalDraw() {

	}

	/**
	 * Test method for
	 * {@link seprini.models.Airport#Airport(float, float, boolean)}.
	 */
	@Test
	public void testAirport() {
		// Tests that runwayStart waypoint is created in the correct position
		assertEquals(airport.runwayStart.coords.x, 100 - 77, 0);
		assertEquals(airport.runwayStart.coords.y, 200 - 60, 0);

		// Tests that runwayEnd waypoint is created in the correct position
		assertEquals(airport.runwayEnd.coords.x, 100 + 77, 0);
		assertEquals(airport.runwayEnd.coords.y, 200 + 60, 0);

		// Tests that runwayLeft waypoint is created in the correct position
		assertEquals(airport.runwayLeft.coords.x, 100 - 157, 0);
		assertEquals(airport.runwayLeft.coords.y, 200 - 60, 0);

		// Tests that runwayRight waypoint is created in the correct position
		assertEquals(airport.runwayRight.coords.x, 100 - 77, 0);
		assertEquals(airport.runwayRight.coords.y, 200 - 140, 0);

		// Tests that the airports size is set as the same values in the config
		// file
		assertEquals(airport.size.x, 164, 0);
		assertEquals(airport.size.y, 125, 0);

	}

	/**
	 * Test method for {@link seprini.models.Airport#setTimeLeft(int)}.
	 * 
	 * When passed a positive integer, it will leave timeTillFreeRunway as 5 If
	 * a negative number is passed into it, it will set timeTillFreeRunway as 0
	 */
	@Test
	public void testSetTimeLeft() {
		airport.setTimeLeft(3);
		assertEquals(airport.timeTillFreeRunway, 5, 0);

		airport.setTimeLeft(-1);
		assertEquals(airport.timeTillFreeRunway, 0, 0);

	}

	/**
	 * Test method for
	 * {@link seprini.models.Airport#insertAircraft(seprini.models.Aircraft)}.
	 */
	@Test
	public void testInsertAircraft() {
	}

	/**
	 * Test method for {@link seprini.models.Airport#takeoff(int)}.
	 */
	@Test
	public void testTakeoff() {
	}

}
