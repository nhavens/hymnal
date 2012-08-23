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
public class Tune implements TemplateHashModel {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, width = 50)
	private String name;

	@DatabaseField(canBeNull = false, foreign = true)
	private Meter meter;

	@DatabaseField(canBeNull = false)
	private String composer;

	@DatabaseField(canBeNull = false, width = 15)
	private String keyOf;

	@DatabaseField(canBeNull = false, width = 5)
	private String timeSignature;

	@DatabaseField(width = 8)
	private String tempo = null;

	/*
	 * lilypond string representing the duration of the notes in the partial
	 * measure beginning a Tune, and null if the Tune begins at the beginning of
	 * a measure.
	 */
	@DatabaseField(width = 15)
	private String partialString = null;

	@DatabaseField(canBeNull = false, foreign = true)
	private Text defaultText;

	@ForeignCollectionField(orderColumnName = "part")
	private Collection<Voice> voices;

	public Tune(String name, Meter meter, String composer, String keyOf,
			String timeSignature, String tempo, String partialString,
			Text defaultText) {
		this.name = name;
		this.meter = meter;
		this.composer = composer;
		this.keyOf = keyOf;
		this.timeSignature = timeSignature;
		this.tempo = tempo;
		this.partialString = partialString;
		this.defaultText = defaultText;
		this.voices = null;
	}

	public Tune() {
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

	public String getComposer() {
		return composer;
	}

	public String getKeyOf() {
		return keyOf;
	}

	public String getTimeSignature() {
		return timeSignature;
	}

	public String getTempo() {
		return tempo;
	}

	public String getPartialString() {
		return partialString;
	}

	public void setDefaultText(Text defaulText) {
		this.defaultText = defaulText;
	}

	public Text getDefaultText() {
		return defaultText;
	}

	public Collection<Voice> getVoices() {
		return voices;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((composer == null) ? 0 : composer.hashCode());
		result = prime * result
				+ ((defaultText == null) ? 0 : defaultText.hashCode());
		result = prime * result + id;
		result = prime * result + ((keyOf == null) ? 0 : keyOf.hashCode());
		result = prime * result + ((meter == null) ? 0 : meter.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((partialString == null) ? 0 : partialString.hashCode());
		result = prime * result + ((tempo == null) ? 0 : tempo.hashCode());
		result = prime * result
				+ ((timeSignature == null) ? 0 : timeSignature.hashCode());
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
		Tune other = (Tune) obj;
		if (composer == null) {
			if (other.composer != null)
				return false;
		} else if (!composer.equals(other.composer))
			return false;
		if (defaultText == null) {
			if (other.defaultText != null)
				return false;
		} else if (defaultText.getId() != other.defaultText.getId())
			return false;
		if (id != other.id)
			return false;
		if (keyOf == null) {
			if (other.keyOf != null)
				return false;
		} else if (!keyOf.equals(other.keyOf))
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
		if (partialString == null) {
			if (other.partialString != null)
				return false;
		} else if (!partialString.equals(other.partialString))
			return false;
		if (tempo == null) {
			if (other.tempo != null)
				return false;
		} else if (!tempo.equals(other.tempo))
			return false;
		if (timeSignature == null) {
			if (other.timeSignature != null)
				return false;
		} else if (!timeSignature.equals(other.timeSignature))
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
		else if (key.equalsIgnoreCase("composer"))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return composer;
				}
			};
		else if (key.equalsIgnoreCase("key"))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return Tune.this.keyOf;
				}
			};
		else if (key.equalsIgnoreCase("timeSignature"))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return timeSignature;
				}
			};
		else if (key.equalsIgnoreCase("tempo") && tempo != null
				&& !tempo.equals(""))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return tempo;
				}
			};
		else if (key.equalsIgnoreCase("partialString") && partialString != null
				&& !partialString.equals(""))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return partialString;
				}
			};
		else if (key.equalsIgnoreCase("voices"))
			return new TemplateSequenceModel() {

				public int size() throws TemplateModelException {
					return voices.size();
				}

				public TemplateModel get(int index)
						throws TemplateModelException {
					try {
						return voices.toArray(new Voice[voices.size()])[index];
					} catch (ArrayIndexOutOfBoundsException e) {
						return null;
					}
				}
			};
		else
			return null;
	}

	/*
	 * Return whether this Tune is not fully populated for use by FreeMarker in
	 * creating a lilypond file. There is no concern for id and defaultText
	 * fields here, as they are not used by FreeMarker.
	 * 
	 * @see freemarker.template.TemplateHashModel#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return (this.name == null || this.name.equals("") || this.meter == null
				|| this.composer == null || this.composer.equals("")
				|| this.keyOf == null || this.keyOf.equals("")
				|| this.timeSignature == null || this.timeSignature.equals("")
				|| this.voices == null || this.defaultText == null);
	}
}
