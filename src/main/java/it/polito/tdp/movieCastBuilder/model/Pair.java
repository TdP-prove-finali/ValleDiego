package it.polito.tdp.movieCastBuilder.model;

import java.util.Objects;

public class Pair {
	
	private Actor a1;
	private Actor a2;
	private int w;
	
	public Pair(Actor a1, Actor a2, Integer w) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.w = w;
	}

	public Actor getA1() {
		return a1;
	}

	public Actor getA2() {
		return a2;
	}

	public int getW() {
		return w;
	}

	@Override
	public int hashCode() {
		return Objects.hash(a1, a2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		return Objects.equals(a1, other.a1) && Objects.equals(a2, other.a2);
	}

	@Override
	public String toString() {
		return "Pair [a1=" + a1.getName() + ", a2=" + a2.getName() + ", w=" + w + "]\n";
	}
	
	
	

}
