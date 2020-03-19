package tetris.model;

import tetris.components.Coord;
import tetris.enumeration.FigureRotation;
import tetris.enumeration.FigureType;

public class Figure {

	protected FigureType type;
	protected int color;
	protected Coord coord;
	protected FigureRotation rot;
	
	public Figure(FigureType type) {
		this.type = type;
		this.rot = FigureRotation.STANDART;
	}
	
	public Figure(FigureType type, int color) {
		this.type = type;
		this.rot = FigureRotation.STANDART;
		this.color = color;
	}
	
	public Figure(FigureType type, int color, Coord coord) {
		this.type = type;
		this.rot = FigureRotation.STANDART;
		this.color = color;
		this.coord = coord;
	}
	
	public FigureType getType() {
		return this.type;
	}
	
	public int getWidth() {
		return getWidth(rot);
	}
	
	public int getWidth(FigureRotation rot) {
		switch (rot) {
			case ISTANDART: return this.type.getWidth();
			case LEFTSIDE: return this.type.getHeight();
			case RIGHTSIDE: return this.type.getHeight();
			default: return this.type.getWidth();
		}
	}
	
	public int getHeight() {
		return getHeight(rot);
	}
	
	public int getHeight(FigureRotation rot) {
		switch (rot) {
			case ISTANDART: return this.type.getHeight();
			case LEFTSIDE: return this.type.getWidth();
			case RIGHTSIDE: return this.type.getWidth();
			default: return this.type.getHeight();
		}
	}
	
	public int[][] getFigureMap() {
		return getNormalFigureMap(rot);
	}
	
	public int[][] getNormalFigureMap(FigureRotation rot) {
		int height = type.getHeight(),
				width = type.getWidth();
		int[][] fig = new int[height][width];
		int[][] figure = type.getFigureMap();
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				fig[i][j] = figure[i][j];
		switch (rot) {
			case ISTANDART:
				int temp;
				for (int y = 0; y < height; y++)
					for (int j = 0; j < width/2; j++) {
						temp = fig[y][j];
						fig[y][j] = fig[height-1-y][width-1+j];
						fig[height-1-y][width-1+j] = temp;
					}
				break;
			case LEFTSIDE:
				int[][] figureL = new int[width][height];
				for (int y = 0; y < height; y++)
					for (int x = 0; x < width; x++) {
						figureL[width-1-x][y] = fig[y][x];
					}
				fig = figureL;
				break;
			case RIGHTSIDE:
				int[][] figureR = new int[width][height];
				for (int y = 0; y < height; y++)
					for (int x = 0; x < width; x++) {
						figureR[x][height-1-y] = fig[y][x];
					}
				fig = figureR;
				break;
			default: break;
		}
		return fig;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public Coord getCoord() {
		return this.coord;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void setCoord(Coord coord) {
		this.coord = coord;
	}
	
	public void turnLeft() {
		this.rot = this.rot.turnLeft();
	}
	
	public void turnRight() {
		this.rot = this.rot.turnRight();
	}
	
	public FigureRotation getFigureRotation() {
		return this.rot;
	}
	
	public void setFigureRotation(FigureRotation rot) {
		this.rot = rot;
	}
	
	public String toString() {
		return type.toString()+"("+coord.getX()+", "+coord.getY()+")";
	}
}
