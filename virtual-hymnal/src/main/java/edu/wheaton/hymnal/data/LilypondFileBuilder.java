package edu.wheaton.hymnal.data;

import java.io.IOException;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class LilypondFileBuilder {
	private static final LilypondFileBuilder INSTANCE = new LilypondFileBuilder();
	private static Configuration cfg;
	private static final String HYMN_TEMPLATE = "hymn_template.ftl";
	private static Template temp;

	private LilypondFileBuilder() {
		// Specify the data source where the template files come from.
		// Here I set a file directory for it:
		try {
			cfg = new Configuration();
			cfg.setClassForTemplateLoading(this.getClass(), "/");
			temp = cfg.getTemplate(HYMN_TEMPLATE);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		// Specify how templates will see the data-model. This is an advanced
		// topic...
		// but just use this:
		cfg.setObjectWrapper(new DefaultObjectWrapper());
	}

	/**
	 * Use the values from hymn and the HYMN_TEMPLATE freemarker template file
	 * to generate a lilypond file for the hymn and write it to writer.
	 * 
	 * @param hymn
	 *            Hymn for which to generate a lilypond file
	 * @param writer
	 *            The Writer to which to write the lilypond file
	 * @throws LilypondException
	 */
	public void processHymn(Hymn hymn, Writer writer) throws LilypondException {
		try {
			temp.process(hymn, writer);
			writer.flush();
			writer.close();
		} catch (TemplateException e) {
			e.printStackTrace();
			throw new LilypondException(e);
		} catch ( IOException e) {
			e.printStackTrace();
			throw new LilypondException(e);
		}
	}

	public static LilypondFileBuilder getInstance() {
		return INSTANCE;
	}
}
