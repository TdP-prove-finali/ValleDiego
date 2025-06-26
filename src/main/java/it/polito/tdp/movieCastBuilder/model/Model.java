package it.polito.tdp.movieCastBuilder.model;

import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.Levenshtein;
import it.polito.tdp.movieCastBuilder.db.imdbDAO;

public class Model {
	
	private imdbDAO dao;
	private List<String> lGenres = new LinkedList<>();
	private List<Movie> lMovies = new LinkedList<>();
	private List<String> lDirectors = new LinkedList<>();
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	private double minIMDBrating;
	private double maxIMDBrating;
	private int minVotes;
	private int maxVotes;
	private int minMetaScore;
	private int minGross;
	
	final double alfa = 0.5;
	final double beta = 0.5;
	
	public Model() {
		dao = new imdbDAO();
		this.lGenres = this.dao.getGenre();
		this.lMovies = this.dao.getMovies();
		this.minVotes = this.dao.getMinVotes();
		this.maxVotes = this.dao.getMaxVotes();
		this.minIMDBrating = this.dao.getMinIMDBrating(); 
		this.maxIMDBrating = this.dao.getMaxIMDBrating();
		this.minMetaScore = this.dao.getMinMetaScore();
		this.minGross = this.dao.getminGross();
		
	}

	public void prova() {
		// TODO Auto-generated method stub
	}

	public void getGenre() {
		// TODO Auto-generated method stub
		System.out.println("Genre: "+this.lGenres.size());
	}

	public void getDirector(String genre) {
		// TODO Auto-generated method stub
		this.lDirectors = this.dao.getDirector(genre);
		System.out.println("Director: "+lDirectors.size()+"\n ");
	}

