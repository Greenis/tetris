package tetris.enumeration;

/**
 * All possible figure situation. Default situation is STANDART.
 */
public enum FigureRotation {

	STANDART,
	LEFTSIDE,
	RIGHTSIDE,
	ISTANDART;
	
	/**
	 * @return Figure rotation which can be achieved after left turning from current state.
	 */
	public FigureRotation turnLeft() {
		switch (this) {
			case STANDART: return LEFTSIDE;
			case LEFTSIDE: return ISTANDART;
			case RIGHTSIDE: return STANDART;
			case ISTANDART: return RIGHTSIDE;
			default: return null;
		}
	}
	
	/**
	 * @return Figure rotation which can be achieved after right turning from current state.
	 */
	public FigureRotation turnRight() {
		switch (this) {
			case STANDART: return RIGHTSIDE;
			case LEFTSIDE: return STANDART;
			case RIGHTSIDE: return ISTANDART;
			case ISTANDART: return LEFTSIDE;
			default: return null;
		}
	}
	
}
