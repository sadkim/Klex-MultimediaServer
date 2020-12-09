import java.sql.SQLException;
import java.sql.Savepoint;

/** Un routine pour confirmer une insertion qui n'est pas fait en cascade **/

public class Confirmation {
	
	/** Permet de confirmer des insertions , des suppressions faites depuis l'interface Klex 
	 * @param message : le message à affichier à l'utilisateur 
	 * @return un boolean indiquant si l'opération est confirmée ou annulée
	 */
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

	/** Permet de confirmer ou annuler des insertions qui ont faits depuis d'autres classes 
	 * @param message : le message à afficher
	 * @param svpt : le savepoint pour faire le rollback au cas d'une annulation 
	 * @return un boolean indiquant si l'opération est confirmée ou annulée
	 */
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
