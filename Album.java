import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

public class Album {
	
	public static void addAlbum (String titre, String Artiste,String dateSortie, String URL) throws SQLException {
		
		if (!Artist.ArtisteExiste(Artiste)){
			System.out.println("l'artiste pricipale n'est pas enregistré dans la base de donnée, créez le tout d'abord");
			return;
		}
		
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"SELECT * FROM ALBUM where TitreAlbum = ? and NumArtiste = ?");
		statement.setString(1,titre);
		
		/* Cherche numéro de l'artiste associé */

		int NumArtiste = Artist.getNumArtiste(Artiste);
		statement.setInt(2,NumArtiste);
		ResultSet resultat = statement.executeQuery();
		
		if (resultat.next()) {		//Un tel titre + numéro d'artist existe.
			System.out.println("L'album est déjà dans le BD.");
		}
		else {
			statement = BdClass.getConnection().prepareStatement(
					"INSERT INTO Album(idAlbum, TitreAlbum, NumArtiste, DateSortieAlbum, URLimgPochette) "
					+ "values(idAlbumSeq.nextval, ?, ?, TO_DATE(?, ‘YYYY-MM-DD’), ?)");
			
			statement.setString(1, titre);
			statement.setInt(2, NumArtiste);
			statement.setString(3, dateSortie);
			statement.setString(4, URL);
			statement.executeQuery();
			BdClass.getConnection().commit();
		}
		/* Forcer l'ajout d'une categorie  : puisque l'ajout des pistes n'est pas obligatoires on commit apres l'ajout 
		 * des categories */
		System.out.println("Maintenant il faut ajouter une categorie");
		boolean contrainteSatis = false;
		boolean fini = false;
		while (!fini || !contrainteSatis){
			System.out.println("Tapez Categoriser pour associer l'album à une categorie existante");
			System.out.println("Tapez nouvelleCategorie si vous avez besoin de créer une nouvelle catégorie");
			System.out.println("Tapez annuler pour annuler la création de l'album");
			System.out.println("Tapez fini si vous avez voulez confirmer la creation de l'album");
				
			String commande = Klex.scanner.nextLine();
			switch (commande) {
				case "Categoriser":
					Savepoint svptCategorisation = BdClass.getConnection().setSavepoint("svpCategorisation");
				    // TODO  Appeler un lecteur de commande et pas add 	
					break;

				case "nouvelleCategorie":
					// TODO : avec des savepoints !!
					break;
				
				case "annuler":
					BdClass.getConnection().rollback();
					return;
				case "fini" :
					if (!contrainteSatis){
						System.out.println("vous devez associer au moins une catégorie à cet album ");
						break;
					}
					fini = true;
					BdClass.getConnection().commit();
					return;
			}
		}
		
		/* On suggere la creation d'une piste au moins : on renvoie vers la creation des pistes mais en
		 * enregistrant l'id de l'album */
		System.out.println("Voulez vous ajouter des pistes a cet album ? [Y/N]");
	}
	
	public static void searchAlbum(String titre, int numArtiste) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"SELECT * FROM ALBUM where TitreAlbum = ? and NumArtiste = ?");
		
		ResultSet resultat = statement.executeQuery();
		
		while (resultat.next()) {
			System.out.println("Titre Album : " + resultat.getString("TitreAlbum") + "|" + "NumArtiste");
		}
	}
	
	/**Renvoyer le nombre de pistes dans un album
	 * Renvoyer -1 sinon**/
	public static int getNbPiste(int IdAlbum) throws SQLException {
		if (AlbumExiste(IdAlbum)) {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM ALBUM where IdAlbum = ?");
			statement.setInt(1, IdAlbum);
			ResultSet resultat = statement.executeQuery();
			int nbPiste = 0;
			while (resultat.next()) {
				nbPiste++;
				}
			return nbPiste;
			}
		return -1;
	}
	

	/**Renvoyer true si un tel album existe**/
	public static boolean AlbumExiste(int IdAlbum) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM ALBUM where IdAlbum = ?");
		statement.setInt(1, IdAlbum);
		ResultSet resultat = statement.executeQuery();
		if (resultat.next()) {
			return true;
		}
		return false;
	}
}
