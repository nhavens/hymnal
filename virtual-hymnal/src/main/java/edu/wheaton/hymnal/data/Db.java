package edu.wheaton.hymnal.data;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Abstract class containing behavior common to any Db subtype. All fields are
 * static, because we assume that all subtypes will use the Singleton pattern. 
 * This class uses ormlite to auto-generate queries based on annotated classes.
 * @author neile
 *
 */
public abstract class Db {
	protected static ConnectionSource connectionSource;

	protected static Dao<Text, String> textDao;
	protected static Dao<Tune, String> tuneDao;
	protected static Dao<Meter, String> meterDao;
	
	public Text[] selectAllTexts() {
		List<Text> texts = null;
		try {
			texts = textDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return texts.toArray(new Text[texts.size()]);
	}

	/**
	 * Return an array of the Text in the database that match the input
	 * parameter or null if no matches.
	 */
	public Text[] selectTextsFor(Tune tune) {
		List<Text> matches = null;
		try {
			// here we are required to use the column name "meter_id" rather
			// than the field name "meter"
			matches = textDao.queryForEq("meter_id", tune.getMeter().getId());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return matches.toArray(new Text[matches.size()]);
	}

	public Tune selectDefaultTuneFor(Text text) {
		try {
			tuneDao.refresh(text.getDefaultTune());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return text.getDefaultTune();
	}

	public Tune[] selectAllTunes() {
		List<Tune> tunes = null;
		try {
			tunes = tuneDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return tunes.toArray(new Tune[tunes.size()]);
	}

	/**
	 * Return an array of the Tunes in the database that match the input
	 * parameter or null if no matches.
	 */
	public Tune[] selectTunesFor(Text text) {
		List<Tune> matches = null;
		try {
			// here we are required to use the column name "meter_id" rather
			// than the field name "meter"
			matches = tuneDao.queryForEq("meter_id", text.getMeter().getId());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return matches.toArray(new Tune[matches.size()]);
	}

	public Text selectDefaultTextFor(Tune tune) {
		try {
			textDao.refresh(tune.getDefaultText());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return tune.getDefaultText();
	}
	
	/**
	 * Refresh data to prepare for rendering. We only need to refresh the
	 * fields marked "foreign", not those marked "foreignCollection".
	 * @param text The selected Text
	 * @param tune The selected Tune
	 */
	public void refreshMeters(Text text, Tune tune) {
		try {
			meterDao.refresh(text.getMeter());
			meterDao.refresh(tune.getMeter());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			connectionSource.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}