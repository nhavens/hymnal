package edu.wheaton.hymnal.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

@DatabaseTable
public class Voice implements TemplateHashModel {
	@DatabaseField(generatedId = true)
	private int id;

	/*
	 * The enum value representing the part for this voice (SOPRANO, ALTO,
	 * TENOR, or BASS. NOTE: In the db we store the ordinal integer rather than
	 * the enum String value. 0 = SOPRANO 1 = ALTO 2 = TENOR 3 = BASS
	 */
	@DatabaseField(dataType = DataType.ENUM_INTEGER)
	private Part part;

	@DatabaseField(canBeNull = false, foreign = true)
	private Tune tune;

	@DatabaseField(canBeNull = false, dataType = DataType.LONG_STRING)
	private String notes;

	public enum Part {
		SOPRANO, ALTO, TENOR, BASS;

		/*
		 * When converting to a String, capitalize only the first letter.
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}
	}

	public Voice(Part part, String notes, Tune tune) {
		this.part = part;
		this.notes = notes;
		this.tune = tune;
	}

	public Voice() {
	}

	public int getId() {
		return id;
	}

	public String getPart() {
		return part.toString();
	}

	public Tune getTune() {
		return tune;
	}

	public String getNotes() {
		return notes;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		if (key.equalsIgnoreCase("part"))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return getPart();
				}
			};
		else if (key.equalsIgnoreCase("notes"))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return notes;
				}
			};
		else
			return null;
	}

	@Override
	public boolean isEmpty() {
		return (this.part == null || this.tune == null || this.notes == null || this.notes
				.equals(""));
	}
}