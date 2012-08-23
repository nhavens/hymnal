package edu.wheaton.hymnal.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

@DatabaseTable
public class Meter implements TemplateHashModel {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = false, uniqueIndex = true, width = 20)
	private String beats = null;

	@DatabaseField(width = 25)
	private String name = null;

	public Meter(String beats, String name) {
		this.beats = beats;
		this.name = name;
	}

	public Meter() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beats == null) ? 0 : beats.hashCode());
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
		Meter other = (Meter) obj;
		if (beats == null) {
			if (other.beats != null)
				return false;
		} else if (!beats.equalsIgnoreCase(other.beats))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBeats() {
		return beats;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return beats + ": " + name;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		if (key.equalsIgnoreCase("beats"))
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return beats;
				}
			};
		else if (key.equalsIgnoreCase("name") && name != null && !name.equals(""))
			
			return new TemplateScalarModel() {

				public String getAsString() throws TemplateModelException {
					return name;
				}
			};
		else
			return null;
	}

	@Override
	public boolean isEmpty() {
		return (this.beats == null || this.beats.equals(""));
	}
}
