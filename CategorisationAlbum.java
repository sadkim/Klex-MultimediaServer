import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategorisationAlbum {
	
	/** Permet de savoir si une categorie existe deja dans la base de donnee **/
	public static boolean CategorisationAlbumExist(int idAlbum, String categorie) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From CategorisationAlbum where idAlbum = ? and categorie = ? ");

		statement.setInt(1, idAlbum);
		statement.setString(2, categorie);
		ResultSet resultat =statement.executeQuery();
		return resultat.next();
	}
	
	/** Permet d'ajouter un album dans une categorie directement depuis l'interface Klex **/
	public static boolean readInfoCategoAlb() throws SQLException{
		System.out.println("inserez le nom de l'album");
		//TODO : ajout de la categorisation depuis l'exterieur 
		return false;
	}
	
	/** Permet de categoriser un album directement apres sa creation : sans commiter **/
	public static boolean readInfoCategoAlb(int idAlbum , boolean enCascade) throws SQLException{
		
		// dans ce cas il faut verifier si les donnees existe deja
		if (!enCascade && !Album.AlbumExiste(idAlbum )){
				System.out.println("l'album n'existe pas dans la base");
				return false;
		}

		System.out.println("Insérez le nom de la catégorie");
		String categorie = Klex.scanner.nextLine();
		if (CategorieMusique.ExisteCategMusique(categorie)){
			return addCategorisationAlbum(idAlbum , categorie,enCascade);
		}
		else{
			System.out.println("la catégorie n'existe pas");
			return false;
		}
	}
	
	
	public static boolean addCategorisationAlbum (int idAlbum , String categorie, boolean enCascade) throws SQLException {
		
		/* voir si la categorisation existe deja */
		if (CategorisationAlbumExist(idAlbum , categorie)) {
			System.out.println("l'album appartient déjà à cette catégorie");
			return false;	
		}
		
		PreparedStatement s = BdClass.getConnection().prepareStatement(
				"insert into CategorisationAlbum(idAlbum , categorie) values (?, ?)");
		
		s.setInt(1,idAlbum );
		s.setString(2, categorie);
		s.executeQuery();
		if (!enCascade){
			/* On annule seulement dans ce cas : en cas d'insertions en cascade des savepoints sont utilises */
			Confirmation.confirmerSansCascade("Voulez vous confirmer cette catégorisation ? ");
		}
		return true;
	}
}
	
