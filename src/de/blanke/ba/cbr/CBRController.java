package de.blanke.ba.cbr;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.blanke.ba.logik.Board;
import de.blanke.ba.mas.AgentenOperations;
import de.blanke.ba.model.Stein;
import de.blanke.ba.spieler.Spieler;
import de.dfki.mycbr.core.ICaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.IntegerDesc;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.util.Pair;

/**
 * Diese Klasse stellt den Zugriff auf das System bereit.
 * @author Paul Blanke, 05.12.2018
 *
 */
public class CBRController {
	
	// CBR Einstellungen
	private CBR_Engine engine = new CBR_Engine();
	private Project project = engine.loadCBRProject();
	private String conceptName = "m�hle";
	private Concept concept = null;
	private ICaseBase casebase = null;
	private AgentenOperations operations = new AgentenOperations();
	private List<Integer> resultSet = new ArrayList<>();
	
	// F�r die Fehlerbehandlung wird ein Logger eingesetzt. @log4j
	private static final Logger logger = Logger.getLogger(CBRController.class);
	
	
	

	/**
	 * Diese Methode initalisiert die zentrale CBR Infrastruktur, die ben�tigt wird, und l�det alle F�lle.
	 */
	private void initCBRStrcuture() {
		PropertyConfigurator.configure(CBRController.class.getResource("log4j.info"));
		this.concept = project.getConceptByID(conceptName);
		try {
			this.concept = project.getConceptByID(conceptName);
			this.casebase = project.createDefaultCB("cb");
			Collection<Instance> instances = concept.getAllInstances();
			for(Instance ini:instances) {
				casebase.addCase(ini);
			}
			logger.info("CBR System: Laden beendent: Es sind " + casebase.getCases().size()+  " vorhanden (Initalisierungsphase)");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Kontruktor
	public CBRController() {
		this.initCBRStrcuture();
	}
	
	/**
	 * Diese Methode verarbeitet die Query an das System.
	 * @param board Eingabeparameter.
	 * @param spieler Eingabeparameter.
	 * @return Liste von Integers als Ausgabeparameter.
	 */
	public List<Integer> executeQuery(Board board, Spieler spieler) {
		// Werte die Daten aus:
		this.resultSet.clear();
		int spielphase = spieler.getSpielPhase();
		int anzahlDerEigenenSpielsteine = spieler.getAnzahlSteine();
		int m�hlen = spieler.getAnzahlM�hlen();
		int spielsteineR1 = 0;
		int	spielsteineR2 = 0;
		int spielsteineR3 = 0;
		List<Stein> dataToCheck = spieler.getPosiSteine();
		
		for(Stein stein:dataToCheck) {
			if(stein.getRing() == 0) {
				spielsteineR1++;
			} else if(stein.getRing() == 1) {
				spielsteineR2++;
			} else {
				spielsteineR3++;
			}
		}
		
		// Set up the retrieval & prepare the Query
		try {
			Retrieval retrieval = new Retrieval(concept, casebase);
			Instance query = retrieval.getQueryInstance();
			query.addAttribute("Spielphase", spielphase);
			query.addAttribute("Anzahl_d_eigenen_Spielsteine", anzahlDerEigenenSpielsteine);
			query.addAttribute("Spielsteine_Ring_1", spielsteineR1);
			query.addAttribute("Spielsteine_Ring_2", spielsteineR2);
			query.addAttribute("Spielsteine_Ring_3", spielsteineR3);
			query.addAttribute("M�hlen", m�hlen);
			retrieval.start();
			logger.info("CBR Query execute with: " + spielphase + ", "+ anzahlDerEigenenSpielsteine + ", "
					+ spielsteineR1 + ", "+ spielsteineR2 + ", " + spielsteineR3 + ","+ m�hlen);
			List<Pair<Instance, Similarity>> result = retrieval.getResult();
			IntegerDesc l�sungADesc = (IntegerDesc) concept.getAllAttributeDescs().get("L�sungfeld_Start");
			IntegerDesc l�sungBDesc = (IntegerDesc) concept.getAllAttributeDescs().get("L�sungsfeld_Ziel");
			IntegerDesc spQuery = (IntegerDesc) concept.getAllAttributeDescs().get("Spielphase");
			List<Instance> dataResult = new ArrayList<>();
			List<Integer> dataQuery = new ArrayList<>();
			for(int i = 0; i <= 10; i++) {
				dataResult.add(result.get(i).getFirst());
				dataQuery.add(Integer.parseInt(result.get(i).getFirst().getAttForDesc(l�sungBDesc).getValueAsString()));
			}
			
			resultSet.add(Integer.parseInt(dataResult.get(0).getAttForDesc(l�sungBDesc).getValueAsString()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	private void pruefeAufEinBelegtesFeld() {
		
	}
	
	
}	
