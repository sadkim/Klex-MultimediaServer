import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategorisationFilm {

	public static boolean ElementsExiste(String titre, int anneeSortie, String categorie) throws SQLException {
		return Film.existeFilm(titre, anneeSortie) && CategorieFilm.ExisteCategFilm(categorie);
	}
	
	public static void ajouterCategFilm(String titre, int anneeSortie, String categorie) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO CategorisationFilm (titre, anneeSortie, categorie) values(?,?,?)");
		statement.setString(1, titre);
		statement.setInt(2, anneeSortie);
		statement.setString(3, categorie);
		statement.executeQuery();
		BdClass.getConnection().commit();
	}
	
	public static void associerFilmCateg(String titre, int anneeSortie, String categorie) throws SQLException {
		boolean existe = ElementsExiste(titre, anneeSortie, categorie);
		if (existe) {
			ajouterCategFilm(titre, anneeSortie, categorie);
		}
	}
}
