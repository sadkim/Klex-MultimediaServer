import java.util.Scanner;
import java.sql.SQLException;

/** Un routine pour confirmer une insertion qui n'est pas fait en cascade **/

public class Confirmation {
	public static void confirmerSansCascade() throws SQLException {
		System.out.println("Vous confirmez cet ajout? [Y/N]");
    	boolean bienLu = false;
		while (!bienLu){
		String reponse = Klex.scanner.nextLine();
			switch(reponse){
				case "Y":
					bienLu = true;
					BdClass.getConnection().commit(); 
					break;
				case "N":
					bienLu = false;
					BdClass.getConnection().rollback();
					break;
				default:
					System.out.println("mauvais réponse");	
			}
		}	
	}
}
