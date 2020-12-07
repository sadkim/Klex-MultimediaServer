import java.sql.SQLException;
import java.sql.Savepoint;

/** Un routine pour confirmer une insertion qui n'est pas fait en cascade **/

public class Confirmation {

	public static boolean confirmerSansCascade(String message) throws SQLException {
		while (true){
			System.out.println(message);
			String reponse = Klex.scanner.nextLine();
			switch(reponse){
				case "Y":
					BdClass.getConnection().commit(); 
					return true;
				case "N":
					BdClass.getConnection().rollback();
					return false;
				default:
					System.out.println("Mauvaise réponse");	
			}
		}	
	}

	public static boolean confirmerAvecCascade(String message, Savepoint svpt) throws SQLException {
		while (true){
			System.out.println(message);
			String reponse = Klex.scanner.nextLine();
			switch(reponse){
				case "Y":
					return true;	
				case "N":
					BdClass.getConnection().rollback(svpt);
					return false;
				default:
					System.out.println("Mauvaise réponse");	
			}
		}	
	}
}
