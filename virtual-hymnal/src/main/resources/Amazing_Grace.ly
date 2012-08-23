\version "2.14.2"
#(set-default-paper-size "letter")
#(ly:set-option 'point-and-click #f)
#(ly:set-option 'delete-intermediate-files #t)

\paper { indent = 0.0 }
\header { 
    title = "Amazing Grace" 
}

keyTime = { 
    \key g \major 
    \time 3/4 
    \tempo 4 = 90 
    \partial 4 
	\override Score . LyricText #'font-size = #-1
	\override Score . LyricHyphen #'minimum-distance = #1
	\override Score . LyricSpace #'minimum-distance = #0.8
}
%\override Score.MetronomeMark #'stencil = #point-stencil

SopranoMusic = {
	d'4 g'2 b'8 (g') b'2
	a'4 g'2 e'4 d'2 \bar "" \break
	d'4 g'2 b'8 (g') b'2 a'4 d''2.
	b'4 d''4. (b'8) d''8 (b') g'2 \break
	d'4 e'4. (g'8) g'8 (e') d'2
	d'4 g'2 b'8 (g') b'2 a'4 g'2
	\bar "|."
}
AltoMusic = {
	b4 b2 d'4 d'2
	c'4 b2 c'4 b2
	b4 b2 b4 d'2 d'4 d'2.
	d'4 d'2 d'4 d'2
	d'4 c'4. (d'8) c'4 b2
	d'4 b2 d'4 d'2 c'4 b2
}
TenorMusic = {
	g4 d2 g4 g2
	fis4 g2 g4 g2
	g4 d2 g4 g2 fis4 g2.
	g4 b4. (g8) b8 (g) g2
	g4 g2 e8 (g) g2
	g4 g2 g8 (b) g2 fis4 g2
}
BassMusic = {
	g,4 g,2 g,8 (b,) d2
	d4 e2 c4 g,2
	g,4 g,2 g,8 (b,) d2 c4 b,2.
	g,4 g2 g4 b,2
	b,4 c4. (b,8) c4 g,2
	b,4 e2 d4 d2 d4 g,2
}

VerseOne = \lyricmode {
	\set stanza = "1. "
	A -- maz -- ing grace! How sweet the sound
	That saved a wretch like me!
	I once was lost, but now am found;
	Was blind, but now I see.
}
VerseTwo = \lyricmode {
	\set stanza = "2. "
	’Twas grace that taught my heart to fear,
	And grace my fears re -- lieved;
	How pre -- cious did that grace ap -- pear
	The hour I first be -- lieved!
}
VerseThree = \lyricmode {
	\set stanza = "3. "
	Through ma -- ny dan -- gers, toils and snares,
	I have al -- rea -- dy come;
	’Tis grace hath brought me safe thus far,
	And grace will lead me home.
}
VerseFour = \lyricmode {
	\set stanza = "4. "
	The Lord has prom -- ised good to me,
	His Word my hope se -- cures;
	He will my Shield and Por -- tion be,
	As long as life en -- dures.
}
VerseFive = \lyricmode {
	\set stanza = "5. "
	Yea, when this flesh and heart shall fail,
	And mor -- tal life shall cease,
	I shall pos -- sess, with -- in the veil,
	A life of joy and peace.
}
VerseSix = \lyricmode {
	\set stanza = "6. "
	The earth shall soon dis -- solve like snow,
	The sun for -- bear to shine;
	But God, who called me here be -- low,
	Will be for -- e -- ver mine.
}
VerseSeven = \lyricmode {
	\set stanza = "7. "
	When we’ve been there ten thou -- sand years,
	Bright shin -- ing as the sun,
	We’ve no less days to sing God’s praise
	Than when we’d first be -- gun.
}

\score {
	\new ChoirStaff	<<
		\new Staff <<
			\clef "treble"
			\new Voice = "Sop" { \voiceOne \keyTime	\SopranoMusic }
			\new Voice = "Alto" { \voiceTwo \AltoMusic }
			\new Lyrics \lyricsto "Sop" { \VerseOne }
			\new Lyrics \lyricsto "Sop" { \VerseTwo }
			\new Lyrics \lyricsto "Sop" { \VerseThree }
			\new Lyrics \lyricsto "Sop" { \VerseFour }
			\new Lyrics \lyricsto "Sop" { \VerseFive }
			\new Lyrics \lyricsto "Sop" { \VerseSix }
			\new Lyrics \lyricsto "Sop" { \VerseSeven }
		>>
		\new Staff <<
			\clef bass
			\new Voice = "Tenor" { \voiceOne \keyTime \TenorMusic }
			\new Voice = "Bass" { \voiceTwo \BassMusic }
		>>
	>>
	\layout {	
		\context {
			\Score \remove "Bar_number_engraver"
		}
	}
}

\markup {
	\column {
		\line{\italic Text: John Newton (1725 - 1807), 1779 (poet of the seventh verse is unknown, 1829)}
		\line{\italic Melody: James P. Carrell (1787 - 1854) and David S. Clayton, 1831}
		\line{\italic Parts: Edwin Othello Excell (1851 - 1921), 1900}
		\line{\italic {Tune Name:} New Britain}
		\line{\italic {Poetic Meter:} 8 6 8 6: Common Meter}
	}
}
