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
				System.out.println("Veuillez tapez le categorie de Film");
				String categorieFilm = Klex.scanner.nextLine();
				while(!CategorieFilm.ExisteCategFilm(categorieFilm)) {
					System.out.println("Aucun film de ce categorie n'existe.");
					System.out.println("Veuillez tapez un autre categorie.");
					categorieFilm = Klex.scanner.nextLine();
				}
				SelectFilm(categorieFilm);
				break;
				
			case "A" :
				System.out.println("Veuillez tapez la categorie de musique");
				String categorieMusique = Klex.scanner.nextLine();
				while(!CategorieMusique.ExisteCategMusique(categorieMusique)) {
					System.out.println("Aucun album de ce categorie n'existe.");
					System.out.println("Veuillez tapez un autre categorie.");
					categorieMusique = Klex.scanner.nextLine();
			}
				SelectAlbum(categorieMusique);
				break;
				
			case "P" :
				System.out.println("Veuillez tapez la categorie de piste");
				String categoriePiste = Klex.scanner.nextLine();
				while(!CategorieMusique.ExisteCategMusique(categoriePiste)) {
					System.out.println("Aucun album de ce categorie n'existe.");
					System.out.println("Veuillez tapez un autre categorie.");
					categoriePiste = Klex.scanner.nextLine();
			}
				SelectPiste(categoriePiste);
				break;				
		}
	}
	
	public static void SelectFilm(String categorie) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Film WHERE CATEGORIE LIKE '%?%'");
		statement.setString(1, categorie);
		ResultSet resultat = statement.executeQuery();
		int index = 1;
		while (resultat.next()) {
			String Titrefilm = resultat.getString("titre");
			int anneeSortie = resultat.getInt("anneeSortie");
			System.out.println(index + "  Titre : " + Titrefilm + "|" + "Année Sortie : " + anneeSortie);
			index++;
		}
	}

	
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
