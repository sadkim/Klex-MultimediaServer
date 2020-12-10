/* Selectionner un utilisateur */

SELECT * FROM Utilisateur where email = 'henry.cool@gmail.com' and codeAccess = 'password';

/** Exemple de sélection d'un film  : selon une categorie et une langue **/

SELECT Distinct Film.titre, Film.anneeSortie , Film.resume, Film.urlAffiche  
FROM Film, Fichier, Flux, CategorisationFilm , ContenuMultimedia 
where Film.titre  = categorisationFilm.titre and 
	Film.anneeSortie =  categorisationFilm.anneeSortie and
	Film.titre =ContenuMultimedia.titre and
	Film.anneeSortie  = contenuMultimedia.anneeSortie and 
    contenuMultimedia.idFichier= Fichier.idFichier and
    Flux.idFichier = Fichier.idFichier  and 
    Film.titre like '%' 
	and ageMin < 21
	and (Flux.langue = 'français' and flux.typeFlux = 'audio')
	and (categorie = 'suspense') order by titre;


/** Selectioner Une piste selon la langue de l'audio et une categorie */ 
SELECT Distinct Pistes.titrePiste , Pistes.dureePiste , Album.titreAlbum , Album.dateSortieAlbum, Artist.nomArtiste 
FROM Pistes, Album , Artist, CategorisationPiste , Fichier , ContenuMultimedia , Flux 
Where Pistes.idAlbum = Album.idAlbum 
		and Album.numArtiste = Artist.numArtiste
		and Pistes.numPiste = CategorisationPiste.numPiste
		and Pistes.idAlbum  = CategorisationPiste.idAlbum 
		and Pistes.numPiste = ContenuMultimedia.numPiste
		and Pistes.idAlbum = ContenuMultimedia.idAlbum 
		and Fichier.idFichier = ContenuMultimedia.idFichier
		and Fichier.idFichier = Flux.idFichier
		and Pistes.titrePiste like '%' 
		and categorie = 'pop' 
		and (Flux.langue = 'anglais' and Flux.typeFlux = 'audio');

/** Selectionner un album **/
SELECT Album.titreAlbum , Album.dateSortieAlbum , Artist.nomArtiste 
	FROM Album , Artist, CategorisationAlbum
	Where Album.idAlbum = CategorisationAlbum.idAlbum 
	and Album.numArtiste = Artist.numArtiste 
 	and Album.titreAlbum like '%'  and categorie = 'pop';

/** Selectionner les fichiers d'un utilisateur **/
SELECT * FROM Fichier WHERE email = 'henry.cool@gmail.com';

/** Selectionner les fichiers associes a un film **/
SELECT * FROM ContenuMultimedia WHERE titre = 'TENET' and anneeSortie = 2020;

