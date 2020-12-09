import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Langue {

	/** Permet d'afficher les langues disponibles sur la base de donnée **/
	public static void languesDisponibles() throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Langue");
		ResultSet resultat =statement.executeQuery();
		while(resultat.next()) {
			System.out.println(resultat.getString("langue"));
		}

	}
	/** Permet de vérifier si la langue existe dans la base ou pas */
	public static boolean langueExiste(String langue) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM langue where langue = ?");
		statement.setString(1, langue);
		ResultSet resultat = statement.executeQuery();
		if(resultat.next()) {
			return true;
		}
		return false;
	}
	
	/** Permet d'ajouter la langue dans la base de donnée **/
	public static void ajouterLangue(String langue) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO Langue (langue) values(?)");
		statement.setString(1, langue);
		statement.executeQuery();
		BdClass.getConnection().commit();
	}
}
