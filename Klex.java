
import java.sql.SQLException;
import java.util.Scanner;
import except.EmailAlreadyExistsException;
import except.NoSuchUserException;

public class Klex {
	public static void main(String[] args)  {
		String url="jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1" ;
    	String user = "megzaria" ;
    	String passwd = "159357";
		BdClass.connect(url, user,passwd);	
	    Scanner scanner = new Scanner(System.in);
	    String commande;
	    boolean continuer= true;
    	System.out.println("bienvenue sur Klex");

	    while(continuer) {
	    	System.out.println("tappez connection si vous avez déjà un compte");
	    	System.out.println("tappez inscription si vous êtes ici pour la première fois");
	    	System.out.println("tappez aide si vous avez besoin d'aide fonction pas encore disponible");
	    	System.out.println("tappez exit pour quitter l'application");
			/* C'est uniquement pour pouvoir tester la validité du code ajouté :) */
			System.out.println("tapez ajout_artiste si vous voulez ajouter un nouveau artiste");
			System.out.println("tapez ajout_logiciel si vous voulez ajouter un nouveau logiciel");
	    	commande = scanner.nextLine();
	    	switch(commande) {
	    	case "connection":
		    	System.out.println("votre email svp");
	    		String email =scanner.nextLine();
		    	System.out.println("votre mot de passe svp");
	    		String password =scanner.nextLine();
	    		try {
					User user1 = new User(email, password);
				} catch (NoSuchUserException e) {
			    	System.out.println(e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    		
	    		
	    		break;
	    	case "inscription":
	    		System.out.println("votre email svp");
	    		String email1 =scanner.nextLine();
	    		System.out.println("votre nom svp");
	    		String nom =scanner.nextLine();
	    		System.out.println("votre prenom svp");
	    		String prenom =scanner.nextLine();
	    		System.out.println("votre age svp");
	    		int age =Integer.parseInt(scanner.nextLine());
		    	System.out.println("votre mot de passe svp");
	    		String password1 =scanner.nextLine();
		    	System.out.println("choisissez votre langue préferé");
		    	try {
					langue.languesDisponibles();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	    		String langue =scanner.nextLine();

	    		try {
					User user1 = new User(email1, nom, prenom, age, password1, langue);
				} catch (EmailAlreadyExistsException e) {
			    	System.out.println(e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (NoSuchUserException e) {
		    		System.out.println("NoSuchUserException juste aprés la création de l'entrée donc l'entrée ne c'est pas bien crée");

				}
	    		break;
			case "ajout_artiste":
				Artist.readArtistInfo(scanner);
				break;
	    
			case "ajout_logiciel":
				Logiciel.readLogicielInfo(scanner);
				break;
			case "exit":
	    		continuer = false;
	    		break;
	    	default:
		    	System.out.println("klex est un logiciel de gestion de fichiers");

	    		break;
	    	}
	    }
	    scanner.close();
	    try {
			BdClass.closeConnection();
		} catch (SQLException e) {
	    	System.out.println("couldn't close connection");
			e.printStackTrace();
		}


	}

}
