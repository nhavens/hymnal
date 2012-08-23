package edu.wheaton.hymnal.data;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Container class for text and tune portions of a hymn. Used by freemarker.
 * @author neile
 *
 */
public class Hymn implements TemplateHashModel {
	private Text text;
	private Tune tune;

	public Hymn(Text text, Tune tune) {
		this.text = text;
		this.tune = tune;
	}

	public Text getText() {
		return text;
	}

	public Tune getTune() {
		return tune;
	}

	public TemplateModel get(String key) throws TemplateModelException {
		if (key.equalsIgnoreCase("text"))
			return text;
		else if (key.equalsIgnoreCase("tune"))
			return tune;
		else
			return null;
	}

	public boolean isEmpty() throws TemplateModelException {
		return (this.text == null || this.tune == null);
	}

}
