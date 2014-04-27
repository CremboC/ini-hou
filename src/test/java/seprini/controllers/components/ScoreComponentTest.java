/**
 * 
 */
package seprini.controllers.components;

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
public class ScoreComponentTest {

	ScoreComponent scoreComponent;

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
		scoreComponent = new ScoreComponent();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.ScoreComponent#ScoreComponent()}.
	 */
	@Test
	public void testScoreComponent() {
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.ScoreComponent#incrementScore(double)}
	 * .
	 */
	@Test
	public void testIncrementScore() {
		scoreComponent.incrementScore(5);

		assertEquals(scoreComponent.getScore(), 5, 0);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.ScoreComponent#decrementScore(double)}
	 * .
	 */
	@Test
	public void testDecrementScore() {
		scoreComponent.decrementScore(5);

		assertEquals(scoreComponent.getScore(), -5, 0);
	}

	/**
	 * Test method for
	 * {@link seprini.controllers.components.ScoreComponent#getScore()}.
	 */
	@Test
	public void testGetScore() {
		assertEquals(scoreComponent.getScore(), 0, 0);

		scoreComponent.incrementScore(5);

		assertEquals(scoreComponent.getScore(), 5, 0);

		scoreComponent.decrementScore(3);

		assertEquals(scoreComponent.getScore(), 2, 0);

	}

}
