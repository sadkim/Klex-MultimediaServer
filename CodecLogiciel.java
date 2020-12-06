import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Savepoint;

public class CodecLogiciel {
	
	/** Permet de verifier si l'association existe deja **/
	public static boolean assoCodLogicielExist(String marqueLogiciel, String modeleLogiciel, String c) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From CodecLogiciel where marqueLogiciel = ?   and modeleLogiciel = ? and Codec = ? ");

		statement.setString(1, marqueLogiciel);
		statement.setString(2, modeleLogiciel);
		statement.setString(3, c);

		ResultSet resultat =statement.executeQuery();

		return resultat.next();
	}
	
	/** Permet d'ajouter une association codec logiciel depuis la classe Klex **/
	public static boolean readInfoAssCodLog()throws SQLException {
		System.out.println("Inserer la marque du logiciel svp");
    	String marqueLogiciel = Klex.scanner.nextLine();
    	
		System.out.println("Insérer le modèle du logiciel svp");
    	String modeleLogiciel = Klex.scanner.nextLine();
		
		if (!Logiciel.logicielExist(marqueLogiciel, modeleLogiciel)){
			System.out.println("Le logiciel n'existe pas");
			return false;
		}
		return readInfoAssCodLog(marqueLogiciel, modeleLogiciel, false);
	}
	
	/** Permet d'ajouter une association codec logiciel apres creation de logiciel **/

	public static boolean readInfoAssCodLog(String marqueLogiciel, String modeleLogiciel, boolean enCascade) throws SQLException{
		System.out.println("Veuillez insérer le nom du Codec");
		String c = Klex.scanner.nextLine();
			
		if(Codec.codecExist(c)) { 
			System.out.println("le codec n'existe pas dans la base ajoutez le");
			return false;
		}
		
		return addAssoLogicielCodec(marqueLogiciel, modeleLogiciel, c, enCascade);
	}
	
	public static boolean addAssoLogicielCodec(String marqueLogiciel, String modeleLogiciel, String codec, boolean enCascade) 
		throws SQLException {
		
		if (assoCodLogicielExist(marqueLogiciel, modeleLogiciel, codec)){
			System.out.println("le codec et le logiciel sont deja associes");
			return false;	
		}
		
		PreparedStatement s = BdClass.getConnection().prepareStatement(
			"insert into CodecLogiciel(MarqueLogiciel, ModeleLogiciel, Codec) values (?, ?, ?)");
		s.setString(1,marqueLogiciel);
		s.setString(2, modeleLogiciel);
		s.setString(3, codec);
		s.executeQuery();

		if (!enCascade){
			/* On annule seulement dans ce cas : en cas d'insertions en cascade des savepoints sont utilises */
			Confirmation.confirmerSansCascade("Voulez vous confirmer cette association ? ");
		}
		return true;
	}
}	
