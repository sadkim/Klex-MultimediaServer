import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Scanner;


import except.FilmAlreadyExistException;
import except.FilmDoesNotExistException;

public class Fichier {

	
		public static void addFichier(float tailleFichier) throws SQLException, FilmDoesNotExistException{ 
			// ajout du fichier à la base de donnée et savepoint
			
			int idFichier=ajouterFichierEntree(tailleFichier);
			Savepoint fichierAjoute = BdClass.getConnection().setSavepoint("fichier ajouté");
			
			//receullir les information à props de la liaison
			
			System.out.println("il est obligatoire de lier le fichier à un film ou bien une piste ");
			System.out.println("choisissez: film | piste ");
			String categorie = Klex.scanner.nextLine();
			switch(categorie){
			case "film":
				System.out.println("ecrivez cherche pour chercher si un film existe ou voir la liste des films existants , ecrivez existant pour choisir parmis les films existant et nouveau pour créer un nouveau film ou annuler pour revenir à l'étape précedente");
				String choix = Klex.scanner.nextLine();
				boolean repeat = true;
				
				while(repeat) {
					switch(choix) {
						
					
					//lier à un fim existant
					case "existant":
						//film data
						System.out.println("tappez le nom du film");
						String titre = Klex.scanner.nextLine();
						System.out.println("tappez l'annee de sortie");
						int annee = Integer.parseInt(Klex.scanner.nextLine());
						
						
						try {
							Film film =new Film(titre,annee);
							contenuMultimedia(idFichier,film.getTitre(), film.getAnneeSortie(),0,0);
							Savepoint fichierContenu = BdClass.getConnection().setSavepoint("film et sont reliés");
							repeat = false;
							//TODO ici creation d'un nouveaux flux
							} catch (SQLException e) {
								e.printStackTrace();
							} catch (FilmDoesNotExistException e) {
								e.printStackTrace();
								System.out.println("le titre ou la date du film sont erroné essayer de chercher le film ");
							}
						break;


						
					//creer un nouveau film et lier à ce film
					case "nouveau" :
						try {
							Film.readInfoFilm(idFichier);
							repeat = false;
						} catch (FilmAlreadyExistException e) {
							System.out.println("le film que vous essayez de créer existe déja ");
;
						}
						break;

						

					case "annuler":
						//TODO
						repeat = false;
						break;
						
						

					case "cherche":
						System.out.println("tappez le nom du film pour le chercher ou bien le bouton entrée pour afficher tout les films disponible");
						String titre1 = Klex.scanner.nextLine();
						Film.searchFilm(titre1);
						break;
					default :
						break;
						
						
					}
				}
				break;

				
				
			case "piste":
				System.out.println("la version actuelle de Klex permet d'associer les fichiers à des nouvelles pistes dont" +
						"l'album est déjà enregistré dans la base");
				System.out.println("ecrivez cherche pour chercher une piste, ecrivez existant pour choisir parmis les pistes" +
						"existantes et nouveau pour créer une nouvelle piste ou annuler pour revenir à l'étape précedente");
				boolean repeat2 = true;
				String choix2 = Klex.scanner.nextLine();
				while(repeat2) {
					switch(choix2) {
						case "existant":
							System.out.println(" Tappez le nom de l'artiste ");
							String nomArtiste = Klex.scanner.nextLine();
							System.out.println(" Tappez le titre de l'album ");
							String titre = Klex.scanner.nextLine();
							System.out.println(" Tappez le nom de la piste ");
							String nomPiste = Klex.scanner.nextLine();
							try {
								Integer numArtiste = Artist.getNumArtiste(nomArtiste);
								if (numArtiste == null){
									System.out.println("le nom de l'artiste que vous avez insérer n'est pas enregistré");
									break;
								}
								Integer idAlbum = Album.trouveIdentifiant(titre, numArtiste);
								if (idAlbum == null){
									System.out.println("l'album que vous avez insérer n'est pas enregistrée");
									break;
								}
								
								Integer numPiste = Piste.getNbPiste(idAlbum , nomPiste);
								if (numPiste == null){
									System.out.println("la piste que vous avez insérer n'est pas enregistrée");
									break;
								}
								Piste piste = new Piste(idAlbum , numPiste);
								contenuMultimedia(idFichier, null,0,  piste.getIdAlbum(), piste.getnumPiste());
								Savepoint fichierContenu = BdClass.getConnection().setSavepoint("piste_et_fichier_relies");
								repeat2 = false;
							} catch (SQLException e) {
								e.printStackTrace();
							}
							break;

						/* creer une nouvelle piste et la relier à ce fichier */
						case "nouveau" :
							Piste.readInfoPiste(idFichier, true);
							repeat2 = false;
							break;	

						case "annuler":
							repeat2 = false;
							break;
						
						case "cherche":
							System.out.println("tappez le nom de la piste pour la chercher");
							String titre1 = Klex.scanner.nextLine();
							Piste.searchPiste(titre1);;
							break;
						default :
							break;
					}
				}
				break;
			}
			
			
			// insertion du flux
			inssererFlux(idFichier);
			BdClass.getConnection().commit();
			System.out.println("votre fichier, le Film ou la piste si vous avez choisis d'en ajouter et le flux associé ont bien été crée.\n merci pour votre contribution");
			

		}

