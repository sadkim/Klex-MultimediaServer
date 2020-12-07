import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectionFilm {	
	
	public static void readInfoSelectionFilm() throws SQLException {
		System.out.println("Veuillez préciser le type de recherche en tapant : C (Categorie) ou T (Tout)");
		String type = Klex.scanner.nextLine();
		switch(type) {
		case "C" :
			SelectFilmCateg();
			break;
		case "T" :
			SelectFilmTout();
			break;
			}
	}
	
	public static void SelectFilmCateg() throws SQLException {
		/**Assurer que le categorie existe**/
		System.out.println("Veuillez tapez la catégorie d'un film");
		String categorie = Klex.scanner.nextLine();
		while(!CategorieFilm.ExisteCategFilm(categorie)) {
			System.out.println("Un tel categorie n'existe pas. Veuillez tapez une autre.");
			categorie = Klex.scanner.nextLine();
		}
		
		/**Affichage**/
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Film WHERE CATEGORIE LIKE '%?%' order by titre");
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
	public static void SelectFilmTout() throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Film where ageMin < ? order by titre");
		ResultSet resultat = statement.executeQuery();
		int index = 1;
		while (resultat.next()) {
			String Titrefilm = resultat.getString("titre");
			int anneeSortie = resultat.getInt("anneeSortie");
			System.out.println(index + "  Titre : " + Titrefilm + "|" + "Année Sortie : " + anneeSortie);
			index++;
			}
	}

}
