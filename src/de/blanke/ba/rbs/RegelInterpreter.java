package de.blanke.ba.rbs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.blanke.ba.logik.Board;
import de.blanke.ba.logik.SpielLogikErweChecks;
import de.blanke.ba.model.Stein;
import de.blanke.ba.spieler.Spieler;
/**
 * Diese Klasse stellt die Regel bereit und wertet eine Anfrage an das System aus.
 * @author Paul Blanke
 *
 */
public class RegelInterpreter {
	// Regeln f�r die Spielphasen
	private RegelSet data = new RegelSet();
	private List<RegelSpielPhase0> spielphase0 = data.getSpielphase0();
	private List<RegelSpielPhase1u2> spielphase1 = data.getSpielphase1();
	private List<RegelSpielPhase1u2> spielphase2 = data.getSpielphase1();
	private List<RegelSpielPhase0> spielphase3 = data.getSpielphase3();
	private List<Regel> uebergreifendeRegeln = data.getUbergreifendeRegeln();
	private SpielLogikErweChecks logikCheck = new SpielLogikErweChecks();
	
	// R�ckgabe Liste von Steinen speziell f�r Spielphase 1 und 2.
	private List<Stein> dataBack = new ArrayList<>();
	

	/**
	 * Diese Methode verarbeitet eine Anfrage an das System.
	 * @param board
	 * @param spieler
	 * @return
	 */
	public List<Stein> sendQuery(Board board, Spieler spieler, Spieler spielerB) {
		this.setzeColor(spieler.getSpielFarbe());
		this.dataBack.clear();
		
		switch(spieler.getSpielPhase()) {
		
		case 0:    	analyseQuerySpielPhase0(board, spieler);
					break;
					
		case 1:     analyseQuerySpielPhase1(board, spieler);
					break;
					
		case 2:     analyseQuerySpielPhase2(board, spieler);
					break;
					
		case 3: 	analyseQuerySpielPhase3(board, spieler);
					break;
		default:    break;
		}
		return this.dataBack;
	}
	
	
/**
 * Die folgenden Methoden analysieren die Anfrage und liefern eine passende Antwort.	
 * @param board
 * @param spieler
 */
	private void analyseQuerySpielPhase0(Board board, Spieler spieler) {
		
		for(RegelSpielPhase0 regel: spielphase0) {
			if(regel.getIfTeil().contains("Frei") && board.checkFeld(regel.getIfStein().convertToFeld())) {
				this.dataBack.add(regel.getElseStein());
				spielphase0.remove(regel);
				break;
			} else  if(regel.getIfTeil().contains("Belegt") && !board.checkFeld(regel.getIfStein().convertToFeld()) &&
						board.checkFeld(regel.getElseStein().convertToFeld())){
				this.dataBack.add(regel.getElseStein());
				spielphase0.remove(regel);
				break;
			} else if(regel.getIfTeil().contains("Zufall")) {
				// Solange wie nichts gefunden wurde, suche weiter:)
				while(!board.checkFeld(regel.getIfStein().convertToFeld())) {
					regel.erzeugeZuf�llig();
					
					if(board.checkFeld(regel.getIfStein().convertToFeld())) {
						
						this.dataBack.add(regel.getElseStein());
						regel.erzeugeZuf�llig();
						break;
					}
				}
			}
			
		}
	}
	
