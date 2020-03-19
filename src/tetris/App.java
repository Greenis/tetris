package tetris;

import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import tetris.controller.*;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Tetris");
		Group root = new Group();
		Scene scene = new Scene(root, 800, 400);
		stage.setScene(scene);
		stage.show();
		SceneManager scmanager = new SceneManager(stage, scene, root);
		stage.setOnCloseRequest(scmanager.getOnCloseWindow());
		scmanager.launch();
	}

}
