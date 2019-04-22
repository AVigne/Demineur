package Tests;

import UI.DemineurUi;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application {
    Stage stage;
    Scene scene;

    int initialX;
    int initialY;

    @Override
    public void start(Stage s) throws Exception {
        new DemineurUi(s);
    }
	
    public static void main(String[] args) { 
    	launch(args); 
    } 
}