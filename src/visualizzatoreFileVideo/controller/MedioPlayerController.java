 package visualizzatoreFileVideo.controller;

 
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import visualizzatoreFileVideo.model.GestisciDB;
import visualizzatoreFileVideo.model.GestoreScene;

public class MedioPlayerController implements Initializable{
	
	
	
	
	//								PROBLEMA NELLO SLIDER E FORSE NELLA FUNZIONE setOnEndOfMedia
	
	
	
	
	public static boolean riproduciPlaylist= false;  //mi serve a capire quando ho avviato una playlist da PlaylistController.java
	private int trackIndex=0;	//indice corrente dell'eventuale media nella playlist 
	private ArrayList<MediaPlayer> playlistMedia; 
	private ListView<String> elencoCanzoni;	
	
	@FXML 
	private BorderPane borderPane;
	
	@FXML
    private MenuItem openMenuItem;

    @FXML
    private MenuItem logoutMenuItem;
    
    @FXML
    private CheckMenuItem songsPlaylistCheckMenuItem;

    @FXML
    private MenuItem minePlaylistsMenuItem;
    
    @FXML
    private MenuItem deleteAccountMenuItem;
    
    @FXML
    private MenuItem aboutMenuItem;
    
    @FXML
    private Button playButton;
    
    @FXML
    private Button backSecButton;
    
    @FXML
    private Button aheadSecButton;
    
    @FXML
    private Button precButton;
    
    @FXML
    private Button succButton;
    
    @FXML
    private MediaView mv;
    
    @FXML
    private Slider sliderH;
    
    @FXML
    private ImageView backgroundImage;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    void openMenuItemClick(ActionEvent event) {
    	
    	FileChooser fc= new FileChooser();
    	fc.getExtensionFilters().add(new ExtensionFilter("MP4 Files","*.mp4"));
    	fc.getExtensionFilters().add(new ExtensionFilter("MP3 Files","*.mp3"));
    	File selectedFile= fc.showOpenDialog(null);
    	try {
    		
    		if(selectedFile!=null) {
    			if(!playlistMedia.isEmpty())
    				playlistMedia.clear();
    			
    			if(mv.getMediaPlayer()!=null)
    				mv.getMediaPlayer().stop();
    			
    			Media selectedMedia = new Media(selectedFile.toURI().toURL().toString());
    			
    			MediaPlayer mp= new MediaPlayer(selectedMedia);
    			mv.setMediaPlayer(mp);
    			
    			setOnReadyAndCurrenTimeMP(); //imposto lo slider ai suo valori iniziali, finali e aggiungo un listener per il suo movimento
    			
    			mp.setAutoPlay(true);
    			elencoCanzoni.getItems().add(selectedFile.getName()); //aggiungo il singolo media all'elenco
    			
    			ImageView img = new ImageView("/visualizzatoreFileVideo/img/pauseImageIcon.png");
    			img.setFitHeight(30);
    			img.setFitWidth(30);
    			playButton.setGraphic(img);
    			
    		}
    	}
    	catch(MalformedURLException e) {
    		GestoreScene.getGs().showError("ERROR 007");
    	}

    }
    
    @FXML
    void songsPlaylistCheckMenuItemValidation(ActionEvent event) {
    	//l'elenco dei media nella playlist (o singolo media) è sempre presente, qui decido quando è visibile
    	if(mv.getMediaPlayer()!=null) {
	    	if(songsPlaylistCheckMenuItem.isSelected()) {
	    		elencoCanzoni.setVisible(true);
	    	}
	    	else {
	    		elencoCanzoni.setVisible(false);
	    	}
    	}
    }

	@FXML
    void logoutMenuItemClick(ActionEvent event) {
    	MediaPlayer mp = mv.getMediaPlayer();
    	if(mp!=null) {
    		mp.stop();
    	}
    	GestoreScene.getGs().setLoginScene();
    }
	
	@FXML
	void aboutMenuItemClick(ActionEvent event) {
		GestoreScene.getGs().showWarning("All images are taken from:\n" + 
				"www.icons8.it\n" + 
				"https://it.freepik.com/\n" + 
				"other random sites on google.\n\n" + 
				"THANKS TO ALL!");
	}
    
    @FXML
    void playButtonClick(ActionEvent event) {
    	MediaPlayer mp = mv.getMediaPlayer();
    	if(mp!=null) {
    		if(mp.getStatus()==Status.PLAYING) {
    			setGraficButtonPlay();
    			mp.pause();
    		}
    		else {
    			setGraficButtonPause();
    			mp.play();
    		}
    	}
    }
    
    @FXML
    void backSecButtonClick(ActionEvent event) {
    	MediaPlayer mp = mv.getMediaPlayer();
    	mp.seek(Duration.seconds(mp.getCurrentTime().toSeconds()-15));
    }
    
