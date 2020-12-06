import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CaategorieMusique {

	public static void CategoriesMusiqueDispo() throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM CategorieMusique");
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
			System.out.println("cette categorie de la musque existe déjà");
			return;
		}

		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"INSERT INTO CategorieMusique(categorie) values(?)");
		
		statement.setString(1, categorie);
		statement.executeQuery();
		if (!onCascade){
			Confirmation.confirmerSansCascade();
		}
	}

	public static boolean categorieExist(String c) throws SQLException {
		PreparedStatement s = BdClass.getConnection().prepareStatement("Select * From CategorieMusique where categorie = ?");
		s.setString(1, c);
		ResultSet r = s.executeQuery();
		return r.next();
	}
}
