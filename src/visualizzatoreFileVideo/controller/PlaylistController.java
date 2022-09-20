package visualizzatoreFileVideo.controller;


import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import visualizzatoreFileVideo.model.GestisciDB;
import visualizzatoreFileVideo.model.GestoreScene;


public class PlaylistController implements Initializable{

	private HashMap<String, ArrayList<String>> mappaPlaylist;
	private ArrayList<Label> listaLabelPlaylist;
	public static String editPlaylistSelected;
	public static ArrayList<String> pathCanzoniPlaylist;
	
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private AnchorPane anchorPaneRight;
	
	@FXML
	private AnchorPane anchorPaneLeft;
    
    @FXML
    private MenuItem addPlaylistMenuItem;
    
    @FXML
    private Menu editPlaylistMenu;
    
    @FXML
    private CheckMenuItem nameCheckMenuItem;

    @FXML
    private CheckMenuItem categoryCheckMenuItem;

    @FXML
    private MenuItem retryMenuItem;
    
    @FXML
    private VBox playlistsVbox;
    
    @FXML
    private FlowPane flowPaneListSongs;
    
    @FXML
    void addPlaylistMenuItemClick(ActionEvent event) {
    	GestoreScene.getGs().setAddPlaylist();
    }

    //ordino per categoria
    @FXML
    void categoryCheckMenuItemValidation(ActionEvent event) {
    	
    	if(nameCheckMenuItem.isSelected())
    		nameCheckMenuItem.setSelected(false);
    	playlistsVbox.getChildren().clear();
    	Collections.sort(listaLabelPlaylist, new Comparator<Label>() {
    		
			@Override
			public int compare(Label o1, Label o2) {
				return GestisciDB.getGestore().generePlaylist(o1.getText()).compareToIgnoreCase(GestisciDB.getGestore().generePlaylist(o2.getText()));
			}
			
		});
    	playlistsVbox.getChildren().clear();
    	for(Label l: listaLabelPlaylist)
    		playlistsVbox.getChildren().add(l);
    }

    //ordino per nome
    @FXML
    void nameCheckMenuItemValidation(ActionEvent event) {
    	if(nameCheckMenuItem.isSelected())
    		categoryCheckMenuItem.setSelected(false);
    	playlistsVbox.getChildren().clear();
    	Collections.sort(listaLabelPlaylist, new Comparator<Label>() {

			@Override
			public int compare(Label o1, Label o2) {
				return o1.getText().compareToIgnoreCase(o2.getText());
			}
			
		});
    	playlistsVbox.getChildren().clear();
    	for(Label l: listaLabelPlaylist)
    		playlistsVbox.getChildren().add(l);
    }


    @FXML
    void retryMenuItemClick(ActionEvent event) {
    	GestoreScene.getGs().setVisualizzatoreScene(); //passo a GraficaVisualizzatore.fxml
    }
    
    public Label createLabelPlaylist(String pathImage, String title) {
    	ImageView coverPlaylist;
		coverPlaylist= new ImageView(new Image(pathImage)); 
		coverPlaylist.setFitWidth(70);
		coverPlaylist.setFitHeight(70);
		Label l= new Label(title, coverPlaylist);
		l.setFont(new Font("Forte", 15));
		l.setAlignment(Pos.CENTER);
		l.setContentDisplay(ContentDisplay.TOP);
		l.setMinSize(200, 50);
		return l;
    }
    
    public Label createLabelMedia(String title, String songOrVideo) {
    	ImageView coverSong;
    	if(songOrVideo.equalsIgnoreCase("video"))
    		coverSong= new ImageView(new Image("/visualizzatoreFileVideo/img/videoIcon.png")); 
    	else if(songOrVideo.equalsIgnoreCase("song")){
    		coverSong= new ImageView(new Image("/visualizzatoreFileVideo/img/songIcon.png"));
    	}
    	else {
    		coverSong= new ImageView(new Image("/visualizzatoreFileVideo/img/questionMarkIcon.png"));
    	}
		coverSong.setFitWidth(50);
		coverSong.setFitHeight(50);
		Label l= new Label(title, coverSong);
		l.setPrefSize(150, 100);
		l.setWrapText(true);
		l.setFont(new Font("Times new roman", 14));
		l.setAlignment(Pos.CENTER);
		l.setContentDisplay(ContentDisplay.TOP);
		l.setMinSize(150, 50);
		return l;
    }
    
