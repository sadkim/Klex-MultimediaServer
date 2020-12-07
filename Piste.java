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
	

	/** Cette méthode permet de lire les informations d'une piste aprés la création de l'album **/
	public static void readInfoPiste(int idAlbum) {
		System.out.println("le titre de la piste ? ");
		String titre = Klex.scanner.nextLine();
		
		System.out.println("la durée de la piste ? ");	
		int dureePiste = Klex.scanner.nextInt();
			
		try {
			addPiste (idAlbum, titre, dureePiste);
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (PisteDoesNotExistException e) {
						// TODO Auto-generated catch block
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
		
		return false; }

}
