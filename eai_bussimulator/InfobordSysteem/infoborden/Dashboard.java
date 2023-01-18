package dashboard;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mockDatabaseLogger.ArrivaLogger;
import infoborden.Infobord;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import bussimulator.Runner;

public class Dashboard extends Application {
		
    private void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}
    
    private void startBord(String halte, String richting) {
		Infobord infobord = new Infobord(halte,richting);
		Platform.runLater(new Runnable() {
			public void run() {             
				infobord.start(new Stage());
			}
		});	    	
    }
	private void startAlles() {
		thread(new Runner(),false); 
	}

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		// Create a pane and set its properties
		TextField halteInput = new TextField("A-Z");
		TextField richting = new TextField("1 of -1");
		GridPane pane = createPane(halteInput, richting);
		pane = addButton(pane, halteInput, richting);
		Scene scene = new Scene(pane);
		primaryStage.setTitle("BusSimulatie control-Center");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public GridPane createPane(TextField halteInput, TextField richting) {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		pane.setHgap(5.5);
		pane.setVgap(5.5);
		pane.add(new Label("Halte:"), 0, 0);
		pane.add(halteInput, 1, 0);
		pane.add(new Label("Richting:"), 0, 1);
		pane.add(richting, 1, 1);
		return pane;
	}

	public GridPane addButton(GridPane pane, TextField halteInput, TextField richting) {
		Button btBord = new Button("Start Bord");
		btBord.setOnAction(e -> {startBord(halteInput.getText(), richting.getText());});
		Button btStart = new Button("Start");
		btStart.setOnAction( e -> {startAlles();});
		Button btLogger = new Button("Start Logger");
		btLogger.setOnAction( e -> {thread(new ArrivaLogger(), false);});
		pane.add(btBord, 1, 5);
		pane.add(btStart, 2, 5);
		pane.add(btLogger, 3, 5);
		GridPane.setHalignment(btBord, HPos.LEFT);
		GridPane.setHalignment(btStart, HPos.LEFT);
		GridPane.setHalignment(btLogger, HPos.LEFT);
		return pane;
	}
} 