		private static void inssererFlux(int idFichier) throws SQLException {
			String type ="haha";
			String langue ="hey";
			String codec = null;
			int debit=0;
			int resHauteurVid=0;
			int resLargeurVid=0;
			int echantillonage =0;
			while(!type.equals("video") && !type.equals("audio") && !type.equals("text")) {
				System.out.println("inserez le type de fllux svp video | audio | text");
				type = Klex.scanner.nextLine();
			}
			boolean repeat = true;
			while(repeat) {
				System.out.println("choisissez la langue du flux");
				Langue.languesDisponibles();
				langue = Klex.scanner.nextLine();
				if(!Langue.langueExiste(langue)) {
					boolean continu = true;
					while(continu) {
						System.out.println("Langue inexistante voulais vous ajouter cette langue oui/non" );
		    			String rep =Klex.scanner.nextLine();
		    			if(rep.equals("oui")) {
		    				Langue.ajouterLangue(langue);
		    				continu=false;
		    				repeat = false;
		    			} else if(rep.equals("non")) {
		    				continu = false;
		    			}
					}
				}
				else{
					repeat = false;
				}
			}

			repeat = true;
			while(repeat) {
				System.out.println("choisissez le codec");
				Codec.codecDisponibles();
				codec = Klex.scanner.nextLine();
				if(!Codec.codecExist(codec)) {
					boolean continu = true;
					while(continu) {
						System.out.println("codec inexistantant voulais vous ajouter ce codec oui/non" );
		    			String rep =Klex.scanner.nextLine();
		    			if(rep.equals("oui")) {
		    				Codec.addCodec(codec, true);
		    				continu=false;
		    				repeat = false;
		    			} else if(rep.equals("non")) {
		    				continu = false;
		    			}
					}
				}
				else{
					repeat = false; //TODO <<==== ajouter car une boucle infini est remarqué 
				}
			}

			System.out.println("entrez debit" );
			debit = Integer.parseInt(Klex.scanner.nextLine());
			if(type.equals("video")){
				boolean erreur = false;
				while (resHauteurVid == 0 || resLargeurVid == 0){
					if (erreur){ System.out.println("erreur de saisie, entrez des valeurs non nulles");}
					System.out.println("entrez resHaueurVid" );
					resHauteurVid = Integer.parseInt(Klex.scanner.nextLine());
					System.out.println("entrez resLargeurVid" );
					resLargeurVid = Integer.parseInt(Klex.scanner.nextLine());
					erreur = true;
				}
			}
			if(type.equals("audio")){
				System.out.println("entrez echantillonage : 16 , 24, 32" );
				boolean continu = true;
				while (continu){
					echantillonage = Integer.parseInt(Klex.scanner.nextLine());
					if (echantillonage != 16 && echantillonage != 24 && echantillonage != 32){
						System.out.println("mauvais réponse, insérer un echantillonage correct");
					}
					else{
						continu = false;
					}
				}
			}
			Flux.addFlux(type, idFichier, langue, codec, debit, resLargeurVid, resHauteurVid, echantillonage);
		}
		
		public static void addFichier(float tailleFichier,Film film) throws SQLException {
			int idFichier =ajouterFichierEntree(tailleFichier);
			Savepoint fichierAjoute = BdClass.getConnection().setSavepoint("fichier_ajoute");
			contenuMultimedia(idFichier, film.getTitre(), film.getAnneeSortie(), 0, 0);
			Savepoint fichierContenu = BdClass.getConnection().setSavepoint("film_et_fichier_sont_relies");
			inssererFlux(idFichier);
			BdClass.getConnection().commit();
			System.out.println("Film,et fichiers et flux associés ont été crée ");
		}
	
		public static void addFichier(float tailleFichier,Piste piste) throws SQLException {
			int idFichier =ajouterFichierEntree(tailleFichier);
			Savepoint fichierAjoute = BdClass.getConnection().setSavepoint("fichier_ajoute");
			contenuMultimedia(idFichier,null ,0, piste.getIdAlbum(), piste.getnumPiste());
			Savepoint fichierContenu = BdClass.getConnection().setSavepoint("piste_et_fichier_sont_relies");
			inssererFlux(idFichier);
			BdClass.getConnection().commit();
			System.out.println("piste et fichiers et flux associés ont été crée ");
		}
	
		/*
		 * \brief ajoute l'entrée Fichier à la base de donnée et la relie à l'utilisateur actuellement connecté*/
		
		private static int ajouterFichierEntree(float tailleFichier) throws SQLException {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("Select idFichierSeq.nextval From dual");
			ResultSet resultat = statement.executeQuery();
			//loin d'y arriver juste pour ne pas avoir des SQLException
			if (!resultat.next()){ throw new SQLException("we reached the limits");} 
			int idFichier= resultat.getInt("NEXTVAL");

			statement = BdClass.getConnection().prepareStatement(
					"INSERT INTO Fichier (idFichier, DateFichier, tailleFichier, Email) values(?,sysdate,?,?)");
			statement.setInt(1, idFichier);
			statement.setFloat(2, tailleFichier);
			statement.setString(3, User.getEmail());
			statement.executeQuery();
			return idFichier;
		}
		
		private static void contenuMultimedia(int idFichier, String Titre, int anneeSortie,int IdAlbum, int numPiste ) 
			throws SQLException {// ne pas utiliser avant de verifier que le film ou l'album existent bien
			PreparedStatement statement = BdClass.getConnection().prepareStatement(
					"INSERT INTO ContenuMultimedia (idFichier, Titre, AnneeSortie,IdAlbum ,NumPiste ) values(?,?,?,?,?)");
			statement.setInt(1, idFichier);
			if(anneeSortie !=0) {
				statement.setString(2, Titre);
				statement.setInt(3, anneeSortie);
				statement.setNull(4, java.sql.Types.INTEGER);
				statement.setNull(5, java.sql.Types.INTEGER);
			}
			else if (anneeSortie == 0){
				statement.setNull(2, java.sql.Types.INTEGER);
				statement.setNull(3, java.sql.Types.INTEGER);
				statement.setInt(4, IdAlbum);
				statement.setInt(5, numPiste);
			}
			statement.executeQuery();
		}
		
}
