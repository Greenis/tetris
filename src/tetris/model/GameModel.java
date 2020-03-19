package tetris.model;

import java.util.*;

import tetris.components.*;
import tetris.enumeration.GameLifeCircle;
import tetris.enumeration.GameMove;
import tetris.enumeration.ManagerMessage;

/**
 * Class describes model of game Tetris.
 * @author Alexander.Natareev
 */
public class GameModel implements IObservable {
	
	protected ArrayList<IObserver> listeners;
	
	protected FigureFactory figureFactory;
	protected GameParameter params;

	/** Cell has 0, if block is empty, otherwise it has index of color. */
	protected int[][] field;
	
	/** Current figure. User control this figure. */
	protected Figure main;
	/** Next figure which will be main when current will be fixated. */
	protected Figure next;
	
	protected Figure shadow;
	
	/** Gained score in the game. */
	protected int score;
	
	/** State of this game. One of three states: stop, pause, running. */
	protected GameLifeCircle state;
	
	/** Reached level in the game. */
	protected int level;
	/** Required number of removed lines to next level. */
	protected int levelNeed;
	/** Remains number of lines for next level. */
	protected int levelRemaing;
	
	/** Speed of falling figure. */
	protected double speed;
	/** How long this game go on. */
	protected long duration;
	/** Last check of game duration. */
	protected long lastWatch;
	
	/**
	 * Construct new game.
	 * @param params - parameters of game, such as height and width of table, line cost and so on. {@link GameParameter}}.
	 * @param speed - start speed of falling figure.
	 */
	public GameModel(GameParameter params, double speed) {
		this.params = params;
		this.figureFactory = FigureFactory.getInstance();
		this.params = params;
		this.field = new int[this.params.getHeight()][this.params.getWidth()];
		for (int i = 0; i < params.getHeight(); i++)
			Arrays.fill(field[i], 0);
		this.score = 0;
		this.state = GameLifeCircle.INITIALIZED;
		this.level = 1;
		this.levelNeed = params.getBasicLevelNeed();
		this.levelRemaing = levelNeed;
		this.speed = speed;
		this.duration = 0;
		this.lastWatch = 0;
		
		this.main = figureFactory.getFigure();
		this.shadow = figureFactory.getShadow(this.main);
		this.next = figureFactory.getFigure();
		setShadow();
		this.listeners = new ArrayList<IObserver>();
	}
	
	/**
	 * Determines could be or not current figure move in input direction.
	 * @param direction - checking direction.
	 * @return - true, if figure can move in input direction, false - otherwise.
	 */
	protected boolean canMove(GameMove direction) {
		switch (direction) {
			case DOWN: return canBe(main.getCoord().getX(), main.getCoord().getY()+1);
			case LEFT: return canBe(main.getCoord().getX()-1, main.getCoord().getY());
			case RIGHT: return canBe(main.getCoord().getX()+1, main.getCoord().getY());
			case ROTATE:
				main.turnLeft();
				boolean r = canBe(main.getCoord().getX(), main.getCoord().getY());
				main.turnRight();
				return r;
			default: return false;
		}
	}
	
