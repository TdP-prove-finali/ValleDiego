package it.polito.tdp.movieCastBuilder.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import info.debatty.java.stringsimilarity.JaroWinkler;
import it.polito.tdp.movieCastBuilder.model.Actor;
import it.polito.tdp.movieCastBuilder.model.Movie;
import it.polito.tdp.movieCastBuilder.model.Pair;



public class imdbDAO {

	public List<String> getGenre() {
		// TODO Auto-generated method stub
		String sql = "SELECT DISTINCT TRIM(value) AS genre\r "
				+ "FROM (\r "
				+ "    SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(genre, ',', numbers.n), ',', -1) AS value\r "
				+ "    FROM movies\r "
				+ "    JOIN (\r "
				+ "        SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 \r "
				+ "    ) numbers ON CHAR_LENGTH(genre) - CHAR_LENGTH(REPLACE(genre, ',', '')) >= numbers.n - 1\r "
				+ ") subquery\r "
				+ "ORDER BY genre;";
		
		List<String> lGenre = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				lGenre.add(res.getString("genre"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println(lGenre.size());
		return lGenre;
	}

	public List<String> getDirector(String genre) {
		// TODO Auto-generated method stub
		String sql = "SELECT DISTINCT m.director\r "
				+ "FROM movies m\r "
				+ "JOIN actor a ON m.director = a.primaryName\r "
				+ "WHERE FIND_IN_SET(?, REPLACE(m.genre, ', ', ',')) \r "
				+ "  AND a.deathYear = 0\r "
				+ "ORDER BY m.director;\r "
				+ "\r "
				+ "";
		List<String> lDirector = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, genre);
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				lDirector.add(res.getString("director"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return lDirector;
	}

	public List<Actor> getVertici(String genre) {
		// TODO Auto-generated method stub
		
		String sql = "WITH attori_film_fantasy AS (\r "
				+ "    SELECT Star1 AS attore, series_Title, Released_Year\r "
				+ "    FROM topFilm.movies\r "
				+ "    WHERE Genre LIKE ?\r "
				+ "    UNION\r "
				+ "    SELECT Star2, series_Title, Released_Year\r "
				+ "    FROM topFilm.movies\r "
				+ "    WHERE Genre LIKE ?\r "
				+ "    UNION\r "
				+ "    SELECT Star3, series_Title, Released_Year\r "
				+ "    FROM topFilm.movies\r "
				+ "    WHERE Genre LIKE ?\r "
				+ "    UNION\r "
				+ "    SELECT Star4, series_Title, Released_Year\r "
				+ "    FROM topFilm.movies\r "
				+ "    WHERE Genre LIKE ?\r "
				+ ")\r "
				+ "SELECT\r "
				+ "    af.attore AS name,\r "
				+ "    a.birthYear,\r "
				+ "    a.primaryProfession AS profession,\r "
				+ "    a.knownForTitle AS knowForTitle,\r "
				+ "    af.series_Title AS movie,\r "
				+ "    af.Released_Year \r "
				+ "FROM\r "
				+ "    attori_film_fantasy af\r "
				+ "JOIN\r "
				+ "    actor a ON af.attore = a.primaryName\r "
				+ "WHERE\r "
				+ "    a.deathYear = 0 \r "
				+ "    AND a.birthYear + 6 <= af.Released_Year \r "
				+ "ORDER BY\r "
				+ "    a.primaryName, af.series_Title;";

		Map<String,Actor> mActor = new TreeMap<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, "%" + genre + "%");
			st.setString(2, "%" + genre + "%");
			st.setString(3, "%" + genre + "%");
			st.setString(4, "%" + genre + "%");
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) { // è necessario evitare omonimi, non avendo modo di sapere qual'è l'attore giusto, ho escogitato questi metodi per scegliere quello verosimilmente corretto 
				
			
				JaroWinkler jw = new JaroWinkler();
				double now = jw.similarity(res.getString("knowForTitle"), res.getString("movie"));
				
				Actor a = new Actor(res.getString("name"),res.getInt("birthYear"),res.getString("profession"),res.getString("knowForTitle"),now,0,0,0,0,0,0,0,0,0,0,0,0,0);
				
				if(!mActor.containsKey(res.getString("name"))) {//se il nome non c'è lo aggiungo
					
					a.setJw(now);
					mActor.put(res.getString("name"), a);
					
				}else if((mActor.get(res.getString("name")).getBirthYear() != res.getInt("birthYear")) || (!mActor.get(res.getString("name")).getProfession().equals(res.getString("profession")))) {//controllo che non sia lo stesso attore
					
					double old = mActor.get(res.getString("name")).getJw();
					
					if(now >= 0.62 && old < 0.62) {//Controllo se il nuovo attore entra per somiglianza nome
						
						mActor.remove(res.getString("name"));
						mActor.put(res.getString("name"), a);
				        
					}else if(!(now < 0.62 && old >= 0.62)){// Controllo se rimane il vecchio attore per somiglianza nome
						
						if(((a.getProfession().contains("actor")) && !(mActor.get(res.getString("name")).getProfession().contains("actor")))
							|| ((a.getProfession().contains("actress")) && !(mActor.get(res.getString("name")).getProfession().contains("actress")))) {// controllo se il nuovo attore ha actor nel nome, a differenza di quello vecchio
							
								a.setJw(now);
								mActor.put(res.getString("name"), a);
							
						}else if(!((!(a.getProfession().contains("actor")) && (mActor.get(res.getString("name")).getProfession().contains("actor")))
								|| (!(a.getProfession().contains("actress")) && (mActor.get(res.getString("name")).getProfession().contains("actress"))))){// controllo se il vecchio attore ha actor nel nome, a differenza di quello nuovo
							
								if(now > old) {//non è sicuro chi sia l'attore giusto. a caso scelgo di mettere chi ha il nome più simile. 
									
									mActor.remove(res.getString("name"));
									mActor.put(res.getString("name"), a);
								
								}
							
							
						}
						
					}
					
				}else if(a.getJw() < now) { //Aggiorno il valore jw
					a.setJw(now);
				}
				
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		//System.out.println(mActor.values().size());
		
		return new ArrayList<>(mActor.values());

		
		
	}

	public List<Movie> getMovies() {
		// TODO Auto-generated method stub
		String sql = "SELECT *\r "
				+ "FROM movies";
		
		List<Movie> lMovie = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				List<String> Cast = new ArrayList<String>();
				Cast.add(res.getString("Star1"));
				Cast.add(res.getString("Star2"));
				Cast.add(res.getString("Star3"));
				Cast.add(res.getString("Star4"));
				lMovie.add(new Movie(res.getString("Poster_Link"),res.getString("Series_Title"),res.getInt("Released_Year"),res.getString("Certificate"),res.getString("Runtime"),res.getString("Genre"),res.getDouble("Imdb_Rating"),res.getString("Overview"),res.getString("Director"),res.getInt("Meta_score"),Cast,res.getInt("No_of_Votes"),res.getInt("Gross")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("Film: "+ lMovie.size());
		
		return lMovie;
	}

	public double getMinIMDBrating() {
		// TODO Auto-generated method stub
		String sql = "SELECT MIN(IMDB_Rating) AS min\r "
				+ "FROM movies\r "
				+ ";";
		double result = 0;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result = res.getDouble("min");
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return (Double) null;
		}
		return result;
	}

	public double getMaxIMDBrating() {
		// TODO Auto-generated method stub
		String sql = "SELECT MAX(IMDB_Rating) AS max\r "
				+ "FROM movies\r "
				+ ";";
		double result = 0;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result = res.getDouble("max");
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return (Double) null;
		}
		return result;
	}

	public int getMinVotes() {
		// TODO Auto-generated method stub
		String sql = "SELECT MIN(No_of_Votes) AS min\r "
				+ "FROM movies\r "
				+ ";";
		int result = 0;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result = res.getInt("min");
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return (Integer) null;
		}
		return result;
	}

	public int getMaxVotes() {
		// TODO Auto-generated method stub
		String sql = "SELECT MAX(No_of_Votes) AS max\r "
				+ "FROM movies\r "
				+ ";";
		int result = 0;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result = res.getInt("max");
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return (Integer) null;
		}
		return result;
	}

