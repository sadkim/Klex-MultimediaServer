import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

import except.PisteDoesNotExistException;

public class Piste {
	
	private int idAlbum ;
	private int numPiste;
	
	public int getIdAlbum () {
		return idAlbum ;
	}

	public int getnumPiste() {
		return numPiste ;
	}

	public Piste (int idAlbum , int numPiste) throws SQLException, PisteDoesNotExistException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From Pistes where idAlbum = ? and numPiste = ?");
		statement.setInt(1, idAlbum);
		statement.setInt(2, numPiste);
		ResultSet resultat =statement.executeQuery();
		if(resultat.next()) {
			this.idAlbum  = resultat.getInt("idAlbum");
			this.numPiste = resultat.getInt("numPiste");
		}else {
			throw new PisteDoesNotExistException();
		}
	}
	
	/** Cette méthode permet de lire les informations d'une piste à partir de l'interface Klex **/
	public static void readInfoPiste(boolean addDelete){
		/* Lecture des informations de  l'album */
		try{
			System.out.println("le nom de l'artiste ?");
			String nomArtiste = Klex.scanner.nextLine();
			Integer numArtiste = Artist.getNumArtiste(nomArtiste);
			if (numArtiste == null){
				System.out.println("Cet artiste n'existe pas");
				return;
			}
			System.out.println("le titre de l'album ?");
			String titreAlbum = Klex.scanner.nextLine();
			Integer idAlbum = Album.trouveIdentifiant(titreAlbum , numArtiste);
			if (idAlbum == null){
				System.out.println("Cet album n'existe pas");
				return;
			}
			readInfoPiste(idAlbum, addDelete);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** Cette méthode permet de lire les informations d'une piste aprés la création de l'album **/
	public static void readInfoPiste(int idAlbum, boolean addDelete) {
		System.out.println("le titre de la piste ? ");
		String titre = Klex.scanner.nextLine();
	
		try {
			System.out.println("la durée de la piste ? ");	
			int dureePiste = Klex.scanner.nextInt();
			
			if(addDelete)
				addPiste (idAlbum, titre, dureePiste);
			else
				toSupprimePiste(idAlbum, titre);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	

	public static void addPiste(int IdAlbum, String titrePiste, int dureePiste) throws SQLException {
		if (Album.AlbumExiste(IdAlbum)) {
			PreparedStatement statement = BdClass.getConnection().prepareStatement(
					"SELECT * FROM PISTES where IdAlbum = ? and titrePiste = ?");
			
			statement.setInt(1,IdAlbum);
			statement.setString(2, titrePiste);
			ResultSet resultat = statement.executeQuery();
			
			/* Piste existe déja */
			if (resultat.next()) {
				System.out.println("Le piste est déjà presente dans l'album.");
			}
			else {
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
							e.printStackTrace();
						} catch (PisteDoesNotExistException e) {
    					System.out.println("cette piste n'existe pas");
						}
    				}else if (commande.equals("annuler")){
    					BdClass.getConnection().rollback();
    					System.out.println("ajout piste annulé");
						return;
					}else{
						if (!contSatis){
							System.out.println("vous devez au moins associer un fichier");
						}
						else{
							ajoutFichierFini = true;	
						}
					}
    			}
				BdClass.getConnection().commit(); //<<======= ici le commit 
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
	
	public static void supprimerPiste(int IdAlbum, int numPiste) {
		
		/** Suppression des fichiers associees a la piste */
		PreparedStatement statement1 = BdClass.getConnection().prepareStatement(
				"SELECT * FROM Fichier f, ContenuMultimedia m where f.idFichier = m .idFichier and m.IdAlbum = ? and m.NumPiste = ?");
		statement1.setInt(1, IdAlbum);
		statement1.setInt(2, numPiste);
		ResultSet resultat1 = statement1.executeQuery();
		while(resultat.next()) {
			int idFichierSuppr = resultat1.getString("idFichier"); 
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
		while(resultat.next()) {
			int numArtisteSuppr = resultat2.getString("NumArtiste"); 
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
		Artist.nettoyageArtiste();
		Album.nettoyageAlbum();
		BdClass.getConnection().commit(); //<<======= ici le commit 

	}

}
