import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategorisationPiste {

	/** Permet de savoir si une categorie existe deja dans la base de donnee **/
	public static boolean ElementsExiste(int idAlbum, int numPiste, String categorie) throws SQLException {
		return Piste.pisteExiste(idAlbum, numPiste) && CategorieMusique.ExisteCategMusique(categorie);
	}
	
	/** Permet de verifier si l'association existe deja ou pas **/
	public static boolean CategorisationPisteExist(int idAlbum , int numPiste, String categorie) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From CategorisationPiste where idAlbum = ? and numPiste = ? and categorie = ? ");

		statement.setInt(1, idAlbum);
		statement.setInt(2, numPiste);
		statement.setString(3, categorie);
		ResultSet resultat =statement.executeQuery();
		return resultat.next();
	}	
	
	/** Permet d'ajouter une piste  dans une categorie directement depuis l'interface Klex **/
	public static boolean readInfoCategoPiste(){
		System.out.println("inserez les donnees de la piste ");
		//TODO : ajout de la categorisation depuis l'exterieur 
	}
	
	/** Permet de categoriser une piste  directement apres sa creation : sans commiter **/
	public static boolean readInfoCategoPiste (int idAlbum , int numPiste, boolean enCascade) {
		
		System.out.println("Insérez le nom de la catégorie");
		String categorie = Klex.scanner;
		return associerPisteCateg(idAlbum, numPiste, enCascade);

	}
	
	/** Faire l'association **/
	public static boolean associerPisteCateg(int idAlbum, int numPiste, String categorie, boolean enCascade) throws SQLException {
		boolean existe = ElementsExiste(idAlbum, numPiste, categorie);
		boolean assoExistant = categorisationPisteExist(idAlbum, numPiste, categorie);
		if (existe && !assExistant) {
			return ajouterCategPiste(idAlbum, numPiste, categorie, enCascade);
		}
		return false;
	}

	public static boolean ajouterCategPiste(int idAlbum, int numPiste, String categorie, boolean enCascade) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"INSERT INTO CategorisationFilm (titre, anneeSortie, categorie) values(?,?,?)");
		statement.setInt(1, idAlbum);
		statement.setInt(2, numPiste);
		statement.setString(3, categorie);
		statement.executeQuery();
		if (!enCascade){
			Confirmation.confirmerSansCascade("Voulez vous confirmer cette categorisation ? ");
		}
	}
}
