import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Codec {
	
	/** Verifier l'existance d'un codec */
	public static boolean codecExist(String c) throws SQLException {
		PreparedStatement s = BdClass.getConnection().prepareStatement("Select * From Codec where codec = ?");
		s.setString(1, c);
		ResultSet r = s.executeQuery();
		return r.next();
	}

	/** Permet l'ajout d'un codec depuis la classe Klex **/
	public static boolean readCodecInfo() throws SQLException { 
		return readCodecInfo(false);
	}
	
	
	/** Permet l'ajout d'un codec depuis logiciel : sans commiter **/
	public static boolean readCodecInfo(boolean enCascade) throws SQLException{  
		System.out.println("Veuillez insérer le nom du codec");
		String codec = Klex.scanner.nextLine();
		return addCodec(codec, enCascade);	
	}
	
	/* si la creation est en cascade on ne commite pas */
	public static boolean addCodec(String c, boolean enCascade) throws SQLException { 

		if(Codec.codecExist(c)) { 
			System.out.println("le codec existe deja");
			return false;
		}

		PreparedStatement s = BdClass.getConnection().prepareStatement("insert into Codec(codec) values (?)");
		s.setString(1,c);
		s.executeQuery();

		if (!enCascade){
			/* On annule seulement dans ce cas : en cas d'insertions en cascade des savepoints sont utilises */
			Confirmation.confirmerSansCascade("Voulez vous confirmez l'ajout de ce codec ?");
		}
		return true; // On a bien ajoute un codec
	}
}	
