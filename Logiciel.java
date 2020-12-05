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
	
	public static void readLogicielInfo(Scanner scanner) {
		System.out.println("Inserer la marque du logiciel svp");
    	String marqueLogiciel =scanner.nextLine();
    	
		System.out.println("Insérer le modèle du logiciel svp");
    	String modeleLogiciel =scanner.nextLine();
    	
		boolean lu = false;
		Integer resolLargMax = null;
		Integer resolHautMax = null;

		while (!lu){
			System.out.println("est ce que votre logiciel lit des videos ? [Y/N]");
    		String reponse =scanner.nextLine();
			switch(reponse){
				case "Y":
					System.out.println("inserer la resolution maximale de largeur[espace]la resolution maximale de hauteur svp");
					resolLargMax = scanner.nextInt();
					resolHautMax = scanner.nextInt();
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
			addLogiciel(scanner, marqueLogiciel, modeleLogiciel, resolLargMax, resolHautMax);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}


	public static void addLogiciel(Scanner scanner, String marqueLogiciel, String modeleLogiciel, Integer resolLargMax,
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
				
				String commande = scanner.nextLine();
				switch (commande) {
					case "assoCodecLogiciel":
						uneAssociationFait = uneAssociationFait || readInfoAssoLogCod(scanner, marqueLogiciel,modeleLogiciel);
						break;

					case "ajoutCodec":
						uneAssociationFait = uneAssociationFait || readInfoAssoLogNewCod(scanner, marqueLogiciel,modeleLogiciel);
						break;
				
					case "annuler":
						BdClass.getConnection().rollback();
    					//BdClass.getConnection().releaseSavepoint(svptLogiciel);
						return;
					case "fini" :
						if (!uneAssociationFait){
							System.out.println("vous devez associer au moins un codec à votre logiciel");
							break;
						}
						fini = true;
						/* une commit pour s'assurer  detruire le savepoint */
						BdClass.getConnection().commit();
    					//BdClass.getConnection().releaseSavepoint(svptLogiciel);
						return;
				}
			}
		}
	}
					
	
	private static boolean readInfoAssoLogNewCod(Scanner scanner, String marqueLogiciel, String modeleLogiciel) 
			throws SQLException {
		System.out.println("le codec est directement associé au logiciel aprés l'insertion");
		System.out.println("inserez le nom du codec");
		String c = scanner.nextLine();
		if(Codec.codecExist(c)) { 
			System.out.println("le codec existe deja, precedz vous a l'ajout des associations codec logiciel");
			return false;
		}
		else{
			Savepoint svptAjoutCodec = BdClass.getConnection().setSavepoint("savepointAjoutCodec");
			PreparedStatement s = BdClass.getConnection().prepareStatement("insert into Codec(codec) values (?)");
			s.setString(1,c);
			s.executeQuery();

			System.out.println("Vous confirmez cet ajout? [Y/N]");
    		boolean bienLu = false;
			while (!bienLu){
				String reponse =scanner.nextLine();
				switch(reponse){
					case "Y":
						bienLu = true;
						addAssoLogicielCodec(marqueLogiciel,modeleLogiciel,c);
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
		}
		return false;
	}
						
			
	private static boolean readInfoAssoLogCod(Scanner scanner, String marqueLogiciel, String modeleLogiciel)
			throws SQLException {
		boolean existVerifie = false;
		boolean fini = false;
		while (!fini || !existVerifie){
			Savepoint svptAssoCodec = BdClass.getConnection().setSavepoint("savepointAssoCodec");
			System.out.println("Veuillez insérer le nom du Codec");
			String c = scanner.nextLine();
			
			if(Codec.codecExist(c)) { 
				System.out.println("le codec n'existe pas dans la base ajoutez le");
				return false;
			}
			/* Ajouter l'association */
			addAssoLogicielCodec(marqueLogiciel, modeleLogiciel, c);
			boolean bienLu = false;
			while (!bienLu){
				System.out.println("Tapez[E]pour ajouter plus,[A]pour annuler la création de l'association [F] si c'est terminé");
    			String reponse =scanner.nextLine();
				switch(reponse){
					case "E":
						/* detruire le savepoint */
						//BdClass.getConnection().releaseSavepoint(svptAssoCodec);
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
	
	/* A utiliser avec vigilance : on ne commit pas ici : duplication ? */
	private static void addAssoLogicielCodec(String marqueLogiciel, String modeleLogiciel, String codec) 
		throws SQLException {
		PreparedStatement s = BdClass.getConnection().prepareStatement(
			"insert into CodecLogiciel(MarqueLogiciel, ModeleLogiciel,   Codec) values (?, ?, ?)");
		s.setString(1,marqueLogiciel);
		s.setString(2, modeleLogiciel);
		s.setString(3, codec);
		s.executeQuery();
	}
}
