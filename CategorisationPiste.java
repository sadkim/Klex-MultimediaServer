import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategorisationPiste {

	public static boolean ElementsExiste(int idAlbum, int numPiste, String categorie) throws SQLException {
		return Piste.existePiste(idAlbum, numPiste) && CategorieMusique.ExisteCategMusique(categorie);
	}
	
	public static void ajouterCategPiste(int idAlbum, int numPiste, String categorie) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO CategorisationFilm (titre, anneeSortie, categorie) values(?,?,?)");
		statement.setInt(1, idAlbum);
		statement.setInt(2, numPiste);
		statement.setString(3, categorie);
		statement.executeQuery();
		BdClass.getConnection().commit();
	}
	
	public static void associerPisteCateg(int idAlbum, int numPiste, String categorie) throws SQLException {
		boolean existe = ElementsExiste(idAlbum, numPiste, categorie);
		if (existe) {
			ajouterCategPiste(idAlbum, numPiste, categorie);
		}
	}
}