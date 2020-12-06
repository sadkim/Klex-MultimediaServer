import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
	
public class CategorieFilm {
	
	/** Permet de savoir si une categorie des films exist deja dans la base de donnees **/
	public static boolean ExisteCategFilm(String categorie) throws SQLException{

		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From CategorieFilm where categorie = ?");
		statement.setString(1, categorie);
		ResultSet resultat =statement.executeQuery();
		return resultat.next();
	}

	/** Permet d'ajouter une categorie directement depuis l'interface Klex **/
	public static void lireCategFilm () throws SQLException {
		lireCategFilm (false);
	}

	/** Permet d'ajouter une categorie lors de la creation d'un film avec onCascade a true on ne commite pas **/
	public static void lireCategFilm (boolean enCascade) throws SQLException{
		System.out.println("Inserer la categorie du film svp");
		String categorie = Klex.scanner.nextLine();
		boolean existe = ExisteCategFilm(categorie);
		if (existe) {
			System.out.println("Cette catégorie de film est déjà enregistré dans la base de données");
		}
		else {
			ajouterCategFilm(categorie, enCascade);
		}
	}
	
		
	public static void ajouterCategFilm(String categorie, boolean onCascade) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"INSERT INTO CategorieFilm (categorie) values(?)");
		
		statement.setString(1, categorie);
		statement.executeQuery();
		if (!onCascade){
			Confirmation.confirmerSansCascade();
		}
	}
	
	/** Affiche l'ensemble des categories disponibles **/
	public static void CategoriesFilmDispo() throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM CategorieFilm ");
		ResultSet resultat =statement.executeQuery();
		while(resultat.next()) {
			System.out.println(resultat.getString("categorie"));
		}
	}
}
