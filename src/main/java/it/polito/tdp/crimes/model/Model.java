package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {

	public EventsDao dao;
	public Graph<String, DefaultWeightedEdge> grafo;
	public List<Arco> archi = new ArrayList<>();
	public List<String> best;

	public List<String> getCategorie() {
		dao = new EventsDao();
		return dao.getCategorie();
	}

	public List<Integer> getAnni() {
		dao = new EventsDao();
		return dao.getAnni();
	}

	public void creaGrafo(String cId, int anno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao = new EventsDao();

		// vertici
		Graphs.addAllVertices(grafo, dao.getVertici(cId, anno));

		// archi

		for (Arco a : dao.getArchi(cId, anno)) {

			if ((a.getPeso() > 0) && (grafo.getEdge(a.getId2(), a.getId1()) == null)) {
				Graphs.addEdge(grafo, a.getId1(), a.getId2(), a.getPeso());
				archi.add(a);
				
			}
		}

	}

	public int getNV() {
		
		return grafo.vertexSet().size();
	}

	public Object getNA() {
		
		return grafo.edgeSet().size();
	}

	public Arco getBestArco() {
		Arco best = new Arco(null, null, 0);
		
		for (Arco a: archi ) {
			if(a.getPeso() > best.getPeso()) {
				best = a;
			}
		}
		
		return best;
	}

	public List<Arco> getBestArchi() {
		List<Arco> bestArchi = new ArrayList<>();
		
		for (Arco a: archi) {
			if(a.getPeso()== getBestArco().getPeso()) {
				bestArchi.add(a);
			}
		}
		
		return bestArchi;
	}
	
	public List<String> trovaPercorso(String sorgente, String destinazione) {
		List<String> parziale = new ArrayList<>();
		
		parziale.add(sorgente);
		this.best = new ArrayList<>(parziale);
		
		trovaRiscorsivo(destinazione,parziale);
		return this.best;
	}

	private void trovaRiscorsivo(String destinazione, List<String> parziale) {

		//CASO TERMINALE? -> quando l'ultimo vertice inserito in parziale è uguale alla destinazione
		if(parziale.get(parziale.size() - 1).equals(destinazione)) {
			//percorso deve comprendere TUTTI i vertici ed essere di peso minimo
			if(parziale.size()==getNV() && getPesotot(parziale) < getPesotot(best)) {
				this.best = new ArrayList<>(parziale);
			}
			return;
		}
		
		//scorro i vicini dell'ultimo vertice inserito in parziale
		for(String vicino : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size() -1 ))) {
			//cammino aciclico -> controllo che il vertice non sia già in parziale
			if(!parziale.contains(vicino)) {
				//provo ad aggiungere
				parziale.add(vicino);
				//continuo la ricorsione
				this.trovaRiscorsivo(destinazione, parziale);
				//faccio backtracking
				parziale.remove(parziale.size() -1);
			}
		}
	}

	private int getPesotot(List<String> parziale) {
		int tot = 0;
		
		for(int i = 0; i<parziale.size() -1 ; i++) {
			DefaultWeightedEdge e = grafo.getEdge(parziale.get(i), parziale.get(i+1));
			
			tot+=grafo.getEdgeWeight(e);
		}
		return tot;
	}

}
