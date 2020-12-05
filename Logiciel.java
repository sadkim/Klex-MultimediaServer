import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;

public class Logiciel {

	public static void readLogicielInfo(Scanner scanner){
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


	public static void addLogiciel(Scanner scanner, String marqueLogiciel, String modeleLogiciel, int resolLargMax,
			int resolHautMax) throws SQLException {
		
		PreparedStatement statement = BdClass.getConnection().prepareStatement("Select * From Logiciel where marqueLogiciel = ?   and modeleLogiciel = ?");

		statement.setString(1, marqueLogiciel);
		statement.setString(2, modeleLogiciel);

		ResultSet resultat =statement.executeQuery();

		if(resultat.next()) {
			System.out.println("le logiciel est déjà enregistré dans la base de données" + 
					" vous pouvez toutefois lui ajouter des codecs");
		}
		
		else {
			statement = BdClass.getConnection().prepareStatement("INSERT INTO Logiciel(MarqueLogiciel, ModeleLogiciel, resolutionLargMax, resolutionHautMax) values(?,?,?,?,?)");

			statement.setString(1, marqueLogiciel);
			statement.setString(2, modeleLogiciel);
			statement.setInt(3, resolLargMax);
			statement.setInt(4, resolHautMax);

			statement.executeQuery();
			System.out.println("Maintenant,il faut associer des codecs avec la commande 'assoCodecLogiciel'" + 
			"si vous êtes sûres que le codec n'est pas crée ajoutez le d'abord avec la commande 'ajoutCodec'" + 
			"si vous n'ajouterez pas une association avec les codec" +
			" votre commande sera annulé vous pouvez rien taper si vous voulez annulez");
    		
			String commande = scanner.nextLine();
    		if(commande.equals("assoCodecLogiciel")) {
    			//TODO
				BdClass.getConnection().commit();  //<<======== ici le commit 
    		}else if (commande.equals("ajoutCodec")){
				//TODO 	
				/* // l'ajout des codecs doit etre suivi par l'ajout des associations codecs logiciels 
				while (){
				}*/
				BdClass.getConnection().commit();  //<<========== ici le commit
			}
			else{ 
    			BdClass.getConnection().rollback();
    			System.out.println("ajout logiciel annulé");
			}
    	}
	}
}
