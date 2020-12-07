import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public abstract class Selection {
	/* Permet à l'utilisateur de faire une selection*/
	
	public static void readInfoSelection() {
		System.out.println("Veuillez choisir le type de contenu que vous voulez afficher: F (Film), A (Album), P(Piste)");
		String type = Klex.scanner.nextLine();
		switch(type) {
			case "F":
				System.out.println("Veuillez tapez le categorie de Film");
				String categorieFilm = Klex.scanner.nextLine();

				break;
				
			case "A" :

				break;
				
			case "P" :

				break;				
		}
	}
	public abstract void readInfoSelectionType() throws SQLException;
	
	public static void SelectPiste(String categorie) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Pistes WHERE CATEGORIE LIKE '%?%'");
		statement.setString(1, categorie);
		ResultSet resultat = statement.executeQuery();
		int index = 1;
		while (resultat.next()) {
			String TitrePiste = resultat.getString("titrePiste");
			int dureePiste = resultat.getInt("dureePiste");
			System.out.println(index + "  Titre : " + TitrePiste + "|" + "Durée : " + dureePiste);
			index++;
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
