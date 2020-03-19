package tetris.enumeration;

/**
 * Figure type enumeration. It contains bitmap of figure, its width and height. Each figure has all parameter for 'standard' rotation mode.
 */
public enum FigureType {

	BLOCK("block"),
	LINE("line"),
	GU("gu"),
	IGU("igu"),
	CROWN("crown"),
	LIGHTNING("lightning"),
	ILIGHTNING("ilightning");
	
	protected int[][] figure;
	protected int width;
	protected int height;
	
	private FigureType(String type) {
		switch (type) {
			case "block": {
				this.width = 2;
				this.height = 2;
				this.figure = new int[height][width];
				this.figure[0][0] = 1;
				this.figure[0][1] = 1;
				this.figure[1][0] = 1;
				this.figure[1][1] = 1;
				break;
			}
			case "line": {
				this.width = 1;
				this.height = 4;
				this.figure = new int[height][width];
				this.figure[0][0] = 1;
				this.figure[1][0] = 1;
				this.figure[2][0] = 1;
				this.figure[3][0] = 1;
				break;
			}
			case "gu": {
				this.width = 2;
				this.height = 3;
				this.figure = new int[height][width];
				this.figure[0][0] = 1;
				this.figure[0][1] = 1;
				this.figure[1][0] = 1;
				this.figure[1][1] = 0;
				this.figure[2][0] = 1;
				this.figure[2][1] = 0;
				break;
			}
			case "igu": {
				this.width = 2;
				this.height = 3;
				this.figure = new int[height][width];
				this.figure[0][0] = 1;
				this.figure[0][1] = 1;
				this.figure[1][0] = 0;
				this.figure[1][1] = 1;
				this.figure[2][0] = 0;
				this.figure[2][1] = 1;
				break;
			}
			case "crown": {
				this.width = 3;
				this.height = 2;
				this.figure = new int[height][width];
				this.figure[0][0] = 0;
				this.figure[0][1] = 1;
				this.figure[0][2] = 0;
				this.figure[1][0] = 1;
				this.figure[1][1] = 1;
				this.figure[1][2] = 1;
				break;
			}
			case "lightning": {
				this.width = 3;
				this.height = 2;
				this.figure = new int[height][width];
				this.figure[0][0] = 1;
				this.figure[0][1] = 1;
				this.figure[0][2] = 0;
				this.figure[1][0] = 0;
				this.figure[1][1] = 1;
				this.figure[1][2] = 1;
				break;
			}
			case "ilightning": {
				this.width = 3;
				this.height = 2;
				this.figure = new int[height][width];
				this.figure[0][0] = 0;
				this.figure[0][1] = 1;
				this.figure[0][2] = 1;
				this.figure[1][0] = 1;
				this.figure[1][1] = 1;
				this.figure[1][2] = 0;
				break;
			}
		}
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int[][] getFigureMap() {
		return this.figure;
	}
	
}
