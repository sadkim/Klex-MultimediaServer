
/* Insertion de quelques langues */
insert into Langue (Langue) values ('français');
insert into Langue (Langue) values ('arabe');
insert into Langue (Langue) values ('anglais');
insert into Langue (Langue) values ('allemand');
insert into Langue (Langue) values ('espagnol');


/***** Insertion d'un utilisateur avec ses fichiers et ses contenus *****/

/*** Notre premier utilisateur ***/
insert into Utilisateur(Email, CodeAccess, Nom, Prenom, Age, LanguePrefere) values(
		'henry.cool@gmail.com', 'password','Cool', 'Henry', 19, 'français');

/** On va insérer quelque films **/

INSERT INTO Film (Titre,ANNEESORTIE, RESUME,AGEMIN,URLAFFICHE) values('Harry Potter and the goblet of fire',
					2005, 'un film de fantaisie basé sur un roman de JK Rowling' , 12, 'https:/super_affiche_pour_harry.jpg');

INSERT INTO Film (Titre,ANNEESORTIE, RESUME,AGEMIN,URLAFFICHE) values('TENET', 2020 ,'le cinéma et la physique unis',15,
								'https:/cest_tenet.jpg');

insert into URLExtPhoto(Titre, AnneeSortie, URL) values('TENET',2020, 'https:/tenet_behind_scenes.jpg');

/* Les categories correspondants */
insert into CategorieFilm (categorie) values ('action');
insert into CategorieFilm (categorie) values ('fantaisie');
insert into CategorieFilm (categorie) values ('suspense');
insert into CategorieFilm (categorie) values ('science fiction');


/* Inserer les catogeries des films */

insert into CategorisationFilm (Titre, AnneeSortie, Categorie) values ('Harry Potter and the goblet of fire', 2005, 'fantaisie');
insert into CategorisationFilm (Titre, AnneeSortie, Categorie) values ('TENET', 2020, 'action');
insert into CategorisationFilm (Titre, AnneeSortie, Categorie) values ('TENET', 2020, 'suspense');
insert into CategorisationFilm (Titre, AnneeSortie, Categorie) values ('TENET', 2020, 'science fiction');


/* Inserer des fichiers pour cet utilisaeur et ces films */

insert into Fichier(idFichier, DateFichier, TailleFichier, Email) values (idFichierSeq.nextval, 
		TO_DATE('2020-12-04','YYYY-MM-DD'), 12397, 'henry.cool@gmail.com');

insert into Fichier(idFichier, DateFichier, TailleFichier, Email) values (idFichierSeq.nextval, 
		TO_DATE('2020-12-04','YYYY-MM-DD'), 38769, 'henry.cool@gmail.com');

insert into Fichier(idFichier, DateFichier, TailleFichier, Email) values (idFichierSeq.nextval, 
		TO_DATE('2020-12-04','YYYY-MM-DD'), 23978, 'henry.cool@gmail.com');

insert into Fichier(idFichier, DateFichier, TailleFichier, Email) values (idFichierSeq.nextval, 
		TO_DATE('2020-12-04','YYYY-MM-DD'), 50000, 'henry.cool@gmail.com');

/* Inserer les codecs et les flux pour les fichiers de cet utilisateur correspondant a ces films */

/*Codec : */
insert into Codec (Codec) values ('MP4');
insert into Codec (Codec) values ('MPEG2');
insert into Codec (Codec) values ('ACC');
insert into Codec (Codec) values ('H264');

/* Flux : */

insert into Flux(NoFlux, Debit,TypeFlux,ResLargeurVid, ResLongeurVid, Echantillonage, Codec, Langue,IdFichier) values
		(NoFluxSeq.nextval, 392, 'video', 1080, 1000, NULL, 'MP4', 'français', 1);

insert into Flux(NoFlux, Debit,TypeFlux,ResLargeurVid, ResLongeurVid, Echantillonage, Codec, Langue,IdFichier) values
		(NoFluxSeq.nextval, 120, 'video', 400, 500, NULL, 'MP4', 'français', 1);

insert into Flux(NoFlux, Debit,TypeFlux,ResLargeurVid, ResLongeurVid, Echantillonage, Codec, Langue,IdFichier) values
		(NoFluxSeq.nextval, 392, 'audio', NULL, NULL, 24 , 'MPEG2', 'français', 2);

