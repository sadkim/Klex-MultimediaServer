import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import except.EmailAlreadyExistsException;
import except.NoSuchUserException;

public class User {
		private String email;
		private String nom;
		private String prenom;
		private int age;
		private String languePrefere;
		
		
		public User(String email,String codeAcces) throws NoSuchUserException, SQLException  {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Utilisateur where email = ? and codeAccess =?");
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
			ResultSet resultat =statement.executeQuery();
			if(resultat.next()) {
				throw new EmailAlreadyExistsException("l'email:" + email +" est déja utilisé vous pouvait vous cennecter directement");
			}
			statement = BdClass.getConnection().prepareStatement("SELECT * FROM langue where langue = ?");
			statement.setString(1, languePrefere);
			statement.executeQuery();
			if(!resultat.next()) {
			    Scanner scanner2 = new Scanner(System.in);
			    boolean continu = true;
				while(continu) {
					System.out.println("langue inexistante voulais vous ajouter cette langue oui/non" );
		    		String rep =scanner2.nextLine();
		    		if(rep.equals("oui")) {
		    			Langue.ajouterLangue(languePrefere);
		    			continu=false;
		    		}else if(rep=="non") {
					    scanner2.close();
		    			return;
		    		}

					}
			    scanner2.close();

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
			this.email =resultat.getString("email");
			this.nom =resultat.getString("nom");
			this.prenom =resultat.getString("prenom");
			this.age =resultat.getInt("age");
			this.languePrefere = resultat.getString("languePrefere");
			System.out.println("vous êtes bien connécté au compte de " +this.prenom +" " + this.nom );
		}
		public String getEmail() {
			return email;
		}


		public String getNom() {
			return nom;
		}


		public String getPrenom() {
			return prenom;
		}


		public int getAge() {
			return age;
		}
		
		
}
