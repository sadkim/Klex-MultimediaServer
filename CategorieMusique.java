import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategorieMusique {
	
	/** Permet de verifier si une categorie existe deja dans la base de donnee **/
	public static boolean ExisteCategMusique(String categorie) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From CategorieMusique where categorie = ?");
		statement.setString(1, categorie);
		ResultSet resultat =statement.executeQuery();
		return resultat.next();
	}
	
	/** Permet d'ajouter une categorie directement depuis l'interface Klex **/
	public static void lireCategMusique() throws SQLException {
		lireCategMusique(false);
	}
	
	/** Permet d'ajouter une categorie musique depuis un album ou piste : on ne commite pas **/
	public static void lireCategMusique(boolean enCascade) throws SQLException{
		
		System.out.println("Inserer la categorie de la piste svp");
		String categorie = Klex.scanner.nextLine();
		boolean existe = ExisteCategMusique(categorie);
		if (existe) {
			System.out.println("Cette catégorie de musique est déjà enregistré dans la base de données");
		}
		else {
			ajouterCategMusique(categorie, enCascade);
		}
	}	
	
	public static void ajouterCategMusique(String categorie, boolean onCascade) throws SQLException {

		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"INSERT INTO CategorieMusique (categorie) values(?)");
		statement.setString(1, categorie);
		statement.executeQuery();
		if (!onCascade){
			Confirmation.confirmerSansCascade("Voulez vous confirmer la création de cette catégorie? ");
		}	
	}
	
	/** Permet d'afficher l'ensembles des categories musicales disponibles dans la base de donnees **/
	public static void CategoriesMusiqueDispo() throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM CategorieMusique");
		ResultSet resultat =statement.executeQuery();
		while(resultat.next()) {
			System.out.println(resultat.getString("categorie"));
		}
	}
}