	public int getMinMetaScore() {
		// TODO Auto-generated method stub
		String sql = "SELECT MIN(Meta_score) AS min\r "
				+ "FROM movies\r "
				+ "WHERE Meta_score > 0;";
		int result = 0;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result = res.getInt("min");
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return (Integer) null;
		}
		return result;
	}

	public int getminGross() {
		// TODO Auto-generated method stub
		String sql = "SELECT MIN(gross) AS min\r "
				+ "FROM movies\r "
				+ "";
		int result = 0;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result = res.getInt("min");
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return (Integer) null;
		}
		return result;
	}

	public List<Pair> getArchi(Set<Actor> lActor) {
		// TODO Auto-generated method stub
		String sql = "SELECT \r "
				+ "    LEAST(actor1, actor2) AS a1,\r "
				+ "    GREATEST(actor1, actor2) AS a2,\r "
				+ "    COUNT(*) AS weight\r "
				+ "FROM (\r "
				+ "    SELECT genre, star1 AS actor1, star2 AS actor2 FROM movies \r "
				+ "    UNION ALL\r "
				+ "    SELECT genre, star1 AS actor1, star3 AS actor2 FROM movies \r "
				+ "    UNION ALL\r "
				+ "    SELECT genre, star1 AS actor1, star4 AS actor2 FROM movies \r "
				+ "    UNION ALL\r "
				+ "    SELECT genre, star2 AS actor1, star3 AS actor2 FROM movies \r "
				+ "    UNION ALL\r "
				+ "    SELECT genre, star2 AS actor1, star4 AS actor2 FROM movies \r "
				+ "    UNION ALL\r "
				+ "    SELECT genre, star3 AS actor1, star4 AS actor2 FROM movies \r "
				+ ") AS actor_pairs\r "
				+ " WHERE actor1 <> actor2\r "
				+ "GROUP BY actor1, actor2\r "
				+ "ORDER BY weight DESC;\r "
				+ "";
		List<Pair> result = new ArrayList<>();
		
		
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			Map<String,Actor> mActor = new TreeMap<>();
			
			for(Actor a : lActor) {
				mActor.put(a.getName(), a);
			}
			while(res.next()) {
				
				if(mActor.containsKey(res.getString("a1")) && mActor.containsKey(res.getString("a2"))) {
					
					result.add(new Pair(mActor.get(res.getString("a1")),mActor.get(res.getString("a2")),res.getInt("weight")));
					
				}
				
			}
			//System.out.println("coppie : "+result);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		for(Pair p : result) {
			if(p.getA1().equals(p.getA2())) {
				System.out.println(p);
			}
		}
		
		return result;
	}
	
}