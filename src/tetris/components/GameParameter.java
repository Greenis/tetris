package tetris.components;

/**
 * Describe parameter of game model Tetris.
 */
public class GameParameter {
	
	/** Height of board. */
	protected int boardWidth;
	/** Width of board. */
	protected int boardHeight;
	/** Basic line price which user gain for each line on 1 level. */
	protected int basicLinePrice;
	/** Basic level need which user must reach to up level. */
	protected int basicLevelNeed;
	/** Factor for level need which production give level need for next level. */
	protected double levelNeedFactor;
	/** Factor speed for speed which production give speed for next level. */
	protected double speedFactor;
	
	/**
	 * Create the set of parameters. State of game is pause.
	 */
	public GameParameter(int height, int width, int basicLinePrice, int basicLevelNeed, double levelNeedFactor, double speedFactor) {
		this.boardHeight = height;
		this.boardWidth = width;
		this.basicLinePrice = basicLinePrice;
		this.basicLevelNeed =  basicLevelNeed;
		this.levelNeedFactor = levelNeedFactor;
		this.speedFactor = speedFactor;
	}
	
	/** @return Height of board. */
	public int getHeight() {
		return this.boardHeight;
	}
	
	/** @return Width of board. */
	public int getWidth() {
		return this.boardWidth;
	}
	
	/** @return Basic line price which user gain for each line on 1 level. */
	public int getBasicLinePrice() {
		return this.basicLinePrice;
	}
	
	/** @return Basic level need which user must reach to up level. */
	public int getBasicLevelNeed() {
		return this.basicLevelNeed;
	}
	
	/** @return Factor for level need which production give level need for next level. */
	public double getLevelNeedFactor() {
		return this.levelNeedFactor;
	}
	
	/** @return Factor speed for speed which production give speed for next level. */
	public double getSpeedFactor() {
		return speedFactor;
	}
	
}
