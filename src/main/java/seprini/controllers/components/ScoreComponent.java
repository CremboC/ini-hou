package seprini.controllers.components;

public class ScoreComponent {
	private int score;

	public ScoreComponent() {

	}

	/**
	 * Adds argument value to the score.
	 * 
	 * @param d
	 */
	public void incrementScore(double d) {
		score += d;
	}

	public void decrementScore(double d) {
		score -= d;
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
