package visualizzatoreFileVideo;


import javafx.application.Application;
import javafx.stage.Stage;
import visualizzatoreFileVideo.model.GestoreScene;

public class Main extends Application{

	public static void main(String[] args)	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			GestoreScene.getGs().init(primaryStage);
		}
		catch(Exception e) {
			
		}
	}
}