insert into Flux(NoFlux, Debit,TypeFlux,ResLargeurVid, ResLongeurVid, Echantillonage, Codec, Langue,IdFichier) values
		(NoFluxSeq.nextval, 392, 'video', 1080, 1000, NULL, 'MP4', 'français', 3);

insert into Flux(NoFlux, Debit,TypeFlux,ResLargeurVid, ResLongeurVid, Echantillonage, Codec, Langue,IdFichier) values
		(NoFluxSeq.nextval, 392, 'audio', NULL, NULL, 16, 'MPEG2', 'français', 4);

/* Les associations entre fichiers et contenus multimedia */

insert into ContenuMultimedia (idFichier, Titre, AnneeSortie, IdAlbum , NumPiste) values 
		(1,'Harry Potter and the goblet of fire', 2005, NULL, NULL);

insert into ContenuMultimedia (idFichier, Titre, AnneeSortie, IdAlbum , NumPiste) values 
		(2,'Harry Potter and the goblet of fire', 2005, NULL, NULL);

insert into ContenuMultimedia (idFichier, Titre, AnneeSortie, IdAlbum , NumPiste) values 
		(3,'TENET', 2020, NULL, NULL);

insert into ContenuMultimedia (idFichier, Titre, AnneeSortie, IdAlbum , NumPiste) values 
		(4,'TENET', 2020, NULL, NULL);


/*Artistes :*/

Insert into Artist(NumArtiste, NomArtiste, URLphotoArtiste, Biographie, SpecialitePrincipale, DateNaissance) values
		(idArtistSeq.nextval, 'Daniel Radicliff' , 'https:/daniel_radicliff.jpeg', NULL , 'Acteur' ,
		 	TO_DATE('1989-01-29', 'YYYY-MM-DD')) ;

Insert into Artist(NumArtiste, NomArtiste, URLphotoArtiste, Biographie, SpecialitePrincipale, DateNaissance) values
		(idArtistSeq.nextval, 'Robert Pattinson' , 'https:/robert_pattinson.jpeg', 'acteur, musicien et mannequin britannique',
		 	'Acteur' , TO_DATE('1986-05-13', 'YYYY-MM-DD')) ;

/* Ajouter la contribution des artistes dans les films */

insert into ContributionFilm (NumArtiste, Titre, AnneeSortie, role) values 
		(1, 'Harry Potter and the goblet of fire', 2005, 'main role');

insert into ContributionFilm (NumArtiste, Titre, AnneeSortie, role) values 
		(2, 'Harry Potter and the goblet of fire', 2005, 'a student in Hogwarrts');

insert into ContributionFilm (NumArtiste, Titre, AnneeSortie, role) values 
		(2, 'TENET', 2020, 'main role');



/***** Insertion d'un deuxieme utilisateur avec plus de gout musical *****/

insert into Utilisateur(Email, CodeAccess, Nom, Prenom, Age, LanguePrefere) values(
		'rock.it@gmail.com', 'pass forgotten','Mark', 'Mark', 21, 'anglais');


/* Ajouter un album avec des pistes */

insert into Album(idAlbum, TitreAlbum, NumArtiste, DateSortieAlbum, URLimgPochette) values(
		idAlbumSeq.nextval, 'Tresspassing' , 1, TO_DATE('2012-05-15', 'YYYY-MM-DD'), 'https:/tresspassing.jpeg'); 

/* Ajouter des categories musique et categoriser cet album */
		
insert into CategorieMusique (categorie) values ('rock');
insert into CategorieMusique (categorie) values ('pop');

insert into CategorisationAlbum (IdAlbum , Categorie) values (1, 'pop');
insert into CategorisationAlbum (IdAlbum , Categorie) values (1, 'rock');

/* Ajouter des pistes */

insert into Pistes (idAlbum, numPiste, titrePiste, dureePiste) values
		(1, NoPisteSeq.nextval, 'underneath', 4);

insert into Pistes (idAlbum, numPiste, titrePiste, dureePiste) values
		(1, NoPisteSeq.nextval, 'runnin', 4);

