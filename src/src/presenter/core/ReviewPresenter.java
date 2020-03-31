package presenter.core;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.dto.Recensione;
import model.dto.Utente;
import presenter.utils.PasswordUtility;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Dario Nitti
 */
public class ReviewPresenter {

    @FXML
    private Label messagerece;

    @FXML
    private TextField emailrece;

    @FXML
    private TextField testorece;

  /* @FXML
    private void CreaRecensione(ActionEvent event) throws DataLayerException {
        try (Connection connection = DataLayer.getConnection()) {
            if (testorece != null) {
                Recensione recensione = new Recensione();
                recensione.setUtente(utente);
                recensione.setTesto(testorece.toString());
                recensione.setPubblicazione(pubblicazione);
                RecensioneDAO dao = RecensioneDAO.getInstance();
                int recensioneID = dao.store(recensione);
                if (recensioneID > 0) {
                    Recensione recensioneCreata = dao.getByPK(recensioneID);
                    messagerece.setText("Recensione inviata, in attesa di approvazione");
                }
            }
            else{
                messagerece.setText("Non hai inserito nessun testo");
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Errore connessione al DB", ex);
        }*/


  /* @FXML
    private void ApprovaRecensione(ActionEvent event) throws DataLayerException {
        try (Connection connection = DataLayer.getConnection()) {
                Recensione recensione = utente.getRecensione();
                RecensioneDAO dao = RecensioneDAO.getInstance();
                int recensioneID = dao.store(recensione);
                if (recensioneID > 0) {
                    recensioneApprovata = dao.getByPK(recensioneID);
                    messagerece.setText("Recensione approvata");
                }
        } catch (SQLException ex) {
            throw new DataLayerException("Errore connessione al DB", ex);
        }
    }
*/
}
