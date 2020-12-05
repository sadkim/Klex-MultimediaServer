import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import except.FilmDoesNotExistException;


public class Film {
	private String titre;
	private int anneeSortie;
	public String getTitre() {
		return titre;
	}

	public int getAnneeSortie() {
		return anneeSortie;
	}

	public Film(String titre, int anneeSortie) throws SQLException, FilmDoesNotExistException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("Select * From Film where Titre = ? and anneeSortie = ?");
		statement.setString(1, titre);
		statement.setInt(2, anneeSortie);
		ResultSet resultat =statement.executeQuery();
		if(resultat.next()) {
			this.titre = resultat.getString("titre");
			this.anneeSortie = resultat.getInt("anneeSortie");
		}else {
			throw new FilmDoesNotExistException("le titre ou la date du film est erroné");
		}
	}

	public static void addFilm(String titre, int anneeSortie, String Resume, int ageMin, String urlAffiche, fichier Fichier, Scanner scanner) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("Select * From Film where Titre = ? and anneeSortie = ?");
		statement.setString(1, titre);
		statement.setInt(2, anneeSortie);
		ResultSet resultat =statement.executeQuery();
		if(resultat.next()) {
			System.out.println("le film est déja dans la base de données vous pouvez lire ses fichiers ou ajouter de nouveau tappez help pour avoir de l'aide ");
		}
		else {
		statement = BdClass.getConnection().prepareStatement("INSERT INTO Film (Titre, anneeSortie, Resume, ageMin, UrlAffiche) values(?,?,?,?,?)");
		statement.setString(1, titre);
		statement.setInt(2, anneeSortie);
		if(Resume.equals("")) {
			Resume = null;
		}
		statement.setString(3, Resume);
		statement.setInt(4, ageMin);
		statement.setString(5, urlAffiche);
		statement.executeQuery();
		System.out.println("à chaque film doit être associée au moins un fichier ajouter un fichier en tappant ajouteFichier ou bien annulez la création en tappant autre chose");
    	String commande = scanner.nextLine();
    	if(commande.equals("ajouteFichier")) {
    		//TODO
    	}else {
    		BdClass.getConnection().rollback();
    		System.out.println("ajout film annulé");

    	}

		BdClass.getConnection().commit();
		}

	}
	
	public static void searchFilm(String name, User user) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM film where titre like'*?*' and ageMin < ? order by titre");
		statement.setString(1, name);
		statement.setInt(2, user.getAge());
		ResultSet resultat =statement.executeQuery();
		System.out.println("titre | annéeSortie | Resume | UrlAffiche");
		while(resultat.next()) {
			System.out.println(resultat.getString("Titre")+" | "+ resultat.getString("anneeSortie") +" | "+ resultat.getString("Resume") +" | "+ resultat.getString("urlAffiche"));
		}

	}
}
