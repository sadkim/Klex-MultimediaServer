import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Album {
	
	
	public static void addAlbum(String titre, String Artiste,String dateSortie, String URL) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM ALBUM where TitreAlbum = ? and NumArtiste = ?");
		statement.setString(1,titre);
		
		/**Cherche numéro de l'artiste associé**/
		//TODO
		int NumArtiste = Artist.getNumArtiste();	//pas fini
		statement.setInt(2,NumArtiste);
		ResultSet resultat = statement.executeQuery();
		
		if (resultat.next()) {		//Un tel titre + numéro d'artist existe.
			System.out.println("L'album est déjà dans le BD.");
		}
		else {
			statement = BdClass.getConnection().prepareStatement("INSERT INTO Album(idAlbum, TitreAlbum, NumArtiste, DateSortieAlbum, URLimgPochette) "
					+ "values(idAlbumSeq.nextval, ?, ?, TO_DATE(?, ‘YYYY-MM-DD’), ?)");
			statement.setString(1, titre);
			statement.setInt(2, numArtiste);
			statement.setString(3, dateSortie);
			statement.setString(4, URL);
			statement.executeQuery();
			BdClass.getConnection().commit();
		}
	}
	
	public static void searchAlbum(String titre, int numArtiste) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM ALBUM where TitreAlbum = ? and NumArtiste = ?");
		ResultSet resultat = statement.executeQuery();
		while (resultat.next()) {
			System.out.println("Titre Album : " + resultat.getString("TitreAlbum") + "|" + "NumArtiste");
		}
	}
	
	/**Renvoyer le nombre de pistes dans un album
	 * Renvoyer -1 sinon**/
	public static int getNbPiste(int IdAlbum) throws SQLException {
		if (AlbumExiste(IdAlbum)) {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM ALBUM where IdAlbum = ?");
			statement.setInt(1, IdAlbum);
			ResultSet resultat = statement.executeQuery();
			int nbPiste = 0;
			while (resultat.next()) {
				nbPiste++;
				}
			return nbPiste;
			}
		return -1;
	}
	
	/**Renvoyer true si un tel album existe**/
	public static boolean AlbumExiste(int IdAlbum) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM ALBUM where IdAlbum = ?");
		statement.setInt(1, IdAlbum);
		ResultSet resultat = statement.executeQuery();
		if (resultat.next()) {
			return true;
		}
		return false;
	}
}
