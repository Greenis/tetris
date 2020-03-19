package tetris.view;

import javafx.scene.canvas.*;
import javafx.scene.text.*;
import tetris.components.Coord;
import tetris.components.ResourceCenter;
import tetris.model.*;

/**
 * Class is responsible for painting visual component of game. This object must be linked with {@link GameModel} object containing information for drawing.
 */
public class GameView {

	protected GameModel game;
	protected Canvas canvas;
	
	protected ResourceCenter res;
	
	protected double _block;
	protected double _lineWidth;
	protected double _cupBegX, _cupBegY;
	protected double _infoBegX, _infoBegY;
	
	public GameView(Canvas canvas) {
		this.canvas = canvas;
		this.res = ResourceCenter.getInstance();
	}
	
	/**
	 * Set {@link GameModel} for this view.
	 * @param game contain all info which must be painted.
	 */
	public void setController(GameModel game) {
		this.game = game;
	}
	
	/**
	 * Main methods for painting game view. Paint cup with figures, info plate and content of info plate.
	 */
	public synchronized void paint() {
		_block = (canvas.getHeight()-(_lineWidth*game.getParams().getHeight()))/game.getParams().getHeight();
		_lineWidth = Math.max(1, 0.075*_block);
		_cupBegY = 0;
		_cupBegX = canvas.getWidth()/2-game.getParams().getWidth()*_block*1.0/2-(game.getParams().getWidth()-1)*_lineWidth/2;
		
		GraphicsContext image = canvas.getGraphicsContext2D();

		paintBackground(image);
		paintBlocks(image);
		
		_infoBegX = 1.01*(canvas.getWidth()-_cupBegX+_lineWidth*game.getParams().getWidth()*0.5);
		_infoBegY = canvas.getHeight()/5;
		paintInfoPlate(image);
		paintInfo(image);
	}
	
	/**
	 * Draw figures in cup.
	 * @param image - {@see GraphicContext} of painted component.
	 */
	protected void paintBlocks(GraphicsContext image) {
		int[][] field = game.getField();
		//image.setStroke(Color.BLACK);
		image.setStroke(res.getForeground());
		image.setLineWidth(0);
		double dx = -_lineWidth, dy = -_lineWidth;
		for (int i = 0; i < game.getParams().getWidth(); i++) {
			dx += _lineWidth;
			dy = -_lineWidth;
			for (int j = 0; j < game.getParams().getHeight(); j++) {
				dy += _lineWidth;
				image.setFill(res.getColor(field[j][i]));
				//image.setFill(_colors.get(Math.abs(field[j][i])));
				image.fillRect(_cupBegX+i*_block+dx, _cupBegY+j*_block+dy, _block, _block);
			}
		}
//		int[][] figure = game.getMainFigure().getFigureMap();
//		int h = figure.length,
//				w = figure[0].length;
//		Coord coord = game.getMainFigure().getCoord();
//		int color = game.getMainFigure().getColor();
//		dx = _lineWidth;
//		dy = _lineWidth;
//		image.setStroke(res.getForeground());
//		for (int y = 0; y < h; y++) {
//			for (int x = 0; x < w; x++) {
//				image.setFill(res.getColor(color));
//				//image.setFill(_colors.get(Math.abs(color)));
//				if (figure[y][x] != 0)
//					image.fillRect(_cupBegX+(coord.getX()+x)*(_block+dx), _cupBegY+(coord.getY()-h+1+y)*(_block+dy), _block, _block);
//			}
//		}
		paintFigure(image, game.getShadowFigure());
		paintFigure(image, game.getMainFigure());
	}
	
