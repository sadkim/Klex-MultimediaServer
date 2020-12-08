import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

public class Album {
	
	private static List<Filtre> filtres = new ArrayList<Filtre>();
	
	public static void addFilter(Filtre filtre) {
		Album.filtres.add(filtre);
	}
	
	public static void deleteFilters() {
		Album.filtres.clear();
	}
	
	public static void readInfoAlbum () {
		System.out.println("Nom du album?");
		String titre = Klex.scanner.nextLine();
		
		System.out.println("Nom de l'Artist?");	
		String nomArtist = Klex.scanner.nextLine();
			
		System.out.println("Date de Sortie? (sous la forme AAAA-MM-JJ");
		boolean dateLu = false;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date dateSortie = null;
		while (!dateLu){
			String date = Klex.scanner.nextLine();
			if(date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
				try{
					dateSortie = f.parse(date);
					dateLu = true;
				} catch (ParseException e){
				}
			}
			else{
				System.out.println("Erreur de format ressaisissez la");
			}
		}

		System.out.println("URL vers l'album?");
		String URLAlbum = Klex.scanner.nextLine();
		try {
			Album.addAlbum(titre, nomArtist, dateSortie, URLAlbum);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
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
				CategorieMusique.CategoriesMusiqueDispo();
				
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
			while (true){
				System.out.println("Tapez [P] si vous voulez ajouter une piste, Tapez [T] si vous voulez terminer");
				String reponse = Klex.scanner.nextLine();
				switch (reponse){
					case "P":
						Piste.readInfoPiste(idAlbum , null, true);
						break;
					case "T":
						return;
					default :
						System.out.println("mauvais réponse");
				}
			}
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
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Pistes where IdAlbum = ?");
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
	
	public static Integer trouveIdentifiant(String titre, int numArtiste) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
			"SELECT idALbum from Album WHERE TitreAlbum = ? and  NumArtiste = ?");

		statement.setString(1, titre);
		statement.setInt(2, numArtiste);
		statement.executeQuery();
		ResultSet resultat = statement.executeQuery();
		if (resultat.next()){
			return resultat.getInt("IdAlbum");
		}
		return null;
	}
	
	/** Supression des albums qui ne sont pas references par d'autre contenus : qui ne sont pas reference par pistes**/
	public static void nettoyageAlbum() throws SQLException {
		PreparedStatement s = BdClass.getConnection().prepareStatement(
				"SELECT IdAlbum FROM Album where IdAlbum NOT IN (SELECT IdAlbum FROM Pistes)");
		ResultSet result = s.executeQuery();
		while(result.next()){
			int idAlbumSuppr = result.getInt("IdAlbum"); 
			PreparedStatement statementSuppr = BdClass.getConnection().prepareStatement(
					"DELETE FROM Album WHERE IdAlbum = ?");
			statementSuppr.setInt(1, idAlbumSuppr);
			statementSuppr.executeQuery();
		}
	}
	
	public static void rechercheFiltreAlbum(String name) throws SQLException { 
		
		String req = "SELECT Album.titreAlbum , Album.dateSortieAlbum , Artist.nomArtiste " +
			"FROM Album , Artist, CategorisationAlbum  " + 
			"Where Album.idAlbum = CategorisationAlbum.idAlbum and Album.numArtiste = Artist.numArtiste " + 
			"and Album.titreAlbum like ? and ";

		req += "(";
		
		String to_terminate = "1 = 1";
		for(Filtre monFiltre: filtres) {
			if(monFiltre.getChamp().equals("categorie")) {
				to_terminate = "1 = 0";
				req += " ";
				req += monFiltre.getChamp();
				req += " = ";
				req += "'" + monFiltre.getValeur() + "'";
				req += " OR ";
			}
		}
		
		req += to_terminate;
		req += ")";

		req += " order by titreAlbum";
		PreparedStatement statement = BdClass.getConnection().prepareStatement(req);
		statement.setString(1, "%"+ name + "%");

		ResultSet resultat =statement.executeQuery();
		System.out.println("titre de l'album | date de sortie | artiste principale");
		while(resultat.next()) {
			System.out.println(resultat.getString("titreAlbum")+" | "+ resultat.getDate("dateSortieAlbum") + 
					" | "+  resultat.getString("nomArtiste")); 
		}
	}
}

