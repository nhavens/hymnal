package edu.wheaton.hymnal.data;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

public class LilypondWrapper extends Observable {
	private File hymnPNG;
	private String hymnFilePath;
	private List<String> command;
	private ProcessBuilder lilypondPB;
	private Process lilypondProcess;

	public LilypondWrapper() {
		try {
			this.hymnPNG = File.createTempFile("hymn", ".png");

			// make sure we clean up after ourselves
			this.hymnPNG.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		hymnFilePath = hymnPNG.getAbsolutePath();
		System.out.println(hymnFilePath);

		this.command = new ArrayList<String>();
		// command
		this.command.add("lilypond");
		// arg - output as png file
		this.command.add("--png");
		// arg - specify output file
		this.command.add("-o");
		// arg - specify output file name
		this.command.add(this.hymnFilePath.substring(0,
				this.hymnFilePath.length() - 4));
		// arg - read input from stdin
		this.command.add("-");

		this.lilypondPB = new ProcessBuilder(command);
	}

	/**
	 * Using lilypond, generate a PNG for the hymn composed of the text and tune
	 * arguments. This method returns void, because it will spawn a new process
	 * to generate the PNG file. Before calling this method, be sure to call
	 * addObserver().
	 * 
	 * @param text
	 * @param tune
	 * @return URL The URL for the generated PNG file of the hymn
	 * @throws LilypondException
	 */
	public void generateHymnPng(final Text text, final Tune tune) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				try {
					/*
					 * Truncate file if sheet music has already been generated.
					 * This is necessary for the case in which lilypond creates
					 * 2 pages of sheet music and writes nothing to hymnPNG. If
					 * left as is, would look like the file contained the new
					 * music, when it still contains the old. This is b/c
					 * lilypond has written to "hymn...-page1.png" and
					 * "hymn...-page2.png".
					 */
					if (hymnPNG.length() > 0L) {
						hymnPNG.delete();
						hymnPNG.createNewFile();
					}

					// start the lilypond process
					lilypondProcess = lilypondPB.start();

					// generate the lilypond input file and set it as the stdin
					// for the lilypond process
					OutputStream os = lilypondProcess.getOutputStream();
					LilypondFileBuilder lfb = LilypondFileBuilder.getInstance();
					lfb.processHymn(new Hymn(text, tune),
							new OutputStreamWriter(os));

					// block this thread until the lilypond completes
					lilypondProcess.waitFor();

					/*
					 * Handle the case in which lilypond generates 2 pages of
					 * sheet music. For now, we will assume that lilypond will
					 * not ever generate 3 pages for one hymn.
					 */
					if (hymnPNG.length() == 0L) {
						String partialFilePath = hymnFilePath.substring(0,
								hymnFilePath.length() - 4);
						URL pg1URL = new URL("file:" + partialFilePath
								+ "-page1.png");
						URL pg2URL = new URL("file:" + partialFilePath
								+ "-page2.png");
						BufferedImage pg1img = ImageIO.read(pg1URL);
						BufferedImage pg2img = ImageIO.read(pg2URL);

						// stitch two pages together vertically
						BufferedImage columnImage = new BufferedImage(
								pg1img.getWidth(), 2 * pg1img.getHeight(),
								BufferedImage.TYPE_INT_RGB);
						Graphics2D g2dColumn = columnImage.createGraphics();
						g2dColumn.drawImage(pg1img, 0, 0, null);
						g2dColumn
								.drawImage(pg2img, 0, pg1img.getHeight(), null);
						ImageIO.write(columnImage, "png", hymnPNG);

						// delete two individual page Files
						new File(pg1URL.getPath()).delete();
						new File(pg2URL.getPath()).delete();
					}

					// this method must return a Void type
					return null;
				} catch (IOException e) {
					throw new LilypondException(e);
				} catch (InterruptedException e) {
					throw new LilypondException(e);
				}
			}

			@Override
			protected void done() {
				super.done();

				// send the PNG file name to any Observers once the lilypond
				// command has completed
				LilypondWrapper.this.setChanged();
				LilypondWrapper.this.notifyObservers(hymnPNG.toURI());
			}
		};

		worker.execute();
	}
}
