package visualizzatoreFileVideo.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import visualizzatoreFileVideo.Database;
import visualizzatoreFileVideo.model.GestisciDB;
import visualizzatoreFileVideo.model.GestoreScene;

public class EditPlaylistController implements Initializable{
	
	//praticamente identica ad AddPLaylistController tranne per gli elemtni già presenti nella playlist che faccio comparire appena si passa a questo fxml
	//e per listAdded e listRemoved che sono ArrayList che contengono rispettivamente i nuovi elementi da aggiugnere e i vecchi da rimuovere

	private HashMap<String, String> path;
	private ArrayList<String> listAdded;
	private ArrayList<String> listRemoved;
	private String pathCoverPlaylist;
	
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
    			pathCoverPlaylist = selectedFile.toURI().toString();
    		}
    }	  
    
    @FXML
    void createButtonClick(ActionEvent event) {
    	if(GestoreScene.getGs().showConfirmation("Are you sure to save changes?")) {
	    	String changeName = playlistNameTextField.getText();
	    	String changeCategory = genereTextField.getText();
	    	String playlistName = PlaylistController.editPlaylistSelected;
	    	if(!changeName.isEmpty()) { 
	    		GestisciDB.getGestore().cambiaNomePlaylist(changeName);
	    		playlistName = changeName;
	    	}
	    	if(!changeCategory.isEmpty())
	    		GestisciDB.getGestore().cambiaCategoriaPlaylist(changeCategory);
	    	GestisciDB.getGestore().cambiaCoverPlaylist(pathCoverPlaylist);
	    	if(!listRemoved.isEmpty())
	    		GestisciDB.getGestore().eliminaCanzoniSelezionate(listRemoved);
	    	for(int i=0; i<listAdded.size(); i++) {
				if(GestisciDB.getGestore().trova(Database.CANZONE_TABLE, Database.CANZONE_TABLE_COLUMN1_TITOLO, listAdded.get(i))) {
					GestisciDB.getGestore().modificaPathCanzoneEsistente(listAdded.get(i), path.get(listAdded.get(i)));	
				}
				else {
					GestisciDB.getGestore().inserisciCanzone(listAdded.get(i), path.get(listAdded.get(i)));
				}
				GestisciDB.getGestore().inserisciContiene(listAdded.get(i), playlistName);
			}
	    	GestoreScene.getGs().setPlaylist();
    	}
    }
    
    @FXML
    void retryButtonClick(ActionEvent event) {
    	if(GestoreScene.getGs().showConfirmation("No saved changes. Do you want to continue?")){
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
	    			if(!listView.getItems().contains(f.getName())) {
		    			listView.getItems().add(f.getName());
		    			listAdded.add(f.getName());
		    			path.put(f.getName(), f.toURI().toURL().toString());
	    			}
	    		}
	    	}
    	}
    	catch(MalformedURLException e) {
    		GestoreScene.getGs().showError("ERROR 009.");
    	}
    }
    
    @FXML
    void removeButtonClick(ActionEvent event) {
    	listRemoved.add(listView.getSelectionModel().getSelectedItem());
    	listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		path = new HashMap<String, String>();
		listRemoved = new ArrayList<String>();
		listAdded = new ArrayList<String>();
		
		backgroundImage.setFitWidth(800);
		backgroundImage.setFitHeight(600);
		backgroundImage.setOpacity(0.30);
		backgroundImage.setImage(new Image("/visualizzatoreFileVideo/img/sfondo1.png"));
		
		ArrayList<String> list = GestisciDB.getGestore().listaCanzoniPlaylist(PlaylistController.editPlaylistSelected);
		listView.getItems().addAll(list);
		
		playlistNameTextField.setText(PlaylistController.editPlaylistSelected);
		genereTextField.setText(GestisciDB.getGestore().generePlaylist(PlaylistController.editPlaylistSelected));
		String oldCoverPlaylist = GestisciDB.getGestore().pathPlaylistCover(PlaylistController.editPlaylistSelected);
		pathCoverPlaylist = oldCoverPlaylist;
		coverImageView.setImage(new Image(pathCoverPlaylist));
		
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