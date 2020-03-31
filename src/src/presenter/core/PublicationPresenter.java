package presenter.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.DataLayerException;
import model.dao.impl.AutoreDAO;
import model.dao.impl.PubblicazioneDAO;
import model.dto.*;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dario Nitti
 */
public class PublicationPresenter {
  @FXML
  private TextField titolopub;

  @FXML
  private TextArea descrizionepub;

  @FXML
  private TextField isbnpub;

  @FXML
  private TextField linguapub;

  @FXML
  private TextField editorepub;

  @FXML
  private TextField giornodatapub;

  @FXML
  private TextField mesedatapub;

  @FXML
  private TextField annodatapub;

  @FXML
  private TextField nomeautorepub;

  @FXML
  private TextField paginepub;

  @FXML
  private TextField cognomeautorepub;
  @FXML
  public ListView<Pubblicazione> pubblicazioniList;

  @FXML
  private TextField autoripub;

  @FXML
  private Label messagepub;
  @FXML
  private Label messagerece;
  @FXML
  private TextField emailrece;
  @FXML
  private TextField testorece;
  @FXML
  private ListView listrecepubblicazione;

  private boolean ruoloUtenteLoggato;
  @FXML
  private Button aggiungiPubblicazione;
  @FXML
  private ListView listpubblicazioni;
  private ObservableList<String> items = FXCollections.observableArrayList();


  /*Metodo logout*/
  @FXML
  public void Logout (ActionEvent event) throws DataLayerException, IOException {
    Parent login_page = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
    Scene login_page_scene = new Scene(login_page);
    Stage login_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    login_stage.setScene(login_page_scene);
    login_stage.show();
  }

  @FXML
  void PaginaProfilo(ActionEvent event) throws DataLayerException, IOException {
    if (ruoloUtenteLoggato == false) {
      Parent profilo_passivo_page = FXMLLoader.load(getClass().getResource("/view/ProfilePassive.fxml"));
      Scene profilo_passivo_page_scene = new Scene(profilo_passivo_page);
      Stage profilo_passivo_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      profilo_passivo_stage.setScene(profilo_passivo_page_scene);
      profilo_passivo_stage.show();

    }
    if (ruoloUtenteLoggato == true) {
      Parent profilo_attivo_page = FXMLLoader.load(getClass().getResource("/view/ActiveProfile.fxml"));
      Scene profilo_attivo_page_scene = new Scene(profilo_attivo_page);
      Stage profilo_attivo_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      profilo_attivo_stage.setScene(profilo_attivo_page_scene);
      profilo_attivo_stage.show();
    }
  }
  @FXML
  private void CreaPubblicazione(ActionEvent event) throws DataLayerException {
    if (titolopub != null || nomeautorepub != null || descrizionepub != null || isbnpub != null ||
            linguapub != null || editorepub != null || cognomeautorepub != null) {
      Pubblicazione pubblicazione = new Pubblicazione();
      Autore autore = new Autore();
      PubblicazioneDAO dao = PubblicazioneDAO.getInstance();
      AutoreDAO autoreDAO = AutoreDAO.getInstance();
      pubblicazione.setTitolo(titolopub.getText());
      pubblicazione.setDescrizione(descrizionepub.getText());
      pubblicazione.setEditore(editorepub.getText());
      pubblicazione.setIsbn(isbnpub.getText());
      pubblicazione.setLingua(linguapub.getText());
      String dataPub = annodatapub.getText() + "-" + mesedatapub.getText() + "-" + giornodatapub.getText();
      Date DataPub = Date.valueOf(dataPub);
      pubblicazione.setData(DataPub);
      pubblicazione.setIndice("");
      autore.setNome(nomeautorepub.getText());
      autore.setCognome(cognomeautorepub.getText());
      autoreDAO.store(autore);
      List<Autore> listAutori = new ArrayList<Autore>();
      List<Ristampa> listRistampe = new ArrayList<Ristampa>();
      pubblicazione.setRistampe(listRistampe);
      List<Recensione> listRecensioni = new ArrayList<Recensione>();
      pubblicazione.setRecensioni(listRecensioni);
      List<Tag> listTag = new ArrayList<Tag>();
      List<Operazione> listOperazioni = new ArrayList<Operazione>();
      pubblicazione.setOperazioni(listOperazioni);
      pubblicazione.setTags(listTag);
      List<Like> listLike = new ArrayList<Like>();
      pubblicazione.setLikes(listLike);
      List<Sorgente> listSorgenti = new ArrayList<Sorgente>();
      pubblicazione.setSorgenti(listSorgenti);
      listAutori.add(autore);
      pubblicazione.setAutori(listAutori);
       /*         List<Autore> listAutori = new ArrayList<Autore>();
            AutoreDAO autoreDAO = AutoreDAO.getInstance();
            listAutori = autoreDAO.getAll();*/
      dao.store(pubblicazione);
      messagepub.setText("Pubblicazione creata");
    }
    else{
      messagepub.setText("Campi obbligatori non riempiti");
    }
  }

