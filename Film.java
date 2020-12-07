import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import except.FilmAlreadyExistException;
import except.FilmDoesNotExistException;


public class Film {
	
	private String titre;
	private int anneeSortie;
	
	
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
			
			System.out.println("à chaque film doit être associée au moins un fichier ajouter un fichier" +
					"en tappant ajouteFichier ou bien annulez la création en tappant autre chose");
			
			String commande = Klex.scanner.nextLine();
			boolean continu = true;
			while(continu) {
				if(commande.equals("ajouteFichier")) {
    				System.out.println("quelle est la taille de votre fichier");
    				float taille = Float.parseFloat(Klex.scanner.nextLine());

    				try {
    					Fichier.addFichier(taille, new Film(titre, anneeSortie));
    					continu= false;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FilmDoesNotExistException e) {
						// TODO Auto-generated catch block
    					System.out.println("ce film n'existe pas");
					}
    			}else {
    				BdClass.getConnection().rollback();
    				System.out.println("ajout film annulé");
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
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"SELECT * FROM film where titre like'*?*' and ageMin < ? order by titre");
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
	
	
}