    public boolean checkMp3(String file) {
    	return Pattern.matches("(.*).mp3", file);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//svuoto sempre il Vbox per aggiornarlo
		playlistsVbox.getChildren().clear();
		ArrayList<String> listaPlaylist = GestisciDB.getGestore().listaPlaylist();
		
		//inizializzo le strutture dati 
		listaLabelPlaylist = new ArrayList<Label>();
		pathCanzoniPlaylist = new ArrayList<String>();
		mappaPlaylist = GestisciDB.getGestore().creaMappaPlaylist();
		
		//per ogni playlist dell'utente creo una label che al rappresenti
		for(String s: listaPlaylist) {
			String pathImage = GestisciDB.getGestore().pathPlaylistCover(s);
			Label l = createLabelPlaylist(pathImage, s);
    		playlistsVbox.getChildren().add(l);
    		listaLabelPlaylist.add(l);
    	}
    	
		
		for(Label playlistLabel: listaLabelPlaylist) {
			//creo tooltip per ogni playlistLabel dove sono contenuti: nome playlist, genere e numero elementi
			String genere = GestisciDB.getGestore().generePlaylist(playlistLabel.getText());
			int numElem = GestisciDB.getGestore().pathCanzoni(playlistLabel.getText()).size();
			Tooltip t0 = new Tooltip("Playlist's title: \"" + playlistLabel.getText() + "\"\nCategory: \"" + genere + "\"\n" + numElem + " elements.");
    		t0.setPrefWidth(200);
    		t0.setOpacity(0.90);
    		t0.setWrapText(true);
    		playlistLabel.setTooltip(t0);
			
    		//imposto un evento dove:
    		
    		//1) se clicco 1 volta su una playlist, svuoto il FlowPane, creo le varie label che rappresentano le canzoni, creo un tooltip per ogni label 
    		//   dove faccio vedere il titolo per esteso e aggiungo il tutto al FlowPane
    		
    		//2) se clicco 2 volte riempio l'ArrayList pathCanzoniPlaylist che sarà visualizzato dal MedioPlayerController.java, setto la variabile
    		//   booleana riproduciPlaylist a true e passo a GraficaVisualizzatore.fxml
    		
    		//3) imposto un evento per ogni elemento del FlowPane dove passo alla riproduzione della elemento selezionato
    		playlistLabel.setOnMouseClicked(click0 -> {
				if(click0.getClickCount()==1) {
					flowPaneListSongs.getChildren().clear();
					ArrayList<String> temp = mappaPlaylist.get(playlistLabel.getText());
					for(String s: temp) {
						Label label;
						if(checkMp3(s))
							label= createLabelMedia(s, "song");
						else
							label= createLabelMedia(s, "video");
						
			    		Tooltip t1 = new Tooltip(s);
			    		t1.setPrefWidth(170);
			    		t1.setOpacity(0.90);
			    		t1.setWrapText(true);
			    		label.setTooltip(t1);
			    		flowPaneListSongs.getChildren().add(label);
			    	}
				}
				
				if(click0.getClickCount()==2) {
					pathCanzoniPlaylist = GestisciDB.getGestore().pathCanzoni(playlistLabel.getText());
					MedioPlayerController.riproduciPlaylist=true;
					GestoreScene.getGs().setVisualizzatoreScene();
				}
				for(Node node: flowPaneListSongs.getChildren()) {
					node.setOnMouseClicked(click1->{
						if(click1.getClickCount()==2) {
							if(node instanceof Label) {
								Label lab = (Label) node;
								pathCanzoniPlaylist.add(GestisciDB.getGestore().pathCanzone(lab.getText()));
								MedioPlayerController.riproduciPlaylist=true;
								GestoreScene.getGs().setVisualizzatoreScene();
							}
						}
					});
				}
				
			});
		}
		
		//creo un menuItem con tutte le playlist presenti: se clicco su una di queste nel menu, passo a GraficaEditPlaylist.fxml inizializando la
		//variabile statica editPlaylistSelected
		for(Label l: listaLabelPlaylist)
			editPlaylistMenu.getItems().add(new MenuItem(l.getText()));
		
		for(MenuItem m: editPlaylistMenu.getItems()) {
			m.setOnAction(click->{
				editPlaylistSelected = m.getText();
				GestoreScene.getGs().setEditPlaylist();
			});
		}
	}


}
