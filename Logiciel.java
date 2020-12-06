import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Savepoint;

public class Logiciel {
	
	public static boolean logicielExist(String marqueLogiciel, String modeleLogiciel) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"Select * From Logiciel where marqueLogiciel = ?   and modeleLogiciel = ?");

		statement.setString(1, marqueLogiciel);
		statement.setString(2, modeleLogiciel);

		ResultSet resultat =statement.executeQuery();

		return resultat.next();
	}
	
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
					System.out.println("un mauvais réponse");
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
		
		/* Un save point pour pouvoir annuler la creation du logiciel */
    	BdClass.getConnection().setAutoCommit(false);
		
		if(logicielExist(marqueLogiciel, modeleLogiciel)) {
			System.out.println("le logiciel est déjà enregistré dans la base de données" + 
					" vous pouvez toutefois lui ajouter des codecs");
		}
		
		else {
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
						uneAssociationFait = uneAssociationFait || readInfoAssoLogCod(marqueLogiciel,modeleLogiciel);
						break;

					case "ajoutCodec":
						uneAssociationFait = uneAssociationFait || readInfoAssoLogNewCod(marqueLogiciel,modeleLogiciel);
						break;
				
					case "annuler":
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
					
	
	private static boolean readInfoAssoLogNewCod(String marqueLogiciel, String modeleLogiciel) 
			throws SQLException {
		System.out.println("le codec est directement associé au logiciel aprés l'insertion");
		
		Savepoint svptAjoutCodec = BdClass.getConnection().setSavepoint("savepointAjoutCodec");
		
		System.out.println("inserez le nom du codec");
		String c = Klex.scanner.nextLine();
		boolean codecCree = Codec.addCodec(c, true);
		if (!codecCree){
			return false;
		}
		
		System.out.println("Vous confirmez cet ajout? [Y/N]");
    	boolean bienLu = false;
		while (!bienLu){
			String reponse = Klex.scanner.nextLine();
			switch(reponse){
				case "Y":
					bienLu = true;
					CodecLogiciel.addAssoLogicielCodec(marqueLogiciel, modeleLogiciel, c, true);
					//BdClass.getConnection().commit(); //<<==== pas de commit ici sinon on va 'commiter' le logiciel  
					return true;
				case "N":
					bienLu = false;
					BdClass.getConnection().rollback(svptAjoutCodec);
					return false;
				default:
					System.out.println("mauvais réponse");	
			}
		}
		return false;
	}
						
			
	private static boolean readInfoAssoLogCod(String marqueLogiciel, String modeleLogiciel)
			throws SQLException {
		boolean existVerifie = false;
		boolean fini = false;
		while (!fini || !existVerifie){
			Savepoint svptAssoCodec = BdClass.getConnection().setSavepoint("savepointAssoCodec");
			System.out.println("Veuillez insérer le nom du Codec");
			String c = Klex.scanner.nextLine();
			
			if(Codec.codecExist(c)) { 
				System.out.println("le codec n'existe pas dans la base ajoutez le");
				return false;
			}
			/* Ajouter l'association */
			CodecLogiciel.addAssoLogicielCodec(marqueLogiciel, modeleLogiciel, c, true);
			boolean bienLu = false;
			while (!bienLu){
				System.out.println("Tapez[E]pour ajouter plus,[A]pour annuler la création de l'association [F] si c'est terminé");
    			String reponse = Klex.scanner.nextLine();
				switch(reponse){
					case "E":
						existVerifie = true;
						bienLu = true;
						break;
					case "F":
						//BdClass.getConnection().commit();  //<<==== pas de commit sinon "on commite la table 
						return true;
					
					case "A":
						BdClass.getConnection().rollback(svptAssoCodec);
						bienLu = true;
						break;
					default :
						System.out.println("mauvais réponse");
				}
			}
		}
		return false;
	}
}
