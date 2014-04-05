package seprini.controllers.components;

public class ScoreComponent {
	private int score;

	public ScoreComponent() {

	}

	/**
	 * Adds argument value to the score.
	 * 
	 * @param value
	 */
	public void incrementScore(int value) {
		score += value;
	}

	/**
	 * Gets this score
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}
}
