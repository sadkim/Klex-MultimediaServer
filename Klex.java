
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
			System.out.println("tapez ajout_flux si vous voulez ajouter un nouveau logiciel");
			System.out.println("tapez ajout_album si vous voulez ajouter un nouveau album");
			System.out.println("tapez ajout_piste si vous voulez ajouter un nouveau piste");
			
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
		    	System.out.println("choisissez votre langue préferée");
		    	try {
					Langue.languesDisponibles();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	    		String language =scanner.nextLine();

	    		try {
					User user1 = new User(email1, nom, prenom, age, password1, language);
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
				
				//------------------------------------------------------------------------//
				//																		  //
				//				Si l'utilisateur veut ajouter un Flux : 				  //
				//																		  //
				//																		  //
				//------------------------------------------------------------------------//
				
			case "ajout_flux":
				
				System.out.println("Veuillez taper le type de votre flux parmi [video | audio | text] : \n");
				String type = scanner.nextLine();
				System.out.println("Veuillez taper votre idFichier: \n");
				int idFichier =Integer.parseInt(scanner.nextLine());
				System.out.println("Veuillez taper votre debit: \n");
				int debit =Integer.parseInt(scanner.nextLine());
				System.out.println("Veuillez taper votre codec: \n");
				String codec =scanner.nextLine();
		    	System.out.println("choisissez votre langue préferée: \n");
		    	
		    	int resLargeurVid = 0, resHauteurVid = 0, echantillonage = 0;
		    	try {
					Langue.languesDisponibles();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	    		String fluxLang = scanner.nextLine();
	    		if(type == "video") {
	    			System.out.println("Veuillez taper votre resLargeurVid");
	    			resLargeurVid =Integer.parseInt(scanner.nextLine());
					System.out.println("Veuillez taper votre resHauteurVid");
					resHauteurVid =Integer.parseInt(scanner.nextLine());
	    		}
	    		else if (type == "audio") {
	    			System.out.println("Veuillez taper votre echantillonage");
	    			echantillonage = Integer.parseInt(scanner.nextLine());
	    		}
				try {
	    			Flux.addFlux(type,idFichier,fluxLang,codec,debit,resLargeurVid,resHauteurVid,echantillonage);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
				
				
			case "ajout_album":
				System.out.println("Nom du album?");
				String titre = scanner.nextLine();
				System.out.println("Nom de l'Artist?");	
				String nomArtist = scanner.nextLine();
				System.out.println("Date de Sortie? (sous la forme AAAA-MM-JJ");
				String dateSortie = scanner.nextLine();
				System.out.println("URL vers l'album?");
				String URLAlbum = scanner.nextLine();
				try {
					Album.addAlbum(titre, nomArtist, dateSortie, URLAlbum);
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
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
