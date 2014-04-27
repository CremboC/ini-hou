/**
 * 
 */
package seprini.models.types;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

/**
 * @author Leslie
 * 
 */
public class PlayerTest {
	Player player;

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
		player = new Player(1);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link seprini.models.types.Player#Player(int)}.
	 */
	@Test
	public void testPlayer() {
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getLeft()}.
	 */
	@Test
	public void testGetLeft() {
		assertEquals(player.getLeft(), Keys.LEFT, 0);
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getRight()}.
	 */
	@Test
	public void testGetRight() {
		assertEquals(player.getRight(), Keys.RIGHT, 0);
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getAltIncrease()}.
	 */
	@Test
	public void testGetAltIncrease() {
		assertEquals(player.getAltIncrease(), Keys.UP, 0);
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getAltDecrease()}.
	 */
	@Test
	public void testGetAltDecrease() {
		assertEquals(player.getAltDecrease(), Keys.DOWN, 0);
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getSpeedIncrease()}.
	 */
	@Test
	public void testGetSpeedIncrease() {
		assertEquals(player.getSpeedIncrease(), Keys.NUMPAD_8, 0);
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getSpeedDecrease()}.
	 */
	@Test
	public void testGetSpeedDecrease() {
		assertEquals(player.getSpeedDecrease(), Keys.NUMPAD_2, 0);
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getReturnToPath()}.
	 */
	@Test
	public void testGetReturnToPath() {
		assertEquals(player.getReturnToPath(), Keys.NUMPAD_0, 0);
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getSwitchPlane()}.
	 */
	@Test
	public void testGetSwitchPlane() {
		assertEquals(player.getSwitchPlane(), Keys.NUMPAD_5, 0);
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getTakeoff()}.
	 */
	@Test
	public void testGetTakeoff() {
		assertEquals(player.getTakeoff(), Keys.NUMPAD_1, 0);
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getNumber()}.
	 */
	@Test
	public void testGetNumber() {
		assertEquals(player.getNumber(), 1, 0);
	}

	/**
	 * Test method for {@link seprini.models.types.Player#getColor()}.
	 */
	@Test
	public void testGetColor() {
		assertEquals(player.getColor(), Color.BLUE);
	}
}