insert into Pistes (idAlbum, numPiste, titrePiste, dureePiste) values
		(1, NoPisteSeq.nextval, 'outlaws of love', 4);

/* Categoriser les pistes */

insert into CategorisationPiste (IdAlbum, numPiste, Categorie) values (1, 1, 'pop');
insert into CategorisationPiste (IdAlbum, numPiste, Categorie) values (1, 2, 'rock');
insert into CategorisationPiste (IdAlbum, numPiste, Categorie) values (1, 3, 'pop');

/* Les pistes : ses fichiers et ses flux */

insert into Fichier(idFichier, DateFichier, TailleFichier, Email) values (idFichierSeq.nextval, 
		TO_DATE('2020-12-05','YYYY-MM-DD'), 12000, 'rock.it@gmail.com');

insert into Fichier(idFichier, DateFichier, TailleFichier, Email) values (idFichierSeq.nextval, 
		TO_DATE('2020-12-05','YYYY-MM-DD'), 12000, 'rock.it@gmail.com');

insert into Fichier(idFichier, DateFichier, TailleFichier, Email) values (idFichierSeq.nextval, 
		TO_DATE('2020-12-05','YYYY-MM-DD'), 12000, 'rock.it@gmail.com');

insert into Flux(NoFlux, Debit,TypeFlux,ResLargeurVid, ResLongeurVid, Echantillonage, Codec, Langue,IdFichier) values
		(NoFluxSeq.nextval, 392, 'audio', NULL, NULL, 16, 'MPEG2', 'anglais', 5);

insert into Flux(NoFlux, Debit,TypeFlux,ResLargeurVid, ResLongeurVid, Echantillonage, Codec, Langue,IdFichier) values
		(NoFluxSeq.nextval, 120, 'audio', NULL, NULL, 24, 'MPEG2', 'anglais', 6);

insert into Flux(NoFlux, Debit,TypeFlux,ResLargeurVid, ResLongeurVid, Echantillonage, Codec, Langue,IdFichier) values
		(NoFluxSeq.nextval, 392, 'audio', NULL, NULL, 24 , 'MPEG2', 'anglais', 7);

/* Associer les pistes avec leurs fichiers */

insert into ContenuMultimedia (idFichier, Titre, AnneeSortie, IdAlbum , NumPiste) values (5, NULL, NULL, 1, 1);
insert into ContenuMultimedia (idFichier, Titre, AnneeSortie, IdAlbum , NumPiste) values (6, NULL, NULL, 1, 2);
insert into ContenuMultimedia (idFichier, Titre, AnneeSortie, IdAlbum , NumPiste) values (7, NULL, NULL, 1, 3);

/* Les artistes qui interviennent */

Insert into Artist(NumArtiste, NomArtiste, URLphotoArtiste, Biographie, SpecialitePrincipale, DateNaissance) values
		(idArtistSeq.nextval, 'Adam Lambert' , 'https:/adam_lambert.jpeg', NULL , 'Chanteur' ,
		 	TO_DATE('1982-01-29', 'YYYY-MM-DD')) ;


insert into ContributionPiste(NumArtiste, idAlbum , numPiste, Instrument) values (3, 1, 1, 'voix');
insert into ContributionPiste(NumArtiste, idAlbum , numPiste, Instrument) values (3, 1, 2, 'voix');
insert into ContributionPiste(NumArtiste, idAlbum , numPiste, Instrument) values (3, 1, 3, 'voix');


/***** Ajouter des logiciels *****/

Insert into Logiciel(MarqueLogiciel, ModeleLogiciel, ResolutionLargMax, ResolutionHautMax) values(
		'AwesomeMediaPlayer', 'Apple', 3840, 2160);

/*** Associer a un codec ***/

insert into CodecLogiciel(MarqueLogiciel, ModeleLogiciel, Codec) values ('AwesomeMediaPlayer', 'Apple', 'MP4');
insert into CodecLogiciel(MarqueLogiciel, ModeleLogiciel, Codec) values ('AwesomeMediaPlayer', 'Apple', 'MPEG2');
insert into CodecLogiciel(MarqueLogiciel, ModeleLogiciel, Codec) values ('AwesomeMediaPlayer', 'Apple', 'ACC');
