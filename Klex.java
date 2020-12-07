
import java.sql.SQLException;
import java.util.Scanner;
import except.EmailAlreadyExistsException;
import except.FilmAlreadyExistException;
import except.FilmDoesNotExistException;
import except.NoSuchUserException;


public class Klex {

	public static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args)  {
		String url ="jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1" ;
    	String user = "megzaria" ;
    	String passwd = "159357";
		BdClass.connect(url, user,passwd);	
	    //Scanner scanner = new Scanner(System.in);
	    
    	System.out.println("bienvenue sur Klex");

	    
	    scanner.close();
	    try {
			BdClass.closeConnection();
		} catch (SQLException e) {
	    	System.out.println("couldn't close connection");
			e.printStackTrace();
		}


	}
	private static void boucleNonConnecte() {
		String commande;
	    boolean continuer= true;
		while(continuer) {
	    	System.out.println("tappez connection si vous avez déjà un compte");
	    	System.out.println("tappez inscription si vous êtes ici pour la première fois");
	    	System.out.println("tappez exit pour quitter l'application");
	    	commande = scanner.nextLine();
	    	switch(commande) {
	    	case "connection":
		    	System.out.println("votre email svp");
	    		String email =scanner.nextLine();
		    	System.out.println("votre mot de passe svp");
	    		String password =scanner.nextLine();
	    		try {
					new User(email, password);
					boucleConnecte();
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
	    		String langue1 =scanner.nextLine();


	    		try {
					new User(email1, nom, prenom, age, password1, langue1);
					boucleConnecte();
				} catch (EmailAlreadyExistsException e) {
			    	System.out.println(e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (NoSuchUserException e) {
		    		System.out.println(
							"NoSuchUserException juste aprés la création de l'entrée donc l'entrée ne c'est pas bien crée");
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
		
	}
	private static void boucleConnecte() {
		String commande;
	    boolean continuer= true;
		while(continuer) {
	    
	    	System.out.println("tappez aide pour voir les commandes disponibles");
	    	
			
			

	    	commande = scanner.nextLine();
	    	switch(commande) {
	    	case "aide":
	    		help();
				break;
	    
			case "ajout_logiciel":
				Logiciel.readLogicielInfo();
				break;
			
			case "ajout_album":
				Album.readInfoAlbum ();
				break;
			
			case "ajout_film":
				try {
					Film.readInfoFilm(0);
				} catch (FilmAlreadyExistException e2) {
					// ce cas est impossible
					e2.printStackTrace();
				}
				break;
			case "ajout_fichier":
		    	System.out.println("entrez la taille du fichier");
		    	
				try {
					Fichier.addFichier(Integer.parseInt(scanner.nextLine()));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (FilmDoesNotExistException e) {
					
					e.printStackTrace();
// hiiiiiiiiiiiiiiiiiiiiiiiiiiiii TODO ce cas normalement doit re traité avant d'arriver ici
				}
				break;
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


			case "ajout_piste":
				Piste.readInfoPiste();
				break;				
							
			case "exit":
	    		continuer = false;
	    		break;
	    	default:
		    	System.out.println("klex est un logiciel de gestion de fichiers");

	    		break;
	    	}
	    }
	}
	
	private static void help() {
		/* Les ajouts majeurs */
		System.out.println("tapez ajout_artiste si vous voulez ajouter un nouveau artiste");
		System.out.println("tapez ajout_logiciel si vous voulez ajouter un nouveau logiciel");
		System.out.println("tapez ajout_album si vous voulez ajouter un nouveau album");
		System.out.println("tapez ajout_piste si vous voulez ajouter un nouveau piste");
		System.out.println("tapez ajout_film si vous voulez ajouter un nouveau film");
		
		/* Ajout de modification : a rassembler dans une commande apres */
		System.out.println("tapez ajout_flux si vous voulez ajouter un nouveau logiciel");
	}
}

