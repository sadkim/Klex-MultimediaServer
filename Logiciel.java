import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Savepoint;

public class Logiciel {

	/** Permet de verifier si lo logiciel existe deja **/
	public static boolean logicielExist(String marqueLogiciel, String modeleLogiciel) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From Logiciel where marqueLogiciel = ?   and modeleLogiciel = ?");

		statement.setString(1, marqueLogiciel);
		statement.setString(2, modeleLogiciel);

		ResultSet resultat =statement.executeQuery();

		return resultat.next();
	}
	
	/** Permet d'ajouter un logiciel depuis la classe Klex **/
	public static void readLogicielInfo() {
		System.out.println("Inserer la marque du logiciel svp");
    	String marqueLogiciel = Klex.scanner.nextLine();
    	
		System.out.println("Insérer le modèle du logiciel svp");
    	String modeleLogiciel = Klex.scanner.nextLine();
    	
		boolean lu = false;
		Integer resolLargMax = null;
		Integer resolHautMax = null;

		while (!lu){
			System.out.println("est ce que votre logiciel lit des videos ? [Y/N]");
    		String reponse = Klex.scanner.nextLine();
			switch(reponse){
				case "Y":
					System.out.println("inserer la resolution maximale de largeur[espace]la resolution maximale de hauteur svp");
					resolLargMax = Klex.scanner.nextInt();
					resolHautMax = Klex.scanner.nextInt();
					lu = true;
					break;
				case "N":
					lu = true;
					break;
				default:
					System.out.println("Mauvaiss réponse");
					break;
			}
		}
					
		try {
			addLogiciel(marqueLogiciel, modeleLogiciel, resolLargMax, resolHautMax);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}


	public static void addLogiciel(String marqueLogiciel, String modeleLogiciel, Integer resolLargMax,
			Integer resolHautMax) throws SQLException {
		
		
		if(logicielExist(marqueLogiciel, modeleLogiciel)) {
			System.out.println("le logiciel est déjà enregistré dans la base de données"); 
		}
		
		else {
			/* Ajout d'un nouveau logiciel */
			PreparedStatement statement = BdClass.getConnection().prepareStatement(
					"INSERT INTO Logiciel(MarqueLogiciel, ModeleLogiciel, resolutionLargMax, resolutionHautMax) values(?,?,?,?)");

			statement.setString(1, marqueLogiciel);
			statement.setString(2, modeleLogiciel);
			if (resolLargMax == null){
				statement.setNull(3, java.sql.Types.INTEGER);
				statement.setNull(4, java.sql.Types.INTEGER);
			} else{
				statement.setInt(3, resolLargMax);
				statement.setInt(4, resolHautMax);
			}
			statement.executeQuery();
			
			/* Forcer l'ajout d'une association codec logiciel */
			System.out.println("Maintenant, il faut associer des codecs à ce logiciel");
			boolean uneAssociationFait = false;
			boolean fini = false;
			while (!uneAssociationFait || !fini){
				System.out.println("Tapez assoCodecLogiciel pour associer votre logiciel à un codec existant");
				System.out.println("Tapez ajoutCodec si vous avez besoin de le recréer");
				System.out.println("Tapez annuler pour annuler la création du logiciel");
				System.out.println("Tapez fini si vous avez fini");
				
				String commande = Klex.scanner.nextLine();
				switch (commande) {
					case "assoCodecLogiciel":
						Savepoint svptAssoCodec = BdClass.getConnection().setSavepoint("savepointAssoCodec");
						boolean ajoute = CodecLogiciel.readInfoAssCodLog(marqueLogiciel, modeleLogiciel, true);
						if (ajoute){
							uneAssociationFait = uneAssociationFait || Confirmation.confirmerAvecCascade(
								"Voulez vous confirmaer cette association ?", svptAssoCodec);
						}
						break;

					case "ajoutCodec":
						Savepoint svptAjoutCodec = BdClass.getConnection().setSavepoint("savepointAjoutCodec");
						Codec.readCodecInfo(true);
						boolean ajoute2 = Confirmation.confirmerAvecCascade(
								"Voulez vous confirmaer l'ajout de ce codec ?", svptAjoutCodec);
						if (ajoute2) {System.out.println("maintenant, vous devez faire l'association");}
						break;
				
					case "annuler":
						System.out.println("Cet annulation va supprimer le logiciel et les codecs crées avec lui");
						BdClass.getConnection().rollback();
						return;

					case "fini" :
						if (!uneAssociationFait){
							System.out.println("vous devez associer au moins un codec à votre logiciel");
							break;
						}
						fini = true;
						/* C'est le seul commit  */
						BdClass.getConnection().commit();
						return;
				}
			}
		}
	}
}
