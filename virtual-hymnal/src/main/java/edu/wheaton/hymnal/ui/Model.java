package edu.wheaton.hymnal.ui;

import java.util.Observable;

import edu.wheaton.hymnal.data.Db;
import edu.wheaton.hymnal.data.H2Db;
import edu.wheaton.hymnal.data.Text;
import edu.wheaton.hymnal.data.Tune;

public class Model extends Observable {
	private String topLevelSelection;
	private Tune tune;
	private Text text;
	private State st;
	/**
	 * We need to remember from which state we entered the RenderState so that
	 * we can properly respond if the user switches back to chooser mode.
	 */
	private State preRenderState;
	private static Db db = H2Db.getInstance();

	public Model() {
		topLevelSelection = null;
		tune = null;
		text = null;
		st = new InitialState();
		preRenderState = null;
	}

	private abstract class State {
		public State setTopLevelSelection(String selection) {
			tune = null;
			text = null;
			if (selection != null && selection != ""
					&& !selection.equals(topLevelSelection)) {
				topLevelSelection = selection;
				setChanged();
				return new GotTopLevelState();
			} else
				return st; // stay in the current state for bad input or if
							// selection does not change
		}

		public abstract State setTune(Tune t);

		public abstract State setText(Text t);
	}

	private class InitialState extends State {

		// This method should not be called
		public State setTune(Tune t) {
			return this;
		}

		// This method should not be called
		public State setText(Text t) {
			return this;
		}
	}

	private class GotTopLevelState extends State {
		public State setTune(Tune t) {
			tune = t;
			// auto-select the default text initially
			text = db.selectDefaultTextFor(t);
			setChanged();
			return new GotBothTuneFirstState();
		}

		public State setText(Text t) {
			text = t;
			// auto-select the default tune initially
			tune = db.selectDefaultTuneFor(t);
			setChanged();
			return new GotBothTextFirstState();
		}
	}

	private class GotBothTextFirstState extends State {
		public State setTune(Tune t) {
			tune = t;
			setChanged();
			return st;
		}

		public State setText(Text t) {
			text = t;
			tune = db.selectDefaultTuneFor(t);
			setChanged();
			return st;
		}
	}

	private class GotBothTuneFirstState extends State {
		public State setTune(Tune t) {
			tune = t;
			text = db.selectDefaultTextFor(t);
			setChanged();
			return st;
		}

		public State setText(Text t) {
			text = t;
			setChanged();
			return st;
		}
	}

	private class RenderState extends State {
		// This should not happen, because the model should only be in this
		// state when the MusicPage is displayed. There is no way to change
		// the topLevelSelection from the MusicPage.
		public State setTopLevelSelection(String selection) {
			return st;
		}

		public State setTune(Tune t) {
			tune = t;
			setChanged();
			return st;
		}

		public State setText(Text t) {
			text = t;
			setChanged();
			return st;
		}
	}

	public String getTopLevelSelection() {
		return topLevelSelection;
	}

	public void setTopLevelSelection(String selection) {
		st = st.setTopLevelSelection(selection);
		notifyObservers(topLevelSelection);
		printModel();
	}

	public Tune getTune() {
		return tune;
	}

	public void setTune(Tune t) {
		st = st.setTune(t);
		notifyObservers(tune);
		printModel();
	}

	public Text getText() {
		return text;
	}

	public void setText(Text t) {
		st = st.setText(t);
		notifyObservers(text);
		printModel();
	}

	public void printModel() {
//		System.out.println("Model:\n\ttopLevelSelection: " + topLevelSelection
//				+ "\n\ttext: " + text + "\n\ttune: " + tune + "\n\tstate: "
//				+ st.getClass().getName());
	}

	/**
	 * Whether the model is ready to be rendered as sheet music.
	 */
	public boolean isRenderReady() {
		return (st instanceof GotBothTuneFirstState | st instanceof GotBothTextFirstState);
	}

	public void render() {
		this.preRenderState = st;
		this.st = new RenderState();
	}

	public void returnToChooser() {
		this.st = preRenderState;
		this.preRenderState = null;
	}
}
