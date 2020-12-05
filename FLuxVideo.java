

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FLuxVideo {
	
	// Je suppose qu'il y aura une classe "Flux" mère des classes "FluxVideo", "FluxAudio", ... , et c'est elle qui s'occupera des données communes aux flux.
	
	//----------------------------------------------------------------------------------------------------------------//
	//																												  //
	//																												  //
	//					Fonction créant un flux vidéo en lui passant les paramètres adéquats.						  //
	//																												  //
	//																												  //
	//																												  //
	//----------------------------------------------------------------------------------------------------------------//
	
	public static void addFluxVideo(int idFichier, String langue, String codec, int debit, int resLargeurVid, int resHauteurVid) throws SQLException {
		
		// on pourra remonter la recherche dans la classe mère
		
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO Flux (Debit, Type, ResLargeurVid, ResLongeurVid, Codec, Langue, IdFichier) values(?,\'video\',?,?,?,?,?)");

		statement.setInt(1, debit);
		statement.setInt(2, resLargeurVid);
		statement.setInt(3, resHauteurVid);
		statement.setString(4, codec);
		statement.setString(5, langue);
		statement.setInt(6, idFichier);

		statement.executeQuery();
		

		BdClass.getConnection().commit();

	}
	
		//----------------------------------------------------------------------------------------------------------------//
		//																												  //
		//																												  //
		//					Fonction supprimant un flux video en lui passant les paramètres adéquats.					  //
		//																												  //
		//																												  //
		//																												  //
		//----------------------------------------------------------------------------------------------------------------//
	
	public static void deleteFluxVideo(int noFLux) throws SQLException {
			
			
			PreparedStatement statement = BdClass.getConnection().prepareStatement("DELETE FROM Flux where noFlux = ? and type = \'video\' ");
	
			
			statement.setInt(1, noFLux);
			//s tatement.setString(4, langue);
			
			
			statement.executeQuery();
			
	
			BdClass.getConnection().commit();
	
		}
	
	
}
