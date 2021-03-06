import java.sql.SQLException;
import java.util.Scanner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ContributionArtiste {
	
	/** on va faire un commit car les contributions ne sont pas obligatoires, on fait un commit de piste et de film
	 * avant appel à ces méthodes 
	 * ces méthodes ne vérifient pas complétement l'existance des élements **/
	private static boolean readContribPiste(int numArtiste) throws SQLException{
		System.out.println("Insérez le nom de l'album");
		String titreAlbum = Klex.scanner.nextLine();
		Integer idAlbum = Album.trouveIdentifiant(titreAlbum , numArtiste);
		if (idAlbum == null){
			System.out.println("l'album n'existe pas");
			return false;
		}
		System.out.println("Insérer le nom de la piste");
		String nomPiste = Klex.scanner.nextLine();
		Integer numPiste = Piste.getNbPiste(idAlbum , nomPiste);
		if (numPiste == null){
			System.out.println("la piste n'existe pas");
			return false;
		}
		System.out.println("Insérez l'instrument de cet artiste");
		String instrument = Klex.scanner.nextLine();
		return addContributionPiste(numArtiste, idAlbum, numPiste, instrument);

	}
	
	private static boolean readContribFilm(int numArtiste) throws SQLException{
		System.out.println("Insérez le titre du film");
		String titre = Klex.scanner.nextLine();
		System.out.println("Insérer l'année de la sortie du film");
		int anneeSortie = Integer.parseInt(Klex.scanner.nextLine());
		if(!Film.existeFilm (titre, anneeSortie)){
			System.out.println("le film n'existe pas");
			return false;
		}
		System.out.println("Insérez le rôle de cet artiste dans ce film");
		String role = Klex.scanner.nextLine();
		return addContributionFilm(numArtiste, titre, anneeSortie, role);
	}
	

	private static boolean readContribPiste(int idAlbum, int numPiste) throws SQLException{
		System.out.println("Insérez le nom de l'artiste");
		String nomArtiste = Klex.scanner.nextLine();
		Integer numArtiste = Artist.getNumArtiste(nomArtiste);
		if (numArtiste == null){
			System.out.println("l'artiste n'existe pas");
			return false;
		}
		System.out.println("Insérez l'instrument de cet artiste");
		String instrument = Klex.scanner.nextLine();
		return addContributionPiste(numArtiste, idAlbum, numPiste, instrument);
	}

	private static boolean readContribFilm(String titreFilm, int anneeSortie) throws SQLException{
		System.out.println("Insérez le nom de l'artiste");
		String nomArtiste = Klex.scanner.nextLine();
		Integer numArtiste = Artist.getNumArtiste(nomArtiste);
		if (numArtiste == null){
			System.out.println("l'artiste n'existe pas");
			return false;
		}
		System.out.println("Insérez le rôle de cet artiste dans ce film");
		String role = Klex.scanner.nextLine();
		return addContributionFilm(numArtiste, titreFilm, anneeSortie, role);
	}
	
	private static boolean addContributionPiste(int numArtiste, int idAlbum, int numPiste, String instrument) throws SQLException{
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"insert into ContributionPiste(numArtiste, idAlbum, numPiste, instrument) values(?, ?, ?, ?)");
		statement.setInt(1, numArtiste);
		statement.setInt(2, idAlbum);
		statement.setInt(3, numPiste);
		statement.setString(4, instrument);
		statement.executeQuery();
		return Confirmation.confirmerSansCascade("Voulez vous confirmer la contribution de cet artiste [Y/N]");
	}

	private static boolean addContributionFilm(int numArtiste,String titreFilm, int anneeSortie, String role) throws SQLException{
		
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
				"insert into ContributionFilm(numArtiste, titre, anneeSortie, role) values(?, ?, ?, ?)");
		statement.setInt(1, numArtiste);
		statement.setString(2, titreFilm);
		statement.setInt(3, anneeSortie);
		statement.setString(4, role);
		statement.executeQuery();
		return Confirmation.confirmerSansCascade("Voulez vous confirmer la contribution de cet artiste [Y/N]");
	}

	public static boolean contributionsEnCascade(int numArtiste) throws SQLException{
		boolean termine = false;
		String commande = null;
		boolean ajoute = false;
		while (!termine){
			System.out.println("Tapez film si vous voulez ajouter une contribution dans un film");
			System.out.println("Tapez piste si vous voulez ajouter une contribution dans une piste");
			System.out.println("Tapez terminer si vous avez terminé");
			commande = Klex.scanner.nextLine();
			switch (commande){
				case "film" : 
					ajoute = ajoute || readContribFilm(numArtiste);
					break;
				case "piste" :
					ajoute = ajoute || readContribPiste(numArtiste);
					break;
				case "terminer":
					termine = true;
					break;
				default :
					System.out.println("mauvais réponse");
					break;
			}
		}
		return ajoute;
	}


	public static boolean contributionsEnCascade(String titre, int anneeSortie) throws SQLException{
		boolean termine = false;
		String commande = null;
		boolean ajoute = false;
		while (!termine){
			System.out.println("Tapez artiste si vous voulez ajouter la contribution d'un artiste");
			System.out.println("Tapez terminer si vous avez terminé");
			commande = Klex.scanner.nextLine();
			switch (commande){
				case "artiste" : 
					ajoute = ajoute || readContribFilm (titre, anneeSortie);
					break;
				case "terminer":
					termine = true;
					break;
				default :
					System.out.println("mauvais réponse");
					break;
			}
		}
		return ajoute;	
	}


	public static boolean contributionsEnCascade(int idAlbum , int numPiste) throws SQLException{
		boolean termine = false;
		String commande = null;
		boolean ajoute = false;
		while (!termine){
			System.out.println("Tapez artiste si vous voulez ajouter la contribution d'un artiste");
			System.out.println("Tapez terminer si vous avez terminé");
			commande = Klex.scanner.nextLine();
			switch (commande){
				case "artiste" : 
					ajoute = ajoute || readContribPiste(idAlbum , numPiste);
					break;
				case "terminer":
					termine = true;
					break;
				default :
					System.out.println("mauvais réponse");
					break;
			}
		}
		return ajoute;
	}

	public static void ajouterUneContribution() throws SQLException{
		System.out.println("Tapez le nom de l'artiste");
		String nomArtiste = Klex.scanner.nextLine();
		Integer numArtiste = Artist.getNumArtiste(nomArtiste);
		if (numArtiste == null){
			System.out.println("l'artiste n'existe pas");
			return;
		}
		System.out.println("Tapez [film] si vous voulez ajouter une intervention dans un film");
		System.out.println("Tapez [piste] si vous voulez ajouter une intervention dans une piste");
		System.out.println("Tapez autre chose pour annuler cette opération");
		String commande = Klex.scanner.nextLine();
		if (commande.equals("film")){
			readContribFilm(numArtiste);
		} else if (commande.equals("piste")){
			readContribPiste(numArtiste);
		}
		else{
			return;
		}
	}
}
