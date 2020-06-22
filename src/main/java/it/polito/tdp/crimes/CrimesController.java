/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Arco> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	if(boxCategoria.getValue() == null || boxAnno.getValue()== null) {
    		txtResult.appendText("selezionare i valori di anno e categoria");
    		return;
    	}
    	
    	int anno = boxAnno.getValue();
    	String cId = boxCategoria.getValue();
    	
    	
    	
    	model.creaGrafo(cId, anno);
    	
    	txtResult.appendText(String.format("Grafo creato ! \n"
    			+ "#Vertici:  %d \n #Archi: %d \n", model.getNV(), model.getNA()));
    	
    	txtResult.appendText("Archi di peso massimo:  \n");
    	for (Arco a: model.getBestArchi()) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    	
    	boxArco.getItems().setAll(model.getBestArchi());
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	Arco a = boxArco.getValue();
    	
    	if (a== null) {
    		txtResult.setText("Selezionare un arco");
    	}
    	
    	String sorgente = a.getId1();
    	String destinazione = a.getId2();
    	
    	txtResult.appendText("Percorso di peso minimo: \n");
    	
    	if(model.trovaPercorso(sorgente, destinazione).size()<model.getNV()) {
    		txtResult.appendText("Non esiste un percorso che tocca tutti i vertici");
    		return;
    	}
    	for(String s: model.trovaPercorso(sorgente, destinazione)) {
    		txtResult.appendText(s+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().setAll(model.getCategorie());
    	this.boxAnno.getItems().setAll(model.getAnni());
    }
}
