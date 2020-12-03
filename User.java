import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import except.EmailAlreadyExistsException;
import except.NoSuchUserException;

public class User {
		private String email;
		private String nom;
		private String prenom;
		private int age;
		
		
		public User(String email,String codeAcces) throws NoSuchUserException, SQLException  {
			connect(email, codeAcces);
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
		
		public User(String email, String nom, String prenom, int age, String codeAcces) throws SQLException, EmailAlreadyExistsException, NoSuchUserException {
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Utilisateur where email = ?");
			statement.setString(1, email);
			ResultSet resultat =statement.executeQuery();
			if(resultat.next()) {
				throw new EmailAlreadyExistsException("l'email:" + email +" est déja utilisé vous pouvait vous cennecter directement");
			}
			statement = BdClass.getConnection().prepareStatement("INSERT INTO Utilisateur (email, nom, prenom, age, codeAcces) values(?,?,?,?,?)");
			statement.setString(1, email);
			statement.setString(2, nom);
			statement.setString(3, prenom);
			statement.setInt(4, age);
			statement.setString(5, codeAcces);
			statement.executeQuery();
			BdClass.getConnection().commit();
			connect(email,codeAcces);
			

			

		}
		private void connect(String email,String codeAcces) throws NoSuchUserException, SQLException{
			PreparedStatement statement = BdClass.getConnection().prepareStatement("SELECT * FROM Utilisateur where email = ? and codeAcces =?");
			statement.setString(1, email);
			statement.setString(2, codeAcces);
			ResultSet resultat =statement.executeQuery();
			if(resultat.next()) {
				this.email =resultat.getString("email");
				this.nom =resultat.getString("nom");
				this.prenom =resultat.getString("prenom");
				this.age =resultat.getInt("age");
			}else{
				throw new NoSuchUserException("aucun utilisateur avec l'email :" + email + "ou mot de passe incorrecte");
			}
		}

		
}
