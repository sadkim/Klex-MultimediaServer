import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Savepoint;

public class CategorisationAlbum {
	
	public static boolean CategorisationAlbumExist(int idAlbum, String categorie) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From CategorisationAlbum where idAlbum = ? and categorie = ? ");

		statement.setInt(1, idAlbum);
		statement.setString(2, categorie);
		ResultSet resultat =statement.executeQuery();
		return resultat.next();
	}

	// TODO : on ne va pas evidemment demander un idALbum sauf si c'est en cascade : cree depuis la classe album
	public static void addCategorisationAlbum(int IdAlbum , String categorie) throws SQLException {
		addAssoLogicielCodec(marqueLogiciel, modeleLogiciel, codec, false);
	}
	
	public static void addCategorisationAlbum (int idAlbum , String categorie boolean enCascade) throws SQLException {
		
		/* voir si la categorisation existe deja */
		if (CategorisationAlbumExist(idAlbum , categorie)) {
			System.out.println("l'album appartient déjà à cette catégorie");
			return;	
		}
		if (!categorieExist(categorie)){
			System.out.println("la catégorie n'est pas enregistrée dans la base, créez la tout d'abord");
			return;
		}
		
		// dans ce cas il faut verifier si les donnees existe deja
		if (!enCascade && !AlbumExiste(idAlbum ) {
				System.out.println("l'album n'existe pas dans la base");
				return;
		}

		PreparedStatement s = BdClass.getConnection().prepareStatement(
			"insert into CategorisationAlbum(idAlbum , categorie) values (?, ?)");
		
		s.setInt(1,idAlbum );
		s.setString(2, categorie);
		s.executeQuery();
		if (!enCascade){
			/* On annule seulement dans ce cas : en cas d'insertions en cascade des savepoints sont utilises */
			Confirmation.confirmerSansCascade();
		}
	}
}
	