	protected void paintFigure(GraphicsContext image, Figure fig) {
		int[][] figure = fig.getFigureMap();
		int h = figure.length,
				w = figure[0].length;
		Coord coord = fig.getCoord();
		int color = fig.getColor();
		double dx = _lineWidth,
				dy = _lineWidth;
		image.setStroke(res.getForeground());
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				image.setFill(res.getColor(color));
				if (figure[y][x] != 0)
					image.fillRect(_cupBegX+(coord.getX()+x)*(_block+dx), _cupBegY+(coord.getY()-h+1+y)*(_block+dy), _block, _block);
			}
		}
	}
	
	/**
	 * Paint background into screen.
	 * @param image {@link GraphicContext} of painted component.
	 */
	protected void paintBackground(GraphicsContext image) {
		//image.setFill(Color.WHITESMOKE);
		image.setFill(res.getBackground());
		image.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		//image.setFill(Color.BLACK);
		image.setFill(res.getForeground());
		image.fillRect(_cupBegX-_lineWidth, _cupBegY, game.getParams().getWidth()*_block+(game.getParams().getWidth()+1)*_lineWidth, canvas.getHeight());
	}
	
	/**
	 * Paint panel with extra info about game such as next figure, score and level.
	 * @param image {@link GraphicContext} of painted component.
	 */
	protected void paintInfoPlate(GraphicsContext image) {
		//image.setFill(Color.LIGHTGRAY);
		image.setFill(res.getEmpty());
		
		double dx = 6*_block;
		image.fillRect(_infoBegX, _infoBegY, dx, 6.5*_block);
		//image.setStroke(Color.BLACK);
		image.setStroke(res.getForeground());
		image.strokeLine(_infoBegX, _infoBegY, _infoBegX+dx, _infoBegY);
		image.strokeLine(_infoBegX, _infoBegY, _infoBegX, _infoBegY+6.5*_block);
		image.strokeLine(_infoBegX+dx, _infoBegY, _infoBegX+dx, _infoBegY+6.5*_block);
		image.strokeLine(_infoBegX, _infoBegY+6.5*_block, _infoBegX+dx, _infoBegY+6.5*_block);
	}
	
	/**
	 * Paint all extra info about game in InfoPlate
	 * @param image
	 */
	protected void paintInfo(GraphicsContext image) {
		//image.setFill(Color.BLACK);
		image.setFill(res.getBackground());
		image.setFont(new Font("Verdana", (int) _block-1));
		image.fillText("Level: "+this.game.getLevel(), _infoBegX+0.3*_block, _infoBegY+0.9*_block);
		image.fillText("Score: "+this.game.getScore(), _infoBegX+0.3*_block, _infoBegY+1.9*_block);
		
		//image.setFill(Color.LIGHTGRAY);
		//image.setStroke(Color.BLACK);
		image.setFill(res.getEmpty());
		image.setStroke(res.getForeground());
		image.setLineWidth(_lineWidth);
		int[][] figure = game.getNextFigure().getFigureMap();
		int w = game.getNextFigure().getWidth();
		int h = game.getNextFigure().getHeight();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (figure[y][x] != 0) {
					image.setFill(res.getColor(game.getNextFigure().getColor()));
					//image.setFill(_colors.get(game.getNextFigure().getColor()));
					image.fillRect(_infoBegX+1.5*_block+x*_block, _infoBegY+2.3*_block+y*_block, _block, _block);
					
					// upper horizontal
					image.strokeLine(_infoBegX+1.5*_block+x*_block,		_infoBegY+2.3*_block+y*_block,		_infoBegX+1.5*_block+(x+1)*_block,	_infoBegY+2.3*_block+y*_block);
					// left vertical
					image.strokeLine(_infoBegX+1.5*_block+x*_block,		_infoBegY+2.3*_block+y*_block,		_infoBegX+1.5*_block+x*_block,		_infoBegY+2.3*_block+(y+1)*_block);
					// right vertical
					image.strokeLine(_infoBegX+1.5*_block+(x+1)*_block,	_infoBegY+2.3*_block+y*_block,		_infoBegX+1.5*_block+(x+1)*_block,	_infoBegY+2.3*_block+(y+1)*_block);
					// lower horizontal
					image.strokeLine(_infoBegX+1.5*_block+x*_block,		_infoBegY+2.3*_block+(y+1)*_block,	_infoBegX+1.5*_block+(x+1)*_block,	_infoBegY+2.3*_block+(y+1)*_block);
				}
			}
		}
	}
	
}
