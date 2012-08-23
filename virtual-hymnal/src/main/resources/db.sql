PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE meter (
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	beats VARCHAR(20) NOT NULL,
	name VARCHAR(25)
);
INSERT INTO "meter" VALUES(1,'8 6 8 6','Common Meter');
CREATE TABLE tune (
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	name VARCHAR(50) NOT NULL,
	meter_id INTEGER NOT NULL REFERENCES meter(id),
	composer VARCHAR(50) NOT NULL,
	keyOf VARCHAR(15) NOT NULL,
	timeSignature VARCHAR(5) NOT NULL,
	tempo VARCHAR(8),
	partialString VARCHAR(15),
	defaultText_id INTEGER NOT NULL REFERENCES text(id)
);
INSERT INTO "tune" VALUES(1,'New Britain',1,'James P. Carrell (1787 - 1854) and David S. Clayton, 1831','g \major','3/4','4 = 90','4', 1);
CREATE TABLE voice (
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	part INTEGER NOT NULL,
	tune_id INTEGER NOT NULL REFERENCES tune(id),
	notes TEXT NOT NULL
);
INSERT INTO "voice" VALUES(1,3,1,'	g,4 g,2 g,8 (b,) d2
	d4 e2 c4 g,2
	g,4 g,2 g,8 (b,) d2 c4 b,2.
	g,4 g2 g4 b,2
	b,4 c4. (b,8) c4 g,2
	b,4 e2 d4 d2 d4 g,2');
INSERT INTO "voice" VALUES(2,1,1,'	b4 b2 d''4 d''2
	c''4 b2 c''4 b2
	b4 b2 b4 d''2 d''4 d''2.
	d''4 d''2 d''4 d''2
	d''4 c''4. (d''8) c''4 b2
	d''4 b2 d''4 d''2 c''4 b2');
INSERT INTO "voice" VALUES(3,0,1,'	d''4 g''2 b''8 (g'') b''2
	a''4 g''2 e''4 d''2 \bar "" \break
	d''4 g''2 b''8 (g'') b''2 a''4 d''''2.
	b''4 d''''4. (b''8) d''''8 (b'') g''2 \break
	d''4 e''4. (g''8) g''8 (e'') d''2
	d''4 g''2 b''8 (g'') b''2 a''4 g''2
	\bar "|."');
INSERT INTO "voice" VALUES(4,2,1,'	g4 d2 g4 g2
	fis4 g2 g4 g2
	g4 d2 g4 g2 fis4 g2.
	g4 b4. (g8) b8 (g) g2
	g4 g2 e8 (g) g2
	g4 g2 g8 (b) g2 fis4 g2');
CREATE TABLE text (
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	name VARCHAR(50) NOT NULL,
	meter_id INTEGER NOT NULL REFERENCES meter(id),
	author VARCHAR(50) NOT NULL,
	defaultTune_id INTEGER NOT NULL REFERENCES tune(id)
);
INSERT INTO "text" VALUES(1,'Amazing Grace',1,'John Newton (1725 - 1807), 1779 (poet of the seventh verse is unknown, 1829)', 1);
CREATE TABLE stanza (
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	number SMALLINT NOT NULL,
	text_id INTEGER NOT NULL REFERENCES text(id),
	words TEXT NOT NULL
);
INSERT INTO "stanza" VALUES(1,1,1,'	A -- maz -- ing grace! How sweet the sound
	That saved a wretch like me!
	I once was lost, but now am found;
	Was blind, but now I see.');
INSERT INTO "stanza" VALUES(2,2,1,'	’Twas grace that taught my heart to fear,
	And grace my fears re -- lieved;
	How pre -- cious did that grace ap -- pear
	The hour I first be -- lieved!');
INSERT INTO "stanza" VALUES(3,3,1,'	Through ma -- ny dan -- gers, toils and snares,
	I have al -- rea -- dy come;
	’Tis grace hath brought me safe thus far,
	And grace will lead me home.');
INSERT INTO "stanza" VALUES(4,4,1,'	The Lord has prom -- ised good to me,
	His Word my hope se -- cures;
	He will my Shield and Por -- tion be,
	As long as life en -- dures.');
INSERT INTO "stanza" VALUES(5,5,1,'	Yea, when this flesh and heart shall fail,
	And mor -- tal life shall cease,
	I shall pos -- sess, with -- in the veil,
	A life of joy and peace.');
INSERT INTO "stanza" VALUES(6,6,1,'	The earth shall soon dis -- solve like snow,
	The sun for -- bear to shine;
	But God, who called me here be -- low,
	Will be for -- e -- ver mine.');
INSERT INTO "stanza" VALUES(7,7,1,'	When we’ve been there ten thou -- sand years,
	Bright shin -- ing as the sun,
	We’ve no less days to sing God’s praise
	Than when we’d first be -- gun.');
DELETE FROM sqlite_sequence;
INSERT INTO "sqlite_sequence" VALUES('meter',1);
INSERT INTO "sqlite_sequence" VALUES('tune',1);
INSERT INTO "sqlite_sequence" VALUES('text',1);
INSERT INTO "sqlite_sequence" VALUES('voice',4);
INSERT INTO "sqlite_sequence" VALUES('stanza',7);
COMMIT;
PRAGMA foreign_keys=ON;