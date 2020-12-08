
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
	    
    	System.out.println("bienvenue sur Klex");
	   	boucleNonConnecte(); 
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


	private static void boucleConnecte() throws SQLException {
		String commande;
	    boolean continuer= true;
		while(continuer) {
	    
	    	System.out.println("tappez aide pour voir les commandes disponibles");
	    	
	    	commande = scanner.nextLine();
	    	switch(commande) {
	    	case "aide":
	    		help1();
				break;
	    	
			case "ajouter_contenus":
				ajoutContenus();
				break;

			case "chercher_films":
				chercherFilms();
				break;
			
			case "chercher_pistes":
				chercherPistes();
				break;

			case "chercher_albums":
				chercherAlbums();
				break;
			
			case "supprimer":
				suppressionContenus();
				break;

			case "modifier":
				modifierBase();
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
	
	private static void ajoutContenus() throws SQLException {
		String commande;
	    boolean continuer= true;
		while(continuer) {
	    
	    	System.out.println("tappez aide pour voir les commandes disponibles");
	    	
	    	commande = scanner.nextLine();
	    	switch(commande) {
	   			case "aide":
	    			help2();
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
				
				case "ajout_artiste":
					Artist.readArtistInfo();
					break;
			
				case "ajout_piste":
					Piste.readInfoPiste(true);
					break;
			
				case "ajout_contrib":
					ContributionArtiste.ajouterUneContribution();
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
		
	private static void chercherFilms()throws SQLException {
		String commande;
	    boolean continuer= true;
		while(continuer) {
	    
	    	System.out.println("tappez aide pour voir les commandes disponibles");
	    	
	    	commande = scanner.nextLine();
	    	switch(commande) {
	   			case "aide":
	    			help5();
					break;
	    	
				case "ajout_filtre":
					System.out.println("choisissez le type de filtre langue |langueSousTitre | categorie");
					String categ = scanner.nextLine();
					String valeur = "";
					if(categ.equals("langue") || categ.equals("langueSousTitre")) {
						boolean repeat = true;
						while(repeat) {
							System.out.println("choisissez la langue");
							Langue.languesDisponibles();
							valeur = scanner.nextLine();
							if(Langue.langueExiste(valeur)) {
								repeat =false;
							}
						}
					}else if(categ.equals("categorie")) {
						boolean repeat = true;
						while(repeat) {
							System.out.println("choisissez la categorie");
							CategorieFilm.CategoriesFilmDispo();
							valeur = scanner.nextLine();
							if(CategorieFilm.ExisteCategFilm(valeur)) {
								repeat =false;
							}
						}
					
					}else{
						System.out.println("filtreEronnée");	
					}
					Film.addFilter(new Filtre(categ, valeur));
					break;
				case "chercher":
					System.out.println("Tapez le nom du film que vous voulez chercher");
					System.out.println("Tapez sur la touche Entrer si vous voulez faire une recherche globale");
					String name = scanner.nextLine();
					Film.searchFilm(name);
					break;

				case "vider_filtres":	
					Film.deleteFilters();
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
	
	private static void chercherPistes()throws SQLException {
		String commande;
	    boolean continuer= true;
		while(continuer) {
	    
	    	System.out.println("tappez aide pour voir les commandes disponibles");
	    	
	    	commande = scanner.nextLine();
	    	switch(commande) {
	   			case "aide":
	    			help5();
					break;
	    	
				case "ajout_filtre":
					System.out.println("choisissez le type de filtre langue | categorie");
					String categ = scanner.nextLine();
					String valeur = "";
					if(categ.equals("langue")) {
						boolean repeat = true;
						while(repeat) {
							System.out.println("choisissez la langue");
							Langue.languesDisponibles();
							valeur = scanner.nextLine();
							if(Langue.langueExiste(valeur)) {
								repeat =false;
							}
						}
					}else if(categ.equals("categorie")) {
						boolean repeat = true;
						while(repeat) {
							System.out.println("choisissez la categorie");
							CategorieMusique.CategoriesMusiqueDispo();
							valeur = scanner.nextLine();
							if(CategorieMusique.ExisteCategMusique(valeur)){
								repeat =false;
							}
						}
					
					}else{
						System.out.println("filtreEronnée");	
					}

					Piste.addFilter(new Filtre(categ, valeur));
					break;

				case "chercher":
					System.out.println("Tapez le nom de la piste que vous voulez chercher");
					System.out.println("Tapez sur la touche Entrer si vous voulez faire une recherche globale");
					String name = scanner.nextLine();
					Piste.rechercheFiltrePiste(name);
					break;

				case "vider_filtres":	
					Piste.deleteFilters();
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

	private static void chercherAlbums() throws SQLException {
		String commande;
	    boolean continuer= true;
		while(continuer) {
	    	System.out.println("tappez aide pour voir les commandes disponibles");
	    	
	    	commande = scanner.nextLine();
	    	switch(commande) {
	   			case "aide":
	    			help5();
					break;
	    	
				case "ajout_filtre":
					System.out.println("choisissez le type de filtre : categorie");
					String categ = scanner.nextLine();
					String valeur = "";
					
					if(categ.equals("categorie")) {
						boolean repeat = true;
						while(repeat) {
							System.out.println("choisissez la categorie");
							CategorieMusique.CategoriesMusiqueDispo();
							valeur = scanner.nextLine();
							if(CategorieMusique.ExisteCategMusique(valeur)){
								repeat =false;
							}
						}
					
					}else{
						System.out.println("filtreEronnée");	
					}

					Album.addFilter(new Filtre(categ, valeur));
					break;

				case "chercher":
					System.out.println("Tapez le nom de l'album que vous voulez chercher");
					System.out.println("Tapez sur la touche Entrer si vous voulez faire une recherche globale");
					String name = scanner.nextLine();
					Album.rechercheFiltreAlbum(name);
					break;

				case "vider_filtres":	
					Album.deleteFilters();
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

	private static void suppressionContenus() throws SQLException {
		String commande;
	    boolean continuer= true;
		while(continuer) {
	    
	    	System.out.println("tappez aide pour voir les commandes disponibles");
	    	
	    	commande = scanner.nextLine();
	    	switch(commande) {
	   			case "aide":
	    			help3();
					break;
	    	
 				case "supprimer_film":
					Film.toSupprimeFilm();
					break;
				
				case "supprimer_piste":
					Piste.readInfoPiste(false);
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
	
	private static void modifierBase() throws SQLException {
		String commande;
	    boolean continuer= true;
		while(continuer) {
	    
	    	System.out.println("tappez aide pour voir les commandes disponibles");
	    	
	    	commande = scanner.nextLine();
	    	switch(commande) {
	   			case "aide":
	    			help4();
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
	    				System.out.println("Veuillez tapper votre resLargeurVid");
	    				resLargeurVid =Integer.parseInt(scanner.nextLine());
						System.out.println("Veuillez taper votre resHauteurVid");
						resHauteurVid =Integer.parseInt(scanner.nextLine());
	    			}
	    			else if (type == "audio") {
	    				System.out.println("Veuillez tapper votre echantillonage");
	    				echantillonage = Integer.parseInt(scanner.nextLine());
	    			}
					try {
	    				Flux.addFlux(type,idFichier,fluxLang,codec,debit,resLargeurVid,resHauteurVid,echantillonage);
					} catch (SQLException e) {
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
	}
	
	private static void help1(){
		System.out.println("Tapez [ajouter_contenus] si vous voulez faire des ajouts" + 
				"[film/piste/artiste/interventions/fichiers/logiciel]");
		System.out.println("Tapez [chercher_films] pour faire des recherches filtrées des films dans la base");
		System.out.println("Tapez [chercher_pistes] pour faire des recherches filtrées des pistes dans la base");
		System.out.println("Tapez [chercher_albums] pour faire des recherches filtrées des albums dans la base");
		System.out.println("Tapez [supprimer] pour supprimer des films ou des pistes");
		System.out.println("Tapez modifier si vous voulez faire des modifications dans la base");
		System.out.println("Tapez exit pour se déconnecter");
	}

	public static void help2(){
		System.out.println("tapez ajout_artiste si vous voulez ajouter un nouveau artiste");
		System.out.println("tapez ajout_album si vous voulez ajouter un nouveau album");
		System.out.println("tapez ajout_piste si vous voulez ajouter un nouveau piste");
		System.out.println("tapez ajout_film si vous voulez ajouter un nouveau film");
		System.out.println("tapez ajout_contrib si vous voulez ajouter la contribution d'un artiste dans un film/piste");
		System.out.println("tapez ajout_logiciel si vous voulez ajouter un nouveau logiciel");
		System.out.println("tapez ajout_filtre si vous voulez faire un recherche filtré");	
		System.out.println("Tapez exit pour quitter la rubrique");
	}

	public static void help3(){
		System.out.println("tapez supprimer_piste si vous voulez supprimer une piste");
		System.out.println("tapez supprimer_film si vous voulez supprimer un film");
		System.out.println("Tapez exit pour quitter la rubrique");
	}

	public static void help4(){
		System.out.println("tapez ajout_flux si vous voulez ajouter un nouveau logiciel");
		System.out.println("Tapez exit pour quitter la rubrique");
	}

	public static void help5(){
		System.out.println("tapez ajout_filtre si vous voulez ajouter un filtre ");	
		System.out.println("tapez vider_filtres si vous voulez vider les filtres ");	
		System.out.println("tapez chercher si vous voulez chercher un film");
		System.out.println("Un recherche sans ajout d'un filtre affiche tous les films disponibles");
		System.out.println("Tapez exit pour quitter la rubrique");
	
	}
}
