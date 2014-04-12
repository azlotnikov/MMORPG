CREATE TABLE mobs (
	id          INT         NOT NULL PRIMARY KEY,
	name        VARCHAR(30) NOT NULL,
	temp        VARCHAR(30) NOT NULL,

	depth       INT NOT NULL,
	rarity      INT NOT NULL,
	expKill     INT NOT NULL,

	speed       INT NOT NULL,
	hit_points  INT NOT NULL,
	armor_class INT NOT NULL,
	alertness   INT NOT NULL,
	
	blows       TEXT,
	flags       TEXT,
	spells      TEXT,
	description TEXT,

	UNIQUE(name)
);