package model.dao.impl;

import model.DataLayerException;
import model.dao.BaseDAO;
import model.dto.Pubblicazione;
import model.dto.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 05/03/2020
 */

public class PubblicazioneDAO extends BaseDAO<Pubblicazione> {

  private AutoreDAO autoreDAO = AutoreDAO.getInstance();
  private PreparedStatement getIDs, selectByISBN, selectAddedRecently, selectUpdatedRecently, selectAddedByUtente, insert, update, delete;
  // TODO elenco pubblicazioni aventi stessi autori

  // Singleton
  private PubblicazioneDAO () {
    try {
      init();

    } catch (DataLayerException dle) {
      dle.printStackTrace();
    }
  } // impedisce l'instanziazione dall'esterno
  private static PubblicazioneDAO instance = new PubblicazioneDAO();
  public static PubblicazioneDAO getInstance () {
    return instance;
  }

  @Override
  protected void init () throws DataLayerException {

    try (Connection connection = dataLayer.getConnection()) {
      getIDs = connection.prepareStatement("SELECT isbn FROM pubblicazione ORDER BY titolo");
      selectByISBN = connection.prepareStatement("SELECT * FROM pubblicazione WHERE isbn=?");
      selectAddedRecently = connection.prepareStatement("SELECT isbn FROM operazione WHERE tipo='inserimento' ORDER BY timestamp DESC LIMIT 10");
      selectUpdatedRecently = connection.prepareStatement("SELECT isbn FROM operazione WHERE tipo='modifica' ORDER BY timestamp DESC");
      selectAddedByUtente = connection.prepareStatement("SELECT isbn FROM operazione WHERE id_utente=? AND tipo='inserimento' ORDER BY timestamp DESC");
      insert = connection.prepareStatement("INSERT INTO pubblicazione (isbn, titolo, editore, data, lingua, pagine, descrizione, indice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
      update = connection.prepareStatement("UPDATE pubblicazione SET titolo=?, editore=?, data=?, lingua=?, pagine=?, descrizione=?, indice=? WHERE isbn=?");
      delete = connection.prepareStatement("DELETE FROM pubblicazione WHERE isbn=?");

    } catch (SQLException ex) {
      throw new DataLayerException("Errore inizializzazione PubblicazioneDAO", ex);
    }
  }

  @Override
  protected void destroy () throws DataLayerException {

    try {
      getIDs.close();
      selectByISBN.close();
      selectAddedRecently.close();
      selectUpdatedRecently.close();
      selectAddedByUtente.close();
      insert.close();
      update.close();
      delete.close();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore finalizzazione PubblicazioneDAO", ex);
    }
  }

  @Override
  protected Pubblicazione createFromRS (ResultSet rs) throws DataLayerException {

    Pubblicazione pubblicazione = new Pubblicazione();
    try {
      pubblicazione.setIsbn(rs.getString("isbn"));
      pubblicazione.setTitolo(rs.getString("titolo"));
      pubblicazione.setEditore(rs.getString("editore"));
      pubblicazione.setData(rs.getDate("data"));
      pubblicazione.setLingua(rs.getString("lingua"));
      pubblicazione.setPagine(rs.getInt("pagine"));
      pubblicazione.setDescrizione(rs.getString("descrizione"));
      pubblicazione.setIndice(rs.getString("indice"));
      //pubblicazione.setAutori(AutoreDAO.getInstance().getByPubblicazione(pubblicazione)); // TODO verificare

    } catch (SQLException ex) {
      throw new DataLayerException("Errore creazione Pubblicazione", ex);
    }
    return pubblicazione;
  }

  @Override
  public void store (Pubblicazione pubblicazione) throws DataLayerException {

    try {
      if (getByPK(pubblicazione.getIsbn()) != null) { // UPDATE pubblicazione SET titolo=?, editore=?, data=?, lingua=?, pagine=?, descrizione=?, indice=? WHERE isbn=?
        update.setString(1, pubblicazione.getTitolo());
        update.setString(2, pubblicazione.getEditore());
        update.setDate(3, pubblicazione.getData());
        update.setString(4, pubblicazione.getLingua());
        update.setInt(5, pubblicazione.getPagine());
        update.setString(6, pubblicazione.getDescrizione());
        update.setString(7, pubblicazione.getIndice());
        update.setString(8, pubblicazione.getIsbn());
        update.executeUpdate();

      } else { // INSERT INTO pubblicazione (isbn, titolo, editore, data, lingua, pagine, descrizione, indice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        insert.setString(1, pubblicazione.getIsbn());
        insert.setString(2, pubblicazione.getTitolo());
        insert.setString(3, pubblicazione.getEditore());
        insert.setDate(4, pubblicazione.getData());
        insert.setString(5, pubblicazione.getLingua());
        insert.setInt(6, pubblicazione.getPagine());
        insert.setString(7, pubblicazione.getDescrizione());
        insert.setString(8, pubblicazione.getIndice());
        insert.executeUpdate();


        // TODO chiama storeAutPub su AutoreDAO
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore salvataggio Pubblicazione", ex);
    }
  }

  @Override
  public Pubblicazione getByPK (Object obj) throws DataLayerException {
    // SELECT * FROM pubblicazione WHERE isbn=?

    String isbn = (String) obj;
    try {
      selectByISBN.setString(1, isbn);
      try (ResultSet rs = selectByISBN.executeQuery()) {
        if (rs.next()) {
          return createFromRS(rs);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Pubblicazione", ex);
    }
    return null;
  }

  @Override
  public List<Pubblicazione> getAll () throws DataLayerException {
    // SELECT isbn FROM pubblicazione ORDER BY isbn

    List<Pubblicazione> pubblicazioni = new ArrayList<>();
    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        pubblicazioni.add(getByPK(rs.getString("isbn")));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Pubblicazioni", ex);
    }
    return pubblicazioni;
  }

  public List<Pubblicazione> getUpdatedRecently () throws DataLayerException {
    // SELECT isbn FROM operazione WHERE tipo='modifica' ORDER BY timestamp DESC

    List<Pubblicazione> pubblicazioni = new ArrayList<>();
    try (ResultSet rs = selectUpdatedRecently.executeQuery()) {
      while (rs.next()) {
        pubblicazioni.add(getByPK(rs.getString("isbn")));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Pubblicazioni");
    }
    return pubblicazioni;
  }

  public List<Pubblicazione> getAddedRecently () throws DataLayerException {
    // SELECT isbn FROM operazione WHERE tipo='inserimento' ORDER BY timestamp DESC LIMIT 10

    List<Pubblicazione> pubblicazioni = new ArrayList<>();
    try (ResultSet rs = selectAddedRecently.executeQuery()) {
      while (rs.next()) {
        pubblicazioni.add(getByPK(rs.getString("isbn")));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Pubblicazioni", ex);
    }
    return pubblicazioni;
  }

  public List<Pubblicazione> getAddedByUtente (Utente utente) throws DataLayerException {
    // SELECT isbn FROM operazione WHERE id_utente=? AND tipo='inserimento' ORDER BY timestamp DESC

    List<Pubblicazione> pubblicazioni = new ArrayList<>();
    try {
      selectAddedByUtente.setInt(1, utente.getID());
      try (ResultSet rs = selectAddedByUtente.executeQuery()) {
        while (rs.next()) {
          pubblicazioni.add(getByPK(rs.getString("isbn")));
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Pubblicazioni", ex);
    }
    return pubblicazioni;
  }

  @Override
  public void delete (Pubblicazione pubblicazione) throws DataLayerException {
    // DELETE FROM pubblicazione WHERE isbn=?

    try {
      delete.setString(1, pubblicazione.getIsbn());
      delete.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Pubblicazione", ex);
    }
  }
}
