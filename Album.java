import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Album {
	private int nb_piste;
	
	public static void addAlbum(String titre, int numArtiste,String dateSortie, String URL) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM ALBUM where TitreAlbum = ? and NumArtiste = ?");
		statement.setString(1,titre);
		statement.setInt(2, numArtiste);
		ResultSet resultat = statement.executeQuery();
		
		if (resultat.next()) {		//Un tel titre + numéro d'artist existe.
			System.out.println("L'album est déjà dans le BD.");
		}
		else {
			statement = BdClass.getConnection().prepareStatement("insert into Album(idAlbum, TitreAlbum, NumArtiste, DateSortieAlbum, URLimgPochette) "
					+ "values(idAlbumSeq.nextval, ?, ?, TO_DATE(?, ‘YYYY-MM-DD’), ?)");
			statement.setString(1, titre);
			statement.setInt(2, numArtiste);
			statement.setString(3, dateSortie);
			statement.setString(4, URL);
			statement.executeQuery();
		}
	}
	
	public static void searchAlbum(String titre, int numArtiste) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM ALBUM where TitreAlbum = ? and NumArtiste = ?");
		ResultSet resultat = statement.executeQuery();
		while (resultat.next()) {
			System.out.println("Titre Album : " + resultat.getString("TitreAlbum") + "|" + "NumArtiste");
		}
	}
}
