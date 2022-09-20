package visualizzatoreFileVideo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.mindrot.jbcrypt.BCrypt;

import visualizzatoreFileVideo.controller.LoginController;
import visualizzatoreFileVideo.model.GestoreScene;


public class Database {
	
	private final String url = "jdbc:sqlite:test.db";
	public static Database db = null;
	private Connection con;
	public static final String USER_TABLE = "user";
	public static final String PLAYLIST_TABLE = "playlist";
	public static final String CONTIENE_TABLE = "contiene";
	public static final String CANZONE_TABLE = "canzone";
	public static final String EMAIL_COLUMN = "email";
	public static final String USER_TABLE_COLUMN1_USERNAME = "username";
	public static final String USER_TABLE_COLUMN2_PASSWORD = "password";
	public static final String USER_TABLE_COLUMN3_EMAIL = "email";
	public static final String PLAYLIST_TABLE_COLUMN1_NOME = "nome";
	public static final String PLAYLIST_TABLE_COLUMN2_USERNAMEUSER = "usernameuser";
	public static final String PLAYLIST_TABLE_COLUMN3_GENERE = "genere";
	public static final String PLAYLIST_TABLE_COLUMN4_PATHCOVERPLAYLIST="pathcoverplaylist";
	public static final String CANZONE_TABLE_COLUMN1_TITOLO = "titolo";
	public static final String CANZONE_TABLE_COLUMN2_PATH = "path";
	public static final String CONTIENE_TABLE_COLUMN1_NOMECANZONE= "nomecanzone";
	public static final String CONTIENE_TABLE_COLUMN2_NOMEPLAYLIST= "nomeplaylist";
	
	
	private Database() {
		try {
			con = DriverManager.getConnection(url);
		} catch (SQLException e) {
			GestoreScene.getGs().showError("ERROR 003");
		}
	}
	
	public static Database getDB() {
		if(db == null)
			db = new Database();
		return db;
	}
	
	public boolean insertValues(String table, ArrayList<String> columns, ArrayList<String> values) {
		if(con == null)
			return false;
		try {
			String query ="INSERT INTO " + table + "(";
			for(int i=0; i<columns.size(); i++) {
				if(i!=columns.size()-1)
					query += columns.get(i) + ", ";
				else
					query += columns.get(i);
			}
			query += ") VALUES(";
			for(int i=0; i<values.size(); i++) {
				if(i!=values.size()-1)
					query += "?, ";
				else
					query += "?);";
				
			}
			PreparedStatement ps = con.prepareStatement(query);
			for(int i=0; i<values.size(); i++) {
				ps.setString(i+1, values.get(i));
			}
			ps.executeUpdate();
			ps.close();
			return true;
		}
		catch(SQLException e) {
			GestoreScene.getGs().showError("ERROR 006");
			e.printStackTrace();
		}
		return false;
	}

	public boolean delete(String table, String column, String value) {
		if(con == null)
			return false;
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM " + table + " WHERE " + column + "=?;");
			ps.setString(1,  value);
			ps.executeUpdate();
			ps.close();	
			return true;
		}
		catch(SQLException e){
			GestoreScene.getGs().showError("ERROR 005");
		}
		return false;
	}
	
	public boolean check(String table, String column, String value) {
		if(con == null)
			return false;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM " + table + " WHERE " + column + "=?;");
			ps.setString(1, value );
			ResultSet res = ps.executeQuery();
			boolean r= res.next();
			res.close();
			ps.close();
			return r;
		}
		catch(SQLException e) {
			GestoreScene.getGs().showError("ERROR 004");
		}
		return false;
	}
	
	public boolean checkUser(String table, String column0, String column1, String value0, String value1) {
		if(con == null)
			return false;
		try {
			PreparedStatement ps0 = con.prepareStatement("SELECT " + column1 + " FROM " + table + " WHERE " + column0 + "=?;");
			ps0.setString(1, value0);
			ResultSet res = ps0.executeQuery();
			if(res.next()) {
				String truePass= res.getString(1);
				res.close();
				ps0.close();
				return BCrypt.checkpw(value1, truePass);
			}
			res.close();
			ps0.close();
		}
		catch(SQLException e) {
			GestoreScene.getGs().showError("ERROR 003");
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<String> listPlaylist() {
		String query = "SELECT nome FROM playlist WHERE usernameuser=?;";
		ArrayList<String> list = new ArrayList<String>();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, LoginController.utenteCorrente);
			ResultSet res = ps.executeQuery();
			while(res.next()) {
				list.add(res.getString(1));
			}
			ps.close();
			res.close();
					
		}
		catch(SQLException e){
			GestoreScene.getGs().showError("ERROR 008");
		}
		return list;
	}
	
	public HashMap<String, ArrayList<String>> createMap(ArrayList<String> list){
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		try {
			for(String s: list) {
				String query = "SELECT canzone.titolo FROM playlist INNER JOIN contiene "
						+ "ON playlist.nome=contiene.nomeplaylist INNER JOIN canzone ON contiene.nomecanzone=canzone.titolo"
						+ " WHERE playlist.usernameuser=? AND playlist.nome=?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, LoginController.utenteCorrente);
				ps.setString(2, s);
				ResultSet res = ps.executeQuery();
				ArrayList<String> temp = new ArrayList<String>();
				while(res.next()) {
					temp.add(res.getString(1));
				}
				map.put(s, temp);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public String resultString(String table, String something, String column, String value) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT " + something + " FROM " + table + " WHERE " + column + "=?;");
			ps.setString(1, value);
			ResultSet res = ps.executeQuery();
			String result = res.getString(1);
			ps.close();
			res.close();
			return result;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> listResultsString(String table, String something, String column, String value){
		ArrayList<String> list = new ArrayList<String>();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT " + something + " FROM " + table + " WHERE " + column + "=?;");
			ps.setString(1, value);
			ResultSet res = ps.executeQuery();
			while(res.next())
				list.add(res.getString(1));
			ps.close();
			res.close();
			return list;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<String> pathSongsPlaylist(String playlistName) {
		ArrayList<String> listPathSong = new ArrayList<String>();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT canzone.path FROM contiene INNER JOIN canzone "
					+ "ON contiene.nomecanzone = canzone.titolo WHERE contiene.nomeplaylist=?;");
			ps.setString(1, playlistName);
			
			ResultSet res = ps.executeQuery();
			while(res.next()) {
				listPathSong.add(res.getString(1));
			}
			ps.close();
			res.close();
			return listPathSong;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean updateTable(String table, String column, String value, String columnCondition, String valueCondition) {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE " + table + " SET " + column + "=? WHERE " + columnCondition + "=?;");
			ps.setString(1, value);
			ps.setString(2, valueCondition);
			ps.execute();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	

}
