package presenter.core;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.DataLayer;
import model.DataLayerException;
import model.dto.Like;
import model.dto.Pubblicazione;
import model.dto.Utente;
import presenter.utils.PasswordUtility;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikePresenter {

    @FXML
    private Label messagelike;

    @FXML
    private ListView listlikes;

    /*Lista di tutti i like di una pubblicazione*/
    /*@FXML
    private void ListLikePubblicazione(ActionEvent event, int idPubblicazione) throws DataLayerException {
        try (Connection connection = DataLayer.getConnection()) {
            PubblicazioneDAO pubblicazioneDao = PubblicazioneDAO.getInstance();
            Pubblicazione pubblicazione = pubblicazioneDao.getPubblicazioneById(idPubblicazione);
                LikeDAO likeDao = LikeDAO.getInstance();
                List<Like> listLike = likeDao.getAll();
                List<Like> listLikePub = new ArrayList<Like>();
                for(Like like: listLike) {
                    if(like.getPubblicazione().equals(pubblicazione)){
                        listLikePub.add(like);
                    }
                }
                pubblicazione.setLikes(listLikePub);
            listlikes.setItems((ObservableList) pubblicazione.getLikes());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new DataLayerException("Errore connessione al DB", e);
        }
    }

    *//*Metodo per mettere like a una pubblicazione*//*
    @FXML
    private void LikePubblicazione(ActionEvent event, int idPubblicazione, int idUtente) throws DataLayerException, SQLException {
        try (Connection connection = DataLayer.getConnection()) {
                Like like = new Like();
            UtenteDAO utenteDao = UtenteDAO.getInstance();
            Utente utente = utenteDao.getUtenteById(idUtente);
            PubblicazioneDAO pubblicazioneDao = PubblicazioneDAO.getInstance();
            Pubblicazione pubblicazione = pubblicazioneDao.getPubblicazioneById(idPubblicazione);
            like.setPubblicazione(pubblicazione);
            like.setUtente(utente);
                int likeID = pubblicazioneDao.likePublication(like);
                if (likeID > 0) {
                    Like like = pubblicazioneDao.getByPK(likeID);
                    messagelike.setText("Like aggiunto");

                }
            } catch (SQLException e) {
            e.printStackTrace();
            throw new DataLayerException("Errore connessione al DB", e);
        }
    }*/
}
