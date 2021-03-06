package de.blanke.ba.cbr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import de.blanke.ba.logik.Board;
import de.blanke.ba.mas.MessageBox;
import de.blanke.ba.mas.MessageBoxSteine;
import de.blanke.ba.model.Stein;
import de.blanke.ba.spieler.Spieler;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
/**
 * Dieser Agent verwaltet das CBR System.
 * Siehe auch @RBSAgent
 * @author Paul Blanke
 *
 */
public class CBRAgent extends Agent{
// Attribute
	private static final long serialVersionUID = 1L;
	private CBRController interpreter = new CBRController();
	private static final Logger logger = Logger.getLogger(CBRAgent.class);
// Agenten Methode
	@Override
	protected void setup() {
		System.out.println("CBR System aktiv!");
		PropertyConfigurator.configure(CBRAgent.class.getResource("log4j.info"));
		logger.info(" Das CBR System wurde aktiviert");
		super.setup();
		addBehaviour(new CyclicBehaviour() {
			private static final long serialVersionUID = 1L;
			@Override
			public void action() {
				/**
				 * Warte auf Nachrichtenempfang
				 */
				ACLMessage msg = receive();
				MessageBox box = null;
				if(msg != null) {
					/**
					 * Werte die Nachricht und sende eine Nachricht zurück.
					 */
					List<Stein> rueckgabe = new ArrayList<>();
					
					try {
						box = (MessageBox) msg.getContentObject();
						// Hole mir die Daten raus
						
					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Board board = box.getBoard();
					Spieler spieler = box.getSpieler();
					boolean reosoning = box.isReosoning();
					// Fehlerhandling
					if(board != null && spieler != null) {
						List <Stein> extractData = interpreter.executeQuery(board, spieler, reosoning);
						for(Stein i: extractData) {
							rueckgabe.add(i);
						}
					} else {
						System.out.println("Error-AgentenEbene: Übertragungsproblem !!");
						logger.info("CBR System: Übertragungsproblem");
					}
					logger.info("CBR-System: Eine Nachricht wurde erzeugt und wird versendet");
					box.cleartheBox();
					// Rücktransport
					 ACLMessage aclmsg = new ACLMessage(ACLMessage.INFORM);
					aclmsg.addReceiver(new AID("Controller Agent", AID.ISLOCALNAME));
					aclmsg.setContent("Zug beendet");
					Stein eins = null;
					Stein zwei = null;
					MessageBoxSteine steine = null;
					// Fehlerhandling zur Sicherheit
					if(!rueckgabe.isEmpty() && spieler.getSpielPhase() == 0) {
						 eins = rueckgabe.get(0);
						 steine = new MessageBoxSteine(eins, zwei);
						 logger.info("CBR System: Nur ein Rückgabeparameter!");
					} else if(!rueckgabe.isEmpty() && rueckgabe.size() >= 2) {
							eins = rueckgabe.get(0);
							zwei = rueckgabe.get(1);
					} else if(rueckgabe.isEmpty()) {
						logger.info("CBR System: Übertragungsproblem");
						System.out.println("CBR System: Es konnte keine Rückgabe ermittelt werden!");
						steine = new MessageBoxSteine(null, null);
					}
					try {
						aclmsg.setContentObject(steine);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					send(aclmsg);
					msg =  null;
				} else {
					block();
				}
			}
		});
	}
}
