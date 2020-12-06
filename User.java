import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import except.EmailAlreadyExistsException;
import except.NoSuchUserException;

public class User {
		private static String email;
		private static String nom;
		private static String prenom;
		private static int age;
		private static String languePrefere;
		
		
		public User(String email,String codeAcces) throws NoSuchUserException, SQLException  {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Utilisateur where email = ? and codeAccess = ?");
			statement.setString(1, email);
			statement.setString(2, codeAcces);
			ResultSet resultat =statement.executeQuery();
			if(resultat.next()) {
				initialiseAtribut(resultat);

			}else{
				throw new NoSuchUserException("aucun utilisateur avec l'email :" + email + "ou mot de passe incorrecte");
			}
		}


		public User(String email, String nom, String prenom, int age, String codeAcces, String languePrefere) throws  EmailAlreadyExistsException, NoSuchUserException, SQLException {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Utilisateur where email = ?");
			statement.setString(1, email);
			ResultSet resultat = statement.executeQuery();
			if(resultat.next()) {
				throw new EmailAlreadyExistsException("L'email:" + email +" est déja utilisé vous pouvait vous connecter directement");
			}
			statement = BdClass.getConnection().prepareStatement("SELECT * FROM langue where langue = ?");
			statement.setString(1, languePrefere);
			statement.executeQuery();
			if(!resultat.next()) {
			    Scanner scanner = new Scanner(System.in);
			    boolean continu = true;
				while(continu) {
					System.out.println("Langue inexistante voulais vous ajouter cette langue oui/non" );
		    		String rep =scanner.nextLine();
		    		if(rep.equals("oui")) {
		    			Langue.ajouterLangue(languePrefere);
		    			continu=false;
		    		} else if(rep=="non") {
					    scanner.close();
		    			return;
		    		}

				}
			    scanner.close();

			}
			statement = BdClass.getConnection().prepareStatement("INSERT INTO Utilisateur (email, nom, prenom, age, codeAccess, LanguePREFERE) values(?,?,?,?,?,?)");
			statement.setString(1, email);
			statement.setString(2, nom);
			statement.setString(3, prenom);
			statement.setInt(4, age);
			statement.setString(5, codeAcces);
			statement.setString(6, languePrefere);
			statement.executeQuery();
			BdClass.getConnection().commit();
			initialiseAtribut(resultat);
		}
		
		
		private void initialiseAtribut(ResultSet resultat) throws SQLException {
			User.email =resultat.getString("email");
			User.nom =resultat.getString("nom");
			User.prenom =resultat.getString("prenom");
			User.age =resultat.getInt("age");
			User.languePrefere = resultat.getString("languePrefere");
			System.out.println("Vous êtes bien connecté au compte de " +User.prenom +" " + User.nom );
		}
		
		
		public static String getEmail() {
			return email;
		}


		public static String getNom() {
			return nom;
		}


		public static String getPrenom() {
			return prenom;
		}


		public static int getAge() {
			return age;
		}


		public static String getLanguePrefere() {
			return languePrefere;
		}
		
		
}
