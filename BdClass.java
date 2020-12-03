import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BdClass {
	private static Connection connection = null;


	public static void connect(String url,String  user,String passwd) {
		 try{
		    	DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		    	connection=DriverManager.getConnection(url, user, passwd);
		    	connection.setAutoCommit(false);
		    }catch(SQLException e){
		    	e.printStackTrace ();
				System.out.println("can nnot connect to database plz check your connection");
				System.exit(1);
		    }
	}

	public static Connection getConnection() {
		return connection;
	
	}
	public static void closeConnection() throws SQLException {
		connection.close();
		connection = null;
	}
	
}
