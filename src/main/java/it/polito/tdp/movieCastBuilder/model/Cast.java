package it.polito.tdp.movieCastBuilder.model;

import java.util.Objects;

public class Cast {
	
	private Actor a1;
	private Actor a2;
	private Actor a3;
	private Actor a4;
	
	private double incassoFilm;
	private double approvazioneCritica;
	private double approvazionePubblico;
	private double intesaRegista;
	private double affinitaGenere;
	
	private double sintoniaAttori;
	private double statCast;
	
	public Cast(Actor a1, Actor a2, Actor a3, Actor a4, double incassoFilm, double approvazioneCritica,
			double approvazionePubblico, double intesaRegista, double affinitaGenere, double sintoniaAttori,
			double statCast) {

		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
		this.a4 = a4;
		this.incassoFilm = incassoFilm;
		this.approvazioneCritica = approvazioneCritica;
		this.approvazionePubblico = approvazionePubblico;
		this.intesaRegista = intesaRegista;
		this.affinitaGenere = affinitaGenere;
		this.sintoniaAttori = sintoniaAttori;
		this.statCast = statCast;
	}



	public double getSintoniaAttori() {
		return sintoniaAttori;
	}



	public void setSintoniaAttori(double sintoniaAttori) {
		this.sintoniaAttori = sintoniaAttori;
	}



	public Actor getA1() {
		return a1;
	}



	public Actor getA2() {
		return a2;
	}



	public Actor getA3() {
		return a3;
	}



	public Actor getA4() {
		return a4;
	}



	public double getStatCast() {
		return statCast;
	}



	public void setStatCast(double statCast) {
		this.statCast = statCast;
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



	public double getIntesaRegista() {
		return intesaRegista;
	}



	public void setIntesaRegista(double intesaRegista) {
		this.intesaRegista = intesaRegista;
	}



	public double getAffinitaGenere() {
		return affinitaGenere;
	}


	
	public void setAffinitaGenere(double affinitaGenere) {
		this.affinitaGenere = affinitaGenere;
	}



	public void setA4(Actor a4) {
		this.a4 = a4;
	}
	
	



	public void setA1(Actor a1) {
		this.a1 = a1;
	}



	public void setA2(Actor a2) {
		this.a2 = a2;
	}



	public void setA3(Actor a3) {
		this.a3 = a3;
	}



	@Override
	public int hashCode() {
		return Objects.hash(a1, a2, a3, a4);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cast other = (Cast) obj;
		return Objects.equals(a1, other.a1) && Objects.equals(a2, other.a2) && Objects.equals(a3, other.a3)
				&& Objects.equals(a4, other.a4);
	}



	@Override
	public String toString() {
		return "Cast [a1=" + a1 + ", a2=" + a2 + ", a3=" + a3 + ", a4=" + a4 + ", incassoFilm=" + incassoFilm
				+ ", approvazioneCritica=" + approvazioneCritica + ", approvazionePubblico=" + approvazionePubblico
				+ ", intesaRegista=" + intesaRegista + ", affinitaGenere=" + affinitaGenere + ", sintoniaAttori="
				+ sintoniaAttori + ",\n statCast=" + statCast + "]";
	}



	
	
	

}
