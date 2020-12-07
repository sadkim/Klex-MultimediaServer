import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

public class Piste {
	
	public static void addPiste(int IdAlbum, String titrePiste, int dureePiste) throws SQLException {
		if (Album.AlbumExiste(IdAlbum)) {
			PreparedStatement statement = BdClass.getConnection().prepareStatement(
					"SELECT * FROM PISTE where IdAlbum = ? and titrePiste = ?");
			
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
					System.out.println("Tapez fini si vous avez voulez confirmer la creation de l'album");
				
					String commande = Klex.scanner.nextLine();
					switch (commande) {
						case "Categoriser":
							Savepoint svptCategorisation = BdClass.getConnection().setSavepoint("svpCategorisation");
							boolean ajoute = CategorisationPiste.readInfoCategoPiste(IdAlbum , nbPiste + 1,  true);
						if (ajoute){
							contrainteSatis = contrainteSatis || confirmerAvecCascade(
								"Voulez vous confirmer la catégorisation [Y/N]", svpCategorisation);
						}
						break;

						case "nouvelleCategorie":
							Savepoint ajoutCategorie = BdClass.getConnection().setSavepoint("svpCategorie");
							lireCategMusique(true);
							boolean ajoute1 = confirmerAvecCascade( "Voulez vous confirmer l'ajout de cette catégorie ? [Y/N]",
								svpCategorie);
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
				//TODO doit etre forcee : au moins un fichier  : les lfux seront frocés dans la création fichier 
				
				//ici le commit
				boolean confirme = Confirmation.confirmerSansCascade("Voulez vous confirmer la création de la piste ? ");
				if (!confirme){ return ;} //Annulation 
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
