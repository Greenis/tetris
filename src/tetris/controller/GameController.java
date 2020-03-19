package tetris.controller;

import java.util.ArrayList;

import javafx.animation.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.util.*;
import tetris.components.*;
import tetris.enumeration.GameLifeCircle;
import tetris.enumeration.GameMove;
import tetris.enumeration.ManagerMessage;
import tetris.model.*;
import tetris.view.*;

/**
 * Main general game manager. It has game model and game view. Current game model is projected on current game view.
 */
public class GameController implements IObserver, IObservable {

	protected ArrayList<IObserver> listeners;
	
	protected GameModel game;
	protected GameView view;
	
	protected EventHandler<KeyEvent> keyHandler;
	protected Timeline timer;
	
	public GameController(GameView view, GameModel model) {
		this.listeners = new ArrayList<IObserver>();
		this.view = view;
		this.game = model;
		this.game.addListener(this);
		this.view.setController(this.game);
		this.timer = new Timeline(new KeyFrame(Duration.millis(model.getSpeed()), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				game.move(GameMove.DOWN);
			}
		}));
		this.timer.setCycleCount(Timeline.INDEFINITE);
		this.keyHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (game.getLyfeCircle().equals(GameLifeCircle.RUNNING)) {
					if (e.getCode() == KeyCode.LEFT) {
						game.move(GameMove.LEFT);
					}
					else if (e.getCode() == KeyCode.RIGHT) {
						game.move(GameMove.RIGHT);
					}
					else if (e.getCode() == KeyCode.DOWN) {
						game.move(GameMove.DOWN);
					}
					else if (e.getCode() == KeyCode.SPACE) {
						game.move(GameMove.FALL);
					}
					else if ((e.getCode() == KeyCode.SHIFT) || (e.getCode() == KeyCode.UP)) {
						game.move(GameMove.ROTATE);
					}
					else if (e.getCode() == KeyCode.ESCAPE) {
						pause();
						notiftAllListeners(ManagerMessage.PAUSE);
					}
				}
			}
		};
	}
	
	public void start() {
		this.game.setLyfeCircle(GameLifeCircle.RUNNING);
		this.timer.play();
		this.view.paint();
		this.view.paint();
		this.view.paint();
		this.view.paint();
	}
	
	public void stop() {
		this.game.setLyfeCircle(GameLifeCircle.STOPPED);
		this.timer.stop();
		
	}
	
	public void pause() {
		this.game.setLyfeCircle(GameLifeCircle.PAUSE);
		this.timer.stop();
	}
	
	public EventHandler<KeyEvent> getKeyHandler() {
		return this.keyHandler;
	}
	
	public GameModel getGameModel() {
		return this.game;
	}
	
	public void draw() {
		view.paint();
	}
	
	protected void notiftAllListeners(ManagerMessage st) {
		for (IObserver obs : listeners) {
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

	@Override
	public void notify(IObservable obj, ManagerMessage s) {
		if (s.equals(ManagerMessage.DRAW)) {
			draw();
		}
		else {
			notiftAllListeners(s);
		}
	}
	
}
