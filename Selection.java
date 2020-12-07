import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Selection {
	/* Permet à l'utilisateur de faire une selection*/
	
	public static void readInfoSelection() throws SQLException {
		System.out.println("Veuillez choisir le type de contenu que vous voulez afficher: F (Film), A (Album), P(Piste)");
		String type = Klex.scanner.nextLine();
		switch(type) {
			case "F":
				SelectionFilm.readInfoSelectionFilm();
				break;
				
			case "P" :
				SelectionPiste.readInfoSelectionPiste();
				break;
				
			case "A" :
				SelectionAlbum.readInfoSelectionAlbum();
				break;				
		}
	}
	
	
	public static void SelectAlbum(String categorie) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Album WHERE CATEGORIE LIKE '%?%'");
		statement.setString(1, categorie);
		ResultSet resultat = statement.executeQuery();
		int index = 1;
		while (resultat.next()) {
			String TitreAlbum = resultat.getString("TitreAlbum");
			int numArtist = resultat.getInt("numArtist");
			String nomArtist = Artist.getNomArtiste(numArtist);
			System.out.println(index + "  Titre : " + TitreAlbum + "|" + "Artist : " + nomArtist);
			index++;
		}
	}
	
}
