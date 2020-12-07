import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Scanner;

import except.FilmDoesNotExistException;

public class Fichier {
		public static void addFichier(float tailleFichier) throws SQLException {
			// ajout du fichier à la base de donnée et savepoint
			
			ajouterFichierEntree(tailleFichier);
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
							contenuMultimedia(film.getTitre(), film.getAnneeSortie(),0,0);
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
						
						//TODO
						repeat = false;
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
				//TODO
		}
		
		public static void addFichier(float tailleFichier,Film film) throws SQLException {
			ajouterFichierEntree(tailleFichier);
			Savepoint fichierAjoute = BdClass.getConnection().setSavepoint("fichier ajouté");
			contenuMultimedia(film.getTitre(), film.getAnneeSortie(), 0, 0);
			Savepoint fichierContenu = BdClass.getConnection().setSavepoint("film et fichier sont reliés");
		}
		
		/*
		 * \brief ajoute l'entrée Fichier à la base de donnée et la relie à l'utilisateur actuellement connecté*/
		
		private static void ajouterFichierEntree(float tailleFichier) throws SQLException {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO Fichier (idFichier, Date, tailleFichier, Email) values(idFichierSeq.nextval,sysdate,?,?)");
			statement.setFloat(1, tailleFichier);
			statement.setString(2, User.getEmail());
			statement.executeQuery();
		}
		
	
		
		
		private static void contenuMultimedia(String Titre, int anneeSortie,int IdAlbum, int numPiste ) throws SQLException {// ne pas utiliser avant de verifier que le film ou l'album existent bien
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT Current_Value FROM SYS.Sequences	WHERE name='idFichierseq';");
			ResultSet resultat = statement.executeQuery();
			int IdFichier = resultat.getInt("Current_value");

			statement = BdClass.getConnection().prepareStatement("INSERT INTO ContenuMultimedia (idFichier, Titre, AnneeSortie,IdAlbum,NumPiste ) values(idFichierSeq.nextval,sysdate,?,?)");
			statement.setInt(1, IdFichier);
			if(anneeSortie !=0) {
			statement.setString(2, Titre);
			statement.setInt(3, anneeSortie);
			}
			if(anneeSortie !=0) {
				statement.setInt(4, IdAlbum);
				statement.setInt(5, numPiste);
			}
			statement.executeQuery();
		}
}
