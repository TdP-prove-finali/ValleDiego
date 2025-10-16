package it.polito.tdp.movieCastBuilder.model;

import java.util.Objects;

public class Actor {//ho aggiunto un dataset con informazioni sugli attori, quali data di nascita e morte, prosfessione e titolo del film per cui sono famosi
	
	private String name;
	private int birthYear; //non includo chi ha valore in deathyear
	private String profession;
	
	private String knowForTitle;//la uso solo per i controlli degli omonimi
	private double jw;// serve per il controllo omonimi
	
	private double nMoviesTot;
	private double nMoviesDirector;
	private double nMoviesGenre;
	private double grossTot; //evita overflow con double
	private double metaScoreTot;
	private double imbdRatingTot;
	
	private double intesaRegista;
	private double incassoFilm;
	private double approvazioneCritica;
	private double approvazionePubblico;
	private double affinitaGenere;
	
	private double statActor; //serve per prendere i 200 migliori
	
	public Actor(String name, int birthYear, String profession, String knowForTitle, double jw, double nMoviesTot, double nMoviesDirector,
			double nMoviesGenre, double grossTot, double metaScoreTot, double imbdRatingTot, double sintoniaAttori,
			double intesaRegista, double incassoFilm, double approvazioneCritica, double approvazionePubblico,
			double affinitaGenere, double statActor) {
		super();
		
		this.name = name;
		this.birthYear = birthYear;
		this.profession = profession;
		this.knowForTitle = knowForTitle;
		this.jw = jw;
		this.nMoviesTot = nMoviesTot;
		this.nMoviesDirector = nMoviesDirector;
		this.nMoviesGenre = nMoviesGenre;
		this.grossTot = grossTot;
		this.metaScoreTot = metaScoreTot;
		this.imbdRatingTot = imbdRatingTot;
		this.intesaRegista = intesaRegista;
		this.incassoFilm = incassoFilm;
		this.approvazioneCritica = approvazioneCritica;
		this.approvazionePubblico = approvazionePubblico;
		this.affinitaGenere = affinitaGenere;
		this.statActor = statActor;
	}

	public double getnMoviesTot() {
		return nMoviesTot;
	}

	public void setnMoviesTot(double d) {
		this.nMoviesTot = d;
	}

	public double getnMoviesDirector() {
		return nMoviesDirector;
	}

	public void setnMoviesDirector(double nMoviesDirector) {
		this.nMoviesDirector = nMoviesDirector;
	}

	public double getnMoviesGenre() {
		return nMoviesGenre;
	}

	public void setnMoviesGenre(double nMoviesGenre) {
		this.nMoviesGenre = nMoviesGenre;
	}

	public double getGrossTot() {
		return grossTot;
	}

	public void setGrossTot(double d) {
		this.grossTot = d;
	}

	public double getMetaScoreTot() {
		return metaScoreTot;
	}

	public void setMetaScoreTot(double metaScoreTot) {
		this.metaScoreTot = metaScoreTot;
	}

	public double getImbdRatingTot() {
		return imbdRatingTot;
	}

	public void setImbdRatingTot(double imbdRatingTot) {
		this.imbdRatingTot = imbdRatingTot;
	}

	public String getName() {
		return name;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public String getProfession() {
		return profession;
	}

	public double getIntesaRegista() {
		return intesaRegista;
	}

	public void setIntesaRegista(double intesaRegista) {
		this.intesaRegista = intesaRegista;
	}

	public double getIncassoFilm() {
		return incassoFilm;
	}

	public void setIncassoFilm(double incassoFilm) {
		this.incassoFilm = incassoFilm;
	}

	public double getApprovazioneCritica() {
		return approvazioneCritica;
	}

	public void setApprovazioneCritica(double approvazioneCritica) {
		this.approvazioneCritica = approvazioneCritica;
	}

	public double getApprovazionePubblico() {
		return approvazionePubblico;
	}

	public void setApprovazionePubblico(double approvazionePubblico) {
		this.approvazionePubblico = approvazionePubblico;
	}

	public double getAffinitaGenere() {
		return affinitaGenere;
	}

	public void setAffinitaGenere(double affinitaGenere) {
		this.affinitaGenere = affinitaGenere;
	}

	public String getKnowForTitle() {
		return knowForTitle;
	}

	public double getJw() {
		return jw;
	}

	public void setJw(double jw) {
		this.jw = jw;
	}
	
	public double getStatActor() {
		return statActor;
	}

	public void setStatActor(double statActor) {
		this.statActor = statActor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actor other = (Actor) obj;
		return Objects.equals(name, other.name);
	}
	//Utile per debug
//	@Override 
//	public String toString() {
//		return "Actor [name=" + name + ", birthYear=" + birthYear + ", profession="
//				+ profession +  ", nMoviesTot=" + nMoviesTot + ", nMoviesDirector=" + nMoviesDirector + ", nMoviesGenre="
//				+ nMoviesGenre + ", grossTot=" + grossTot + ", metaScoreTot=" + metaScoreTot + ", imbdRatingTot="
//				+ imbdRatingTot + ", intesaRegista=" + intesaRegista
//				+ ", incassoFilm=" + incassoFilm + ", approvazioneCritica=" + approvazioneCritica
//				+ ", approvazionePubblico=" + approvazionePubblico + ", affinitaGenere=" + affinitaGenere + ", statActor=" + statActor + "] +\n";
//	}
	
	public String toString() {
		return name; 
	}

	
	
}
	
	