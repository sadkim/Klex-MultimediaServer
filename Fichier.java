import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Scanner;

import except.FilmAlreadyExistException;
import except.FilmDoesNotExistException;

public class Fichier {
	
		public static void addFichier(float tailleFichier) throws SQLException, FilmDoesNotExistException {
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
				System.out.println("ecrivez cherche pour chercher une piste  , ecrivez existant pour choisir parmis les pistes existantes et nouveau pour créer une nouvelle piste ou annuler pour revenir à l'étape précedente");
				//TODO
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
		    			} else if(rep=="non") {
		    				continu = false;
		    			}

					}
				}
			}
			repeat = true;
			while(repeat) {
				System.out.println("choisissez la langue le codec");
				Codec.codecDisponibles();
				codec = Klex.scanner.nextLine();
				if(!Codec.codecExist(codec)) {
					boolean continu = true;
					while(continu) {
						System.out.println("codec inexistantant voulais vous ajouter cette langue oui/non" );
		    			String rep =Klex.scanner.nextLine();
		    			if(rep.equals("oui")) {
		    				Codec.addCodec(codec, true);
		    				continu=false;
		    				repeat = false;
		    			} else if(rep=="non") {
		    				continu = false;
		    			}

					}
				}
			}
			System.out.println("entrez debit" );
			debit = Integer.getInteger(Klex.scanner.nextLine());
			if(type.equals("video")){
				System.out.println("entrez resHaueurVid" );
				resHauteurVid = Integer.getInteger(Klex.scanner.nextLine());
				System.out.println("entrez resLargeurVid" );
			}
			if(type.equals("audio")){
				resLargeurVid = Integer.getInteger(Klex.scanner.nextLine());
				System.out.println("entrez echantillonage" );
				echantillonage = Integer.getInteger(Klex.scanner.nextLine());
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
			Savepoint fichierContenu = BdClass.getConnection().setSavepoint("film_et_fichier_sont_relies");
		}
	
		/*
		 * \brief ajoute l'entrée Fichier à la base de donnée et la relie à l'utilisateur actuellement connecté*/
		
		private static int ajouterFichierEntree(float tailleFichier) throws SQLException {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("Select idFichierSeq.nextval From dual");
			ResultSet resultat = statement.executeQuery();
			//loin d'y arriver juste pour ne pas avoir des SQLException
			if (!resultat.next()){ throw new SQLException("we reached the limits");} 
			int idFichier= resultat.getInt("NEXTVAL");

			statement = BdClass.getConnection().prepareStatement("INSERT INTO Fichier (idFichier, DateFichier, tailleFichier, Email) values(?,sysdate,?,?)");
			statement.setInt(1, idFichier);
			statement.setFloat(2, tailleFichier);
			statement.setString(3, User.getEmail());
			statement.executeQuery();
			return idFichier;
		}
		
	
		
		
		private static void contenuMultimedia(int idFichier, String Titre, int anneeSortie,int IdAlbum, int numPiste ) throws SQLException {// ne pas utiliser avant de verifier que le film ou l'album existent bien
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
