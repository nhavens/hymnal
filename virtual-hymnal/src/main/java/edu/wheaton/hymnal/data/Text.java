package edu.wheaton.hymnal.data;

import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

@DatabaseTable
public class Text implements TemplateHashModel {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, width = 50)
	private String name;

	@DatabaseField(canBeNull = false, foreign = true)
	private Meter meter;

	@DatabaseField(canBeNull = false)
	private String author;

	@DatabaseField(canBeNull = false, foreign = true)
	private Tune defaultTune;

	@ForeignCollectionField(orderColumnName = "number", eager = true)
	private Collection<Stanza> stanzas;

	public Text(String name, Meter meter, String author, Tune defaultTune) {
		this.name = name;
		this.meter = meter;
		this.author = author;
		this.defaultTune = defaultTune;
		this.stanzas = null;
	}

	public Text() {
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Meter getMeter() {
		return meter;
	}

	public String getAuthor() {
		return author;
	}

	public void setDefaultTune(Tune defaultTune) {
		this.defaultTune = defaultTune;
	}

	public Tune getDefaultTune() {
		return defaultTune;
	}

	public Collection<Stanza> getStanzas() {
		return stanzas;
	}

	public void toggleStanzaDisplay(int stanzaNum) {
		Stanza[] stanzaArray = stanzas.toArray(new Stanza[stanzas.size()]);
		try {
			// stanzaNum is one-indexed
			stanzaArray[stanzaNum - 1].toggleDisplay();
		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((defaultTune == null) ? 0 : defaultTune.hashCode());
		result = prime * result + id;
		result = prime * result + ((meter == null) ? 0 : meter.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Text other = (Text) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (defaultTune == null) {
			if (other.defaultTune != null)
				return false;
		} else if (defaultTune.getId() != other.defaultTune.getId())
			return false;
		if (id != other.id)
			return false;
		if (meter == null) {
			if (other.meter != null)
				return false;
		} else if (meter.getId() != other.meter.getId())
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		if (key.equalsIgnoreCase("name"))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return name;
				}
			};
		else if (key.equalsIgnoreCase("meter"))
			return meter;
		else if (key.equalsIgnoreCase("author"))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return author;
				}
			};
		else if (key.equalsIgnoreCase("stanzas"))
			return new TemplateSequenceModel() {

				public int size() throws TemplateModelException {
					if (stanzas == null)
						return -1; // some bad value, b/c can't return null
					else {
						int size = 0;
						for (Stanza s : stanzas)
							if (s.isDisplayed())
								size++;
						return size;
					}
				}

				/**
				 * Return the template model for the Stanza at the specified
				 * index. Since some stanzas may be hidden, this method may
				 * return a subsequent stanza. NOTE: visibleIndex is zero-based.
				 */
				public TemplateModel get(int visibleIndex)
						throws TemplateModelException {
					int visibleStanzasSoFar = 0;
					Stanza[] stanzaArray = stanzas.toArray(new Stanza[stanzas
							.size()]);
					for (int i = 0; i < stanzaArray.length; i++) {
						if (stanzaArray[i].isDisplayed()) {
							if (visibleStanzasSoFar == visibleIndex)
								return stanzaArray[i];
							else
								visibleStanzasSoFar++;
						}
					}
					// only return null if visibleIndex > number visible stanzas
					return null;
				}
			};
		else
			return null;
	}

	/*
	 * Return whether this Text is not fully populated for use by FreeMarker in
	 * creating a lilypond file. There is no concern for id and defaultTune
	 * fields here, as they are not used by FreeMarker.
	 * 
	 * @see freemarker.template.TemplateHashModel#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return (this.name == null || this.name.equals("") || this.meter == null
				|| this.author == null || this.author.equals("")
				|| this.stanzas == null || this.defaultTune == null);
	}
}
