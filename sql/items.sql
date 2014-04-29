CREATE TABLE items (
	id          INT         NOT NULL PRIMARY KEY,
	name        VARCHAR(30) NOT NULL,
	type        VARCHAR(30) NOT NULL,

	UNIQUE(name)
);