import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CategorieMusique {
	
	public static boolean ExisteCategMusique(String categorie) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement("Select * From CategorieMusique where categorie = ?");
		statement.setString(1, categorie);
		ResultSet resultat =statement.executeQuery();
		return resultat.next();
	}
	
	
	public static void ajouterCategMusique(String categorie) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO CategorieMusique (categorie) values(?)");
		statement.setString(1, categorie);
		statement.executeQuery();
		BdClass.getConnection().commit();
	}
	
	public static void lireCategMusique(Scanner scanner) throws SQLException{
		System.out.println("Inserer la categorie de la piste svp");
		String categorie = scanner.nextLine();
		boolean existe = ExisteCategMusique(categorie);
		if (existe) {
			System.out.println("Cette catégorie de musique est déjà enregistré dans la base de données");
		}
		else {
			ajouterCategMusique(categorie);
		}
	}
}
