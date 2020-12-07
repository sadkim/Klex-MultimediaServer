
public class Filtre {
	
	private String champ;
	private String valeur;
	
	public Filtre(String champ, String valeur) {
		this.champ = champ;
		this.valeur = valeur;
	}

	public String getValeur() {
		return valeur;
	}

	public String getChamp() {
		return champ;
	}
}
