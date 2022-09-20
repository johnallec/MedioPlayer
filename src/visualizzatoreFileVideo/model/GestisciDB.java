package visualizzatoreFileVideo.model;


import java.util.ArrayList;
import java.util.HashMap;

import org.mindrot.jbcrypt.BCrypt;

import visualizzatoreFileVideo.Database;
import visualizzatoreFileVideo.controller.PlaylistController;

public class GestisciDB {

	private static GestisciDB gestore = null;

	public static GestisciDB getGestore() {
		if (gestore == null)
			gestore = new GestisciDB();
		return gestore;
	}

	public boolean utenteTrovato(String value) {
		return Database.getDB().check(Database.USER_TABLE, Database.USER_TABLE_COLUMN1_USERNAME, value);
	}

	public boolean emailTrovata(String value) {
		return Database.getDB().check(Database.USER_TABLE, Database.EMAIL_COLUMN, value);	
	}

	public boolean inserisciUser(String username, String password, String email) {
		ArrayList<String> colonne = new ArrayList<String>();
		colonne.add(Database.USER_TABLE_COLUMN1_USERNAME);
		colonne.add(Database.USER_TABLE_COLUMN2_PASSWORD);
		colonne.add(Database.USER_TABLE_COLUMN3_EMAIL);
		ArrayList<String> valori = new ArrayList<String>();
		valori.add(username);
		valori.add(BCrypt.hashpw(password, BCrypt.gensalt(12)));
		valori.add(email);
		return Database.getDB().insertValues(Database.USER_TABLE, colonne, valori);	
	}

	public boolean inserisciPlaylist(String nomePlaylist, String username, String genere, String pathCoverPlaylist) {
		ArrayList<String> colonne = new ArrayList<String>();
		colonne.add(Database.PLAYLIST_TABLE_COLUMN1_NOME);
		colonne.add(Database.PLAYLIST_TABLE_COLUMN2_USERNAMEUSER);
		colonne.add(Database.PLAYLIST_TABLE_COLUMN3_GENERE);
		colonne.add(Database.PLAYLIST_TABLE_COLUMN4_PATHCOVERPLAYLIST);
		ArrayList<String> valori = new ArrayList<String>();
		valori.add(nomePlaylist);
		valori.add(username);
		valori.add(genere);
		valori.add(pathCoverPlaylist);
		return Database.getDB().insertValues(Database.PLAYLIST_TABLE, colonne, valori);	
	}
	
	public boolean inserisciCanzone(String titoloCanzone, String path) {
		ArrayList<String> colonne = new ArrayList<String>();
		colonne.add(Database.CANZONE_TABLE_COLUMN1_TITOLO);
		colonne.add(Database.CANZONE_TABLE_COLUMN2_PATH);
		ArrayList<String> valori = new ArrayList<String>();
		valori.add(titoloCanzone);
		valori.add(path);
		return Database.getDB().insertValues(Database.CANZONE_TABLE, colonne, valori);	
	}
	
	public void modificaPathCanzoneEsistente(String titoloCanzone, String path) {
		Database.getDB().updateTable(Database.CANZONE_TABLE, Database.CANZONE_TABLE_COLUMN2_PATH, path, Database.CANZONE_TABLE_COLUMN1_TITOLO, titoloCanzone);
	}
	
	public boolean inserisciContiene(String nomeCanzone, String nomePlaylist) {
		ArrayList<String> colonne = new ArrayList<String>();
		colonne.add(Database.CONTIENE_TABLE_COLUMN1_NOMECANZONE);
		colonne.add(Database.CONTIENE_TABLE_COLUMN2_NOMEPLAYLIST);
		ArrayList<String> valori = new ArrayList<String>();
		valori.add(nomeCanzone);
		valori.add(nomePlaylist);
		return Database.getDB().insertValues(Database.CONTIENE_TABLE, colonne, valori);	
	}
	
	public boolean eliminaUsername(String username){	
		return Database.getDB().delete(Database.USER_TABLE, Database.USER_TABLE_COLUMN1_USERNAME, username);
	}
	
	public boolean trova(String table, String column, String value) {
		return Database.getDB().check(table, column, value);
	}

	public boolean utenteEPasswordCorretti(String userValue, String passValue) {
		return Database.getDB().checkUser(Database.USER_TABLE, Database.USER_TABLE_COLUMN1_USERNAME, Database.USER_TABLE_COLUMN2_PASSWORD, userValue, passValue);		 
	}

