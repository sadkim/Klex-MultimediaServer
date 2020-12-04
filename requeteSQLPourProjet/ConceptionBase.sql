/* Script Sql pour creer la base de donnees */
/* Ce script est verifie en utilisant le site adminer */

CREATE TABLE Langue(Langue varchar(255) NOT NULL PRIMARY KEY);
CREATE TABLE Codec(Codec varchar(255) NOT NULL PRIMARY KEY);

CREATE TABLE Utilisateur(
	Email varchar(255) NOT NULL PRIMARY KEY Check(Email LIKE '%_@_%._%'),
	CodeAccess VARCHAR(255) NOT NULL,
	Nom VARCHAR(255) NOT NULL,
	Prenom VARCHAR(255)NOT NULL,
	Age INT NOT NULL,
	LanguePrefere varchar(255) NOT NULL,
	constraint LangueExiste FOREIGN KEY (LanguePrefere) REFERENCES Langue(Langue)
);


CREATE TABLE Fichier (
    idFichier int NOT NULL PRIMARY KEY,
    DateFichier Date NOT NULL,
    TailleFichier number(5) NOT NULL,
    Email VARCHAR(255) NOT NULL  Check(Email LIKE '%_@_%._%'),
    constraint IntegriteFichierUtilisateur FOREIGN KEY (Email) REFERENCES utilisateur(Email) 
    ON DELETE CASCADE
);


CREATE TABLE Album (
    idAlbum  INT PRIMARY KEY  NOT NULL,
    TitreAlbum VARCHAR(255),
    NumArtiste INT NOT NULL,
    DateSortieAlbum DATE NOT NULL,
    URLImgPochette VARCHAR(255) NOT NULL 
);


CREATE TABLE Artist (
    NumArtiste int PRIMARY KEY NOT NULL,
    NomArtiste VARCHAR(255) NOT NULL,
    URLphotoArtiste VARCHAR(255) NOT NULL,
    Biographie VARCHAR(255),
    SpecialitePrincipale VARCHAR(255) NOT NULL,
    DateNaissance DATE
);


CREATE TABLE Flux(
	NoFlux INT NOT NULL PRIMARY KEY,
	Debit INT NOT NULL,
	Type varchar(255) NOT NULL CHECK(Type = 'audio' OR Type= 'video' OR Type= 'text'),
	ResLargeurVid INT ,
	ResLongeurVid INT,
	Echantillonage INT CHECK(Echantillonage = 16 OR Echantillonage=24 OR Echantillonage=32),
	Codec varchar(255)  NOT NULL,
	Langue varchar(255) NOT NULL,
	IdFichier INT NOT NULL,
	constraint FluxCodec FOREIGN KEY (Codec) REFERENCES Codec(Codec)  
     	ON DELETE CASCADE,
	constraint FluxLangue FOREIGN KEY (Langue) REFERENCES Langue(Langue)  
     	ON DELETE CASCADE,
	constraint FluxFichier FOREIGN KEY (idFichier) REFERENCES Fichier(idFichier)  
     	ON DELETE CASCADE,
	constraint TypeFichier CHECK((echantillonage != NULL AND ResLargeurVid = NULL AND ResLongeurVid=NULL  AND Type = 'audio')
	OR (ResLargeurVid != NULL AND ResLongeurVid!=NULL AND echantillonage = NULL AND Type = 'video') 
	OR (Type = 'text' AND echantillonage = NULL AND ResLargeurVid = NULL AND  ResLongeurVid = NULL))
);


CREATE TABLE Film(
   Titre VARCHAR(255) NOT NULL,
   AnneeSortie INT NOT NULL Check(AnneeSortie LIKE '____'),
   Resume VARCHAR(1000),
   ageMin INTEGER NOT NULL,
   URLAffiche VARCHAR(255) NOT NULL,
   PRIMARY KEY (Titre, AnneeSortie)
);


CREATE TABLE Logiciel(
    MarqueLogiciel VARCHAR(255) NOT NULL,
    ModeleLogiciel VARCHAR(255) NOT NULL,
    ResolutionLargMax INT,
    ResolutionHautMax INT,
    Constraint pk6 PRIMARY KEY (MarqueLogiciel, ModeleLogiciel),
    Constraint coherenceres CHECK(( ResolutionLargMax= NULL and  ResolutionHautMax=NULL) or 
		( ResolutionLargMax!= NULL and  ResolutionHautMax!=NULL))
);


CREATE TABLE URLExtPhoto(
	Titre varchar(255) NOT NULL,
	AnneeSortie INT NOT NULL Check(AnneeSortie LIKE '____'),
	url varchar(255) NOT NULL,
	constraint FILMExiste FOREIGN KEY (Titre, AnneeSortie) REFERENCES Film(Titre, AnneeSortie)  
    ON DELETE CASCADE
);


CREATE TABLE Pistes(
	idAlbum INT NOT NULL,
	numPiste INT NOT NULL,
	titrePiste varchar(255) NOT NULL,
	dureePiste INT NOT NULL,
	constraint pk1 PRIMARY KEY (idAlbum, NumPiste),
	constraint AlbumExist FOREIGN KEY (idAlbum) REFERENCES Album(idAlbum)  
    ON DELETE CASCADE
);


CREATE TABLE CategorieMusique(
    Categorie VARCHAR(255) PRIMARY KEY NOT NULL
);


CREATE TABLE CategorieFilm(
    Categorie VARCHAR(255) PRIMARY KEY NOT NULL
);


/*contrainte : un film doit avoir au moins une catégorie  : à vérifier après les insertions */ 

