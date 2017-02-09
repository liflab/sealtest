package ca.uqac.lif.ecp.atomic.petrinet;

import java.util.Scanner;

import ca.uqac.lif.ecp.atomic.AtomicEvent;

public class AtomicPetriNetBuilder 
{
	/**
	 * Defines the Petri net from a structured string.
	 * The string itself is just a succession of "words" separated
	 * by whitespace with the following structure:
	 * <dl>
	 * <dt><tt>T label<sub>1</sub> label<sub>2</sub></tt></dt>
	 * <dd>Defines an arrow from a transition named label<sub>1</sub> to a
	 * place named label<sub>2</sub></dd>
	 * <dt><tt>P label<sub>1</sub> label<sub>2</sub></tt></dt>
	 * <dd>Defines an arrow from a place named label<sub>1</sub> to a
	 * transition named label<sub>2</sub></dd>
	 * <dt><tt>M label<sub>1</sub> x</tt></dt>
	 * <dd>Defines an initial marking by putting <i>x</i> tokens into
	 * a place named label<sub>1</sub></dd>
	 * </dl>
	 * For example, Petri net <i>N</i> shown on
	 * <a href="http://en.wikipedia.org/wiki/Petri_net">Wikipedia's entry</a>
	 * would be represented as:
	 * <pre>
	 * T T1 p1
	 * P p1 T2
	 * T T2 p2
	 * P p2 T3
	 * M p1 0
	 * M p2 0
	 * </pre>
	 * The order in which lines are input does not
	 * matter: transitions and places are created the first time their
	 * label is seen by the parser.
	 * <p>
	 * <em>Caveat emptor</em>: the parser assumes the string is well-formed
	 * but does not check it. A malformed string will likely cause a runtime
	 * error of some kind.
	 * @param scanner A scanner to an input string
	 */
	public static PetriNet<AtomicEvent> parseFromString(Scanner scanner) throws java.text.ParseException
	{
		PetriNet<AtomicEvent> net = new PetriNet<AtomicEvent>();
		int line_cnt = 0;
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			line_cnt++;
			if (line.isEmpty() || line.startsWith("#"))
				continue;
			String[] elements = line.split("\\s+");
			String word = elements[0];
			if (word.compareTo("P") == 0)
			{
				// Definition of a place -> transition arrow
				String place_label = elements[1];
				String trans_label = elements[2];
				Place p = net.getAddPlace(new Place(place_label));
				Transition<AtomicEvent> t = net.getAddTransition(new Transition<AtomicEvent>(new AtomicEvent(trans_label)));
				p.addOutgoingTransition(t);
				t.addIncomingPlace(p);
			}
			else if (word.compareTo("T") == 0)
			{
				// Definition of a transition -> place arrow
				String place_label = elements[2];
				String trans_label = elements[1];
				Place p = net.getAddPlace(new Place(place_label));
				Transition<AtomicEvent> t = net.getAddTransition(new Transition<AtomicEvent>(new AtomicEvent(trans_label)));
				p.addIncomingTransition(t);
				t.addOutgoingPlace(p);
			}
			else if (word.compareTo("M") == 0)
			{
				// Definition of an initial marking
				String place_label = elements[1];
				int value = Integer.parseInt(elements[2]);
				Place p = net.getAddPlace(new Place(place_label));
				p.setMarking(value);
			}
			else
			{
				throw new java.text.ParseException("Unknown word " + word, line_cnt);
			}
		}
		return net;
	}
}