	/**
	 * Determines could be or not current figure stand on input coordinate (x1, y1).
	 * @param x1 - column
	 * @param y1 - row
	 * @return
	 */
	protected boolean canBe(int x1, int y1) {
		int[][] figure = main.getFigureMap();
		int height = figure.length,
				width = figure[0].length;
		// Rotation doing with respect to left bottom eagle of main figure. Therefore checking right bound of board.
		if (x1+width > params.getWidth())
			return false;
		// and left bound.
		else if (x1 < 0)
			return false;
		// Check that figure has not crossed bottom bound of board.
		if (y1 >= params.getHeight())
			return false;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// If figure has just been created then its part lies higher of upper bound of board. Pass this line.
				if (y1-height+1+y < 0)
					break;
				// If board at this cell is not empty and bitmap of figure also is not empty then its mean that figure could not stand here.
				else if ((field[y1+1-height+y][x1+x] > 0) && (figure[y][x] == 1))
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Move if it possible main figure in input direction. If {@code direction} equals to Down or Fall and it could not be, then new figure generated and next will main.
	 * @param direction - direction in which main figure will be moved.
	 */
	public void move(GameMove direction) {
		switch (direction) {
			case DOWN:
				if (canMove(GameMove.DOWN)) {
					main.getCoord().setY(main.getCoord().getY()+1);
					setShadow();
					notifyAllListeners(ManagerMessage.DRAW);
				}
				// Generate new figure.
				else {
					fixate();
					getFigures();
					if (canMove(GameMove.DOWN)) {
						move(GameMove.DOWN);
						setShadow();
						notifyAllListeners(ManagerMessage.DRAW);
					}
					else {
						// New figure could not step. Game over.
						setLyfeCircle(GameLifeCircle.STOPPED);
						notifyAllListeners(ManagerMessage.FAIL);
					}
				}
				break;
			case LEFT:
				if (canMove(GameMove.LEFT)) {
					main.getCoord().setX(main.getCoord().getX()-1);
					setShadow();
					notifyAllListeners(ManagerMessage.DRAW);
				}
				break;
			case RIGHT:
				if (canMove(GameMove.RIGHT)) {
					main.getCoord().setX(main.getCoord().getX()+1);
					setShadow();
					notifyAllListeners(ManagerMessage.DRAW);
				}
				break;
			case ROTATE:
				if (canMove(GameMove.ROTATE)) {
					main.turnLeft();
					setShadow();
					notifyAllListeners(ManagerMessage.DRAW);
				}
				break;
			case FALL:
				for (; canBe(main.getCoord().getX(), main.getCoord().getY()); main.getCoord().setY(main.getCoord().getY()+1)) { }
				main.getCoord().setY(main.getCoord().getY()-1);
				fixate();
				getFigures();
				notifyAllListeners(ManagerMessage.DRAW);
				move(GameMove.DOWN);
				break;
		default: return;
		}
	}
	
	protected void setShadow() {
		shadow.setFigureRotation(main.getFigureRotation());
		shadow.getCoord().set(main.getCoord().getX(), main.getCoord().getY());
		for (; canBe(shadow.getCoord().getX(), shadow.getCoord().getY()); shadow.getCoord().setY(shadow.getCoord().getY()+1)) { }
		shadow.getCoord().setY(shadow.getCoord().getY()-1);
	}
	
	/**
	 * Write main figure in board.
	 */
	protected void fixate() {
		int[][] figure = main.getFigureMap();
		int height = figure.length,
				width = figure[0].length;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if ((main.getCoord().getY()-height+1+y > -1) && (figure[y][x] != 0))
					field[main.getCoord().getY()-height+1+y][main.getCoord().getX()+x] = main.getColor();
			}
		}
		// Remove filled lines and count the gained score.
		removeLines();
	}
	
	/**
	 * Remove full lines and update score.
	 */
	protected void removeLines() {
		int y1 = main.getCoord().getY(),
				h = main.getHeight(),
				w = this.params.getWidth();
		boolean[] full = new boolean[h];
		int re = 0;
		
		// Find filled lines, which could not be longer than height of last figure.
		Arrays.fill(full, true);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (y1-y > -1)
					if (field[y1-y][x] == 0) {
						full[y] = false;
						break;
					}
			}
			// Count of founded lines.
			if (full[y]) {
				re++;
			}
		}
		if (re == 0)
			return;
		// Make list of remains lines that lies higher coordinated of figure.
		Queue<int[]> q = new LinkedList<int[]>();
		for (int i = y1; i > -1; i--) {
			if (((i-(y1-h+1) >= 0)) && !full[h-1-(i-(y1-h+1))]) {
				q.add(field[i]);
			} else if (i-(y1-h+1) < 0)
				q.add(field[i]);
		}
		int empty = y1+1-q.size();
		// Update board.
		for (int i = y1; !q.isEmpty(); i--) {
			field[i] = q.poll();
		}
		// Fill new empty upper lines.
		for (int i = 0; i < empty; i++) {
			int[] t = new int[params.getWidth()];
			Arrays.fill(t, 0);
			field[i] = t;
		}
		incScore(re);
	}
	
	/**
	 * Set as main next figure. Also generate new next figure, and its color.
	 */
	protected void getFigures() {
		main = next;
		shadow = figureFactory.getShadow(main);
		next = figureFactory.getFigure();
	}
	
	/**
	 * @return Parameters of this model.
	 * @see GameParameter
	 */
	public GameParameter getParams() {
		return this.params;
	}
	
	/**
	 * Calculate score, if applying increment level, increase falling block speed. 
	 * @param lines - number of removable lines.
	 */
	public void incScore(int lines) {
		// New level has achieved.
		if (levelRemaing - lines < 1) {
			score += level*params.getBasicLinePrice()*levelRemaing;
			level++;
			levelNeed = (int) (levelNeed*params.getBasicLevelNeed());
			incScore(Math.abs(levelRemaing-lines));
			levelRemaing = levelNeed;
			speed *= params.getSpeedFactor();
		}
		else {
			score += level*params.getBasicLinePrice()*lines;
			levelRemaing -= lines;
		}
	}
	
	public GameLifeCircle getLyfeCircle() {
		return this.state;
	}
	
	public void setLyfeCircle(GameLifeCircle istate) {
		if ((this.state == GameLifeCircle.RUNNING) & (istate != GameLifeCircle.RUNNING)) {
			calculateDuratation();
		}
		else if ((this.state != GameLifeCircle.RUNNING) & (istate == GameLifeCircle.RUNNING)) {
			this.lastWatch = System.currentTimeMillis();
		}
		this.state = istate;
	}
	
	/** @return Gained score by game. */
	public int getScore() {
		return this.score;
	}
	
	/** @return Reached level by game. */
	public int getLevel() {
		return this.level;
	}
	
	/** @return Required number of removed lines to next level. */
	public int getLevelNeed() {
		return this.levelNeed;
	}
	
	/** Remains number of lines for next level. */
	public int getLevelRemaing() {
		return this.levelRemaing;
	}
	
	/** @return Speed of falling of main figure at this level. */
	public double getSpeed() {
		return this.speed;
	}
	
	/** @return Duration of game in format HH:MM:SS.*/
	public String getDuratation() {
		if (state.equals(GameLifeCircle.RUNNING)) {
			calculateDuratation();
		}
		return String.format("%02d:%02d:%02d", (this.duration / (1000 * 60 * 60)) % 24, (this.duration / (1000 * 60)) % 60, ((this.duration / 1000)% 60));
	}
	
	/** Increment game duration on value passed from {@code lastWatch} call. */
	protected void calculateDuratation() {
		long time = System.currentTimeMillis();
		this.duration += time-this.lastWatch;
		this.lastWatch = time;
	}
	
	/** @return Copy of board.*/
	public int[][] getField() {
		int[][] r;
		r = new int[params.getHeight()][params.getWidth()];
		for (int i = 0; i < params.getHeight(); i++) {
			for (int j = 0; j < params.getWidth(); j++)
				r[i][j] = field[i][j];
		}
		return r;
	}
	
	/** @return Main figure of model. */
	public Figure getMainFigure() {
		return this.main;
	}
	
	/** @return Next figure of model. */
	public Figure getNextFigure() {
		return this.next;
	}
	
	public Figure getShadowFigure() {
		return this.shadow;
	}
	
	protected void notifyAllListeners(ManagerMessage st) {
		for (IObserver obs : this.listeners) {
			obs.notify(this, st);
		}
	}

	@Override
	public void addListener(IObserver listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(IObserver listener) {
		this.listeners.remove(listener);
	}
	
}
