package it.polito.tdp.movieCastBuilder.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.movieCastBuilder.db.imdbDAO;

public class Model {
	
	private imdbDAO dao;
	private List<String> lGenres = new LinkedList<>();
	private List<Movie> lMovies = new LinkedList<>();
	private List<String> lDirectors = new LinkedList<>();
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Pair, Integer> mPair = new HashMap<>();
	private List<Actor> poolActors = new LinkedList<>();
	private Cast bestCast = new Cast (null,null,null,null,0,0,0,0,0,0,0);
	
	private PriorityQueue<Actor> pq = new PriorityQueue<>(Comparator.comparingDouble(Actor::getStatActor));
	
	private double minIMDBrating;
	private double maxIMDBrating;
	private int minVotes;
	private int maxVotes;
	private int minMetaScore;
	private int minGross;
	
	final double alfa = 0.5;
	final double beta = 0.5;
	final int limitActors = 180;//180 è il valore di compromesso tra riusltato ottimale e tempi di esecuzione non eterni
	
	double maxSintoniaAttori = 0;
	double maxIntesaRegista = 0;
	double maxIncassoFilm = 0;
	double maxApprovazioneCritica = 0;
	double maxApprovazionePubblico = 0;
	double maxAffinitaGenere = 0;
	double maxStatCast = 0; 
	
