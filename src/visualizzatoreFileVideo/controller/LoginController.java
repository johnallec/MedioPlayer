package visualizzatoreFileVideo.controller;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import visualizzatoreFileVideo.model.GestisciDB;
import visualizzatoreFileVideo.model.GestoreScene;


public class LoginController implements Initializable{

	public static String utenteCorrente;
	
    @FXML
    private PasswordField passwordLogin;

    @FXML
    private Button loginButton;

    @FXML
    private Button signInButton;

    @FXML
    private TextField usernameLogin;
    
    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;
    
    @FXML
    private ImageView backgroundImage;
    
    @FXML
    private AnchorPane anchorPane;
    

    
    @FXML
    void loginButtonClick(ActionEvent event) {
    		if(GestisciDB.getGestore().utenteEPasswordCorretti(usernameLogin.getText(), passwordLogin.getText())) {
    			utenteCorrente = usernameLogin.getText(); 
    			GestoreScene.getGs().setVisualizzatoreScene();
    		}
    		else
    			GestoreScene.getGs().showError("USERNAME OR PASSWORD NOT CORRECT!");
    }
    
    @FXML
    void signInButtonClick(ActionEvent event) {
    	GestoreScene.getGs().setRegistrazione();
    }
   
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		backgroundImage.setFitWidth(anchorPane.getPrefWidth());
		backgroundImage.setFitHeight(anchorPane.getPrefHeight());
		backgroundImage.setImage(new Image("/visualizzatoreFileVideo/img/loginBackgroundImage.png"));
		GestoreScene.getGs().resize((int)anchorPane.getPrefWidth(), (int)anchorPane.getPrefHeight());
		
		//creo 2 tooltip su username e password
		usernameLogin.setOnMouseEntered(l->{
			Tooltip t = new Tooltip();
			if(!usernameLogin.getText().isEmpty()) {
				t.setText(usernameLogin.getText());
			}
			else {
				t.setText("Insert username..");
			}
			t.setWrapText(true);
			t.setPrefWidth(100);
			usernameLogin.setTooltip(t);
		});
		
		passwordLogin.setOnMouseEntered(l->{
			Tooltip t = new Tooltip();
			if(!passwordLogin.getText().isEmpty()) {
				t.setText(passwordLogin.getText());
			}
			else {
				t.setText("Insert password..");
			}
			t.setWrapText(true);
			t.setPrefWidth(100);
			passwordLogin.setTooltip(t);
		});
	}
	
	
	

}