package tetris.controller;

import java.util.*;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.*;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import tetris.components.*;
import tetris.enumeration.ManagerMessage;

/**
 * Manager that control menu.
 */
public class MenuManager implements IObservable {

	protected ArrayList<IObserver> listeners;
	
	protected BorderPane pane;
	protected VBox menu;
	protected GridPane grid;
	protected Label lScoreL;
	protected Label lTimeL;
	protected Label lLevelL;
	protected Label lScoreV;
	protected Label lTimeV;
	protected Label lLevelV;
	protected Button bResume;
	protected Button bNew;
	protected Button bExit;
	
	public MenuManager(BorderPane pane) {
		this.listeners = new ArrayList<IObserver>();
		this.pane = pane;
		this.menu = new VBox();
		
		this.pane.heightProperty().addListener(resize);
		this.pane.widthProperty().addListener(resize);
		
		this.pane.setCenter(menu);
		this.menu.setAlignment(Pos.CENTER);
		
		this.grid = new GridPane();
		ColumnConstraints colCon = new ColumnConstraints();
		colCon.setPercentWidth(50);
		this.grid.getColumnConstraints().add(colCon);
		this.grid.getColumnConstraints().add(colCon);
		this.lScoreL = new Label("Score:");
		this.lTimeL = new Label("Duration:");
		this.lLevelL = new Label("Level:");
		this.lScoreV = new Label("0");
		this.lTimeV = new Label("00:00");
		this.lLevelV = new Label("0");
		
		GridPane.setHalignment(lTimeL, HPos.LEFT);
		this.grid.add(lTimeL, 0, 0);
		GridPane.setHalignment(lScoreL, HPos.LEFT);
		this.grid.add(lScoreL, 0, 1);
		GridPane.setHalignment(lLevelL, HPos.LEFT);
		this.grid.add(lLevelL, 0, 2);
		GridPane.setHalignment(lTimeV, HPos.RIGHT);
		this.grid.add(lTimeV, 1, 0);
		GridPane.setHalignment(lScoreV, HPos.RIGHT);
		this.grid.add(lScoreV, 1, 1);
		GridPane.setHalignment(lLevelV, HPos.RIGHT);
		this.grid.add(lLevelV, 1, 2);
		
		this.bResume = new Button("Resume");
		this.bNew = new Button("New");
		this.bExit = new Button("Exit");
		this.bResume.setOnAction(eResume);
		this.bNew.setOnAction(eNew);
		this.bExit.setOnAction(eExit);
	}
	
	protected InvalidationListener resize = new InvalidationListener() {
		@Override
		public void invalidated(Observable e) {
			updateSize();
		}
	};
	
	/**
	 * Switch to certain menu in depending on given {@link ManagerMessage}.
	 * @param mm Given switch signal. If null switch to new-exit menu.
	 */
	public void show(ManagerMessage mm) {
		menu.getChildren().clear();
		if ((mm != null) && (mm.equals(ManagerMessage.FAIL) || mm.equals(ManagerMessage.PAUSE))) {
			menu.getChildren().add(grid);
		}
		if ((mm != null) && mm.equals(ManagerMessage.PAUSE)) {
			menu.getChildren().add(bResume);
		}
		menu.getChildren().add(bNew);
		menu.getChildren().add(bExit);
	}
	
	/**
	 * Set information about current game for displaying on pause or fail.
	 * @param score current score.
	 * @param time current duration.
	 * @param level current level.
	 */
	public void setVal(int score, String time, int level) {
		this.lScoreV.setText(score+"");
		this.lTimeV.setText(time);
		this.lLevelV.setText(level+"");
	}
	
	/**
	 * Set size of all menu components.
	 */
	public void updateSize() {
		this.lScoreL.setFont(new Font(pane.getHeight()*0.05));
		this.lTimeL.setFont(new Font(pane.getHeight()*0.05));
		this.lLevelL.setFont(new Font(pane.getHeight()*0.05));
		this.lScoreV.setFont(new Font(pane.getHeight()*0.05));
		this.lTimeV.setFont(new Font(pane.getHeight()*0.05));
		this.lLevelV.setFont(new Font(pane.getHeight()*0.05));
		
		this.grid.setMinWidth(pane.getWidth()*0.30);
		this.grid.setMaxWidth(pane.getWidth()*0.30);
		
		this.bExit.setMinWidth(pane.getWidth()*0.30);
		this.bNew.setMinWidth(pane.getWidth()*0.30);
		this.bResume.setMinWidth(pane.getWidth()*0.30);
		this.bExit.setMaxWidth(pane.getWidth()*0.30);
		this.bNew.setMaxWidth(pane.getWidth()*0.30);
		this.bResume.setMaxWidth(pane.getWidth()*0.30);
	}
	
	/**
	 * Handler of Resume button.
	 */
	EventHandler<ActionEvent> eResume = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			notifyAllListeners(ManagerMessage.RESUME);
		}
	};
	
	/**
	 * Handler of New button.
	 */
	EventHandler<ActionEvent> eNew = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			notifyAllListeners(ManagerMessage.NEW);
		}
	};
	
	/**
	 * Handler of Exit button.
	 */
	EventHandler<ActionEvent> eExit = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			notifyAllListeners(ManagerMessage.EXIT);
		}
	};
	
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
