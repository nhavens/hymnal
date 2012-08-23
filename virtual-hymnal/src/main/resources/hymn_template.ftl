\version "2.14.2"
%#(set-default-paper-size "letter")
#(ly:set-option 'point-and-click #f)
#(ly:set-option 'delete-intermediate-files #t)

\paper { 
	indent = 0.0
	print-page-number = ##f
	#(define page-breaking ly:minimal-breaking)
}

\header { 
	title = "${text.name}" 
}

keyTime = { 
	\key ${tune.key} 
	\time ${tune.timeSignature} 
<#-- add tempo if it exists -->
<#if tune.tempo??>
	\tempo ${tune.tempo}
</#if> 
<#-- add partial tag if startingBeat is not the beginning of a measure -->
<#if tune.partialString??>
	\partial ${tune.partialString} 
</#if>
	\override Score . LyricText #'font-size = #-1
	\override Score . LyricHyphen #'minimum-distance = #1
	\override Score . LyricSpace #'minimum-distance = #0.8
}

SopranoMusic = {
${tune.voices[0].notes}
}
AltoMusic = {
${tune.voices[1].notes}
}
TenorMusic = {
${tune.voices[2].notes}
}
BassMusic = {
${tune.voices[3].notes}
}

<#-- Stanzas -->
<#list text.stanzas as stanza>
Verse${stanza.numberWord} = \lyricmode {
	\set stanza = "${stanza.number}. "
${stanza.words}
}
</#list>

\score {
	\new ChoirStaff	<<
		\new Staff <<
			\clef "treble"
			\new Voice = "Sop" { \voiceOne \keyTime	\SopranoMusic }
			\new Voice = "Alto" { \voiceTwo \AltoMusic }
			<#-- Add lyrics to the staff -->
			<#list text.stanzas as stanza>
			\new Lyrics \lyricsto "Sop" { \Verse${stanza.numberWord} }
			</#list>
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
		\line{\italic Text: ${text.author}}
		\line{\italic Melody: ${tune.composer}}
		\line{\italic {Tune Name:} ${tune.name}}
		\line{\italic {Poetic Meter:} ${tune.meter.beats}<#if tune.meter.name??>: ${tune.meter.name}</#if>}
	}
}