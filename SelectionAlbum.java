import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectionAlbum {
	public static void readInfoSelectionAlbum() throws SQLException {
		System.out.println("Veuillez préciser le type de recherche en tapant : C (Categorie) ou T (Tout)");
		String type = Klex.scanner.nextLine();
		switch(type) {
		case "C" :
			SelectAlbumCateg();
			break;
		case "T" :
			SelectAlbumTout();
			break;
			}
	}
	
	public static void SelectAlbumCateg() throws SQLException {
		/**Assurer que le categorie existe**/
		System.out.println("Veuillez tapez la catégorie d'un album");
		String categorie = Klex.scanner.nextLine();
		while(!CategorieMusique.ExisteCategMusique(categorie)) {
			System.out.println("Un tel categorie n'existe pas. Veuillez tapez une autre.");
			categorie = Klex.scanner.nextLine();
		}
		
		/**Affichage**/
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Album WHERE CATEGORIE LIKE '%?%' order by titre");
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
	
	public static void SelectAlbumTout() throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Album order by titre");
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