    @FXML
    void aheadSecButtonClick(ActionEvent event) {
    	MediaPlayer mp = mv.getMediaPlayer();
    	mp.seek(Duration.seconds(mp.getCurrentTime().toSeconds()+15));
    }
    
    @FXML
    void precButtonClick(ActionEvent event) {
    	//questo è il codice che avevo implementato, fa quello che deve fare ma il mediaplayer lagga molto, non sono riuscito a trovare una soluzione
    /*
    	if(mv.getMediaPlayer()!=null && !playlistMedia.isEmpty()) {
    	
	    	if(mv.getMediaPlayer().getCurrentTime().toSeconds()<=1.3) { //se clicco due volte consecutive il tempo è minore di 1.3 secondi
	    		mv.getMediaPlayer().stop();
	    		if(!playlistMedia.isEmpty() && trackIndex>0) { //controllo se sto sforando l'ArrayList e se è in esecuzione una playlist
	    			trackIndex--;
	    			mv.setMediaPlayer(playlistMedia.get(trackIndex));
	    			elencoCanzoni.getSelectionModel().select(trackIndex);
	    			setOnReadyAndCurrenTimeMP(mv.getMediaPlayer());
	    			mv.getMediaPlayer().play();
	    		}
	    	}else {
	    		mv.getMediaPlayer().stop();
	        	mv.getMediaPlayer().play();
	    	}
    	}
    */
    	if(mv.getMediaPlayer().getStatus()== Status.PLAYING) {
	    	mv.getMediaPlayer().stop();
	    	mv.getMediaPlayer().play();
    	}
    	else
    		mv.getMediaPlayer().stop();
    }
    
    @FXML
    void succButtonClick(ActionEvent event) {
    	if(mv.getMediaPlayer()!=null && !playlistMedia.isEmpty()) {
    		mv.getMediaPlayer().stop();
    		trackIndex++;
    		if(trackIndex < playlistMedia.size()) {
    			setSlider();
    			mv.getMediaPlayer().stop();
            	mv.setMediaPlayer(playlistMedia.get(trackIndex));
            	setOnReadyAndCurrenTimeMP();
            	mv.getMediaPlayer().play();
            	elencoCanzoni.getSelectionModel().select(trackIndex); // seleziono il media in esecuzione sull'elenco dei media
    		}
    		else {
    			//se il trackIndex è al termine della playlist
    			mv.getMediaPlayer().stop();
    			mv.setMediaPlayer(null);
    			trackIndex=0;
    			playlistMedia.clear();
    			setGraficButtonPlay();
    			elencoCanzoni.getItems().clear();
    		}
    	}
    }
    
    @FXML
    void minePlaylistsMenuItemClick(ActionEvent event) {
    	MediaPlayer mp = mv.getMediaPlayer();
    	if(mp != null) {
    		if(GestoreScene.getGs().showConfirmation("Doing so stop playback, do you want to continue?")) {
    			mp.stop();
    			GestoreScene.getGs().setPlaylist(); //passo a Playlist.fxml
    		}
    	}
    	else GestoreScene.getGs().setPlaylist();	
    }
    
    @FXML
    void deleteAccountMenuItemClick(ActionEvent event) {
    	if(GestoreScene.getGs().showConfirmation("Are you sure to delete your account?")) {
    		GestoreScene.getGs().setEliminaAccount(); //passo a EliminaAccount.fxml
    	}
    }
    
    public void setSlider() {
    	sliderH.setMin(0);
		sliderH.setMax(mv.getMediaPlayer().getMedia().getDuration().toSeconds());
		sliderH.setMajorTickUnit(mv.getMediaPlayer().getMedia().getDuration().toSeconds()); //imposto la "tacca" finale dello slider col termine della canzone
		sliderH.setValue(0);
    }
    
