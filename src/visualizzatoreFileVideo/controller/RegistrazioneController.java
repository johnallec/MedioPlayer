package visualizzatoreFileVideo.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import visualizzatoreFileVideo.model.GestisciDB;
import visualizzatoreFileVideo.model.GestoreScene;

public class RegistrazioneController implements Initializable{

	private final String REGEX_EMAIL = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
	private final String REGEX_PASSWORD= "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%\\.\\*]).{6,20})";
	@FXML
    private AnchorPane anchorPane;
	
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField repeatPassTextField;
    
    @FXML
    private TextField emailTextField;
    
    @FXML
    private Button submitButton;
    
    @FXML
    private Button retryButton;
    
    @FXML
    private ImageView backgroundImage;
    
    
    @FXML
    void submitButton(ActionEvent event) throws SQLException{
    	String user = usernameTextField.getText();
    	String pass = passwordTextField.getText();
    	String email = emailTextField.getText();
    	
    		if(!user.isEmpty() && !pass.isEmpty() && !email.isEmpty()) {
    			if(!GestisciDB.getGestore().utenteTrovato(user)) {
    				if(email.matches(REGEX_EMAIL)) {
	    				if(!GestisciDB.getGestore().emailTrovata(email)) {
	    					if(pass.matches(REGEX_PASSWORD)) {
		    					if(pass.equals(repeatPassTextField.getText())) {
		    						if(GestisciDB.getGestore().inserisciUser(user, pass, email)) {
		    							LoginController.utenteCorrente = user;
		    							GestoreScene.getGs().setVisualizzatoreScene();
		    						}
		    					}
		    					else{
		    						GestoreScene.getGs().showWarning("Passwords don't match!");
		    					}
	    					}
	    					else {
	    						GestoreScene.getGs().showWarning("The lenght of the password must be a minimum of 6 characters and a maximum of 20. "
	    								+ "Must contain at least one uppercase letter, one lowercase letter, a number and "
	    								+ "at least one special character including \"@#$%\" ");
	    					}
	    				}
	    				else {
	    					GestoreScene.getGs().showWarning("Email already exists.");
	    				}
    				}
    				else {
    					GestoreScene.getGs().showWarning("Email not valid. Use something like example@something.com."); 
    				}
    			}
    			else {
    				GestoreScene.getGs().showWarning("Username already exists.");
    			}
    		}
    		else
    			GestoreScene.getGs().showWarning("All fields must be filled.");
    }
    

    @FXML
    void retryButtonClick(ActionEvent event) throws IOException {
    	GestoreScene.getGs().setLoginScene();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//imposto lo sfondo
		backgroundImage.setFitWidth(anchorPane.getPrefWidth());
		backgroundImage.setFitHeight(anchorPane.getPrefHeight());
		backgroundImage.setImage(new Image("/visualizzatoreFileVideo/img/registrazioneBackgroundImage.png"));
		
		//creo i vari tooltip sui vari text field
		usernameTextField.setOnMouseEntered(l->{
			Tooltip t = new Tooltip();
			if(!usernameTextField.getText().isEmpty()) {
				t.setText(usernameTextField.getText());
			}
			else {
				t.setText("Insert username..");
			}
			t.setWrapText(true);
			t.setPrefWidth(100);
			usernameTextField.setTooltip(t);
		});
		
		passwordTextField.setOnMouseEntered(l->{
			Tooltip t = new Tooltip();
			if(!passwordTextField.getText().isEmpty()) {
				t.setText(passwordTextField.getText());
			}
			else {
				t.setText("Insert password..");
			}
			t.setWrapText(true);
			t.setPrefWidth(100);
			passwordTextField.setTooltip(t);
		});
		
		repeatPassTextField.setOnMouseEntered(l->{
			Tooltip t = new Tooltip();
			if(!repeatPassTextField.getText().isEmpty()) {
				t.setText(repeatPassTextField.getText());
			}
			else {
				t.setText("Reinsert password..");
			}
			t.setWrapText(true);
			t.setPrefWidth(100);
			repeatPassTextField.setTooltip(t);
		});
		
		emailTextField.setOnMouseEntered(l->{
			Tooltip t = new Tooltip();
			if(!emailTextField.getText().isEmpty()) {
				t.setText(emailTextField.getText());
			}
			else {
				t.setText("Insert email..");
			}
			t.setWrapText(true);
			t.setPrefWidth(100);
			emailTextField.setTooltip(t);
		});
	}
    
}