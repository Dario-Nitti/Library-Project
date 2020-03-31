package presenter.core;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.DataLayer;
import model.DataLayerException;
import model.dao.impl.UtenteDAO;
import presenter.utils.PasswordUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dto.Utente;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LoginPresenter {

    @FXML
    private Label messagelog;

    @FXML
    private TextField emaillog;

    @FXML
    private PasswordField passlog;

    @FXML
    private Label messagereg;

    @FXML
    private TextField emailreg;

    @FXML
    private TextField nomereg;

    @FXML
    private TextField cognomereg;

    @FXML
    private TextField passreg;
    
    @FXML
    private Label emailutente;


    @FXML
    private void Login(ActionEvent event) throws DataLayerException, IOException {
        if (emaillog != null && emaillog != null) {
            Utente utente = new Utente();
            String email = emaillog.getText();
            utente.setEmail(email);
            utente.setHashPassword(PasswordUtility.getSHA256(passlog.getText()));
            UtenteDAO dao = UtenteDAO.getInstance();
            int utenteID = dao.loginUtente(utente.getEmail(), utente.getHashPassword());
            if (utenteID > 0) {
                System.out.println(utenteID);
                utente = dao.getByPK(utenteID);
                String ruolo = utente.getRuolo();
                System.out.println(ruolo);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
                Parent root = (Parent) loader.load();
                PublicationPresenter publicationPresenter = loader.getController();
                publicationPresenter.ListAllPubblicazioni(event,ruolo);
                Stage stage = new Stage();
                stage.setTitle("Home");
                stage.setScene(new Scene(root));
                stage.show();
                    /*Parent home_page = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));
                    Scene home_page_scene = new Scene(home_page);
                    Stage home_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    home_stage.setScene(home_page_scene);
                    home_stage.show();*/
            } else {
                messagelog.setText("Password e Email errati!");
            }
        } else {
            messagelog.setText("Inserisci i parametri");
        }
    }

    /*Metodo registrazione*/
    @FXML
    private void Registrazione(ActionEvent event) throws DataLayerException {
        if (nomereg != null && cognomereg != null && emailreg != null && passreg != null) {
            Utente utente = new Utente();
            utente.setNome(nomereg.getText());
            utente.setCognome(cognomereg.getText());
            utente.setEmail(emailreg.getText());
            utente.setHashPassword(PasswordUtility.getSHA256(passreg.getText()));
            UtenteDAO dao = UtenteDAO.getInstance();
            if(!dao.checkEmail(utente.getEmail())){
            dao.store(utente);
                    messagereg.setText("Registrazione effettuata");
            }
            else {messagereg.setText("E-mail occupata");}
                }
                else{
            messagereg.setText("Inserisci i parametri");
        }
    }

    /*Metodo per reindirizzamento profilo utente*/
    @FXML
    private void ProfiloUtente(ActionEvent event) throws DataLayerException, IOException {
        /*    Utente utente = new Utente();
            String email = emailutente.getText();
        UtenteDAO dao = UtenteDAO.getInstance();

            for (Utente utenteList: dao.getAll()) {

            }
            UtenteDAO dao = UtenteDAO.getInstance();
            int utenteID = dao.loginUtente(utente.getEmail(), utente.getHashPassword());
                System.out.println(utenteID);
                utente = dao.getByPK(utenteID);
                emailutente.setText(utente.getEmail());
                Parent home_page = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));
                Scene home_page_scene = new Scene(home_page);
                Stage home_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                home_stage.setScene(home_page_scene);
                home_stage.show();*/
    }
    

}
