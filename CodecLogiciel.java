import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Savepoint;

public class CodecLogiciel {
	
	public static boolean assoCodLogicielExist(String marqueLogiciel, String modeleLogiciel, String c) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From CodecLogiciel where marqueLogiciel = ?   and modeleLogiciel = ? and Codec = ? ");

		statement.setString(1, marqueLogiciel);
		statement.setString(2, modeleLogiciel);
		statement.setString(3, c);

		ResultSet resultat =statement.executeQuery();

		return resultat.next();
	}
	
	public static void addAssoLogicielCodec(String marqueLogiciel, String modeleLogiciel, String codec) throws SQLException {
		addAssoLogicielCodec(marqueLogiciel, modeleLogiciel, codec, false);
	}
	
	public static void addAssoLogicielCodec(String marqueLogiciel, String modeleLogiciel, String codec, boolean enCascade) 
		throws SQLException {
		if (assoCodLogicielExist(marqueLogiciel, modeleLogiciel, codec)){
			System.out.println("le codec et le logiciel sont deja associes");
			return;	
		}
		if (!Codec.codecExist(codec)){
			System.out.println("le codec n'existe pas, cree le tout d'abord");
			return;
		}
		
		// dans ce cas il faut verifier si les donnees existe deja
		if (!enCascade && !Logiciel.logicielExist(marqueLogiciel, modeleLogiciel)){
			System.out.println("le logiciel n'existe pas, cree le tout d'abord");
			return;	
		}

		PreparedStatement s = BdClass.getConnection().prepareStatement(
			"insert into CodecLogiciel(MarqueLogiciel, ModeleLogiciel, Codec) values (?, ?, ?)");
		s.setString(1,marqueLogiciel);
		s.setString(2, modeleLogiciel);
		s.setString(3, codec);
		s.executeQuery();

		if (!enCascade){
			/* On annule seulement dans ce cas : en cas d'insertions en cascade des savepoints sont utilises */
			Confirmation.confirmerSansCascade();
		}
	}
}	
