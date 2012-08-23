package edu.wheaton.hymnal.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import edu.wheaton.hymnal.data.FreeMarkerTest;

/*
 * Test executing lilypond command from Java with
 * existing lilypond input file
 */
public class LilyPondTest {
	public static void main(String[] args) throws URISyntaxException {
		try {
			File output = File.createTempFile("hymn", ".png");
			output.deleteOnExit();
			String outputFilename = output.getAbsolutePath();
			System.out.println(outputFilename);

			List<String> command = new ArrayList<String>();
			// command
			command.add("lilypond");
			// arg - output as png file
			command.add("--png");
			// arg - specify output file
			command.add("-o");
			// arg - specify output file name
			command.add(outputFilename.substring(0, outputFilename.length() - 4));
			command.add("-"); // arg - read input from stdin

			ProcessBuilder pb1 = new ProcessBuilder(command);

			// set this file to be stdin for the process created by
			// ProcessBuilder
			Process p1 = pb1.start();
			OutputStream os = p1.getOutputStream();
			new FreeMarkerTest(new OutputStreamWriter(os))
					.processHymn(FreeMarkerTest.getDefaultHymn());

			if (p1.waitFor() == 0) {
				System.out.println("lilypond command completed successfully");
				// handle the case in which lilypond generates 2 pages of
				// sheet music. for now, we will assume that lilypond will
				// not ever generate 3 pages for one hymn
				if (output.length() == 0L) {
					String partialFilePath = outputFilename.substring(0,
							outputFilename.length() - 4);

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
					// start this one at 'height' down the final image
					g2dColumn.drawImage(pg2img, 0, pg1img.getHeight(), null);

					// write the contents of the new image to hymnPNG
					ImageIO.write(columnImage, "png", output);

					// delete two individual pages
					new File(pg1URL.getPath()).delete();
					new File(pg2URL.getPath()).delete();
				}

			} else
				System.exit(1);

			ProcessBuilder pb2 = new ProcessBuilder("eog", outputFilename);
			Process p2 = pb2.start();
			if (p2.waitFor() == 0)
				System.out.println("eog command completed successfully");

			output.delete();
			System.out.println("Program terminated!");
		} catch (IOException e) {
			System.out.println("LilyPondTest ioe");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
