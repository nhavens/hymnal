package edu.wheaton.hymnal.data;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerTest {
	private Configuration cfg;
	private static final String HYMN_TEMPLATE = "hymn_template.ftl";
	private Template temp;
	private Writer writer;
	
	public FreeMarkerTest(Writer writer) {
		this.writer = writer;
		
		cfg = new Configuration();
		// Specify the data source where the template files come from.
		// Here I set a file directory for it:
		try {
			cfg.setDirectoryForTemplateLoading(new File(this.getClass().getResource("/").getPath()));
			temp = cfg.getTemplate(HYMN_TEMPLATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Specify how templates will see the data-model. This is an advanced
		// topic...
		// but just use this:
		cfg.setObjectWrapper(new DefaultObjectWrapper());
	}
	
	public FreeMarkerTest() {
		this(new OutputStreamWriter(System.out));
	}
	
	public void processHymn(Hymn hymn) {
		try {
			temp.process(hymn, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.out.println("FreeMarkerTest ioe");
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
	
	public static Hymn getDefaultHymn() {
		Tune tune = H2Db.getInstance().selectAllTunes()[4];
		Text text = H2Db.getInstance().selectDefaultTextFor(tune);
		H2Db.getInstance().refreshMeters(text, tune);
		return new Hymn(text, tune);
	}
	
	public static void main(String[] args) {
		FreeMarkerTest fmt = new FreeMarkerTest();
		fmt.processHymn(getDefaultHymn());
	}
}
