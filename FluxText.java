

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FluxText {
	
	
	//----------------------------------------------------------------------------------------------------------------//
	//																												  //
	//																												  //
	//					Fonction créant un flux text en lui passant les paramètres adéquats.						  //
	//																												  //
	//																												  //
	//																												  //
	//----------------------------------------------------------------------------------------------------------------//
	
	public static void addFluxText(int idFichier, String langue, String codec, int debit) throws SQLException {
		
		
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO Flux (Debit, Type, Codec, Langue, IdFichier) values(?,\'text\',?,?,?)");

		statement.setInt(1, debit);
		statement.setString(2, codec);
		statement.setString(3, langue);
		statement.setInt(4, idFichier);

		statement.executeQuery();
		
		BdClass.getConnection().commit();

	}
	
		//----------------------------------------------------------------------------------------------------------------//
		//																												  //
		//																												  //
		//					Fonction supprimant un flux text en lui passant les paramètres adéquats.					  //
		//																												  //
		//																												  //
		//																												  //
		//----------------------------------------------------------------------------------------------------------------//
	
	public static void deleteFluxText(int noFLux) throws SQLException {
			
			// on pourra remonter la recherche dans la classe mère
			
			PreparedStatement statement = BdClass.getConnection().prepareStatement("DELETE FROM Flux where noFlux = ? and type = \'text\' ");
	
			
			statement.setInt(1, noFLux);
			//s tatement.setString(4, langue);
			
			
			statement.executeQuery();
			
	
			BdClass.getConnection().commit();
	
		}
	
	
}
