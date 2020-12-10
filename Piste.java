import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

import java.util.ArrayList;
import java.util.List;

public class Piste {
	
	private int idAlbum ;
	private int numPiste;
	private static List<Filtre> filtres = new ArrayList<Filtre>();
	
	public static void addFilter(Filtre filtre) {
		Piste.filtres.add(filtre);
	}
	
	public static void deleteFilters() {
		Piste.filtres.clear();
	}
	
	public int getIdAlbum () {
		return idAlbum ;
	}

	public int getnumPiste() {
		return numPiste ;
	}

	public Piste (int idAlbum , int numPiste) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From Pistes where idAlbum = ? and numPiste = ?");
		statement.setInt(1, idAlbum);
		statement.setInt(2, numPiste);
		ResultSet resultat =statement.executeQuery();
		if(resultat.next()) {
			this.idAlbum  = resultat.getInt("idAlbum");
			this.numPiste = resultat.getInt("numPiste");
		}else {
			System.out.println("la piste n'existe pas ou n'appartient pas à cet artiste");
		}
	}
	
	/** Cette méthode permet de lire les informations d'une piste à partir de l'interface Klex **/
	public static void readInfoPiste(boolean addDelete){
		readInfoPiste(null, addDelete);
	}

	public static void readInfoPiste(Integer idFichier, boolean addDelete){
		/* Lecture des informations de  l'album */
		try{
			System.out.println("le nom de l'artiste principale ou le groupe de l'album ?");
			String nomArtiste = Klex.scanner.nextLine();
			Integer numArtiste = Artist.getNumArtiste(nomArtiste);
			if (numArtiste == null){
				System.out.println("Cet artiste/groupe n'existe pas");
				return;
			}
			System.out.println("le titre de l'album ?");
			String titreAlbum = Klex.scanner.nextLine();
			Integer idAlbum = Album.trouveIdentifiant(titreAlbum , numArtiste);
			if (idAlbum == null){
				System.out.println("Cet album n'existe pas");
				return;
			}
			readInfoPiste(idAlbum , idFichier, addDelete);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** Cette méthode permet de lire les informations d'une piste aprés la création de l'album **/
	public static void readInfoPiste(int idAlbum , Integer IdFichier, boolean addDelete) {
		System.out.println("le titre de la piste ? ");
		String titre = Klex.scanner.nextLine();
		
		System.out.println("la durée de la piste ? ");	
		int dureePiste = Integer.parseInt(Klex.scanner.nextLine());
		try {
			if(addDelete){
				if (IdFichier == null){
					addPiste(idAlbum, titre, dureePiste);
				}
				else{
					addPiste(idAlbum , titre, dureePiste, IdFichier);
				}
			}
			else{
				toSupprimePiste(idAlbum, titre);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private static void addPiste(int IdAlbum , String titrePiste, int dureePiste, int idFichier) throws SQLException {
		
		int numPiste = addPiste(IdAlbum , titrePiste ,dureePiste);
		contenuMultimedia(idFichier, IdAlbum ,numPiste);
	}

	private static Integer addPiste(int IdAlbum, String titrePiste, int dureePiste) throws SQLException {
		if (!Album.AlbumExiste(IdAlbum)) {
			System.out.println("l'album n'existe pas"); // C'est un id loin d'arriver
			return null;
		}

		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"SELECT * FROM PISTES where IdAlbum = ? and titrePiste = ?");
			
		statement.setInt(1,IdAlbum);
		statement.setString(2, titrePiste);
		ResultSet resultat = statement.executeQuery();
			
		/* Piste existe déja */
		if (resultat.next()) {
			System.out.println("Le piste est déjà presente dans l'album.");
			return null;
		}
		int nbPiste = Album.getNbPiste(IdAlbum);
		assert nbPiste != -1;
				
		statement = BdClass.getConnection().prepareStatement(
			"INSERT INTO PISTES(IdAlbum, numPiste, titrePiste, dureePiste) values(?,?,?,?)");
		statement.setInt(1, IdAlbum);
		statement.setInt(2, nbPiste+1);
		statement.setString(3, titrePiste);
		statement.setInt(4, dureePiste);
		statement.executeQuery();
		/* Forcer la catégorisation */
		System.out.println("Maintenant il faut ajouter une categorie");
		boolean contrainteSatis = false;
		boolean fini = false;
		while (!fini || !contrainteSatis){
			System.out.println("Tapez Categoriser pour associer la piste à une categorie existante");
			System.out.println("Tapez nouvelleCategorie si vous avez besoin de créer une nouvelle catégorie");
			System.out.println("Tapez fini si vous avez voulez avez fini la catégorisation de la piste");
			CategorieMusique.CategoriesMusiqueDispo();
			String commande = Klex.scanner.nextLine();
			switch (commande) {
				case "Categoriser":
					Savepoint svptCategorisation = BdClass.getConnection().setSavepoint("svpCategorisation");
					boolean ajoute = CategorisationPiste.readInfoCategoPiste(IdAlbum , nbPiste + 1,  true);
					if (ajoute){
						contrainteSatis = contrainteSatis || Confirmation.confirmerAvecCascade(
						"Voulez vous confirmer la catégorisation [Y/N]", svptCategorisation);
					}
				break;

				case "nouvelleCategorie":
					Savepoint ajoutCategorie = BdClass.getConnection().setSavepoint("svpCategorie");
					CategorieMusique.lireCategMusique(true);
					boolean ajoute1 = Confirmation.confirmerAvecCascade(
							"Voulez vous confirmer l'ajout de cette catégorie ? [Y/N]", ajoutCategorie);
					if (ajoute1){ System.out.println("maintenant, il faut ajouter l'album à cette catégorie"); };
					break;

				case "fini" :
					if (!contrainteSatis){
						System.out.println("vous devez associer au moins une catégorie à cette piste ");
						break;
					}
					fini = true;
					break;
				default :
					System.out.println("mauvais reponse");
			}
		}
		/**Association Piste et fichier**/
		
		System.out.println("à chaque piste doit être associée au moins un fichier");
		boolean ajoutFichierFini = false;
		boolean contSatis = false;
		while (!ajoutFichierFini || !contrainteSatis){
			System.out.println("tapez ajouteFichier pour ajouter un fichier");
			System.out.println("tapez annuler pour annuler la création de la piste");
			System.out.println("taper autre chose pour terminer");
			String commande = Klex.scanner.nextLine();
			if(commande.equals("ajouteFichier")) {
    			System.out.println("quelle est la taille de votre fichier ?");
    			float taille = Float.parseFloat(Klex.scanner.nextLine());
    			try {
    				Fichier.addFichier(taille, new Piste(IdAlbum , nbPiste + 1));
					contSatis = true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}else if (commande.equals("annuler")){
    			BdClass.getConnection().rollback();
    			System.out.println("ajout piste annulé");
				return null;
			}else{
				if (!contSatis){
					System.out.println("vous devez au moins associer un fichier");
				}
				else{
					ajoutFichierFini = true;	
				}
			}
    	}
		/* Faire la commit de la piste */
		BdClass.getConnection().commit();
		ajoutArtiste(IdAlbum, nbPiste + 1);
		return nbPiste + 1;
	}


	private static void ajoutArtiste(int idAlbum, int numPiste) throws SQLException {
		boolean contrainteSatis = false;
		boolean ajoutFini = false;
		String message="une piste doit être associer à au moins un artiste , ajoutez le ";
		while (!ajoutFini || !contrainteSatis){
			System.out.println(message);
			System.out.println("Tapez associer pour associer à un acteur déja existant");
			System.out.println("Tapez nouveau artiste si vous avez besoin de créer un nouvel artist");
			String commande = Klex.scanner.nextLine();
			switch (commande) {
				case "associer":					
					contrainteSatis = contrainteSatis || ContributionArtiste.contributionsEnCascade(idAlbum, numPiste);
					Savepoint artisteFilm = BdClass.getConnection().setSavepoint("artistFilm");
					break;

				case "nouveau":
					Artist.readArtistInfo(false);
					break;
			
				default :
					message = "vous de devez associer au moins un artiste";
					ajoutFini = true;
					break;	
			}
		}
	}

	/**Test l'existence d'une piste.
	 * @throws SQLException **/
	public static boolean PisteExiste(int IdAlbum, int numPiste) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"SELECT * FROM PISTES where IdAlbum = ? and numPiste = ?");
		statement.setInt(1, IdAlbum);
		statement.setInt(2, numPiste);
		ResultSet resultat = statement.executeQuery();
		if (resultat.next()) {
			return true;
		}
		return false;
	}
	
	public static Integer getNbPiste(int IdAlbum, String nomPiste) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"SELECT numPiste FROM PISTES where IdAlbum = ? and nomPiste = ?");
		statement.setInt(1, IdAlbum);
		statement.setString(2, nomPiste);
		ResultSet resultat = statement.executeQuery();
		if (resultat.next()) {
			return resultat.getInt("numPiste");
		}
		return null;
	}	
	
	public static void toSupprimePiste(int IdAlbum, String titrePiste) throws SQLException{
		if (Album.AlbumExiste(IdAlbum)) {
			PreparedStatement statement = BdClass.getConnection().prepareStatement(
					"SELECT * FROM PISTES where IdAlbum = ? and titrePiste = ?");
			
			statement.setInt(1,IdAlbum);
			statement.setString(2, titrePiste);
			ResultSet resultat = statement.executeQuery();
			
			if (resultat.next()) {
				supprimerPiste(IdAlbum, resultat.getInt("numPiste"));
			}
			else {
				System.out.println("Cette piste n'existe pas dans l'Album.");
			}
		}
	}
	
	public static void supprimerPiste(int IdAlbum, int numPiste) throws SQLException {
		
		/** Suppression des fichiers associees a la piste */
		PreparedStatement statement1 = BdClass.getConnection().prepareStatement(
			"SELECT * FROM Fichier f, ContenuMultimedia m where f.idFichier = m .idFichier and m.IdAlbum = ? and m.NumPiste = ?");
		statement1.setInt(1, IdAlbum);
		statement1.setInt(2, numPiste);
		ResultSet resultat1 = statement1.executeQuery();
		while(resultat1.next()) {
			int idFichierSuppr = resultat1.getInt("idFichier"); 
			PreparedStatement statementSupprFichier = BdClass.getConnection().prepareStatement(
					"DELETE FROM Fichier WHERE idFichier = ?");
			statementSupprFichier.setInt(1, idFichierSuppr);
			statementSupprFichier.executeQuery();
		}
		
		/** Suppression des artistes associees a la piste */
		PreparedStatement statement2 = BdClass.getConnection().prepareStatement(
				"SELECT * FROM ContributionPiste where IdAlbum = ? and NumPiste = ?");
		statement2.setInt(1, IdAlbum);
		statement2.setInt(2, numPiste);
		ResultSet resultat2 = statement2.executeQuery();
		while(resultat2.next()) {
			int numArtisteSuppr = resultat2.getInt("NumArtiste"); 
			PreparedStatement statementSupprRole = BdClass.getConnection().prepareStatement(
					"DELETE FROM ContributionPiste WHERE numArtiste = ? and IdAlbum = ? and NumPiste = ?");
			statementSupprRole.setInt(1, numArtisteSuppr);
			statementSupprRole.setInt(2, IdAlbum);
			statementSupprRole.setInt(3, numPiste);
			statementSupprRole.executeQuery();
		}
		
		/** Suppression de la piste */
		PreparedStatement statementSupprPiste = BdClass.getConnection().prepareStatement(
				"DELETE FROM Pistes WHERE IdAlbum = ? and NumPiste = ?");
		statementSupprPiste.setInt(1, IdAlbum);
		statementSupprPiste.setInt(2, numPiste);
		statementSupprPiste.executeQuery();
		
		/** Nettoyage des Artistes et de Album*/
		Album.nettoyageAlbum();
		Artist.nettoyageArtiste();
		BdClass.getConnection().commit(); //<<======= ici le commit 

	}

	public static void searchPiste(String name) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"SELECT TitreAlbum , nomArtiste FROM Pistes Join Album on Piste.IdAlbum = Album.IdAlbum " + 
				" Join Artist on ALbum.numArtiste = Artist.numArtiste Where nomPiste = ?");
		statement.setString(1, name);
		ResultSet resultat = statement.executeQuery();
		boolean exist = false;
		System.out.println("titre  | album  | artiste");
		while(resultat.next()) {
			exist = true;
			System.out.println(name + "|" + resultat.getString("TitreAlbum ")+" | "+ resultat.getString("nomArtiste")); 
		}
		if (!exist){
			System.out.println("la piste que vous cherchez n'existe pas");
		}
	}
	
	/* A utiliser si on est sure de l'existence de piste */
	private static void contenuMultimedia(int idFichier,int IdAlbum, int numPiste ) throws SQLException {
				
			PreparedStatement statement = BdClass.getConnection().prepareStatement(
					"INSERT INTO ContenuMultimedia (idFichier, Titre, AnneeSortie,IdAlbum ,NumPiste ) values(?,?,?,?,?)");
			
			statement.setInt(1, idFichier);
			statement.setString(2, null);
			statement.setNull(3, java.sql.Types.INTEGER);
			statement.setInt(4, IdAlbum);
			statement.setInt(5, numPiste);
			statement.executeQuery();
	}
	
	
	public static void rechercheFiltrePiste(String name) throws SQLException { 
		
		String req = "SELECT Distinct Pistes.titrePiste , Pistes.dureePiste , Album.titreAlbum , Album.dateSortieAlbum , "+ 
			"Artist.nomArtiste " +
			"FROM Pistes, Album , Artist, CategorisationPiste , Fichier , ContenuMultimedia , Flux " + 
			"Where Pistes.idAlbum = Album.idAlbum and Album.numArtiste = Artist.numArtiste " + 
			"and Pistes.numPiste = CategorisationPiste.numPiste and Pistes.idAlbum  = CategorisationPiste.idAlbum " + 
			"and Pistes.numPiste = ContenuMultimedia.numPiste and Pistes.idAlbum = ContenuMultimedia.idAlbum " +
			"and Fichier.idFichier = ContenuMultimedia.idFichier and Fichier.idFichier = Flux.idFichier and " + 
			"Pistes.titrePiste like ? and ";

		req += "(";
		
		String to_terminate = "1 = 1";
		for(Filtre monFiltre: filtres) {
			if(monFiltre.getChamp().equals("langue")) {
				to_terminate = "1 = 0";
				req += " (Flux.";
				req += monFiltre.getChamp();
				req += " = ";
				req += "'" + monFiltre.getValeur() + "'";
				req += " and flux.type = 'audio')";
				req += " OR ";
			}
		}
		
		req += to_terminate;
		req += ")";
		req+=" AND ";

		req += "(";

		to_terminate = "1 = 1";
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

		req += "order by titrePiste";
		PreparedStatement statement = BdClass.getConnection().prepareStatement(req);
		statement.setString(1, "%"+ name + "%");
		ResultSet resultat =statement.executeQuery();
		System.out.println("titre de la piste | l'album | annéeSortie | artiste principale | durée de la piste");
		while(resultat.next()) {
			System.out.println(resultat.getString("titrePiste")+" | "+ resultat.getString("titreAlbum") + 
					" | "+ resultat.getDate("dateSortieAlbum") +" | "+ resultat.getString("nomArtiste") + 
					" | " + resultat.getInt("dureePiste"));
		}
	}	
}
