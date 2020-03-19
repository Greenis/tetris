package tetris.controller;

import javafx.beans.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.*;
import tetris.components.*;
import tetris.enumeration.*;
import tetris.model.*;
import tetris.view.*;

/**
 * General manager of application. Switch scene in depending on switch signal sending by any scene or component game.
 */
public class SceneManager implements IObserver {
	
	protected Stage stage;
	protected Scene scene;
	protected Group root;
	
	/** Canvas for drawing game view. */
	protected Canvas canvas;
	protected GameController game;
	/** Panel for menu items. */
	protected BorderPane grid;
	protected MenuManager menu;
	
	protected EventHandler<WindowEvent> onCloseWindow = new EventHandler<WindowEvent>() {
		@Override
		public void handle(WindowEvent event) {
			try {
				game.stop();
			}
			catch (NullPointerException e) { }
		}
	};
	
	protected InvalidationListener resize = new InvalidationListener() {
		@Override
		public void invalidated(Observable e) {
			updateSize();
		}
	};
	
	public SceneManager(Stage stage, Scene scene, Group group) {
		this.stage = stage;
		this.scene = scene;
		this.root = group;
		this.canvas = new Canvas(800, 400);
		this.root.getChildren().add(canvas);
		this.grid = new BorderPane();
		this.menu = new MenuManager(grid);
		this.menu.addListener(this);
		scene.heightProperty().addListener(resize);
		scene.widthProperty().addListener(resize);
	}
	
	/**
	 * Handler is setting sizes of scenes.
	 */
	protected void updateSize() {
		canvas.setHeight(scene.getHeight());
		canvas.setWidth(scene.getWidth());
		grid.setMinHeight(scene.getHeight());
		grid.setMaxHeight(scene.getHeight());
		grid.setMinWidth(scene.getWidth());
		grid.setMaxWidth(scene.getWidth());
		try {
			if (game.getGameModel().getLyfeCircle() == GameLifeCircle.RUNNING) {
				game.draw();
				game.draw();
				game.draw();
				game.draw();
			}
		}
		catch (NullPointerException e) { }
	}
	
	/**
	 * Give scene control to this instance. In full-screen mode show main menu with buttons "New game" and "Exit"
	 */
	public void launch() {
		this.root.getChildren().add(grid);
		this.menu.show(null);
		this.stage.setFullScreenExitHint("");
		this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		this.stage.setFullScreen(true);
		updateSize();
	}
	
	/**
	 * Switch layout in depending on given {@link ManagerMessage}.
	 * @param mm Switch signal.
	 */
	protected void changeLayout(ManagerMessage mm) {
		this.scene.setOnKeyPressed(null);
		this.root.getChildren().clear();
		switch (mm) {
			case PAUSE:
				game.pause();
				this.root.getChildren().clear();
				this.menu.setVal(game.game.getScore(), game.game.getDuratation(), game.game.getLevel());
				this.root.getChildren().add(grid);
				menu.show(mm);
				break;
			case RESUME:
				this.scene.setOnKeyPressed(game.getKeyHandler());
				root.getChildren().clear();
				root.getChildren().add(canvas);
				game.start();
				break;
			case FAIL:
				game.stop();
				this.root.getChildren().clear();
				this.menu.setVal(game.game.getScore(), game.game.getDuratation(), game.game.getLevel());
				this.root.getChildren().add(grid);
				menu.show(mm);
				break;
			case EXIT:
				onCloseWindow.handle(null);
				stage.close();
				break;
			case NEW:
				this.game = new GameController(new GameView(canvas), new GameModel(new GameParameter(22, 10, 10, 10, 1.0, 0.91), 1000));
				this.game.addListener(this);
				this.scene.setOnKeyPressed(game.getKeyHandler());
				root.getChildren().clear();
				root.getChildren().add(canvas);
				game.start();
				break;
			default:
				return;
		}
		updateSize();
	}
	
	/**
	 * @return Handler controlling exit behavior.
	 */
	public EventHandler<WindowEvent> getOnCloseWindow() {
		return this.onCloseWindow;
	}

	@Override
	public void notify(IObservable obj, ManagerMessage s) {
		changeLayout(s);
	}
}
