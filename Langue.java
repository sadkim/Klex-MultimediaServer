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
	public static void ajouterLangue(String langue) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO Langue (langue) values(?)");
		statement.setString(1, langue);
		statement.executeQuery();
		BdClass.getConnection().commit();
	}
}
