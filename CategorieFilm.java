import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CaategorieFilm {

	public static void CategoriesFilmDispo() throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM CategorieFilm ");
		ResultSet resultat =statement.executeQuery();
		while(resultat.next()) {
			System.out.println(resultat.getString("categorie"));
		}
	}
	
	public static void addCategorie(String categorie) throws SQLException {
		addCategorie(categorie, false);
	}

	public static void addCategorie(String categorie, boolean onCascade) throws SQLException {
		if (categorieExist(categorie)){
			System.out.println("la categorie des films existe déjà");
			return;
		}

		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO CategorieFilm (categorie) values(?)");
		
		statement.setString(1, categorie);
		statement.executeQuery();
		if (!onCascade){
			Confirmation.confirmerSansCascade();
		}
	}

	public static boolean categorieExist(String c) throws SQLException {
		PreparedStatement s = BdClass.getConnection().prepareStatement("Select * From CategorieFilm where categorie = ?");
		s.setString(1, c);
		ResultSet r = s.executeQuery();
		return r.next();
	}
}