	private void analyseQuerySpielPhase1(Board board, Spieler spieler) {
		List<Stein> spielData = spieler.getPosiSteine();
		
		for(RegelSpielPhase1u2 regel: spielphase1) {
			// Wenn der Spieler das Feld besetzt dann werte Regel weiter aus.
			if(spielData.contains(regel.getBesetztesFeld()) && board.checkFeld(regel.getBewegungsFeld().convertToFeld())) {
				dataBack.add(regel.getBesetztesFeld());
				dataBack.add(regel.getBewegungsFeld());
				break;
				
			} else if(regel.getIfTeil().contains("zufall")){
				for(int i = 0; i < spielData.size(); i++) {
					Stein steineins = spielData.get(i);
					if(steineins.getxCord() < 2) {
						Stein zwei = new Stein(steineins.getRing(), steineins.getxCord() + 1, steineins.getyCord(), null);
						if(board.checkFeld(zwei.convertToFeld())) {
							dataBack.add(steineins);
							dataBack.add(zwei);
							break;
						}
						
					} else if(steineins.getyCord() <2 && steineins.getxCord() != 1) {
						Stein zwei = new Stein(steineins.getRing(), steineins.getxCord(), steineins.getyCord() + 1, null);
						if(board.checkFeld(zwei.convertToFeld())) {
							dataBack.add(steineins);
							dataBack.add(zwei);
							break;
						}
					}
				}
			}
			
		}
		
		
		
	}
	private void analyseQuerySpielPhase2(Board board, Spieler spieler) {
		List<Stein> spielData = spieler.getPosiSteine();
		
		for(RegelSpielPhase1u2 regel: spielphase2) {
			// Wenn der Spieler das Feld besetzt dann werte Regel weiter aus.
			if(spielData.contains(regel.getBesetztesFeld()) && board.checkFeld(regel.getBewegungsFeld().convertToFeld())) {
				dataBack.add(regel.getBesetztesFeld());
				dataBack.add(regel.getBewegungsFeld());
				break;
				
			} else if(regel.getIfTeil().contains("zufall")){
				for(int i = 0; i < spielData.size(); i++) {
					Stein steineins = spielData.get(i);
					if(steineins.getxCord() < 2) {
						Stein zwei = new Stein(steineins.getRing(), steineins.getxCord() + 1, steineins.getyCord(), null);
						if(board.checkFeld(zwei.convertToFeld())) {
							dataBack.add(steineins);
							dataBack.add(zwei);
							break;
						}
						
					} else if(steineins.getyCord() <2 && steineins.getxCord() != 1) {
						Stein zwei = new Stein(steineins.getRing(), steineins.getxCord(), steineins.getyCord() + 1, null);
						if(board.checkFeld(zwei.convertToFeld())) {
							dataBack.add(steineins);
							dataBack.add(zwei);
							break;
						}
					}
				}
			}
			
		}
	}
	
	private void analyseQuerySpielPhase3(Board board, Spieler spieler) {
		for(RegelSpielPhase0 regel: spielphase3) {
			if(regel.getIfTeil().contains("Besetzt") && !board.checkFeld(regel.getIfStein().convertToFeld())) {
				this.dataBack.add(regel.getElseStein());
				break;
			}  else if(regel.getIfTeil().contains("zufall")) {
				// Solange wie nichts gefunden wurde, suche weiter:)
				while(board.checkFeld(regel.getIfStein().convertToFeld())) {
					regel.erzeugeZuf�llig();
					System.out.println("Suche!");
					if(!board.checkFeld(regel.getIfStein().convertToFeld())) {
						
						this.dataBack.add(regel.getElseStein());
						regel.erzeugeZuf�llig();
						break;
					}
				}
			}
			
		}
		
		
	}

	
	private void setzeColor(Color color) {
		for(RegelSpielPhase0 regel: spielphase0) {
			regel.setColor(color);
		}
	}
	/**
	 * Diese Methode verwendet einen Trick um die Regeln am Anfang auszuwerten.
	 */
	private void analyseQueryBefore(Spieler spieler,Spieler spielerB,  Board board) {
		// Switch mit der Spielphase des Spielers
		switch (spieler.getSpielPhase()) {
			case 0: 	// Werte nur Regel 1 aus
						List<Stein> rueckgabe = this.logikCheck.sucheZweiInGleicherReihe(spieler.getPosiSteine(), board);
						if(!rueckgabe.isEmpty()) {
							dataBack.add(rueckgabe.get(0));
						}
						break;
						
			case 1:		// Pruefe beide Regeln
			case 2:		List<Stein> rueckgabe2 = this.logikCheck.sucheZweiGleicheReiheUnddrittenSteinDazu(spieler.getPosiSteine(), board);
						if(!rueckgabe2.isEmpty()) {
							dataBack.add(rueckgabe2.get(0));
							dataBack.add(rueckgabe2.get(1));
						}
			break;
						
			case 3:  	// Sp�ter
						break;
			
			default: break;	
		}
	}
}