CREATE TABLE CategorisationFilm (
    Titre VARCHAR(255) NOT NULL,
    AnneeSortie INT  NOT NULL Check(AnneeSortie LIKE '____'),
    Categorie VARCHAR(255) NOT NULL,
    PRIMARY KEY (Titre, AnneeSortie, Categorie),
    CONSTRAINT  IntegriteFilm  FOREIGN KEY (Titre, AnneeSortie) REFERENCES Film(Titre, AnneeSortie)
	ON DELETE CASCADE,
    CONSTRAINT IntegriteCategorieFilm FOREIGN KEY (Categorie) REFERENCES CategorieFilm(Categorie)
	ON DELETE CASCADE
);


/*contrainte : un album doit avoir au moins une catégorie :  à vérifier après les insertions */

CREATE TABLE CategorisationAlbum (
    IdAlbum INT NOT NULL,
    Categorie VARCHAR(255) NOT NULL,
	CONSTRAINT pk2 PRIMARY KEY(IdAlbum, Categorie),
	CONSTRAINT IntegriteAlbum FOREIGN KEY (IdAlbum) REFERENCES Album(IdAlbum)
    ON DELETE CASCADE,
	CONSTRAINT IntegriteCategorieAlbum FOREIGN KEY (Categorie) REFERENCES Categoriemusique(Categorie)
    ON DELETE CASCADE
);


/*contrainte : une piste doit avoir au moins une catégorie :  à vérifier après les insertions*/

CREATE TABLE CategorisationPiste(
	IdAlbum INT NOT NULL,
    numPiste INT NOT NULL,
    Categorie VARCHAR(255) NOT NULL,
    CONSTRAINT pk3 PRIMARY KEY(IdAlbum, numPiste, Categorie),
	CONSTRAINT IntegritePiste FOREIGN KEY (IdAlbum, numPiste) REFERENCES Pistes(IdAlbum, numPiste)
    ON DELETE CASCADE,
	CONSTRAINT IntegriteCategoriePiste FOREIGN KEY (Categorie) REFERENCES CategorieMusique(Categorie)
    ON DELETE CASCADE
);



/*contrainte: chaque piste doit avoir au moins un artiste*/
CREATE TABLE ContributionPiste(
	NumArtiste INT NOT NULL,
   	idAlbum INT NOT NULL,
   	numPiste INT NOT NULL,
    Instrument VARCHAR(255) NOT NULL,
    CONSTRAINT pk4 PRIMARY KEY (NumArtiste, idAlbum, numPiste),
	CONSTRAINT IntegritePiste1 FOREIGN KEY (NumArtiste) REFERENCES ARTIST(NumArtiste)
	ON DELETE CASCADE,
	CONSTRAINT IntegritePiste2 FOREIGN KEY (IdAlbum,numPiste) REFERENCES Pistes(IdAlbum,numPiste)
	ON DELETE CASCADE
);


/* contrainte: chaque Film doit avoir au moins un artiste */
CREATE TABLE ContributionFilm (
    NumArtiste INT NOT NULL,
    Titre VARCHAR(255) NOT NULL,
    AnneeSortie INT NOT NULL Check(AnneeSortie LIKE '____'),
    role VARCHAR(255) NOT NULL,
    constraint pk5 PRIMARY KEY(NumArtiste, Titre, AnneeSortie),
    CONSTRAINT IntegriteartistIntervenu FOREIGN KEY (NumArtiste) REFERENCES Artist(NumArtiste)
    ON DELETE CASCADE,
    CONSTRAINT IntegriteFilmavecContribution FOREIGN KEY (Titre,AnneeSortie) REFERENCES Film(Titre,AnneeSortie)
    ON DELETE CASCADE
);

CREATE TABLE CodecLogiciel(
	MarqueLogiciel VARCHAR(255) NOT NULL,
 	ModeleLogiciel VARCHAR(255) NOT NULL,
 	Codec VARCHAR(255) NOT NULL,
 	Constraint pk7 PRIMARY KEY (MarqueLogiciel, ModeleLogiciel, Codec),
 	CONSTRAINT IntegriteLogiciel FOREIGN KEY (MarqueLogiciel, ModeleLogiciel) REFERENCES Logiciel(MarqueLogiciel, ModeleLogiciel)
	ON DELETE CASCADE,
 	CONSTRAINT IngriteCodec FOREIGN KEY (Codec) REFERENCES Codec(Codec)
	ON DELETE CASCADE
);


CREATE TABLE ContenuMultimedia (
		idFichier int NOT NULL PRIMARY KEY,
		Titre VARCHAR(255),
		AnneeSortie INT Check(AnneeSortie LIKE '____'),
		IdAlbum int,
		NumPiste int,
		CONSTRAINT intgriteFichierContenu FOREIGN KEY (idFichier) REFERENCES Fichier(idFichier)
		ON DELETE CASCADE,
		CONSTRAINT integriteFichierFilm FOREIGN KEY(Titre, AnneeSortie) REFERENCES Film(Titre, AnneeSortie)
		ON DELETE CASCADE,
		CONSTRAINT integriteFichierPiste FOREIGN KEY(idAlbum, numPiste) REFERENCES Pistes(idALbum, numPiste)
		ON DELETE CASCADE,
		constraint ContenuDuFichier CHECK((Titre = NULL AND AnneeSortie = NULL AND idFichier != NULL AND IdAlbum != NULL) 
                OR (idAlbum = NULL  AND NumPiste = NULL AND Titre != NULL AND AnneeSortie != NULL))
);
