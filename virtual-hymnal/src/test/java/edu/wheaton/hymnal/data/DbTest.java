package edu.wheaton.hymnal.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DbTest {
	
	private static Db db;
	
	@Before
	public void initializeDb() {
		db = H2Db.getInstance();
	}

	@Test
	public void testDbContainsAtLeastOneText() {
		assertNotNull(db.selectAllTexts());
		assertTrue(db.selectAllTexts().length >= 1);
	}

	@Test
	public void testDbContainsAtLeastOneTune() {
		assertNotNull(db.selectAllTunes());
		assertTrue(db.selectAllTunes().length >= 1);
	}

	@Test
	public void testAllTextsHaveRequiredFields() {
		Text[] texts = db.selectAllTexts();
		for (Text t : texts) {
			assertTrue(!t.isEmpty());
			// also check all associated stanzas
			for (Stanza s : t.getStanzas()) {
				assertTrue(!s.isEmpty());
			}
		}
	}
	
	@Test
	public void testAllTunesHaveRequiredFields() {
		for (Tune t : db.selectAllTunes()) {
			assertTrue(!t.isEmpty());
			// also check all associated voices
			for (Voice v : t.getVoices()) {
				assertTrue(!v.isEmpty());
			}
		}
	}
	
	@Test
	public void testAllDefaultTextsExist() {
		for (Tune tune : db.selectAllTunes()) {
			Text text = db.selectDefaultTextFor(tune);
			assertNotNull(text);
			assertTrue(!text.isEmpty());
		}
	}
	
	@Test
	public void testAllDefaultTunesExist() {
		for (Text text : db.selectAllTexts()) {
			Tune tune = db.selectDefaultTuneFor(text);
			assertNotNull(tune);
			assertTrue(!tune.isEmpty());
		}
	}
	
	@After
	public void closeDb() {
		db.close();
	}
}
