package tetris.components;

import java.util.Random;

import tetris.enumeration.FigureType;
import tetris.model.Figure;

public class FigureFactory {
	
	protected static FigureFactory instance = null;
	protected Random rand;
	protected ResourceCenter res;
	
	protected FigureFactory() {
		this.rand = new Random();
		this.res = ResourceCenter.getInstance();
	}
	
	public static FigureFactory getInstance() {
		if (instance == null) {
			instance = new FigureFactory();
		}
		return instance;
	}
	
	public Figure getFigure() {
		Figure fig;
		switch (rand.nextInt(7)) {
			case 0: fig = new Figure(FigureType.BLOCK, getColor(), new Coord(5, 0)); break;
			case 1: fig = new Figure(FigureType.LINE, getColor(), new Coord(5, 0)); break;
			case 2: fig = new Figure(FigureType.GU, getColor(), new Coord(5, 0)); break;
			case 3: fig = new Figure(FigureType.IGU, getColor(), new Coord(5, 0)); break;
			case 4: fig = new Figure(FigureType.CROWN, getColor(), new Coord(5, 0)); break;
			case 5: fig = new Figure(FigureType.LIGHTNING, getColor(), new Coord(5, 0)); break;
			case 6: fig = new Figure(FigureType.ILIGHTNING, getColor(), new Coord(5, 0)); break;
			default: fig = null;
		}
		return fig;
	}
	
	public Figure getShadow(Figure fig) {
		return new Figure(fig.getType(), res.getColorIndex(res.getShadowColor()), new Coord(5, 0));
	}
	
	public int getColor() {
		return 1+rand.nextInt(res.getColorRange()-2);
	}
	
}
