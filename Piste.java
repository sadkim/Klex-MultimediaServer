import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Piste {
	
	public static void addPiste(int IdAlbum, String titrePiste, int dureePiste) throws SQLException {
		if (Album.AlbumExiste(IdAlbum)) {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM PISTE where IdAlbum = ? and titrePiste = ?");
			statement.setInt(1,IdAlbum);
			statement.setString(2, titrePiste);
			ResultSet resultat = statement.executeQuery();
			
			/**Piste existe déja?**/
			if (resultat.next()) {
				System.out.println("Le piste est déjà presente dans l'album.");
			}
			else {
				int nbPiste = Album.getNbPiste(IdAlbum);
				assert nbPiste != -1;
				
				statement = BdClass.getConnection().prepareStatement("INSERT INTO PISTES(IdAlbum, numPiste, titrePiste, dureePiste) values(?,?,?,?)");
				statement.setInt(1, IdAlbum);
				statement.setInt(2, nbPiste+1);
				statement.setString(3, titrePiste);
				statement.setInt(4, dureePiste);
				statement.executeQuery();
				
				/**Association Piste et fichier**/
				//TODO 
			}
		}
	}
}
