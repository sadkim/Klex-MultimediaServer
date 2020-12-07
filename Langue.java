import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Langue {
	public static void languesDisponibles() throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Langue");
		ResultSet resultat =statement.executeQuery();
		while(resultat.next()) {
			System.out.println(resultat.getString("langue"));
		}

	}
	
	public static boolean langueExiste(String langue) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM langue where langue = ?");
		statement.setString(1, langue);
		ResultSet resultat = statement.executeQuery();
		if(resultat.next()) {
			return true;
		}
		return false;
	}
	
	public static void ajouterLangue(String langue) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO Langue (langue) values(?)");
		statement.setString(1, langue);
		statement.executeQuery();
		BdClass.getConnection().commit();
	}
}
