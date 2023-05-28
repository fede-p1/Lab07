package it.polito.tdp.poweroutages.model;

import java.util.*;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	Map<Nerc, Integer> nercMap;
	List<Outage> soluzione;
	
	public Model() {
		podao = new PowerOutageDAO();
		this.nercMap = new HashMap<>(podao.getNercMap());
		this.soluzione = new ArrayList<>();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	/*
	 * blackout in massimo di X anni, per un totale di Y ore di disservizio massimo, 
	 * tale da massimizzare il numero totale di persone coinvolte.
	 *   - disservizio viene calcolato come la differenza tra date_event_began e date_event_finished
	 *   - numero totale di ore di disservizio del sottoinsieme di eventi selezionati 
	 *     deve essere sempre minore o uguale del valore Y
	 *   - La differenza tra l’anno dell’evento più recente e l’anno di quello più vecchio 
	 *     deve essere sempre minore o uguale del numero di anni X  
	 */  
	
	public void getOutages(Nerc nerc, int years, int hours){
	
		List<Outage> outageNerc = new ArrayList<>(this.podao.getOutages(nerc));
		Collections.sort(outageNerc);
		
		this.size = outageNerc.size();
		
		
		List<Outage> parziale = new ArrayList<>();
		
		ricorsiva(outageNerc, years, hours, parziale, 0, 0, 0);
				
	}

	int maxCoinvolte = 0;
	int size;
	
	public void ricorsiva(List<Outage> outageNerc, int years, int hours, List<Outage> parziale, int personeCoinvolte, int totOre, int livello) {
		
		// algoritmo finisce quando...?
		if (livello == size)
			return;
				
		while (!outageNerc.isEmpty()) {
			
			Outage o = outageNerc.get(0);
			
			if(parziale.isEmpty() && (totOre + o.getOreDisservizio() <= hours)) {
				parziale.add(o); //devo creare nuova lista?
				personeCoinvolte += o.getCustomersAffected();
				totOre += o.getOreDisservizio();
				List<Outage> outageNerc2 = new ArrayList<Outage>(outageNerc);
				outageNerc2.remove(o);
				ricorsiva(outageNerc2,years, hours,parziale,personeCoinvolte, totOre, livello++ );
				parziale.remove(o);
				personeCoinvolte -= o.getCustomersAffected();
				totOre -= o.getOreDisservizio();
				outageNerc.add(o);
				livello--;
			}
				
			
			else if ((o.getAnnoInizio() - parziale.get(0).getAnnoInizio() <= years) && (totOre + o.getOreDisservizio() <= hours)) {
				if (!parziale.contains(o)) {
					parziale.add(o); //devo creare nuova lista?
					personeCoinvolte += o.getCustomersAffected();
					totOre += o.getOreDisservizio();
					List<Outage> outageNerc2 = new ArrayList<Outage>(outageNerc);
					outageNerc2.remove(o);
					ricorsiva(outageNerc2,years, hours,parziale,personeCoinvolte, totOre, livello++ );
					parziale.remove(o);
					personeCoinvolte -= o.getCustomersAffected();
					totOre -= o.getOreDisservizio();
					outageNerc.add(o);
					livello --;
				}
			}}
			if (personeCoinvolte > maxCoinvolte) {
				maxCoinvolte = personeCoinvolte;
				this.soluzione = parziale;
				}
			
			}

	public List<Outage> getSoluzione() {
		return soluzione;
	}

	public void setSoluzione(List<Outage> soluzione) {
		this.soluzione = soluzione;
	}
			
	
}
