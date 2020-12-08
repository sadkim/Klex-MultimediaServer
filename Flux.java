import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Flux {
		//----------------------------------------------------------------------------------------------------------------//
		//																												  //
		//																												  //
		//					Fonction créant un flux en lui passant les paramètres adéquats.								  //
		//																												  //
		//																												  //
		//																												  //
		//----------------------------------------------------------------------------------------------------------------//
		
		public static void addFlux(String type, int idFichier, String langue, String codec, int debit, int resLargeurVid,
				int resHauteurVid, int echantillonage) throws SQLException {
						
			PreparedStatement statement = BdClass.getConnection().prepareStatement(
					"INSERT INTO Flux (NoFlux, Debit, ResLargeurVid, ResLongeurVid,Echantillonage, Codec, Langue, IdFichier,Type)"
					+ "values(NoFluxSeq.nextval,?,?,?,?,?,?,?, ?)");
			
			
			statement.setInt(1, debit);
			if (resLargeurVid == 0) { statement.setNull(2, java.sql.Types.INTEGER); }
			else {statement.setInt(2, resLargeurVid);}
			
			if (resHauteurVid == 0) {statement.setNull(3, java.sql.Types.INTEGER);}
			else {statement.setInt(3, resHauteurVid);}

			if (echantillonage == 0) {statement.setNull(4, java.sql.Types.INTEGER);}
			else {statement.setInt(4, echantillonage);}
			
			statement.setString(5, codec);
			statement.setString(6, langue);
			statement.setInt(7, idFichier);
			statement.setString(8, type);
			statement.executeQuery();
			//if (statement.executeQuery().next()) BdClass.getConnection().commit(); TODO : le commit est maintenant dans fichier
			//else BdClass.getConnection().rollback();
		}
		
			//----------------------------------------------------------------------------------------------------------------//
			//																												  //
			//																												  //
			//					Fonction supprimant un flux en lui passant le numéro et le type du flux.					  //
			//																												  //
			//																												  //
			//																												  //
			//----------------------------------------------------------------------------------------------------------------//
		
		public static void deleteFlux(int noFLux, String type) throws SQLException {
				
				
				PreparedStatement statement = BdClass.getConnection().prepareStatement(
						"DELETE FROM Flux where noFlux = ? and type = ? ");
		
				
				statement.setInt(1, noFLux);
				statement.setString(2, type);
				
				
				statement.executeQuery();
				
		
				if (statement.executeQuery().next()) BdClass.getConnection().commit();
				else BdClass.getConnection().rollback(); //TODO : à vérifier 
		
			}
		
}