    public void setOnReadyAndCurrenTimeMP() {
    	
    	MediaPlayer mp = mv.getMediaPlayer();
    	
    	mp.setOnReady(() -> {
    		setSlider();
			GestoreScene.getGs().resize(mp.getMedia().getWidth(),mp.getMedia().getHeight() + 300); //imposto la grandezza dello stage
		}); 
    	
    	ChangeListener<Number> cl = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue,
					Number newValue) {
				
				mp.seek(new Duration(newValue.doubleValue()*1000));  // quando muovo attivamente lo slider il video va avanti o indietro di conseguenza
				
			}
			
		}; 
		
		mp.currentTimeProperty().addListener(l ->{
			sliderH.valueProperty().removeListener(cl);
			sliderH.setValue(mp.getCurrentTime().toSeconds());
			sliderH.valueProperty().addListener(cl);
		});
    	
		mp.setOnError(()->GestoreScene.getGs().showError("Unsupported file!"));
    	
    }
    
    //alla fine di ogni media controllo che il trackIndex non sia > del numero di media della playlist e continuo ad incrementarlo, 
	//altrimenti fermo tutto e svuoto le strutture dati
    public void setOnEndOfMedia(MediaPlayer mp) {
    	
    	mp.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				mp.stop();
				trackIndex++;
				if(trackIndex < playlistMedia.size()) {
					mv.setMediaPlayer(playlistMedia.get(trackIndex));
					setOnReadyAndCurrenTimeMP();
					mv.getMediaPlayer().play();
					setSlider();
					elencoCanzoni.getSelectionModel().select(trackIndex);
				}
				else {
					mv.setMediaPlayer(null);
					playlistMedia.clear();
					trackIndex=0;
					riproduciPlaylist = false;
					playlistMedia.clear();
					elencoCanzoni.getItems().clear();
				}
			}
		});
    }
    
    public void setGraficButtonPause() {
    	ImageView img = new ImageView("/visualizzatoreFileVideo/img/pauseImageIcon.png");
		img.setFitHeight(30);
		img.setFitWidth(30);
		playButton.setGraphic(img);
    }
    
    public void setGraficButtonPlay() {
    	ImageView img = new ImageView("/visualizzatoreFileVideo/img/playImageIcon.png");
		img.setFitHeight(30);
		img.setFitWidth(30);
		playButton.setGraphic(img);
    }

    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	//inizializzo le immagini dei bottoni
		ImageView play, succ, prec, ahead, back;
		int dim=30;
		play= new ImageView("/visualizzatoreFileVideo/img/playImageIcon.png");
		prec= new ImageView("/visualizzatoreFileVideo/img/precImageIcon.png");
		succ= new ImageView("/visualizzatoreFileVideo/img/succImageIcon.png");
		ahead= new ImageView("/visualizzatoreFileVideo/img/aheadSecImageIcon.png");
		back= new ImageView("/visualizzatoreFileVideo/img/backSecImageIcon.png");
		
		play.setFitHeight(dim);
		play.setFitWidth(dim);
		
		prec.setFitHeight(dim);
		prec.setFitWidth(dim);
		
		succ.setFitHeight(dim);
		succ.setFitWidth(dim);
		
		ahead.setFitHeight(dim);
		ahead.setFitWidth(dim);
		
		back.setFitHeight(dim);
		back.setFitWidth(dim);
		
		
		playButton.setGraphic(play);
		precButton.setGraphic(prec);
		succButton.setGraphic(succ);
		aheadSecButton.setGraphic(ahead);
		backSecButton.setGraphic(back);
		
		backgroundImage.setImage(new Image("/visualizzatoreFileVideo/img/visualizzatoreBackgroundImage.png"));
		backgroundImage.fitWidthProperty().bind(Bindings.selectDouble(anchorPane.sceneProperty(), "width"));
		backgroundImage.fitHeightProperty().bind(Bindings.selectDouble(anchorPane.sceneProperty(), "height"));
		
		//inizializzo le strutture dati
		playlistMedia = new ArrayList<MediaPlayer>();
		elencoCanzoni = new ListView<String>();
		borderPane.setRight(elencoCanzoni);
		elencoCanzoni.setVisible(false);
		
		if(riproduciPlaylist) { //se il valore è stato impostato a true su PlaylistController.java
			MediaPlayer mp = mv.getMediaPlayer();
			ArrayList<String> canzoniPlaylist = new ArrayList<String>(); //contiene i nomi dei media della playlist
			for(String pathSong: PlaylistController.pathCanzoniPlaylist) {
				playlistMedia.add(new MediaPlayer(new Media(pathSong)));
				canzoniPlaylist.add(GestisciDB.getGestore().nomeCanzone(pathSong));
			}
			
			elencoCanzoni.getItems().addAll(canzoniPlaylist);
			//se clicco due volte su un media nell'elenco (se visibile) riproduco da quel punto
			elencoCanzoni.setOnMouseClicked(l->{
				if(l.getClickCount()==2) {
					trackIndex = elencoCanzoni.getSelectionModel().getSelectedIndex();
					mv.getMediaPlayer().stop();
					mv.setMediaPlayer(playlistMedia.get(trackIndex));
					setOnReadyAndCurrenTimeMP();
					setSlider();
					mv.getMediaPlayer().play();
				}
			});
			
			if(mp!=null)
				mp.stop();
			mp = playlistMedia.get(trackIndex);
			//imposto il primo media player in base al trackIndex e avvio la riproduzione 
			mv.setMediaPlayer(mp);
			setOnReadyAndCurrenTimeMP();
			mp.play();
			setSlider();
			elencoCanzoni.getSelectionModel().select(trackIndex);
			setGraficButtonPause();
			for(MediaPlayer mediaPlayer: playlistMedia)
				setOnEndOfMedia(mediaPlayer);
			
		}
	}


}
