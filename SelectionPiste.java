import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectionPiste {
	
	public static void readInfoSelectionPiste() throws SQLException {
		System.out.println("Veuillez préciser le type de recherche en tapant : C (Categorie) ou T (Tout)");
		String type = Klex.scanner.nextLine();
		switch(type) {
		case "C" :
			SelectPisteCateg();
			break;
		case "T" :
			SelectPisteTout();
			break;
			}
	}
	
	public static void SelectPisteCateg() throws SQLException {
		/**Assurer que le categorie existe**/
		System.out.println("Veuillez tapez la catégorie d'un piste");
		String categorie = Klex.scanner.nextLine();
		while(!CategorieMusique.ExisteCategMusique(categorie)) {
			System.out.println("Un tel categorie n'existe pas. Veuillez tapez une autre.");
			categorie = Klex.scanner.nextLine();
		}
		
		/**Affichage**/
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Pistes WHERE CATEGORIE LIKE '%?%' order by titrePiste");
		statement.setString(1, categorie);
		ResultSet resultat = statement.executeQuery();
		int index = 1;
		while (resultat.next()) {
			String TitrePiste = resultat.getString("titrePiste");
			int dureePiste = resultat.getInt("dureePiste");
			System.out.println(index + "   Titre : " + TitrePiste + "|" + "Durée : " + dureePiste);
			index++;
		}
	}
	
	public static void SelectPisteTout() throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Pistes order by titrePiste");
		ResultSet resultat = statement.executeQuery();
		int index = 1;
		while (resultat.next()) {
			String TitrePiste = resultat.getString("titrePiste");
			int dureePiste = resultat.getInt("dureePiste");
			System.out.println(index + "   Titre : " + TitrePiste + "|" + "Durée : " + dureePiste);
			index++;
		}
	}
	
	
}
