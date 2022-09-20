package visualizzatoreFileVideo.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import visualizzatoreFileVideo.Database;
import visualizzatoreFileVideo.model.GestisciDB;
import visualizzatoreFileVideo.model.GestoreScene;

public class AddPlaylistController implements Initializable{

	private HashMap<String, String> path;
	
	private String pathCoverView;
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private ImageView backgroundImage;
	
    @FXML
    private TextField genereTextField;

    @FXML
    private Button choosePlaylistCoverButton;
   
    @FXML
    protected ImageView coverImageView;
    
    @FXML
    private Button createButton;
    
    @FXML
    private Button retryButton;

    @FXML
    private Button chooseSongButton;
    
    @FXML
    private Button removeButton;
    
    @FXML
    private TextField playlistNameTextField;

    @FXML
    private ListView<String> listView;

    @FXML
    void choosePlaylistCoverButtonClick(ActionEvent event) {
    	FileChooser fc = new FileChooser();
    	fc.getExtensionFilters().add(new ExtensionFilter("JPG Files","*.jpg"));
    	fc.getExtensionFilters().add(new ExtensionFilter("PNG Files","*.png"));
    	File selectedFile = fc.showOpenDialog(null);
    		if(selectedFile!=null) {
    			coverImageView.setImage(new Image(selectedFile.toURI().toString()));
    			coverImageView.setFitWidth(150);
    			coverImageView.setFitHeight(150);
    			pathCoverView = selectedFile.toURI().toString();
    		}
    }	  
    
    //prima della creazione faccio in controlli se effettivamente è stato inserito nome, genere ed elementi. Se la cover non è stata scelta, ne imposto
    //una io di default
    //inoltre ci sono i vari controlli sulle entità del Database
    @FXML
    void createButtonClick(ActionEvent event) {
    	boolean b= true;
    	String playlistName = playlistNameTextField.getText();
		String genere = genereTextField.getText();
    	if(!playlistName.isEmpty()) {
    		if(!genere.isEmpty()) {
		    	if(!listView.getItems().isEmpty()) {
	    			if(!GestisciDB.getGestore().trova(Database.PLAYLIST_TABLE, Database.PLAYLIST_TABLE_COLUMN1_NOME, playlistName)) {
	    				if(isEmpty(coverImageView))
	    					pathCoverView = "/visualizzatoreFileVideo/img/playlistIcon.png";
			    		if(GestisciDB.getGestore().inserisciPlaylist(playlistName, LoginController.utenteCorrente, genere, pathCoverView)) {
				    		for(int i=0; i<listView.getItems().size(); i++) {
				    			if(GestisciDB.getGestore().trova(Database.CANZONE_TABLE, Database.CANZONE_TABLE_COLUMN1_TITOLO, listView.getItems().get(i))) {
				    				GestisciDB.getGestore().modificaPathCanzoneEsistente(listView.getItems().get(i), path.get(listView.getItems().get(i)));		
				    			}
				    			else {
				    				GestisciDB.getGestore().inserisciCanzone(listView.getItems().get(i), path.get(listView.getItems().get(i)));
				    			}
				    			GestisciDB.getGestore().inserisciContiene(listView.getItems().get(i), playlistName);
				    		}
			    		}
			    		else {
			    			b = false;
			    		}
		    		
		    		path.clear();
		    		
		    		if(b)
		    			GestoreScene.getGs().setPlaylist();
	    			}
	    			else {
	    				GestoreScene.getGs().showWarning("Playlist's name already exists.");
	    			}
		    		
		    	}
		    	else {
		    		GestoreScene.getGs().showWarning("No song selected.");
		    	}
    		}
    		else {
    			GestoreScene.getGs().showWarning("Insert category.");
    		}
    	}
    	else {
    		GestoreScene.getGs().showWarning("Insert playlist's name.");
    	}
    }
    
    @FXML
    void retryButtonClick(ActionEvent event) {
    	if(GestoreScene.getGs().showConfirmation("Do you want to continue? By doing so you will lose the entered data.")){
    			GestoreScene.getGs().setPlaylist();
    		}	
    }
    
    @FXML
    void chooseSongButtonClick(ActionEvent event) {
    	FileChooser fc= new FileChooser();
    	fc.getExtensionFilters().add(new ExtensionFilter("MP4 Files","*.mp4"));
    	fc.getExtensionFilters().add(new ExtensionFilter("MP3 Files","*.mp3"));
    	List<File> selectedFiles= fc.showOpenMultipleDialog(null);
    	try{
	    	if(selectedFiles!=null) {
	    		Iterator<File> it = selectedFiles.iterator();
	    		while(it.hasNext()) {
	    			File f = it.next();	    			
	    			listView.getItems().add(f.getName());
	    			path.put(f.getName(), f.toURI().toURL().toString());
	    		}
	    	}
    	}
    	catch(MalformedURLException e) {
    		GestoreScene.getGs().showError("ERROR 007.");
    	}
    }
    
    //rimuove l'elemento selezionato
    @FXML
    void removeButtonClick(ActionEvent event) {
    	path.remove(listView.getSelectionModel().getSelectedItem());
    	listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
    }

    public boolean isEmpty(ImageView imageView) {
        Image image = imageView.getImage();
        return image == null || image.isError();
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//imposto lo sfondo
		path = new HashMap<String, String>();
		backgroundImage.setFitWidth(800);
		backgroundImage.setFitHeight(600);
		backgroundImage.setOpacity(0.30);
		backgroundImage.setImage(new Image("/visualizzatoreFileVideo/img/sfondo1.png"));
		
		//creo 2 tooltip per il nome della playlist ed il genere 
		playlistNameTextField.setOnMouseEntered(l->{
			Tooltip t = new Tooltip();
			if(!playlistNameTextField.getText().isEmpty()) {
				t.setText(playlistNameTextField.getText());
			}
			else {
				t.setText("Insert a playlist's title..");
			}
			t.setWrapText(true);
			t.setPrefWidth(100);
			playlistNameTextField.setTooltip(t);
		});
		
		genereTextField.setOnMouseEntered(l->{
			Tooltip t = new Tooltip();
			if(!genereTextField.getText().isEmpty()) {
				t.setText(genereTextField.getText());
			}
			else {
				t.setText("Insert a category..");
			}
			t.setWrapText(true);
			t.setPrefWidth(100);
			genereTextField.setTooltip(t);
		});
		
		//imposto le immagini per l'aggiunta degli elementi e la rimozione
		ImageView chooseSongImage = new ImageView(new Image("/visualizzatoreFileVideo/img/chooseSongImage.png"));
		chooseSongImage.setFitWidth(30);
		chooseSongImage.setFitHeight(30);
		ImageView removeSongImage = new ImageView(new Image("/visualizzatoreFileVideo/img/removeSongImage.png"));
		removeSongImage.setFitWidth(30);
		removeSongImage.setFitHeight(30);
		chooseSongButton.setGraphic(chooseSongImage);
		removeButton.setGraphic(removeSongImage);
		
	}

}