	//lista Playlist dell'utente corrente
	public ArrayList<String> listaPlaylist(){
		ArrayList<String> listaPlaylist = Database.getDB().listPlaylist();
		return listaPlaylist;
	}
	
	public HashMap<String, ArrayList<String>> creaMappaPlaylist() {
		ArrayList<String> listaPlaylist = listaPlaylist();
		HashMap<String, ArrayList<String>> map = Database.getDB().createMap(listaPlaylist);
		return map;
	}
	
	public String pathPlaylistCover(String nomePlaylist) {
		return Database.getDB().resultString(Database.PLAYLIST_TABLE, Database.PLAYLIST_TABLE_COLUMN4_PATHCOVERPLAYLIST, Database.PLAYLIST_TABLE_COLUMN1_NOME, nomePlaylist);
	}

	public String generePlaylist(String value) {
		return Database.getDB().resultString(Database.PLAYLIST_TABLE, Database.PLAYLIST_TABLE_COLUMN3_GENERE, Database.PLAYLIST_TABLE_COLUMN1_NOME, value);
	}

	public ArrayList<String> pathCanzoni(String playlistName) {
		return Database.getDB().pathSongsPlaylist(playlistName);
	}
	
	public ArrayList<String> listaCanzoniPlaylist(String nomePlaylist){
		return Database.getDB().listResultsString(Database.CONTIENE_TABLE, Database.CONTIENE_TABLE_COLUMN1_NOMECANZONE, Database.CONTIENE_TABLE_COLUMN2_NOMEPLAYLIST, nomePlaylist);
	}

	public void cambiaNomePlaylist(String changeName) {
		Database.getDB().updateTable(Database.PLAYLIST_TABLE, Database.PLAYLIST_TABLE_COLUMN1_NOME, changeName, Database.PLAYLIST_TABLE_COLUMN1_NOME, PlaylistController.editPlaylistSelected);
	}

	public void cambiaCategoriaPlaylist(String changeCategory) {
		Database.getDB().updateTable(Database.PLAYLIST_TABLE, Database.PLAYLIST_TABLE_COLUMN3_GENERE, changeCategory, Database.PLAYLIST_TABLE_COLUMN1_NOME, PlaylistController.editPlaylistSelected);
	}

	public void cambiaCoverPlaylist(String pathCoverPlaylist) {
		Database.getDB().updateTable(Database.PLAYLIST_TABLE, Database.PLAYLIST_TABLE_COLUMN4_PATHCOVERPLAYLIST, pathCoverPlaylist, Database.PLAYLIST_TABLE_COLUMN1_NOME, PlaylistController.editPlaylistSelected);
	}

	public void eliminaCanzoniSelezionate(ArrayList<String> listRemoved) {
		for(String s: listRemoved)
			Database.getDB().delete(Database.CONTIENE_TABLE, Database.CONTIENE_TABLE_COLUMN1_NOMECANZONE, s);
	}

	public void eliminaPlaylist() {
		Database.getDB().delete(Database.PLAYLIST_TABLE, Database.PLAYLIST_TABLE_COLUMN1_NOME, PlaylistController.editPlaylistSelected);
		Database.getDB().delete(Database.CONTIENE_TABLE, Database.CONTIENE_TABLE_COLUMN2_NOMEPLAYLIST, PlaylistController.editPlaylistSelected);
	}
	
	public void gestisciCanzone(String canzone) {
		if(Database.getDB().check(Database.CONTIENE_TABLE, Database.CONTIENE_TABLE_COLUMN1_NOMECANZONE, canzone))
			Database.getDB().delete(Database.CANZONE_TABLE, Database.CANZONE_TABLE_COLUMN1_TITOLO, canzone);
	}

	public String pathCanzone(String value) {
		return Database.getDB().resultString(Database.CANZONE_TABLE, Database.CANZONE_TABLE_COLUMN2_PATH, Database.CANZONE_TABLE_COLUMN1_TITOLO, value);
	}

	public String nomeCanzone(String value) {
		return Database.getDB().resultString(Database.CANZONE_TABLE, Database.CANZONE_TABLE_COLUMN1_TITOLO, Database.CANZONE_TABLE_COLUMN2_PATH, value);
	}

}

