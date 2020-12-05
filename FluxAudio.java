

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FluxAudio {
	
	
	//----------------------------------------------------------------------------------------------------------------//
	//																												  //
	//																												  //
	//					Fonction créant un flux audio en lui passant les paramètres adéquats.						  //
	//																												  //
	//																												  //
	//																												  //
	//----------------------------------------------------------------------------------------------------------------//
	
	public static void addFluxAudio(int idFichier, String langue, String codec, int debit, int echantillonage) throws SQLException {
		
		// on pourra remonter la recherche dans la classe mère
		
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO Flux (Debit, Type, echantillonage, Codec, Langue, IdFichier) values(?,\'audio\',?,?,?,?)");

		statement.setInt(1, debit);
		statement.setInt(2, echantillonage);
		statement.setString(3, codec);
		statement.setString(4, langue);
		statement.setInt(5, idFichier);
		
		statement.executeQuery();
		

		BdClass.getConnection().commit();

	}
	

	//----------------------------------------------------------------------------------------------------------------//
	//																												  //
	//																												  //
	//					Fonction supprimant un flux audio en lui passant les paramètres adéquats.					  //
	//																												  //
	//																												  //
	//																												  //
	//----------------------------------------------------------------------------------------------------------------//
	
	public static void deleteFluxAudio(int noFLux) throws SQLException {
		
		
		PreparedStatement statement = BdClass.getConnection().prepareStatement("DELETE FROM Flux where noFlux = ? and type = \'audio\' ");

		
		statement.setInt(1, noFLux);
		
		
		statement.executeQuery();
		

		BdClass.getConnection().commit();

	}
	
	
}
