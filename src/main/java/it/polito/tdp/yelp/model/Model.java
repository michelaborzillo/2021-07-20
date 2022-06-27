package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	YelpDao dao;
	Graph<User, DefaultWeightedEdge> grafo;
	List<User> user;
	public Model () {
		dao= new YelpDao();
	}
	
	public void creaGrafo(int anno, int recensioni) {
		this.grafo= new SimpleWeightedGraph<User, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		user= dao.getVertici(recensioni);
		Graphs.addAllVertices(this.grafo, user);
		for (User u1: this.user) {
			for (User u2: this.user) {
				if (!u1.equals(u2) && u1.getUserId().compareTo(u2.getUserId())<0) {
					int peso= dao.getPeso(anno, u1, u2);
					if ( peso > 0) {
						Graphs.addEdge(this.grafo, u1, u2, peso);
					}
				}
			}
		}
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
		
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	
	}
	
	public Set<User> getVertici() {
		return this.grafo.vertexSet();
	}
	
	
//	public List<User> getSimili(User u) {
//		int max=0;
//		for (DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
//			if (this.grafo.getEdgeWeight(e)>max) {
//				max=(int) this.grafo.getEdgeWeight(e);
//			}
//		}
//		List<User> result= new ArrayList<>();
//		for (DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
//			if (this.grafo.getEdgeWeight(e)==max) {
//				User u2= Graphs.getOppositeVertex(this.grafo, e, u);
//				result.add(u2);
//		}
//			
//		
//		
//	}
//		Collections.sort(result);
//		return result;
//	}
	public List<UtentiSimili> getUtentiSimili (User u) {
		int max=0;
		List<UtentiSimili> simili= new ArrayList<UtentiSimili>();
		
		for (DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if (this.grafo.getEdgeWeight(e)>max) {
				max=(int) this.grafo.getEdgeWeight(e);
			}
		}
		List<User> result= new ArrayList<>();
		for (DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if (this.grafo.getEdgeWeight(e)==max) {
				User u2= Graphs.getOppositeVertex(this.grafo, e, u);
				result.add(u2);
		}
	}
		for (User uu: result) {
			simili.add(new UtentiSimili(uu,this.grafo.getEdgeWeight(this.grafo.getEdge(u,uu))));
		}
		return simili;
	}
}
