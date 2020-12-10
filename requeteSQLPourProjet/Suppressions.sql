/** Suppression d'un film : exemple TENET :)*/

/* supprimer les fichiers correspondants */

delete from Fichier where idFichier IN (
			SELECT f.idFichier  FROM Fichier f, ContenuMultimedia m 
			where f.idFichier = m .idFichier and m.Titre = 'TENET' and m.AnneeSortie = 2020
);

/* supprimer les interventions des artistes */

Delete FROM ContributionFilm where Titre  = 'TENET' and AnneeSortie = 2020;

/* suppression du film */
DELETE FROM Film WHERE titre = 'TENET' and anneeSortie = 2020;

/* Supprimer les artistes s' ils n’ont plus des interventions */

DELETE FROM ARTIST  where NumArtiste NOT IN (
	SELECT NumArtiste FROM ContributionPiste 
	UNION SELECT NumArtiste 
	FROM ContributionFilm  
	UNION SELECT NumArtiste FROM Album );


/** Suppression d’une piste **/

/* ********** Supprimer la 1ere piste ********* */
/*  supprimer les fichier correspondant */

DELETE FROM Fichier where idFichier IN (
	SELECT f.IdFichier from Fichier f, ContenuMultimedia m 
	where f.idFichier = m .idFichier and m.IdAlbum = 1 and m.NumPiste = 1
);

/*  supprimer les interventions des artistes */

DELETE  FROM ContributionPiste where IdAlbum = 1 and NumPiste = 1;

/* suppression de la piste */

DELETE FROM Pistes WHERE IdAlbum = 1 and NumPiste = 1;


/* suppression de l’album si il n’est plus associés à d’autres pistes */

DELETE FROM Album where IdAlbum NOT IN (SELECT IdAlbum FROM Pistes);

/* Supprimer les artistes s' ils n’ont plus des interventions */

DELETE FROM ARTIST  where NumArtiste NOT IN (
	SELECT NumArtiste FROM ContributionPiste 
	UNION SELECT NumArtiste 
	FROM ContributionFilm  
	UNION SELECT NumArtiste FROM Album );

/** Suppression de la deuxieme et la troisieme piste **/

DELETE FROM Fichier where idFichier IN (
	SELECT f.IdFichier from  Fichier f, ContenuMultimedia m 
	where f.idFichier = m .idFichier and m.IdAlbum = 1 and m.NumPiste = 2
);

DELETE  FROM ContributionPiste where IdAlbum = 1 and NumPiste = 2;
DELETE FROM Pistes WHERE IdAlbum = 1 and NumPiste = 2;

DELETE FROM Fichier where idFichier IN (
	SELECT f.IdFichier from Fichier f, ContenuMultimedia m 
	where f.idFichier = m .idFichier and m.IdAlbum = 1 and m.NumPiste = 3
);

DELETE  FROM ContributionPiste where IdAlbum = 1 and NumPiste = 3;
DELETE FROM Pistes WHERE IdAlbum = 1 and NumPiste = 3;

/* Un nettoyage des artistes et des albums dont on aperçoit les resultats */
DELETE FROM Album where IdAlbum NOT IN (SELECT IdAlbum FROM Pistes);
DELETE FROM ARTIST  where NumArtiste NOT IN (
	SELECT NumArtiste FROM ContributionPiste 
	UNION SELECT NumArtiste 
	FROM ContributionFilm  
	UNION SELECT NumArtiste FROM Album );


