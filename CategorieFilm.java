import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CategorieFilm {
	
	public static boolean ExisteCategFilm(String categorie) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement("Select * From CategorieFilm where categorie = ?");
		statement.setString(1, categorie);
		ResultSet resultat =statement.executeQuery();
		return resultat.next();
	}
		
	public static void ajouterCategFilm(String categorie) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO CategorieFilm (categorie) values(?)");
		statement.setString(1, categorie);
		statement.executeQuery();
		BdClass.getConnection().commit();
	}
	
	public static void lireCategFilm(Scanner scanner) throws SQLException{
		System.out.println("Inserer la categorie du film svp");
		String categorie = scanner.nextLine();
		boolean existe = ExisteCategFilm(categorie);
		if (existe) {
			System.out.println("Cette catégorie de film est déjà enregistré dans la base de données");
		}
		else {
			ajouterCategFilm(categorie);
		}
	}
}


