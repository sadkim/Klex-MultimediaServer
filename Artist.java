import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Artist {
			
	/** A appeler directement si on veut creer un artiste **/
	public static void readArtistInfo() {
		readArtistInfo(false);
	}
	
	/** Permet de créer un artiste depuis album ou film : insertion sans commit si enCascade est true **/
	public static void readArtistInfo(boolean enCascade) {
	
		System.out.println("le nom de l'artiste svp");
    	String nomArtiste = Klex.scanner.nextLine();
    	
		System.out.println("un url pour sa photo svp");
    	String urlPhotoArtiste = Klex.scanner.nextLine();
    	
		System.out.println("son spécialité principale svp");
    	String specialiteP = Klex.scanner.nextLine();
    	
		String biographie = null;
		boolean lu = false;
		while (!lu){
			System.out.println("voulez vous ajouter une biographie ?  [Y/N]");
    		String reponse = Klex.scanner.nextLine();
			switch(reponse){
				case "Y":
					System.out.println("inserer la svp, dans une seule ligne ");
					biographie = Klex.scanner.nextLine();
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
			String date = Klex.scanner.nextLine();
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
			addArtist(nomArtiste, urlPhotoArtiste, specialiteP,biographie, dateNaissance, enCascade);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public static void addArtist(String nomArtiste, String urlPhotoArtiste,String specialiteP, String biographie,
			Date dateNaissance, boolean enCascade) throws SQLException {
		
		if (ArtisteExiste(nomArtiste, urlPhotoArtiste, specialiteP, biographie, dateNaissance)){
			System.out.println("un artiste existe deja avec ces donnees");
			return;
		}
		if (ArtisteExiste(nomArtiste)){
			System.out.println("un artiste existe deja avec ce nom, vous etes sur de continuer tapez [N] sinon on continue");
			String rep = Klex.scanner.nextLine();
			if (rep.equals("N")){
				return;
			}
		}
		
		PreparedStatement statement = BdClass.getConnection().prepareStatement( 
				"INSERT INTO Artist(NumArtiste,NomArtiste,URLphotoArtiste,biographie, specialitePrincipale, DateNaissance)"+
				"values( IdArtistSeq.nextval,?,?,?,?,?)");

		statement.setString(1, nomArtiste);
		statement.setString(2, urlPhotoArtiste);
		if (biographie == null){
			statement.setString(3, null);
		}
		else{
			statement.setString(3, biographie);
		}
		statement.setString(4, specialiteP);
		statement.setDate(5, new java.sql.Date(dateNaissance.getTime()));
		statement.executeQuery();

		/* Commit si et seulement si l'insertion est faite depuis la classe Klex */
		boolean ajoute = true;
		if (!enCascade){
			ajoute = Confirmation.confirmerSansCascade("Voulez vous confirmer la création de l'artiste? [Y/N]");
		}
		/* Proposer la contribution */
		if (!ajoute){ return;}
		System.out.println("Vous pouvez maintenant ajouter une contribution dans une piste ou un film");
		System.out.println("Tapez contribution si oui");
		System.out.println("Tapez autre chose pour terminer");
		String commande = Klex.scanner.nextLine();
		if (commande.equals("contribution")){
			ContributionArtiste.contributionsEnCascade(getNumArtiste(nomArtiste));
		}	
	}
	
	/** Permet de savoir si l'artiste existe deja dans la base de donnees */
	public static boolean ArtisteExiste(String nomArtiste, String urlPhotoArtiste, String specialiteP, 
			String biographie, Date dateNaissance) throws SQLException {

		PreparedStatement statement = BdClass.getConnection().prepareStatement( "SELECT * FROM ARTIST where nomArtiste = ?" + 
				"and urlPhotoArtiste = ?  and biographie = ? and specialitePrincipale = ? and dateNaissance = ? ");
		
		statement.setString(1, nomArtiste);
		statement.setString(2, urlPhotoArtiste);
		statement.setString(3, specialiteP);
		statement.setString(4, biographie);
		
		statement.setDate(5, new java.sql.Date(dateNaissance.getTime()));

		ResultSet resultat = statement.executeQuery();
		return resultat.next();	
	}
		
	/** Permet de savoir si un artiste existe deja avec le meme nom **/

	public static boolean ArtisteExiste(String nomArtiste) throws SQLException {
		PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM ARTIST where nomArtiste = ?");
		statement.setString(1, nomArtiste);
		ResultSet resultat = statement.executeQuery();
		return resultat.next();	
	}
	
	/** Donne l'identifiant de l'artiste a partir de son nom : pas de fiabilite **/

	public static Integer getNumArtiste(String nomArtiste) throws SQLException {
		PreparedStatement s = BdClass.getConnection().prepareStatement("SELECT NumArtiste FROM ARTIST where nomArtiste = ?");
		s.setString(1, nomArtiste);
		ResultSet resultat = s.executeQuery();
		if (resultat.next()){
			return resultat.getInt("NumArtiste");
		}
		return null;
	}
	
	/** Donne le nom de l'artiste à partir de son identifiant **/
	public static String getNomArtiste(int numArtiste) throws SQLException{
		PreparedStatement s = BdClass.getConnection().prepareStatement("SELECT NomArtiste FROM ARTIST where NumArtiste = ?");
		s.setInt(1, numArtiste);
		ResultSet result = s.executeQuery();
		return null;
	}
	
	/** Supression des artistes qui ne sont pas references par d'autre contenus *
	 * On ne supprime pas les artistes qui ont des intervention ou sont des artistes principales dans des albums */
	public static void nettoyageArtiste() throws SQLException{  
		PreparedStatement s = BdClass.getConnection().prepareStatement(
				"SELECT NumArtiste FROM ARTIST where NumArtiste NOT IN (SELECT NumArtiste FROM ContributionPiste" + 
				"UNION SELECT NumArtiste FROM ContributionFilm  UNION SELECT NumArtiste FROM Album )");
		ResultSet result = s.executeQuery();
		while(result.next()){
			int numArtisteSuppr = result.getInt("NumArtiste"); 
			PreparedStatement statementSupprRole = BdClass.getConnection().prepareStatement(
					"DELETE FROM ARTIST WHERE numArtiste = ?");
			statementSupprRole.setInt(1, numArtisteSuppr);
			statementSupprRole.executeQuery();
		}
	}
}
