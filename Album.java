import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Date;

public class Album {
	
	public static void addAlbum (String titre, String Artiste, Date dateSortie, String URL) throws SQLException {
		
		if (!Artist.ArtisteExiste(Artiste)){
			System.out.println("l'artiste pricipale n'est pas enregistré dans la base de donnée, créez le tout d'abord");
			return;
		}
		
		/* Cherche numéro de l'artiste associé */

		int NumArtiste;	
		if (Artist.ArtisteExiste(Artiste)){
			NumArtiste = Artist.getNumArtiste(Artiste);
		}
		else {
			/*Si l'artist saisi par l'utilisateur n'existe pas, faudra en créer un d'abord.*/
			System.out.println("L'artist n'existe pas dans le BD");
			return;
		}
		
		/* Test l'existence d'un Album avec ce titre + artiste */
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"SELECT * FROM ALBUM where TitreAlbum = ? and NumArtiste = ?");
		statement.setString(1,titre);
		statement.setInt(2,NumArtiste);
		ResultSet resultat = statement.executeQuery();
		if (resultat.next()) {		//Un tel titre + numéro d'artist existe.
			System.out.println("L'album est déjà dans le BD.");
		}
		else {
			/* Ajout d'un nouveau album */
			statement = BdClass.getConnection().prepareStatement(
				"INSERT INTO Album(idAlbum, TitreAlbum, NumArtiste, DateSortieAlbum, URLimgPochette) "
					+ "values(idAlbumSeq.nextval, ?, ?, ? , ?)");
			
			statement.setString(1, titre);
			statement.setInt(2, NumArtiste);
			statement.setDate(3, new java.sql.Date(dateSortie.getTime()));
			statement.setString(4, URL);
			statement.executeQuery();
			//BdClass.getConnection().commit();

			/* Forcer l'ajout d'une categorie  : puisque l'ajout des pistes n'est pas obligatoires on commit apres l'ajout 
		 	* des categories */
			int idAlbum = trouveIdentifiant(titre, NumArtiste,dateSortie,URL);
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
						boolean ajoute = CategorisationAlbum.readInfoCategoAlb(idAlbum , true);
						if (ajoute){
							contrainteSatis = contrainteSatis || Confirmation.confirmerAvecCascade(
								"Voulez vous confirmer la catégorisation [Y/N]", svptCategorisation);
						}
						break;

					case "nouvelleCategorie":
						Savepoint ajoutCategorie = BdClass.getConnection().setSavepoint("svpCategorie");
						CategorieMusique.lireCategMusique(true);
						boolean ajoute2 = Confirmation.confirmerAvecCascade( 
								"Voulez vous confirmer l'ajout de cette catégorie ? [Y/N]",
								ajoutCategorie);
						if (ajoute2){ System.out.println("maintenant, il faut ajouter l'album à cette catégorie"); }
						break;
				
					case "annuler":
						System.out.println("l'album sera supprimé avec les nouvelles catégories ajoutée avec lui");
						BdClass.getConnection().rollback();
						return;

					case "fini" :
						if (!contrainteSatis){
							System.out.println("vous devez associer au moins une catégorie à cet album ");
							break;
						}
						fini = true;
						BdClass.getConnection().commit(); //<<================ ici le commit 
						break;
					default :
						System.out.println("mauvais reponse");
				}
			}
			/* On suggere la creation d'une piste au moins : on renvoie vers la creation des pistes mais en
		 	* enregistrant l'id de l'album */
			System.out.println("Voulez vous ajouter des pistes a cet album ? [Y/N]");
			//TODO
		}
	}
	
	public static void searchAlbum (String titre, int numArtiste) throws SQLException {
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
	/**Tester l'existence d'un album à partir de son Identifiant**/
	public static boolean AlbumExiste(int IdAlbum) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM ALBUM where IdAlbum = ?");
		statement.setInt(1, IdAlbum);
		ResultSet resultat = statement.executeQuery();
		if (resultat.next()) {
			return true;
		}
		return false;
	}

	public static Integer trouveIdentifiant(String titre, int numArtiste, Date dateSortie, String URL) throws SQLException {
		
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
			"SELECT idALbum from Album WHERE TitreAlbum = ? and  NumArtiste = ? and DateSortieAlbum = ? and URLimgPochette =? ");

		statement.setString(1, titre);
		statement.setInt(2, numArtiste);
		statement.setDate(3, new java.sql.Date(dateSortie.getTime()));
		statement.setString(4, URL);
		statement.executeQuery();
		
		ResultSet resultat = statement.executeQuery();
		if (resultat.next()){
			return resultat.getInt("IdAlbum");
		}
		return null;
		
	}
}
