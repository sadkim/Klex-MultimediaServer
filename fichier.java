import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Scanner;

import except.FilmDoesNotExistException;

public class fichier {
		public static void addFichier(float tailleFichier,User user, Scanner scaner) throws SQLException {
			ajouterFichierEntree(tailleFichier, user);
			System.out.println("vous voulez lier le fichiel à un: film ou piste");
			String categorie = scaner.nextLine();
			if(categorie.equals("film")){
				System.out.println("ecrivez cherche pour chercher si un film existe , ecrivez existant pour choisir parmis les films existant et nouveau pour créer un nouveau film ou annuler pour revenir à l'étape précedente");
				String choix = scaner.nextLine();
				boolean repeat = true;
				
				while(repeat) {
					if(choix.equals("existant")) {
						System.out.println("tappez le nom du film");
						String titre = scaner.nextLine();
						System.out.println("tappez l'annee de sortie");
						int annee = Integer.parseInt(scaner.nextLine());
						try {
							Film film =new Film(titre,annee);
							contenuMultimedia(film.getTitre(), film.getAnneeSortie(),0,0);

							repeat = false;
								//TODO Ccategoriser
							} catch (SQLException e) {
								e.printStackTrace();
							} catch (FilmDoesNotExistException e) {
								e.printStackTrace();
								System.out.println("le titre ou la date du film sont erroné");
							}

						}else if(choix.equals("nouveau")) {
							//TODO
							repeat = false;
						}else if(choix.equals("annuler")) {
							repeat = false;
						}else if(choix.equals("affiche")) {
							System.out.println("tappez le nom du film pour le chercher ou bien le bouton entrée pour afficher tout les films disponible");
							String titre = scaner.nextLine();
							Film.searchFilm(titre, user);
							repeat = false;

						}else {
							//TODO
						}
					}
				}else if(categorie.equals("piste")) {
					System.out.println("ecrivez cherche pour chercher une piste  , ecrivez existant pour choisir parmis les pistes existantes et nouveau pour créer une nouvelle piste ou annuler pour revenir à l'étape précedente");
					//TODO
				}
				//TODO
		}
		private static void ajouterFichierEntree(float tailleFichier, User user) throws SQLException {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("INSERT INTO Fichier (idFichier, Date, tailleFichier, Email) values(idFichierSeq.nextval,sysdate,?,?)");
			statement.setFloat(1, tailleFichier);
			statement.setString(2, user.getEmail());
			statement.executeQuery();
			Savepoint fichierAjoute = BdClass.getConnection().setSavepoint("fichier ajouté");
		}
		
		public static void addFichier(float tailleFichier,User user,Film film,Scanner scaner/* TODO ajouter piste ici  pour qud c'est prêt*/) throws SQLException {
			ajouterFichierEntree(tailleFichier, user);

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
