import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Codec {
	
	public static boolean codecExist(String c) throws SQLException {
		PreparedStatement s = BdClass.getConnection().prepareStatement("Select * From Codec where codec = ?");
		s.setString(1, c);
		ResultSet r = s.executeQuery();
		return r.next();
	}
}	