	double minSintoniaAttori = 99999999;
	double minIntesaRegista = 9999999;
	double minIncassoFilm = 99999999;
	double minApprovazioneCritica = 9999999;
	double minApprovazionePubblico = 9999999;
	double minAffinitaGenere = 9999999;
	
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
		System.out.println("Genre: "+this.lGenres.size()+lGenres);
	}

	public void getDirector(String genre) {
		// TODO Auto-generated method stub
		this.lDirectors = this.dao.getDirector(genre);
		System.out.println("\nDirector: "+lDirectors.size()+"\n ");
	}

	public Cast findCast(String genre, String director, double inputIncassoFilm, double inputApprovazioneCritica, double inputApprovazionePubblico, double inputSintoniaAttori, double inputIntesaRegista, double inputAffinitaGenere) {
		// TODO Auto-generated method stub
		creaGrafo(genre);

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
		int minDirector = 1001;//valori scelti altissimi per inizializzare la variabile
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
		
		//System.out.println(this.grafo.vertexSet().toString());

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
		
		//Normalizzo le caratteristiche e applico le preferenze dell'utente
		
		for (Actor a : this.grafo.vertexSet()) {
		    a.setIncassoFilm(normalization(a.getIncassoFilm(), maxIncassoFilm, minIncassoFilm) * inputIncassoFilm);
		    a.setApprovazioneCritica(normalization(a.getApprovazioneCritica(), maxApprovazioneCritica, minApprovazioneCritica) * inputApprovazioneCritica);
		    a.setApprovazionePubblico(normalization(a.getApprovazionePubblico(), maxApprovazionePubblico, minApprovazionePubblico) * inputApprovazionePubblico);
		    a.setIntesaRegista(normalization(a.getIntesaRegista(), maxIntesaRegista, minIntesaRegista) * inputIntesaRegista);
		    a.setAffinitaGenere(normalization(a.getAffinitaGenere(), maxAffinitaGenere, minAffinitaGenere) * inputAffinitaGenere);
		    a.setStatActor(a.getIncassoFilm() + a.getApprovazioneCritica() + a.getApprovazionePubblico() + a.getIntesaRegista() + a.getAffinitaGenere());
		}
		
		//Seleziono i "limitActors" attori migliori
		
		poolActors.clear();
		
		if(this.grafo.vertexSet().size() > limitActors) {
			//pq.addAll(this.grafo.vertexSet());
			
			for(Actor a : this.grafo.vertexSet()) {
				
				if(poolActors.size() == limitActors ) {
					if(a.getStatActor() > pq.peek().getStatActor()) {
						Actor r = pq.poll();
						poolActors.remove(r);
						
						poolActors.add(a);
						pq.add(a);
						
						//System.out.println(r.getStatActor());
					}
					
				}else {
					poolActors.add(a);
					pq.add(a);
				}
			}
		}else {
			poolActors.addAll(this.grafo.vertexSet());
		}
		//System.out.println("Primo escluso: " + pq.peek().getStatActor() + "\n" + pq.peek());
		
		//trovo i massimi e minimi per la sesta caratteristica
		
		List<Actor> parziale = new LinkedList<>();
		List<Actor> rimanenti = new ArrayList<>(poolActors);
		
		//Provo per velocizzare, creo un set di Pairs per non controllare il grafo durante la ricorsione. Noto che le tempistiche sono uguali

		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
		    Actor a1 = this.grafo.getEdgeSource(e);
		    Actor a2 = this.grafo.getEdgeTarget(e);
		    int peso = (int) this.grafo.getEdgeWeight(e);

		    Pair p = new Pair(a1, a2, peso);
		    mPair.put(p, p.getW());
		    
		    if(p.getW() != 1) {//solo per debug output
		    	System.out.println(p);
		    }
		}
		
		Cast c = new Cast(null,null,null,null,0,0,0,0,0,0,0);

		//Parte la prima ricorsione
		
		long startTime = System.currentTimeMillis();
		//cerca(c, rimanenti, 0);  //tentativo che controlla mappe al posto del grafo, da eliminare probabilmente
		cerca2(c, rimanenti, 0);
		
		long endTime = System.currentTimeMillis();
		System.out.println("Tempo impiegato per max e min : " + (endTime - startTime) + " ms");
		
		System.out.println("maxSintoniaAttori = "+maxSintoniaAttori+"\nminSintoniaAttori = "+minSintoniaAttori);
		
		//Ricorsione finale
		
		long startTime2 = System.currentTimeMillis();
		maxStatCast = 0; 
		bestCast = cercaCast(c, rimanenti, 0, inputSintoniaAttori);
		 
		long endTime2 = System.currentTimeMillis();
		System.out.println("Tempo impiegato per max e min : " + (endTime - startTime) + " ms");
		System.out.println("\nTempo impiegato Cast: " + (endTime2 - startTime2) + " ms");
		System.out.println("maxStatCast = "+maxStatCast);
		System.out.println("maxSintoniaAttori = "+maxSintoniaAttori+"\nminSintoniaAttori = "+minSintoniaAttori);
		return bestCast;
		
	}	
		
	private Cast cercaCast(Cast c, List<Actor> rimanenti, int start, double inputSintoniaAttori) {
		// TODO Auto-generated method stub
			
		//condizione di terminazione
		
		if(c.getA4() != null) {
			
			c.setIncassoFilm((c.getA1().getIncassoFilm() + c.getA2().getIncassoFilm() + c.getA3().getIncassoFilm() + c.getA4().getIncassoFilm())/4);
			c.setApprovazioneCritica((c.getA1().getApprovazioneCritica() + c.getA2().getApprovazioneCritica() + c.getA3().getApprovazioneCritica() + c.getA4().getApprovazioneCritica())/4);
			c.setApprovazionePubblico((c.getA1().getApprovazionePubblico() + c.getA2().getApprovazionePubblico() + c.getA3().getApprovazionePubblico() + c.getA4().getApprovazionePubblico())/4);
			c.setIntesaRegista((c.getA1().getIntesaRegista() + c.getA2().getIntesaRegista() + c.getA3().getIntesaRegista() + c.getA4().getIntesaRegista())/4);
			c.setAffinitaGenere((c.getA1().getAffinitaGenere() + c.getA2().getAffinitaGenere() + c.getA3().getAffinitaGenere() + c.getA4().getAffinitaGenere())/4);
			
			c.setSintoniaAttori(normalization(c.getSintoniaAttori(), maxSintoniaAttori, minSintoniaAttori) * inputSintoniaAttori);
			c.setStatCast( c.getIncassoFilm() + c.getApprovazioneCritica() + c.getApprovazionePubblico() + c.getIntesaRegista() + c.getAffinitaGenere() + c.getSintoniaAttori());
			
			//controllo se è maxStatCast
					
			if(c.getStatCast() >= maxStatCast) {
				maxStatCast = c.getStatCast();
				bestCast.setA1(c.getA1());
				bestCast.setA2(c.getA2());
				bestCast.setA3(c.getA3());
				bestCast.setA4(c.getA4());
				bestCast.setIncassoFilm(c.getIncassoFilm());
				bestCast.setApprovazioneCritica(c.getApprovazioneCritica());
				bestCast.setApprovazionePubblico(c.getApprovazionePubblico());
				bestCast.setIntesaRegista(c.getIntesaRegista());
				bestCast.setAffinitaGenere(c.getAffinitaGenere());
				bestCast.setSintoniaAttori(c.getSintoniaAttori());
				bestCast.setStatCast(c.getStatCast());
				
				System.out.println(bestCast);
			}
					
			return null;
					
		}else if(c.getA3() != null) {
					
			for(int i = start; i< rimanenti.size(); i++) {
						
				c.setA4(rimanenti.get(i));
				double backup = c.getSintoniaAttori();
						
				if(this.grafo.getEdge(c.getA1(), c.getA4()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA1(), c.getA4())));
				}
				if(this.grafo.getEdge(c.getA4(), c.getA2()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA4(), c.getA2())));
				}
				if(this.grafo.getEdge(c.getA3(), c.getA4()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA3(), c.getA4())));
				}
						
				cercaCast(c, rimanenti, i + 1, inputSintoniaAttori);
				c.setA4(null);
				c.setSintoniaAttori(backup);
			}
					
		}else if(c.getA2() != null) {
					
			for(int i = start; i< rimanenti.size(); i++) {
						
				c.setA3(rimanenti.get(i));
				double backup = c.getSintoniaAttori();
						
				if(this.grafo.getEdge(c.getA1(), c.getA3()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA1(), c.getA3())));
				}
				if(this.grafo.getEdge(c.getA3(), c.getA2()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA3(), c.getA2())));
				}
						
				cercaCast(c, rimanenti, i + 1, inputSintoniaAttori);
				c.setA3(null);
				c.setSintoniaAttori(backup);
			}
					
		}else if(c.getA1() != null) {
					
			for(int i = start; i< rimanenti.size(); i++) {
						
				c.setA2(rimanenti.get(i));
				double backup = c.getSintoniaAttori();
						
				if(this.grafo.getEdge(c.getA1(), c.getA2()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA1(), c.getA2())));
				}
						
				cercaCast(c, rimanenti, i + 1, inputSintoniaAttori);
				c.setA2(null);
				c.setSintoniaAttori(backup);
			}
					
		}else {
					
			for(int i = start; i< rimanenti.size(); i++) {
						
				c.setA1(rimanenti.get(i));
				cercaCast(c, rimanenti, i + 1, inputSintoniaAttori);
				c.setA1(null);
			}
		}			
		
		return bestCast;
	}

	private void cerca2(Cast c, List<Actor> rimanenti, int start) {
		// TODO Auto-generated method stub
		
		//condizione di terminazione
		
		if(c.getA4() != null) {
			
			//controllo se sono i risultati min o max
			
			if(c.getSintoniaAttori() < minSintoniaAttori) {
				minSintoniaAttori = c.getSintoniaAttori();
			}else if(c.getSintoniaAttori() > maxSintoniaAttori) {
				maxSintoniaAttori = c.getSintoniaAttori();
				System.out.println(c.toString());
			}
			
			return;
			
		}else if(c.getA3() != null) {
			
			for(int i = start; i< rimanenti.size(); i++) {
				
				c.setA4(rimanenti.get(i));
				double backup = c.getSintoniaAttori();
				
				if(this.grafo.getEdge(c.getA1(), c.getA4()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA1(), c.getA4())));
				}
				if(this.grafo.getEdge(c.getA4(), c.getA2()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA4(), c.getA2())));
				}
				if(this.grafo.getEdge(c.getA3(), c.getA4()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA3(), c.getA4())));
				}
				
				cerca2(c, rimanenti, i + 1);
				c.setA4(null);
				c.setSintoniaAttori(backup);
			}
			
		}else if(c.getA2() != null) {
			
			for(int i = start; i< rimanenti.size(); i++) {
				
				c.setA3(rimanenti.get(i));
				double backup = c.getSintoniaAttori();
				
				if(this.grafo.getEdge(c.getA1(), c.getA3()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA1(), c.getA3())));
				}
				if(this.grafo.getEdge(c.getA3(), c.getA2()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA3(), c.getA2())));
				}
				
				cerca2(c, rimanenti, i + 1);
				c.setA3(null);
				c.setSintoniaAttori(backup);
			}
			
		}else if(c.getA1() != null) {
			
			for(int i = start; i< rimanenti.size(); i++) {
				
				c.setA2(rimanenti.get(i));
				double backup = c.getSintoniaAttori();
				
				if(this.grafo.getEdge(c.getA1(), c.getA2()) != null) {
					c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA1(), c.getA2())));
				}
				
				cerca2(c, rimanenti, i + 1);
				c.setA2(null);
				c.setSintoniaAttori(backup);
			}
			
		}else {
			
			for(int i = start; i< rimanenti.size(); i++) {
				
				c.setA1(rimanenti.get(i));
				cerca2(c, rimanenti, i + 1);
				c.setA1(null);
			}
		}
		
	}
	
	// Il metodo "cerca" qui sotto è una prova in cui ho usato una mappa invece di controllare il grafo, sperando di risparmiare tempo.
	// Purtroppo impiega lo stesso identico tempo, forse per la creazione di troppe nuove Pair. Se non è migliorabile, verrà eliminato


	private void cerca(Cast c, List<Actor> rimanenti, int start) {
		// TODO Auto-generated method stub
			
		//condizione di terminazione
		
				if(c.getA4() != null) {
					
					//controllo se sono i risultati min o max
					
					if(c.getSintoniaAttori() < minSintoniaAttori) {
						minSintoniaAttori = c.getSintoniaAttori();
					}else if(c.getSintoniaAttori() > maxSintoniaAttori) {
						maxSintoniaAttori = c.getSintoniaAttori();
						System.out.println(c.toString());
					}
					
					return;
					
				}else if(c.getA3() != null) {
					
					for(int i = start; i< rimanenti.size(); i++) {
						
						c.setA4(rimanenti.get(i));
						double backup = c.getSintoniaAttori();

						if(mPair.get(new Pair(c.getA1(), c.getA4(),0)) != null) {
							c.setSintoniaAttori(c.getSintoniaAttori() + mPair.get(new Pair(c.getA1(), c.getA4(),0)));
						}
						if(mPair.get(new Pair(c.getA2(), c.getA4(),0)) != null) {
							c.setSintoniaAttori(c.getSintoniaAttori() + mPair.get(new Pair(c.getA2(), c.getA4(),0)));
						}
						if(mPair.get(new Pair(c.getA3(), c.getA4(),0)) != null) {
							c.setSintoniaAttori(c.getSintoniaAttori() + mPair.get(new Pair(c.getA3(), c.getA4(),0)));
						}
						
						cerca2(c, rimanenti, i + 1);
						c.setA4(null);
						c.setSintoniaAttori(backup);
					}
					
				}else if(c.getA2() != null) {
					
					for(int i = start; i< rimanenti.size(); i++) {
						
						c.setA3(rimanenti.get(i));
						double backup = c.getSintoniaAttori();
						
						if(mPair.get(new Pair(c.getA1(), c.getA3(),0)) != null) {
							c.setSintoniaAttori(c.getSintoniaAttori() + mPair.get(new Pair(c.getA1(), c.getA3(),0)));
						}
						if(mPair.get(new Pair(c.getA3(), c.getA2(),0)) != null) {
							c.setSintoniaAttori(c.getSintoniaAttori() + mPair.get(new Pair(c.getA3(), c.getA2(),0)));
						}
						
						cerca2(c, rimanenti, i + 1);
						c.setA3(null);
						c.setSintoniaAttori(backup);
					}
					
				}else if(c.getA1() != null) {
					
					for(int i = start; i< rimanenti.size(); i++) {
						
						c.setA2(rimanenti.get(i));
						double backup = c.getSintoniaAttori();
						
						if(mPair.get(new Pair(c.getA1(), c.getA2(),0)) != null) {
							c.setSintoniaAttori(c.getSintoniaAttori() + mPair.get(new Pair(c.getA1(), c.getA2(),0)));
						}
						
						cerca2(c, rimanenti, i + 1);
						c.setA2(null);
						c.setSintoniaAttori(backup);
					}
					
				}else {
					
					for(int i = start; i< rimanenti.size(); i++) {
						
						c.setA1(rimanenti.get(i));
						cerca2(c, rimanenti, i + 1);
						c.setA1(null);
					}
				}
		
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
		for(Pair p : this.dao.getArchi(this.grafo.vertexSet())) {
			Graphs.addEdgeWithVertices(this.grafo, p.getA1(), p.getA2(), p.getW());
		}
		System.out.println("Archi: "+ this.grafo.edgeSet().size());
		
		
	}

	public List<Movie> getMoviesActor(Actor a) {
		// TODO Auto-generated method stub
		List<Movie> result = new LinkedList<>();
		
		for(Movie m : lMovies) {
			if(m.getCast().contains(a.getName())) {
				result.add(m);
			}
		}
		System.out.println(result);
		return result;
	}

	public Cast replace(Cast oldCast, Actor rimosso, double inputSintoniaAttori) {
		// TODO Auto-generated method stub
		Cast newCast;
		List<Actor> parziale = new LinkedList<>();
		
		if(!oldCast.getA1().equals(rimosso)) {
			parziale.add(oldCast.getA1());
		}
		if(!oldCast.getA2().equals(rimosso)) {
			parziale.add(oldCast.getA2());
		}
		if(!oldCast.getA3().equals(rimosso)) {
			parziale.add(oldCast.getA3());
		}
		if(!oldCast.getA4().equals(rimosso)) {
			parziale.add(oldCast.getA4());
		}
		
		newCast = new Cast(parziale.get(0), parziale.get(1), parziale.get(2), null, 0,0,0,0,0,0,0);
		maxStatCast = 0;
		
		for(Actor a : this.grafo.vertexSet()) {
			
			if(!a.equals(rimosso) && !newCast.getA1().equals(a) && !newCast.getA2().equals(a) && !newCast.getA3().equals(a)) {
			
				newCast.setA4(a);
				newCast.setSintoniaAttori(0);
			
				calculateSintoniaAttori(newCast);
				calculateCastStat(newCast, inputSintoniaAttori);
			
				if(newCast.getStatCast() >= maxStatCast) {
					maxStatCast = newCast.getStatCast();
					bestCast = new Cast(newCast.getA1(), newCast.getA2(), newCast.getA3(), newCast.getA4(), newCast.getIncassoFilm(), newCast.getApprovazioneCritica(), newCast.getApprovazionePubblico(), newCast.getIntesaRegista(), newCast.getIntesaRegista(), newCast.getSintoniaAttori(), newCast.getStatCast());
					System.out.println(newCast);

				}
			}
		}
		System.out.println("\nFine");
		return bestCast;
	}

	private void calculateSintoniaAttori(Cast c) {//inizialmente queste funzioni venivano chiamate svariate volte nelle ricorsioni, ma ho poi notato che ricopiando il codice senza chiamare la fuzione diminuiva drasticamente i tempi di esecuzione. Adesso solo più Replace() le chiama
		// TODO Auto-generated method stub
		if(this.grafo.getEdge(c.getA1(), c.getA2()) != null) {
			c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA1(), c.getA2())));
		}
		if(this.grafo.getEdge(c.getA1(), c.getA3()) != null) {
			c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA1(), c.getA3())));
		}
		if(this.grafo.getEdge(c.getA1(), c.getA4()) != null) {
			c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA1(), c.getA4())));
		}
		if(this.grafo.getEdge(c.getA3(), c.getA2()) != null) {
			c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA3(), c.getA2())));
		}
		if(this.grafo.getEdge(c.getA4(), c.getA2()) != null) {
			c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA4(), c.getA2())));
		}
		if(this.grafo.getEdge(c.getA3(), c.getA4()) != null) {
			c.setSintoniaAttori(c.getSintoniaAttori() + this.grafo.getEdgeWeight(this.grafo.getEdge(c.getA3(), c.getA4())));
		}
	}

	private void calculateCastStat(Cast c, double inputSintoniaAttori) {
		// TODO Auto-generated method stub
		
		c.setIncassoFilm((c.getA1().getIncassoFilm() + c.getA2().getIncassoFilm() + c.getA3().getIncassoFilm() + c.getA4().getIncassoFilm())/4);
		c.setApprovazioneCritica((c.getA1().getApprovazioneCritica() + c.getA2().getApprovazioneCritica() + c.getA3().getApprovazioneCritica() + c.getA4().getApprovazioneCritica())/4);
		c.setApprovazionePubblico((c.getA1().getApprovazionePubblico() + c.getA2().getApprovazionePubblico() + c.getA3().getApprovazionePubblico() + c.getA4().getApprovazionePubblico())/4);
		c.setIntesaRegista((c.getA1().getIntesaRegista() + c.getA2().getIntesaRegista() + c.getA3().getIntesaRegista() + c.getA4().getIntesaRegista())/4);
		c.setAffinitaGenere((c.getA1().getAffinitaGenere() + c.getA2().getAffinitaGenere() + c.getA3().getAffinitaGenere() + c.getA4().getAffinitaGenere())/4);
		
		c.setSintoniaAttori(normalization(c.getSintoniaAttori(), maxSintoniaAttori, minSintoniaAttori) * inputSintoniaAttori);
		c.setStatCast( c.getIncassoFilm() + c.getApprovazioneCritica() + c.getApprovazionePubblico() + c.getIntesaRegista() + c.getAffinitaGenere() + c.getSintoniaAttori());
	}
}
