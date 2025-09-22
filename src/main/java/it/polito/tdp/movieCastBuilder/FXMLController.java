package it.polito.tdp.movieCastBuilder;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.movieCastBuilder.model.Cast;
import it.polito.tdp.movieCastBuilder.model.Model;
import it.polito.tdp.movieCastBuilder.model.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLController {
	
	private Model model;
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
    	//this.model.prova();
    	this.model.getDirector("Animation"); 
    	Cast c = this.model.findCast("Animation", "Tim Burton", 0.6, 0.6, 0.6, 0.6, 0.6, 0.6);
    	List<Movie> lm1 = this.model.getMoviesActor(c.getA1());
    	List<Movie> lm2 = this.model.getMoviesActor(c.getA2());
    	List<Movie> lm3 = this.model.getMoviesActor(c.getA3());
    	List<Movie> lm4 = this.model.getMoviesActor(c.getA4());
    	c = this.model.replace(c, c.getA2(), 0.6);
    	lm4 = this.model.getMoviesActor(c.getA4());
    	
       
        label.setText("Hello World!");
    }
    
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//    }    
    
    public void setModel(Model model) {
    	this.model = model;
    	this.model.getGenre();
    }
    
}
