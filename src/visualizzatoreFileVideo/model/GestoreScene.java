package visualizzatoreFileVideo.model;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;



public class GestoreScene {
	
	private Scene scene;
	private Stage stage;
	private static GestoreScene gs=null;
	
	public static GestoreScene getGs() {
		if(gs==null)
			gs= new GestoreScene();
		return gs;
	}
	
	public void init(Stage pStage) {
		stage=pStage;
		initScene("GraficaLogin.fxml");
		stage.setScene(scene);
		stage.setTitle("Medio Player");
		stage.setResizable(false);
		stage.show();
	}
	
	public void initScene(String fileFxml) {
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizzatoreFileVideo/view/"+fileFxml));
			Parent root = (Parent) loader.load();
			scene = new Scene(root, 600, 500);
		}
		catch(IOException e) {
			showError("ERROR 001");
		}
	}
	
	public void setLoginScene() {
			setCurrentScene("GraficaLogin.fxml");
			stage.hide();
			stage.setWidth(600);
			stage.setHeight(400);
			stage.setResizable(false);
			stage.show();
	}
	
	public void setVisualizzatoreScene() {	
			setCurrentScene("GraficaVisualizzatore.fxml");
			stage.hide();
			stage.setWidth(800);
			stage.setHeight(600);
			stage.setResizable(true);
			stage.setMinWidth(800);
			stage.setMinHeight(600);
			stage.show();
	}
	
	public void setRegistrazione() {
		setCurrentScene("GraficaRegistrazione.fxml");
		stage.hide();
		stage.setWidth(550);
		stage.setHeight(700);
		stage.setResizable(false);
		stage.show();
	}
	
	public void setPlaylist() {
		setCurrentScene("GraficaPlaylist.fxml");
		stage.hide();
		stage.setWidth(700);
		stage.setHeight(500);
		stage.setResizable(true);
		stage.show();
	}
	
	public void setAddPlaylist() {
		setCurrentScene("GraficaAddPLaylist.fxml");
		stage.setWidth(400);
		stage.setHeight(600);
		stage.setResizable(false);
		stage.show();
	}
	
	public void setEliminaAccount() {
		setCurrentScene("GraficaEliminaAccount.fxml");
		stage.hide();
		stage.setWidth(500);
		stage.setHeight(400);
		stage.setResizable(false);
		stage.show();
	}

	public void setEditPlaylist() {
		setCurrentScene("GraficaEditPlaylist.fxml");
		stage.hide();
		stage.setWidth(300);
		stage.setHeight(500);
		stage.setResizable(false);
		stage.show();
	}
	
	public void setCurrentScene(String fileFxml) {
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualizzatoreFileVideo/view/"+fileFxml));
	    	Parent root = (Parent) loader.load();
	    	scene.setRoot(root);
		}
		catch(IOException e) {
			showError("ERROR 002");
			e.printStackTrace();
		}
	}
	
	public void showError(String message) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("");
		alert.setContentText(message);
		alert.show();
    }
	
	public void showWarning(String message) {
    	Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText("");
		alert.setContentText(message);
		alert.show();
    }

	public boolean showConfirmation(String message) {
		Alert al= new Alert(AlertType.CONFIRMATION);
		al.setTitle("Confirm");
		al.setHeaderText("");
		al.setContentText(message);
		al.showAndWait();
		if(al.getResult()==ButtonType.OK)
			return true;
		return false;
	}

	public void resize(int width, int height) {
		
		stage.setWidth(width);
		stage.setHeight(height);
		stage.setMinWidth(width);
		stage.setMinHeight(height);
		
	}

}