	public void findCast(String genre, String director, int uno, int due, int tre, int quattro, int cinque, int sei) {//scegliere nomi delle caratteristiche
		// TODO Auto-generated method stub
		creaGrafo(genre);
		//Ricorda che mancano gli archi
		
		//Calcolo le statistiche con un doppio for annodato
		
		for(Actor a : this.grafo.vertexSet()) {
			for(Movie m : lMovies) {
				
				if((m.getCast()).contains(a.getName())) {
					
					if(m.getMetaScore() == 0)
						m.setMetaScore(minMetaScore);
					if(m.getGross() == 0) 
						m.setGross(minGross);
					
					a.setnMoviesTot(a.getnMoviesTot() + 1);
					a.setGrossTot(a.getGrossTot() + m.getGross());
					a.setMetaScoreTot(a.getMetaScoreTot() + m.getMetaScore());
					a.setImbdRatingTot(a.getImbdRatingTot() + ema(beta,normalization(m.getImdbRating(),maxIMDBrating,minIMDBrating),normalization(m.getNumVotes(),maxVotes,minVotes)));
					if(m.getDirector().equals(director)) {
						a.setnMoviesDirector(a.getnMoviesDirector() + 1);
					}
					if(m.getGenre().contains(genre)) {
						a.setnMoviesGenre(a.getnMoviesGenre() + 1);
					}
				}
			}
		}
			
		//Trovo i max e min per Genre e Director
		
		int maxDirector = 0;
		int maxGenre = 0;
		int minDirector = 1001;//valori troppo alti per essere min
		int minGenre = 1001;
		
		for(Actor a : this.grafo.vertexSet()) {
			if(a.getnMoviesDirector() > maxDirector) {
				maxDirector = (int) a.getnMoviesDirector();
			}else if(a.getnMoviesDirector() < minDirector) {
				minDirector = (int) a.getnMoviesDirector();
			}
			if(a.getnMoviesGenre() > maxGenre) {
				maxGenre = (int) a.getnMoviesGenre();
			}else if(a.getnMoviesGenre() < minGenre) {
				minGenre = (int) a.getnMoviesGenre();
			}
			
		}
		
		System.out.println("maxDirector = "+maxDirector);
		System.out.println("maxGenre = "+maxGenre);
		System.out.println("minDirector = "+minDirector);
		System.out.println("minGenre = "+minGenre+"\n");
		
		// Calcolo le caratteristiche
		
		double maxSintoniaAttori = 0;
		double maxIntesaRegista = 0;
		double maxIncassoFilm = 0;
		double maxApprovazioneCritica = 0;
		double maxApprovazionePubblico = 0;
		double maxAffinitaGenere = 0;
		
		double minSintoniaAttori = 99999999;
		double minIntesaRegista = 9999999;
		double minIncassoFilm = 99999999;
		double minApprovazioneCritica = 9999999;
		double minApprovazionePubblico = 9999999;
		double minAffinitaGenere = 9999999;
		
		for(Actor a : this.grafo.vertexSet()) {
			
			a.setIntesaRegista(ema(alfa,a.getnMoviesDirector()/a.getnMoviesTot(),normalization(a.getnMoviesDirector(),maxDirector,minDirector)));
			a.setAffinitaGenere(ema(alfa,a.getnMoviesGenre()/a.getnMoviesTot(),normalization(a.getnMoviesGenre(),maxGenre,minGenre)));
			a.setIncassoFilm(a.getGrossTot()/a.getnMoviesTot());
			a.setApprovazioneCritica(a.getMetaScoreTot()/a.getnMoviesTot());
			a.setApprovazionePubblico(a.getImbdRatingTot()/a.getnMoviesTot());
			
			if(a.getIntesaRegista() > maxIntesaRegista) {
				maxIntesaRegista = a.getIntesaRegista();
			}else if (a.getIntesaRegista() < minIntesaRegista) {
				minIntesaRegista = a.getIntesaRegista();
			}
			if(a.getAffinitaGenere() > maxAffinitaGenere) {
				maxAffinitaGenere = a.getAffinitaGenere();
			}else if (a.getAffinitaGenere() < minAffinitaGenere) {
				minAffinitaGenere = a.getAffinitaGenere();
			}
			if(a.getIncassoFilm() > maxIncassoFilm) {
				maxIncassoFilm = a.getIncassoFilm();
			}else if (a.getIncassoFilm() < minIncassoFilm) {
				minIncassoFilm = a.getIncassoFilm();
			}
			if(a.getApprovazioneCritica() > maxApprovazioneCritica) {
				maxApprovazioneCritica = a.getApprovazioneCritica();
			}else if (a.getApprovazioneCritica() < minApprovazioneCritica) {
				minApprovazioneCritica = a.getApprovazioneCritica();
			}
			if(a.getApprovazionePubblico() > maxApprovazionePubblico) {
				maxApprovazionePubblico = a.getApprovazionePubblico();
			}else if (a.getApprovazionePubblico() < minApprovazionePubblico) {
				minApprovazionePubblico = a.getApprovazionePubblico();
			}
			
				
		}
		
		System.out.println(this.grafo.vertexSet().toString());

		System.out.println("\nmaxIntesaRegista = "+maxIntesaRegista);
		System.out.println("minIntesaRegista = "+minIntesaRegista);
		System.out.println("maxAffinitaGenere = "+maxAffinitaGenere);
		System.out.println("minAffinitaGenere = "+minAffinitaGenere);
		System.out.println("maxIncassoFilm = "+maxIncassoFilm);
		System.out.println("minIncassoFilm = "+minIncassoFilm);
		System.out.println("maxApprovazioneCritica = "+maxApprovazioneCritica);
		System.out.println("minApprovazioneCritica = "+minApprovazioneCritica);
		System.out.println("maxApprovazionePubblico = "+maxApprovazionePubblico);
		System.out.println("minApprovazionePubblico = "+minApprovazionePubblico);
		/*
		for (Actor a : this.grafo.vertexSet()) {
		    if (a.getIntesaRegista()>0) {
		        System.out.println(a);
		    }
		}*/

	}

	private double normalization(double v, double vMax, double vMin) {
		// TODO Auto-generated method stub
		double result = 0;
		
		result = (v - vMin)/(vMax - vMin);
		
		return result;
	}

	private double ema(double peso, double v1, double v2) {// Exponential Moving Average
		// TODO Auto-generated method stub
		double result = 0;
		
		result = peso * v1 + (1 - peso) * v2;
		
		return result;
	}

	private void creaGrafo(String genre) {
		// TODO Auto-generated method stub
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(genre));
		System.out.println("Attori: "+ this.grafo.vertexSet().size());
		
		//aggiungo archi
		
		
	}
}
