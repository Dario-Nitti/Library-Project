package presenter.core;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import model.DataLayerException;
import model.dao.impl.UtenteDAO;
import model.dto.Like;
import model.dto.Pubblicazione;
import model.dto.Recensione;
import model.dto.Utente;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dario Nitti
 */
public class UserPresenter {

    @FXML
    private ListView listreceutente;

    @FXML
    private Label messageutente;

    @FXML
    private ToggleButton tipoutenza;

    /*Lista di tutte le recensioni di un utente*/
    /*@FXML
    private void ListRecensioniUtente(ActionEvent event, int idUtente) throws DataLayerException {
            UtenteDAO utenteDao = UtenteDAO.getInstance();
            Utente utente = utenteDao.getUtenteById(idUtente);
            RecensioneDAO recensioneDao = RecensioneDAO.getInstance();
            List<Recensione> listRecensioni = recensioneDao.getAll();
            List<Recensione> listRecensioniUtente = new ArrayList<Recensione>();
            for(Recensione recensione: listRecensioni) {
                if(recensione.getUtente().equals(utente)){
                    listRecensioniUtente.add(recensione);
                }
            }
            utente.setRecensioni(listRecensioniUtente);
            listreceutente.setItems((ObservableList) utente.getRecensioni());
        } 
*/
    /*Modifica tipo di utenza*/
    @FXML
    private void ModificaTipoUtenza(ActionEvent event, int idUtente) throws DataLayerException {
        UtenteDAO utenteDao = UtenteDAO.getInstance();
        Utente utente = utenteDao.getByPK(idUtente);
        String tipoUtenzaUtente = tipoutenza.toString();
        if (tipoUtenzaUtente == "false") {
            utente.setRuolo("attivo");
        }
        if (tipoUtenzaUtente == "true") {
            utente.setRuolo("passivo");
        }
        utenteDao.store(utente);
        messageutente.setText("Inserisci i parametri");
    }

    /*Ingresso nella pagina del profilo*/
    @FXML
    private void PaginaProfilo(ActionEvent event, int idUtente) throws DataLayerException, IOException {
        UtenteDAO utenteDao = UtenteDAO.getInstance();
        Utente utente = utenteDao.getByPK(idUtente);
        String tipoUtenzaUtente = tipoutenza.toString();
        if (tipoUtenzaUtente == "false") {
            Parent profilo_passivo_page = FXMLLoader.load(getClass().getResource("/view/ProfilePassive.fxml"));
            Scene profilo_passivo_page_scene = new Scene(profilo_passivo_page);
            Stage profilo_passivo_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            profilo_passivo_stage.setScene(profilo_passivo_page_scene);
            profilo_passivo_stage.show();
        }
        if (tipoUtenzaUtente == "true") {
            Parent profilo_attivo_page = FXMLLoader.load(getClass().getResource("/view/ActiveProfile.fxml"));
            Scene profilo_attivo_page_scene = new Scene(profilo_attivo_page);
            Stage profilo_attivo_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            profilo_attivo_stage.setScene(profilo_attivo_page_scene);
            profilo_attivo_stage.show();
        }
        utenteDao.store(utente);
        messageutente.setText("Inserisci i parametri");
    }
}
