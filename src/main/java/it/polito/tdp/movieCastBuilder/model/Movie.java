package it.polito.tdp.movieCastBuilder.model;

import java.util.List;

public class Movie {
	
	private String Poster;
	private String Title;
	private int releasedYear;
	private String Certificate;
	private String Runtime;
	private String Genre;
	private double ImdbRating;
	private String Overview;
	private String Director;
	private int MetaScore;
	private List<String> Cast;
	private int NumVotes;
	private int Gross;
	
	public Movie(String poster, String title, int releasedYear, String certificate, String runtime, String genre, double imdbRating,
			String overview, String director, int metaScore, List<String> cast, int numVotes, int gross) {
		super();
		Poster = poster;
		Title = title;
		this.releasedYear = releasedYear;
		Certificate = certificate;
		Runtime = runtime;
		Genre = genre;
		ImdbRating = imdbRating;
		Overview = overview;
		Director = director;
		MetaScore = metaScore;
		Cast = cast;
		NumVotes = numVotes;
		Gross = gross;
	}

	public String getPoster() {
		return Poster;
	}

	public String getTitle() {
		return Title;
	}

	public int getReleasedYear() {
		return releasedYear;
	}

	public String getCertificate() {
		return Certificate;
	}
	
	public String getRuntime() {
		return Runtime;
	}

	public String getGenre() {
		return Genre;
	}

	public double getImdbRating() {
		return ImdbRating;
	}

	public String getOverview() {
		return Overview;
	}

	public String getDirector() {
		return Director;
	}

	public int getMetaScore() {
		return MetaScore;
	}

	public List<String> getCast() {
		return Cast;
	}

	public int getNumVotes() {
		return NumVotes;
	}

	public int getGross() {
		return Gross;
	}
	
	

	public void setMetaScore(int metaScore) {
		MetaScore = metaScore;
	}

	public void setGross(int gross) {
		Gross = gross;
	}

	@Override
	public String toString() {
		return "Movie ["+/* Poster=" + Poster + ",*/  " Title = " + Title + ", releasedYear=" + releasedYear + ", Certificate="
				+ Certificate + ", Runtime ="
						+ Runtime + ", Genre=" + Genre + ", ImdbRating=" + ImdbRating + ", Overview=" + Overview
				+ ", Director=" + Director + ", MetaScore=" + MetaScore + ", Cast=" + Cast + ", NumVotes=" + NumVotes
				+ ", Gross=" + Gross + " /n ]\n";
	}
	
	
	
	
	
	
	
	

}
