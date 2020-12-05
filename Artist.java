import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;

public class Artist {
	private static int numSeqArtist;
	static {
		try{
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT MAX(numArtiste) FROM artist");
			ResultSet resultat =statement.executeQuery();
			if(resultat.next()) {
				numSeqArtist = resultat.getInt("MAX(NUMARTISTE)") + 1;
			}	
			else{
				numSeqArtist = 0;
			}
		} catch (SQLException e){
		}
	}
			
	private static void incrementNumSeqArtist(){
		numSeqArtist++;
	}

	
	public static void readArtistInfo(Scanner scanner){
		System.out.println("le nom de l'artiste svp");
    	String nomArtiste =scanner.nextLine();
    	
		System.out.println("Un url pour sa photo svp");
    	String urlPhotoArtiste =scanner.nextLine();
    	
		System.out.println("Sa spécialité principale svp");
    	String specialiteP =scanner.nextLine();
    	
		String biographie = null;
		boolean lu = false;
		while (!lu){
			System.out.println("voulez vous ajouter une biographie ?  [Y/N]");
    		String reponse =scanner.nextLine();
			switch(reponse){
				case "Y":
					System.out.println("inserer la svp, dans une seule ligne ");
					biographie = scanner.nextLine();
					lu = true;
					break;
				case "N":
					biographie = null;
					lu = true;
					break;
				default:
					System.out.println("Mauvaise réponse");
					break;
			}
		}

    	System.out.println("Sa date de naissance svp (AAAA-MM-JJ)");
		boolean dateLu = false;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date dateNaissance = null;
		while (!dateLu){
			String date = scanner.nextLine();
			if(date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
				try{
					dateNaissance = f.parse(date);
					dateLu = true;
				} catch (ParseException e){
				}
			}
			else{
				System.out.println("Erreur de format ressaisissez la");
			}
		}
		
		try {
			addArtist(nomArtiste, urlPhotoArtiste, specialiteP,biographie, dateNaissance);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}


	public static void addArtist(String nomArtiste, String urlPhotoArtiste,String specialiteP,
			String biographie, Date dateNaissance) throws SQLException {
		//TODO /* Artiste deja existant ?  Accees concurrent ? */
		System.out.println("le numSeq est " + numSeqArtist);
		int numArtiste = numSeqArtist;
		incrementNumSeqArtist();
		/* Les variables de la classe : nécessaire ? */
		PreparedStatement statement = BdClass.getConnection().prepareStatement(
"INSERT INTO Artist(NumArtiste,NomArtiste,URLphotoArtiste,biographie, specialitePrincipale, DateNaissance) values(?,?,?,?,?,?)");

		statement.setInt(1, numArtiste);
		statement.setString(2, nomArtiste);
		statement.setString(3, urlPhotoArtiste);
		if (biographie == null){
			statement.setString(4, null);
		}
		else{
			statement.setString(4, biographie);
		}
		statement.setString(5, specialiteP);
		statement.setDate(6, new java.sql.Date(dateNaissance.getTime()));
		statement.executeQuery();
		BdClass.getConnection().commit();
	}
	
	public boolean ArtistExiste(String nomArtiste) {
		//TODO
		return true;
	}
	
	public int getNumArtist() {
		//TODO
		return 0;
	}
}
