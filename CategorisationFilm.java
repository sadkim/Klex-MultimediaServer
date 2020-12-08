import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class CategorisationFilm {

	/** Permet de savoir si le film et la categorie concerne existe deja dans la base de donnee **/
	public static boolean ElementsExiste(String titre, int anneeSortie, String categorie) throws SQLException {
		return Film.existeFilm(titre, anneeSortie) && CategorieFilm.ExisteCategFilm(categorie);
	}
	
	/** Permet de savoir si la categorisation est deja faite **/
	public static boolean CategorisationFilmExist(String titre, int anneeSortie, String c) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From CategorisationFilm where Titre = ?  and anneeSortie = ? and categorie = ? ");

		statement.setString(1, titre);
		statement.setInt(2, anneeSortie);
		statement.setString(3, c);
		ResultSet resultat =statement.executeQuery();
		return resultat.next();
	}
	
	/** Permet d'ajouter un film  dans une categorie directement depuis l'interface Klex **/
	public static boolean readInfoCategoFilm(){
		System.out.println("inserez les donnees du film ");
		//TODO : ajout de la categorisation depuis l'exterieur 
		return false;
	}
	
	/** Permet de categoriser un film  directement apres sa creation : sans commiter **/
	public static boolean readInfoCategoFilm(String titre , int anneeSortie, boolean enCascade) {
		
		System.out.println("Insérez le nom de la catégorie");
		String categorie = Klex.scanner.nextLine();
		try{
			return associerFilmCateg(titre, anneeSortie, categorie, enCascade);
		} catch (SQLException e){
			System.out.println("la catégorisation n'est pas faite");
			e.printStackTrace();
		}
		return false;
	}
	
	/** Faire l'association **/
	public static boolean associerFilmCateg(String titre, int anneeSortie, String categorie, boolean enCascade) 
		throws SQLException {
			boolean existe = ElementsExiste(titre, anneeSortie, categorie);
			if (!existe){
				System.out.println("le film ou la catégorie n'existe pas");
				return false;
			}
			boolean assoExistant = CategorisationFilmExist(titre, anneeSortie, categorie);
			if (assoExistant){
				System.out.println("le film appartient déjà à cette catégorie");
				return false;
			}
			ajouterCategFilm(titre, anneeSortie, categorie, enCascade);
			return true;
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

