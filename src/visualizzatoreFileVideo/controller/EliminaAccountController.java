package visualizzatoreFileVideo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import visualizzatoreFileVideo.model.GestisciDB;
import visualizzatoreFileVideo.model.GestoreScene;

public class EliminaAccountController implements Initializable{

	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private ImageView backgroundImage;
	
    @FXML
    private TextField passwordTextField;

    @FXML
    private Button deleteButton;
    
    @FXML
    private Button retryButton;
    
    @FXML
    private Label missYouLabel;


    @FXML
    void deleteButtonClick(ActionEvent event) {
    		if(GestisciDB.getGestore().utenteEPasswordCorretti(LoginController.utenteCorrente, passwordTextField.getText())) {
    			GestisciDB.getGestore().eliminaUsername(LoginController.utenteCorrente);
    			GestoreScene.getGs().setLoginScene();
    		}
    		else {
    			GestoreScene.getGs().showError("Password not correct!");
    		}  	
    } 
    
    @FXML
    void retryButtonClick(ActionEvent event) {
    	GestoreScene.getGs().setVisualizzatoreScene();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		backgroundImage.setFitWidth(790);
		backgroundImage.setFitHeight(650);
		backgroundImage.setImage(new Image("/visualizzatoreFileVideo/img/sadStories.png"));
		backgroundImage.setOpacity(0.10);
		missYouLabel.setText("We'll miss you, " + LoginController.utenteCorrente);
		missYouLabel.setFont(new Font("Forte", 24));
		missYouLabel.setAlignment(Pos.CENTER);
	}

}