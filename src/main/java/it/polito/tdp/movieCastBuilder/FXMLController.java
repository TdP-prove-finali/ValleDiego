package it.polito.tdp.movieCastBuilder;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.movieCastBuilder.model.Model;
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
    	this.model.prova();
    	this.model.getDirector("Fantasy"); 
    	this.model.findCast("Fantasy", "Tim Burton", 3,3,3,3,3,3);
       
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
