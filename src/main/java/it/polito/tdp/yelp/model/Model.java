package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private Graph<User, DefaultWeightedEdge> grafo;
	//elenco di vertici
	List<User> utenti;
	private YelpDao dao;
	public Model() {
		dao= new YelpDao();
		
	}
	
	
	public String creaGrafo(int minRevisioni, int anno) {
		this.grafo= new SimpleWeightedGraph<User, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		utenti= dao.getVertici(minRevisioni);
		Graphs.addAllVertices(this.grafo, utenti);
		for (User u1: this.utenti) {
			for (User u2: this.utenti) {
				if (!u1.equals(u2) && u1.getUserId().compareTo(u2.getUserId())<0) {
					int sim= dao.calcolaSimilarita(u1, u2, anno);
					if (sim> 0) {
						Graphs.addEdge(this.grafo, u1, u2, sim);
					}
				}
			}
		}
		return "# VERTICI: "+this.grafo.vertexSet().size()+"\n"+"# ARCHI: "+this.grafo.edgeSet().size();
//		System.out.println("Grafo creato!");
//		System.out.println("# VERTICI: "+this.grafo.vertexSet().size());
//		System.out.println("# ARCHI: "+this.grafo.edgeSet().size());
//		
		
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
		
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	
	}
	
	
	public List<User> getUtenti()  {
		return this.utenti;
	}
	
	
	public List<User> calcolaUtentiSimili(User u) {
		int max=0;
		for (DefaultWeightedEdge e: this.grafo.edgesOf(u)) {  //edgesOf mi da gli archi collegati ad u
			if (this.grafo.getEdgeWeight(e)>max) {
				max= (int )this.grafo.getEdgeWeight(e);
				
			}
			
		}
		
		List<User> result = new ArrayList<User>();
		for (DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if (this.grafo.getEdgeWeight(e)==max)  {//prendo uno dei vertici collegato all'arco, non u
				User u2= Graphs.getOppositeVertex(this.grafo, e, u); //trova opposto
				result.add(u2);
			}
		}
		Collections.sort(result);
		return result;
		
			
	}
	
}
