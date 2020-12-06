import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategorisationFilm {

	/** Permet de savoir si le film et la categorie concerne existe deja dans la base de donnee **/
	public static boolean ElementsExiste(String titre, int anneeSortie, String categorie) throws SQLException {
		return Film.existeFilm(titre, anneeSortie) && CategorieFilm.ExisteCategFilm(categorie);
	}
	
	/** Permet de savoir si la categorisation est deja faite **/
	public static boolean CategorisationFilmExist(String titre, int anneeSortie, String c) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From CategorisationFilm where Titre = ?  and anneeSortie = ? and c = ? ");

		statement.setString(1, titre);
		statement.setInt(2, anneeSortie);
		statement.setString(3, categorie);
		ResultSet resultat =statement.executeQuery();
		return resultat.next();
	}
	
	/** Permet d'ajouter un film  dans une categorie directement depuis l'interface Klex **/
	public static boolean readInfoCategoFilm(){
		System.out.println("inserez les donnees du film ");
		//TODO : ajout de la categorisation depuis l'exterieur 
	}
	
	/** Permet de categoriser un film  directement apres sa creation : sans commiter **/
	public static boolean readInfoCategoFilm(String titre , int anneeSortie, boolean enCascade) {
		
		System.out.println("Insérez le nom de la catégorie");
		String categorie = Klex.scanner;
		return associerFilmCateg(titre, anneeSortie, enCascade);

	}
	
	/** Faire l'association **/
	public static boolean associerFilmCateg(String titre, int anneeSortie, String categorie) throws SQLException {
		boolean existe = ElementsExiste(titre, anneeSortie, categorie);
		boolean assoExistant = categorisationFilmExist(titre, anneeSortie, categorie);
		if (existe && !assExistant) {
			ajouterCategFilm(titre, anneeSortie, categorie);
			return true;
		}
		return false;
	}
	
	/** Permet d'ajouter la categorisation **/
	public static void ajouterCategFilm(String titre, int anneeSortie, String categorie, boolean enCascade) throws SQLException{
		
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"INSERT INTO CategorisationFilm (titre, anneeSortie, categorie) values(?,?,?)");
		
		statement.setString(1, titre);
		statement.setInt(2, anneeSortie);
		statement.setString(3, categorie);
		statement.executeQuery();
		if (!enCascade){
			Confirmation.confirmerSansCascade("Voulez vous confirmer cette categorisation ? ");
		}
	}
}

