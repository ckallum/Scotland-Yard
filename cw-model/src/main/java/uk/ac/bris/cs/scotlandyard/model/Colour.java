package uk.ac.bris.cs.scotlandyard.model;

/**
 * Allowed colour of the players in Scotland Yard game
 */
public enum Colour {
	BLACK, BLUE, GREEN, RED, WHITE, YELLOW;

	/**
	 * Checks whether a colour is Mr.X
	 * 
	 * @return true if colour is Mr.X otherwise false
	 */
	public boolean isMrX() {
		return this == BLACK;
	}

	/**
	 * Checks whether a colour is a detective
	 * 
	 * @return true if colour is a detective otherwise false
	 */
	public boolean isDetective() {
		return !isMrX();
	}

}
