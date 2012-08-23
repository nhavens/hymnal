package edu.wheaton.hymnal.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

@DatabaseTable
public class Stanza implements TemplateHashModel {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false)
	private int number = 0;

	@DatabaseField(canBeNull = false, foreign = true)
	private Text text;

	@DatabaseField(canBeNull = false, dataType = DataType.LONG_STRING)
	private String words;

	private boolean display = true;

	public Stanza(int number, String words, Text text) {
		this.number = number;
		this.words = words;
		this.text = text;
	}

	public Stanza() {
	}

	public int getId() {
		return id;
	}

	public int getNumber() {
		return number;
	}

	public Text getText() {
		return text;
	}

	public String getWords() {
		return words;
	}

	public void toggleDisplay() {
		this.display = !this.display;
	}

	public boolean isDisplayed() {
		return this.display;
	}

	/*
	 * enum to convert a stanza number to a word for the dynamically built
	 * lilypond file.
	 */
	public enum NumberWord {
		ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE;

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

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		if (key.equalsIgnoreCase("numberWord"))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return NumberWord.values()[number - 1].toString();
				}
			};
		else if (key.equalsIgnoreCase("number"))
			return new TemplateNumberModel() {

				public Number getAsNumber() throws TemplateModelException {
					return number;
				}
			};
		else if (key.equalsIgnoreCase("words"))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return words;
				}
			};
		else
			return null;
	}

	@Override
	public boolean isEmpty() {
		return (this.number == 0 || this.text == null || this.words == null || this.words
				.equals(""));
	}
}
