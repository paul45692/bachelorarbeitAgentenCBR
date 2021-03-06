package de.blanke.ba.logik;

import java.util.ArrayList;
import java.util.List;

import de.blanke.ba.model.Feld;
import de.blanke.ba.model.Spielstein;
import de.blanke.ba.model.Stein;
import de.blanke.ba.spieler.Spieler;

/**
 * Diese Klasse stellt die Logik Kontrolle bereit und
 * validiert Spielz�ge auf ihre Umsetzung.
 * Erst nach der endg�ltigen Freigabe werden die Z�ge umgesetzt.
 * @author Paul Blanke.
 *
 */
public class SpielController {
// Attribute
	private Board board = new Board();
	private boolean muehle = false;
	private MuehleDecting decting = new MuehleDecting();
	private Converter helper = new Converter();
	private Feld feld = null;
	private boolean spielEnde = false;
// Getter und Setter	
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public boolean isMuehle() {
		return muehle;
	}
	public void setMuehle(boolean muehle) {
		this.muehle = muehle;
	}
	public boolean isSpielEnde() {
		return spielEnde;
	}
	public void setSpielEnde(boolean spielEnde) {
		this.spielEnde = spielEnde;
	}
	// Methoden	
	/**
	 * Diese Methode stellt die Umsetzung der Aktion Spielstein setzen dar.
	 * @param x Koordinaten
	 * @param y "
	 * @param spieler Der aktuelle Spieler am Zug.
	 * @param steinGUI Der Spielstein von der Oberfl�che.
	 * @return Der Stein wurde erfolgreich gesetzt / nicht gesetzt.
	 */
	public boolean setSpielStein(int x, int y, Spieler spieler, Spielstein steinGUI) {
		 Feld feld = helper.ermitteleFeld(x, y);
		// 1. Spielphase
		if(spieler.getAnzahlSteine() < 9 && spieler.getSpielPhase() == 0) {
			if(board.setzteStein(feld.getRingZahl(), feld.getxCord(), feld.getyCord(), spieler, steinGUI)) {
				spieler.setAnzahlSteine(spieler.getAnzahlSteine() + 1);
				spieler.setAnzahlSpielZ�ge(spieler.getAnzahlSpielZ�ge() + 1);
				Stein stein = new Stein(feld.getRingZahl(), feld.getxCord(), feld.getyCord(), spieler.getSpielFarbe());
				spieler.setzeSpielstein(stein);
				if(spieler.getAnzahlSteine() == 9) {
					spieler.setSpielPhase(1);
					System.out.print("Info: Der "  + spieler.getName() + "hat die erste Spielphase verlassen!");
				}
				return true;
				
			} else {
				return false;
			}
		} else  if(spieler.getSpielPhase() == 1)	{
				if(!this.testAufSpielEnde(spieler)) { 
					if(board.setzteStein(feld.getRingZahl(), feld.getxCord(), feld.getyCord(), spieler, steinGUI)
						&& (this.feld.checkObFeldNachbarnIst(board, feld))) {
						spieler.setAnzahlSpielZ�ge(spieler.getAnzahlSpielZ�ge() + 1);
						Stein stein = new Stein(feld.getRingZahl(), feld.getxCord(), feld.getyCord(), spieler.getSpielFarbe());
						spieler.setzeSpielstein(stein);
						// Wechsel der Spielphase
						if(spieler.getAnzahlSteine() == 3) {
							System.out.print("Der " + spieler.getName() + "wechselt in die dritte Spielphase!");
							spieler.setSpielPhase(2);
						}
						return true;
				
					} else {
						return false;
					}
				} else {
					return false;
				}
		} else if(spieler.getSpielPhase() == 2)  {
			if(!this.testAufSpielEnde(spieler)) {
				if(board.setzteStein(feld.getRingZahl(), feld.getxCord(), feld.getyCord(), spieler, steinGUI)) {
					spieler.setAnzahlSpielZ�ge(spieler.getAnzahlSpielZ�ge() + 1);
					Stein stein = new Stein(feld.getRingZahl(), feld.getxCord(), feld.getyCord(), spieler.getSpielFarbe());
					spieler.setzeSpielstein(stein);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else  {
			return false;
		}
	}
	/**
	 * Diese Methode entfernt eine Stein vom Spielfeld.
	 * @param x
	 * @param y
	 * @return
	 */
	public Spielstein entferneSteinVonFeld(int x, int y, Spieler spieler) {
		feld = helper.ermitteleFeld(x, y);
		Spielstein spielstein = board.entferneStein(feld, spieler);
		spieler.removeStein(feld.convertToStein());
		return spielstein;
	}
	
	
	/**
	 * Diese Methode kontrolliert die M�hle Pr�fung.
	 * @param spieler
	 */
	public boolean pruefeAufMuehle(Spieler spieler) {
		if(spieler.getAnzahlSteine() > 2) { 
			return decting.findeM�hle(spieler);
		} else {
			return false;
		}
	}
	/**
	 * Diese Methode pr�ft auf ein m�gliches Spielende.
	 * Sobald in der ersten und zweiten Spielphase f�r einen Spieler keine Z�ge mehr m�glich sind, wird
	 * das Spiel beendet.
	 * @return Spielende ja / nein.
	 */
	public boolean testAufSpielEnde(Spieler spieler) {
		boolean check = false;
		List<Feld> dataCheck = new ArrayList<>();
		for(Stein stein:spieler.getPosiSteine()) {
			Feld f = stein.convertToFeld();
			if(!f.allefreienNachbarn(board).isEmpty()) {
				dataCheck.add(f.allefreienNachbarn(board).get(0));
			}
		}
		if(dataCheck.isEmpty()) {
			check = true;
			this.spielEnde = true;
			System.out.println("Spiel Ende: Der Spieler:"+  spieler.getName() + "kann keine Z�ge mehr ausf�hren!");
		} else if(spieler.getAnzahlSteine() == 2) {
			check = true;
			System.out.println("Spielende:" + spieler.getName() +  " hat verloren!");
		}
		return check;
	}
}
