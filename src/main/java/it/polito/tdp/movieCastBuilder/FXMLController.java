/**
 * Sample Skeleton for 'castbuilder.fxml' Controller Class
 */

package it.polito.tdp.movieCastBuilder;


import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.movieCastBuilder.model.Actor;
import it.polito.tdp.movieCastBuilder.model.Cast;
import it.polito.tdp.movieCastBuilder.model.Model;
import it.polito.tdp.movieCastBuilder.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FXMLController {
	
	private Model model;
	
	private Cast BestCast;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnGenerateCast"
    private Button btnGenerateCast; // Value injected by FXMLLoader

    @FXML // fx:id="btnReset"
    private Button btnReset; // Value injected by FXMLLoader

    @FXML // fx:id="btnSelectDirector"
    private Button btnSelectDirector; // Value injected by FXMLLoader

    @FXML // fx:id="btnSelectGenre"
    private Button btnSelectGenre; // Value injected by FXMLLoader

    @FXML // fx:id="btnSostituisci"
    private Button btnSostituisci; // Value injected by FXMLLoader

    @FXML // fx:id="cmbActor"
    private ComboBox<Actor> cmbActor; // Value injected by FXMLLoader

    @FXML // fx:id="cmbDirector"
    private ComboBox<String> cmbDirector; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenre"
    private ComboBox<String> cmbGenre; // Value injected by FXMLLoader
    
    @FXML // fx:id="colAnno"
    private TableColumn<Movie, Integer> colAnno; // Value injected by FXMLLoader

    @FXML // fx:id="colCast"
    private TableColumn<Movie, List<String>> colCast; // Value injected by FXMLLoader

    @FXML // fx:id="colCertificato"
    private TableColumn<Movie, String> colCertificato; // Value injected by FXMLLoader

    @FXML // fx:id="colDurata"
    private TableColumn<Movie, String> colDurata; // Value injected by FXMLLoader

    @FXML // fx:id="colGenere"
    private TableColumn<Movie, String> colGenere; // Value injected by FXMLLoader

    @FXML // fx:id="colImdbRating"
    private TableColumn<Movie, Double> colImdbRating; // Value injected by FXMLLoader

    @FXML // fx:id="colIncasso"
    private TableColumn<Movie, Integer> colIncasso; // Value injected by FXMLLoader

    @FXML // fx:id="colMetaScore"
    private TableColumn<Movie, Integer> colMetaScore; // Value injected by FXMLLoader

    @FXML // fx:id="colRegista"
    private TableColumn<Movie, String> colRegista; // Value injected by FXMLLoader

    @FXML // fx:id="colSinossi"
    private TableColumn<Movie, String> colSinossi; // Value injected by FXMLLoader

    @FXML // fx:id="colTitolo"
    private TableColumn<Movie, String> colTitolo; // Value injected by FXMLLoader

    @FXML // fx:id="lblAttesa"
    private Label lblAttesa; // Value injected by FXMLLoader

    @FXML // fx:id="lblSostituto"
    private Label lblSostituto; // Value injected by FXMLLoader

    @FXML // fx:id="slAffinitaGenere"
    private Slider slAffinitaGenere; // Value injected by FXMLLoader

    @FXML // fx:id="slApprovazioneCritica"
    private Slider slApprovazioneCritica; // Value injected by FXMLLoader

    @FXML // fx:id="slApprovazionePubblico"
    private Slider slApprovazionePubblico; // Value injected by FXMLLoader

    @FXML // fx:id="slIncassoFilm"
    private Slider slIncassoFilm; // Value injected by FXMLLoader

    @FXML // fx:id="slIntesaRegista"
    private Slider slIntesaRegista; // Value injected by FXMLLoader

    @FXML // fx:id="slSintoniaAttori"
    private Slider slSintoniaAttori; // Value injected by FXMLLoader

    @FXML // fx:id="tabBestCast"
    private Tab tabBestCast; // Value injected by FXMLLoader

    @FXML // fx:id="tabGeneraCast"
    private Tab tabGeneraCast; // Value injected by FXMLLoader
    
    @FXML // fx:id="tabpane"
    private TabPane tabpane; // Value injected by FXMLLoader

    @FXML // fx:id="tblCurriculum"
    private TableView<Movie> tblCurriculum; // Value injected by FXMLLoader

    @FXML // fx:id="txtBestCast"
    private ListView<Actor> txtBestCast; // Value injected by FXMLLoader

    @FXML // fx:id="txtCurriculum"
    private TextArea txtCurriculum; // Value injected by FXMLLoader

    @FXML
    void doGenerateCast(ActionEvent event) {
    	
    	btnGenerateCast.setDisable(true);
    	//lblAttesa.setText("Attendere qualche secondo");
    	
    	BestCast = this.model.findCast(cmbGenre.getValue(), cmbDirector.getValue(), convert(slIncassoFilm.getValue()), 
    			convert(slApprovazioneCritica.getValue()), convert(slApprovazionePubblico.getValue()), convert(slSintoniaAttori.getValue()), 
    			convert(slIntesaRegista.getValue()), convert(slAffinitaGenere.getValue()));
    	
    	lblAttesa.setText("Best cast generato!");
    	tabBestCast.setDisable(false);
    	
    	getInfoBestCast();
    	tabpane.getSelectionModel().select(tabBestCast);
    	
    }
    
    private void getInfoBestCast() {
		// TODO Auto-generated method stub
    	
    	txtBestCast.getItems().clear();
    	cmbActor.getItems().clear();
    	tblCurriculum.getItems().clear();
    	txtCurriculum.setText("Seleziona un attore per vederne il curriculum");
    	
    	txtBestCast.getItems().add(BestCast.getA1());
    	txtBestCast.getItems().add(BestCast.getA2());
    	txtBestCast.getItems().add(BestCast.getA3());
    	txtBestCast.getItems().add(BestCast.getA4());
    	
    	cmbActor.getItems().add(BestCast.getA1());
    	cmbActor.getItems().add(BestCast.getA2());
    	cmbActor.getItems().add(BestCast.getA3());
    	cmbActor.getItems().add(BestCast.getA4());
	}

	private double convert(double value) {
		// TODO Auto-generated method stub
    	
    	if(value == 1) {
    		value = 0.2;
    	}else if (value == 2) {
    		value = 0.4;
    	}else if (value == 3) {
    		value = 0.6;
    	}else if (value == 4) {
    		value = 0.8;
    	}else if (value == 5) {
    		value = 1;
    	}
    	
		return value;
	}

	@FXML
    void doSelectActor(MouseEvent event) {
    	
    	txtCurriculum.clear();
    	String info = "";
    	Actor a = txtBestCast.getSelectionModel().getSelectedItem();
    	
    	info = info + a.getName() + "\nAge: " + (LocalDate.now().getYear() - a.getBirthYear()) 
    			+ "\n" + a.getProfession().substring(0,1).toUpperCase() + a.getProfession().replace(",", ", ").substring(1);
    	txtCurriculum.setText(info);
    	
    	List<Movie> lm = this.model.getMoviesActor(a);
    	ObservableList<Movie> obs = FXCollections.observableArrayList(lm);
    	tblCurriculum.setItems(obs);
    	
    }

    @FXML
    void doSostituisci(ActionEvent event) {
    	
    	txtBestCast.getItems().clear();
    	BestCast = this.model.replace(BestCast, cmbActor.getValue(), convert(slSintoniaAttori.getValue()));
    	getInfoBestCast();
    	lblSostituto.setText("Attore sostituito!");
    	
    }

    @FXML
    void reset(ActionEvent event) {
    	
    	btnReset.setDisable(true);
    	lblAttesa.setText("");
    	
    	cmbGenre.setDisable(false);
    	btnSelectGenre.setDisable(false);
    	cmbDirector.setDisable(true);
    	cmbGenre.setValue(null);
    	btnSelectDirector.setDisable(true);
    	
    	slAffinitaGenere.setDisable(true);
		slApprovazioneCritica.setDisable(true);
		slApprovazionePubblico.setDisable(true);
		slIncassoFilm.setDisable(true);
		slIntesaRegista.setDisable(true);
		slSintoniaAttori.setDisable(true);
		slAffinitaGenere.setValue(3);
		slApprovazioneCritica.setValue(3);
		slApprovazionePubblico.setValue(3);
		slIncassoFilm.setValue(3);
		slIntesaRegista.setValue(3);
		slSintoniaAttori.setValue(3);
		cmbDirector.getItems().clear();
		cmbDirector.setValue(null);
		
		btnGenerateCast.setDisable(true);
		btnReset.setDisable(true);
		
		tabBestCast.setDisable(true);
		lblSostituto.setText("");
		//txtCurriculum.setDisable(true);
		//txtBestCast.setDisable(true);
		//tblCurriculum.setDisable(true);
		
		
    }

    @FXML
    void selectDirector(ActionEvent event) {
    	if(cmbDirector.getValue() != null) {
    		
    		cmbDirector.setDisable(true);
    		btnSelectDirector.setDisable(true);
    		btnGenerateCast.setDisable(false);
    		lblAttesa.setText("");
    		
    		slAffinitaGenere.setDisable(false);
    		slApprovazioneCritica.setDisable(false);
    		slApprovazionePubblico.setDisable(false);
    		slIncassoFilm.setDisable(false);
    		slIntesaRegista.setDisable(false);
    		slSintoniaAttori.setDisable(false);
    		
    		
    		
    	}else {
    		lblAttesa.setText("Seleziona un regista per proseguire");
    	}
    }

    @FXML
    void selectGenre(ActionEvent event) {
    	if(cmbGenre.getValue() != null) {
    		
    		btnReset.setDisable(false);
    		cmbGenre.setDisable(true);
    		btnSelectGenre.setDisable(true);
    		cmbDirector.setDisable(false);
    		btnSelectDirector.setDisable(false);
    		lblAttesa.setText("");
    		
    		this.cmbDirector.getItems().addAll(this.model.getDirector(cmbGenre.getValue()));
    		
    	}else {
    		lblAttesa.setText("Seleziona un genere cinematografico per proseguire");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnGenerateCast != null : "fx:id=\"btnGenerateCast\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert btnSelectDirector != null : "fx:id=\"btnSelectDirector\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert btnSelectGenre != null : "fx:id=\"btnSelectGenre\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert btnSostituisci != null : "fx:id=\"btnSostituisci\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colAnno != null : "fx:id=\"colAnno\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colCast != null : "fx:id=\"colCast\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colCertificato != null : "fx:id=\"colCertificato\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colDurata != null : "fx:id=\"colDurata\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colGenere != null : "fx:id=\"colGenere\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colImdbRating != null : "fx:id=\"colImdbRating\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colIncasso != null : "fx:id=\"colIncasso\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colMetaScore != null : "fx:id=\"colMetaScore\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colRegista != null : "fx:id=\"colRegista\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colSinossi != null : "fx:id=\"colSinossi\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert colTitolo != null : "fx:id=\"colTitolo\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert cmbActor != null : "fx:id=\"cmbActor\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert cmbDirector != null : "fx:id=\"cmbDirector\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert cmbGenre != null : "fx:id=\"cmbGenre\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert lblAttesa != null : "fx:id=\"lblAttesa\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert lblSostituto != null : "fx:id=\"lblSostituto\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert slAffinitaGenere != null : "fx:id=\"slAffinitaGenere\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert slApprovazioneCritica != null : "fx:id=\"slApprovazioneCritica\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert slApprovazionePubblico != null : "fx:id=\"slApprovazionePubblico\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert slIncassoFilm != null : "fx:id=\"slIncassoFilm\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert slIntesaRegista != null : "fx:id=\"slIntesaRegista\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert slSintoniaAttori != null : "fx:id=\"slSintoniaAttori\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert tabBestCast != null : "fx:id=\"tabBestCast\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert tabGeneraCast != null : "fx:id=\"tabGeneraCast\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert tblCurriculum != null : "fx:id=\"tblCurriculum\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert txtBestCast != null : "fx:id=\"txtBestCast\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert txtCurriculum != null : "fx:id=\"txtCurriculum\" was not injected: check your FXML file 'castbuilder.fxml'.";
        assert tabpane != null : "fx:id=\"tabpane\" was not injected: check your FXML file 'castbuilder.fxml'.";
        
        colAnno.setCellValueFactory(new PropertyValueFactory<>("releasedYear")); 
        colCast.setCellValueFactory(new PropertyValueFactory<>("Cast")); 
        colCertificato.setCellValueFactory(new PropertyValueFactory<>("Certificate")); 
        colDurata.setCellValueFactory(new PropertyValueFactory<>("Runtime")); 
        colGenere.setCellValueFactory(new PropertyValueFactory<>("Genre")); 
        colImdbRating.setCellValueFactory(new PropertyValueFactory<>("ImdbRating")); 
        colIncasso.setCellValueFactory(new PropertyValueFactory<>("Gross")); 
        colMetaScore.setCellValueFactory(new PropertyValueFactory<>("MetaScore")); 
        colRegista.setCellValueFactory(new PropertyValueFactory<>("Director")); 
        colSinossi.setCellValueFactory(new PropertyValueFactory<>("Overview")); 
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("Title")); 
        
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbGenre.getItems().addAll(this.model.getGenre());
    	
    }

}
