import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import except.FilmAlreadyExistException;
import except.FilmDoesNotExistException;


public class Film {
	
	private String titre;
	private int anneeSortie;
	private static List<Filtre> filtres = new ArrayList<Filtre>();
	
	public static void addFilter(Filtre filtre) {
		Film.filtres.add(filtre);
	}
	
	public static void deleteFilters() {
		Film.filtres.clear();
	}
	
	public String getTitre() {
		return titre;
	}

	public int getAnneeSortie() {
		return anneeSortie;
	}

	
	public Film(String titre, int anneeSortie) throws SQLException, FilmDoesNotExistException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From Film where Titre = ? and anneeSortie = ?");
		statement.setString(1, titre);
		statement.setInt(2, anneeSortie);
		ResultSet resultat =statement.executeQuery();
		if(resultat.next()) {
			this.titre = resultat.getString("titre");
			this.anneeSortie = resultat.getInt("anneeSortie");
		}else {
			throw new FilmDoesNotExistException("le titre ou la date du film est erroné");
		}
	}

	
	/** Permet de verifier si un film existe deja dans la base de donnee **/
	public static boolean existeFilm(String titre, int anneeSortie) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From Film where Titre = ? and anneeSortie = ?");
		statement.setString(1, titre);
		statement.setInt(2, anneeSortie);
		ResultSet resultat =statement.executeQuery();
		return resultat.next();
	}
	
	/** Permet de lire les informations du film a partir de l'interface Klex 
	 * @throws FilmAlreadyExistException 
	 * @throws FilmDoesNotExistException */
	public static Film readInfoFilm(int idFichier) throws FilmAlreadyExistException {
		System.out.println("Nom du film ? ");
		String titre = Klex.scanner.nextLine();
		
		System.out.println("Annee de sortie du film ? ");	
		int anneeSortie = Klex.scanner.nextInt();
			
		System.out.println("Un résumé du film ? ");
		String resume  = Klex.scanner.nextLine();
		
		System.out.println("Un âge minimum pour le film ? ");	
		int ageMin = Klex.scanner.nextInt();
		
		System.out.println("l'url de l'affiche du film ?");
		String urlAffiche = Klex.scanner.nextLine();

		try {
			if(idFichier ==0) {
			addFilm (titre, anneeSortie, resume, ageMin, urlAffiche);
			return new Film(titre, anneeSortie);
			}else {
				addFilm (titre, anneeSortie, resume, ageMin, urlAffiche,idFichier);
				return new Film(titre, anneeSortie);
			}
		}
		catch (SQLException | FilmDoesNotExistException e) {
			e.printStackTrace();
			return null;
		}	
	}
	

	/** Permer d'ajouter un film **/
	public static void addFilm(String titre, int anneeSortie, String Resume, int ageMin, String urlAffiche) throws SQLException {
		
		boolean existe = existeFilm(titre, anneeSortie);
		if(existe) {
			System.out.println("Ce film est déjà dans la base de données vous pouvez lire ses fichiers" + 
					" ou ajouter de nouveau tappez help pour avoir de l'aide ");
		}
		else {
			filmToDb(titre, anneeSortie, Resume, ageMin, urlAffiche);
			
			
			String message ="à chaque film doit être associée au moins un fichier ajouter un fichier en tappant ajouteFichier ou bien annulez la création en tappant autre chose";
			boolean continu = true;
			boolean contrainteSatis = false;
			while(continu || !contrainteSatis) {
				System.out.println(message);
				String commande = Klex.scanner.nextLine();

				if(commande.equals("ajouteFichier")) {
    				System.out.println("quelle est la taille de votre fichier");
    				float taille = Float.parseFloat(Klex.scanner.nextLine());

    				try {
    					Fichier.addFichier(taille, new Film(titre, anneeSortie));
    					contrainteSatis = true;
    					message = "Film , Fichiers et flux ont bien été ajoutés si vous voulez ajouter un autre fichier tappez ajouteFichier, tappez n'importe quoi pour quitter";
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (FilmDoesNotExistException e) {
    					System.out.println("ce film n'existe pas");
					}
    			}
    			else{
    				if(!contrainteSatis) {
        				BdClass.getConnection().rollback();
    					System.out.println("ajout film annulé");
    				}else {
    					System.out.println("vos inssertions ont bien été sauvegardés");
    				}
					return;

    			}
			}
		}
	}
	
	private static void addFilm(String titre, int anneeSortie, String Resume, int ageMin, String urlAffiche,int idFichier) throws SQLException, FilmAlreadyExistException {
		boolean existe = existeFilm(titre, anneeSortie);
		if(existe) {
			throw new FilmAlreadyExistException("ce film existe déja");
		}
		else {
			filmToDb(titre, anneeSortie, Resume, ageMin, urlAffiche);
			contenuMultimedia(idFichier, titre, anneeSortie, 0, 0);
		}
	}

	private static void filmToDb(String titre, int anneeSortie, String Resume, int ageMin, String urlAffiche)
			throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"INSERT INTO Film (Titre, anneeSortie, Resume, ageMin, UrlAffiche) values(?,?,?,?,?)");
		
		statement.setString(1, titre);
		statement.setInt(2, anneeSortie);
		if(Resume.equals("")) {
			Resume = null;
		}
		statement.setString(3, Resume);
		statement.setInt(4, ageMin);
		statement.setString(5, urlAffiche);
		statement.executeQuery();
		
		categiserFIlm(titre, anneeSortie);
	}

	private static void categiserFIlm(String titre, int anneeSortie) throws SQLException {
		System.out.println("Un film doit avoir au moins une categorie ajoutez les");
		boolean contrainteSatis = false;
		boolean categorisationFini = false;
		while (!categorisationFini || !contrainteSatis){
			System.out.println("Tapez Categoriser pour ajouter le film à une categorie existante");
			System.out.println("Tapez nouvelleCategorie si vous avez besoin de créer une nouvelle catégorie");
			System.out.println("Tapez fini si vous voulez confirmer la categorisation");
			String commande = Klex.scanner.nextLine();
			switch (commande) {
				case "Categoriser":
					Savepoint svptCategorisation = BdClass.getConnection().setSavepoint("svpCategorisation");
					boolean ajoute = CategorisationFilm.readInfoCategoFilm (titre , anneeSortie, true);
					if (ajoute){
						contrainteSatis = contrainteSatis || Confirmation.confirmerAvecCascade(
							"Voulez vous confirmer la catégorisation [Y/N]", svptCategorisation);
					}
					break;

				case "nouvelleCategorie":
					Savepoint ajoutCategorie = BdClass.getConnection().setSavepoint("svpCategorie");
					CategorieFilm.lireCategFilm (true);
					boolean ajoute2 = Confirmation.confirmerAvecCascade(
							"Voulez vous confirmer l'ajout de cette catégorie ? [Y/N]", ajoutCategorie);
					if (ajoute2){ System.out.println("maintenant, il faut ajouter le film à cette catégorie");};
					break;
			
				case "annuler":
					System.out.println("le film sera supprimé avec les nouvelles catégories ajoutée avec lui");
					BdClass.getConnection().rollback();
					break;

				case "fini" :
					if (!contrainteSatis){
						System.out.println("vous devez associer au moins une catégorie à ce film  ");
						break;
					}
					categorisationFini = true;
					break;
				default :
					System.out.println("mauvais reponse");
			}
		}
	}
	
	
	
	public static void searchFilm(String name) throws SQLException {
		String req = "SELECT Film.titre, Film.anneeSortie FROM Film, Fichier, Flux, Contribution, CategorisationFilm where Film.titre  = categorisationFilm.titre and Film.anneeSortie = categorisationFilm.anneeSortie and Film.titre =ContenuMultimedia.titre and Film.anneeSortie  = contenuMultimedia.anneeSortie and contenuMultimedia.idFichier= Fichier.idFichier and Flux.idFichier = Fichier.idFichier  and Film.titre like '*?*' and ageMin < ? and ";
		req += "(";

		for(Filtre monFiltre: filtres) {
			if(monFiltre.getChamp().equals("langue")) {
				req += " (Flux.";
				req += monFiltre.getChamp();
				req += " = ";
				req += monFiltre.getValeur();
				req += " and flux.type = audio)";
				req += " OR ";
			}

		}
		req += "1 = 0";
		req += ")";
		req+=" AND ";

		req += "(";

		for(Filtre monFiltre: filtres) {
			if(monFiltre.getChamp().equals("langueSousTitre")) {
				req += " (Flux.langue = ";
				req += monFiltre.getValeur();
				req += " and flux.type = text)";
				req += " OR ";
			}

		}
		req += "1 = 0";
		req += ")";

		req+=" AND ";
		req += "(";

		for(Filtre monFiltre: filtres) {
			if(monFiltre.getChamp().equals("categorie")) {
				req += " ";
				req += monFiltre.getChamp();
				req += " = ";
				req += monFiltre.getValeur();
				req += " OR ";
			}

		}
		req += "1 = 0";
		req += ")";

		
		
		req += "order by titre";
		PreparedStatement statement = BdClass.getConnection().prepareStatement(req);
		statement.setString(1, name);
		statement.setInt(2, User.getAge());
		ResultSet resultat =statement.executeQuery();
		System.out.println("titre | annéeSortie | Resume | UrlAffiche");
		while(resultat.next()) {
			System.out.println(resultat.getString("Titre")+" | "+ resultat.getString("anneeSortie") + 
					" | "+ resultat.getString("Resume") +" | "+ resultat.getString("urlAffiche"));
		}
	}

	private static void contenuMultimedia(int idFichier, String Titre, int anneeSortie,int IdAlbum, int numPiste ) throws SQLException {// ne pas utiliser avant de verifier que le film ou l'album existent bien
		PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO ContenuMultimedia (idFichier, Titre, AnneeSortie,IdAlbum,NumPiste ) values(idFichierSeq.nextval,sysdate,?,?)");
		statement.setInt(1, idFichier);
		if(anneeSortie !=0) {
		statement.setString(2, Titre);
		statement.setInt(3, anneeSortie);
		}
		if(anneeSortie !=0) {
			statement.setInt(4, IdAlbum);
			statement.setInt(5, numPiste);
		}
		statement.executeQuery();
	}

	public static void toSupprimeFilm() throws SQLException{
		System.out.println("Titre du film à supprimer svp");
		String titre = Klex.scanner.nextLine();
		System.out.println("L'année de sortie du film à supprimer svp");
		int anneeSortie = Klex.scanner.nextLine();
		boolean existe = existeFilm(titre, anneeSortie);
		if (!existe) {
			System.out.println("Ce film n'existe pas dans la base de données.");
		}
		else {
			supprimerFilm(titre, anneeSortie);
		}

	}
	
	public static void supprimerFilm(String titre, int anneeSortie) throws SQLException {
		
		/** Suppression des fichiers associees au film */
		PreparedStatement statement1 = BdClass.getConnection().prepareStatement(
				"SELECT * FROM Fichier f, ContenuMultimedia m where f.idFichier = m .idFichier and m.Titre like '*?*' and m.AnneeSortie = ?");
		statement1.setString(1, titre);
		statement1.setInt(2, anneeSortie);
		ResultSet resultat = statement1.executeQuery();
		while(resultat.next()) {
			int idFichierSuppr = resultat.getInt("idFichier"); // avoir si l'attribut de la table de jointure est bien idFichier
			PreparedStatement statementSupprFichier = BdClass.getConnection().prepareStatement(
					"DELETE FROM Fichier WHERE idFichier = ?");
			statementSupprFichier.setInt(1, idFichierSuppr);
			statementSupprFichier.executeQuery();
		}
		
		/** Suppression des roles associees au film */
		PreparedStatement statement2 = BdClass.getConnection().prepareStatement(
				"SELECT * FROM ContributionFilm where Titre like '*?*' and AnneeSortie = ?");
		statement2.setString(1, titre);
		statement2.setInt(2, anneeSortie);
		ResultSet resultat1 = statement2.executeQuery();
		while(resultat1.next()) {
			int numArtisteSuppr = resultat1.getInt("numArtiste"); 
			PreparedStatement statementSupprRole = BdClass.getConnection().prepareStatement(
					"DELETE FROM ContributionFilm WHERE numArtiste = ? and Titre like '*?*' and AnneeSortie = ?");
			statementSupprRole.setInt(1, numArtisteSuppr);
			statementSupprRole.setString(2, titre);
			statementSupprRole.setInt(3, anneeSortie);
			statementSupprRole.executeQuery();
		}
		
		/** Suppression du film */
		PreparedStatement statementSupprFilm = BdClass.getConnection().prepareStatement(
				"DELETE FROM Film WHERE titre like ='*?*' and anneeSortie = ?");
		statementSupprFilm.setString(1, titre);
		statementSupprFilm.setInt(2, anneeSortie);
		statementSupprFilm.executeQuery();
		
		/** Nettoyage des Artistes et de Album*/
		Artist.nettoyageArtiste();
		Album.nettoyageAlbum();
		BdClass.getConnection().commit(); //<<======= ici le commit 
	}
	
}