  /*Metodo per creare una recensione*/
  @FXML
  private void CreaRecensione (ActionEvent event, int idUtente, int idPubblicazione) throws DataLayerException {
       /* try {
            if (testorece != null) {
                UtenteDAO utenteDao = UtenteDAO.getInstance();
                Utente utente = utenteDao.getUtenteById(idUtente);
                PubblicazioneDAO pubblicazioneDao = PubblicazioneDAO.getInstance();
                Pubblicazione pubblicazione = pubblicazioneDao.getPubblicazioneById(idPubblicazione);
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
  }

  /*Lista di tutte le recensioni di una pubblicazione*/
  @FXML
  private void ListRecensioniPubblicazione (ActionEvent event, int idPubblicazione) throws DataLayerException {
        /*try {
            PubblicazioneDAO pubblicazioneDao = PubblicazioneDAO.getInstance();
            Pubblicazione pubblicazione = pubblicazioneDao.getPubblicazioneById(idPubblicazione);
            RecensioneDAO recensioneDao = RecensioneDAO.getInstance();
            List<Recensione> listRecensioni = recensioneDao.getAll();
            List<Recensione> listRecensioniPubblicazione = new ArrayList<Recensione>();
            for(Recensione recensione: listRecensioni) {
                if(recensione.getPubblicazione().equals(pubblicazione)){
                    listRecensioniPubblicazione.add(recensione);
                }
            }
            pubblicazione.setRecensioni(listRecensioniPubblicazione);
            listrecepubblicazione.setItems((ObservableList) pubblicazione.getRecensioni());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new DataLayerException("Errore connessione al DB", e);
        }*/
  }

  /*Lista di tutte le pubblicazioni*/
  @FXML
  public void ListAllPubblicazioni (ActionEvent event,String ruolo) throws DataLayerException {
    PubblicazioneDAO pubblicazioneDao = PubblicazioneDAO.getInstance();
    AutoreDAO autoreDao = AutoreDAO.getInstance();
    if(ruolo.equals("attivo")){
      ruoloUtenteLoggato = true;
    }
    if(ruolo.equals("passivo")){
      aggiungiPubblicazione.setVisible(false);
      ruoloUtenteLoggato = false;
    }
    for (Pubblicazione pubblicazione : pubblicazioneDao.getAll()) {
      System.out.println("\n******************************");
      System.out.println(pubblicazione.getTitolo());
      String autoriP = "";
      for (Autore autore : autoreDao.getByPubblicazione(pubblicazione)) {
        autoriP += autore.getNome() + " " + autore.getCognome() + ", ";
        //String autoreNomeCognome = autore.getNome() + autore.getCognome();
        //System.out.println(autoriP);
      }

      System.out.println(autoriP);
      items.setAll(pubblicazione.getTitolo() + ", " + autoriP);
    }
    listpubblicazioni.setItems(items);
  }

  @FXML
  void RecensioneWind (ActionEvent event) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddReview.fxml"));
      AnchorPane AddPubblication = (AnchorPane) fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(AddPubblication));
      stage.setTitle("Inserisci una Recensione");
      stage.show();
    } catch (IOException e1) {
      e1.printStackTrace();
      System.err.println(e1.getMessage());
    }

  }

  @FXML
  void AddPub(ActionEvent event) throws IOException {
    Parent addPub_page = FXMLLoader.load(getClass().getResource("/view/AddPubblication.fxml"));
    Scene addPub_page_scene = new Scene(addPub_page);
    Stage addPub_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    addPub_stage.setScene(addPub_page_scene);
    addPub_stage.show();
  }

  @FXML
  void DettagliPub (ActionEvent event) throws IOException {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DetailsPublication.fxml"));
      AnchorPane AddPubblication = (AnchorPane) fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(AddPubblication));
      stage.setTitle("Dettagli Pubblicazione");
      stage.show();
    } catch (IOException e1) {
      e1.printStackTrace();
      System.err.println(e1.getMessage());
    }
  }

}
