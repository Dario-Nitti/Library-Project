package model.dao.impl;

import model.DataLayerException;
import model.dao.BaseDAO;
import model.dto.Pubblicazione;
import model.dto.Recensione;
import model.dto.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Primiano Medugno
 * @since 05/03/2020
 */

public class RecensioneDAO extends BaseDAO<Recensione> {

  private UtenteDAO utenteDAO = UtenteDAO.getInstance();
  private PubblicazioneDAO pubblicazioneDAO = PubblicazioneDAO.getInstance();
  private PreparedStatement getPKs, selectByPK, selectByISBN, selectByUtente, selectApprovedByISBN, selectWaitingByISBN, insert, update, delete;

  // Singleton
  private RecensioneDAO () {
    try {
      init();

    } catch (DataLayerException dle) {
      dle.printStackTrace();
    }
  } // impedisce l'instanziazione dall'esterno
  private static RecensioneDAO instance = new RecensioneDAO();
  public static RecensioneDAO getInstance () {
    return instance;
  }

  @Override
  protected void init () throws DataLayerException {

    try (Connection connection = dataLayer.getConnection()) {

      getPKs = connection.prepareStatement("SELECT id_utente, isbn FROM recensione ORDER BY timestamp DESC");
      selectByPK = connection.prepareStatement("SELECT * FROM recensione WHERE id_utente=? AND isbn=? ORDER BY timestamp DESC");
      selectByISBN = connection.prepareStatement("SELECT * FROM recensione WHERE isbn=? ORDER BY timestamp DESC");
      selectByUtente = connection.prepareStatement("SELECT * FROM recensione WHERE id_utente=? ORDER BY timestamp DESC");
      selectApprovedByISBN = connection.prepareStatement("SELECT * FROM recensione WHERE isbn=? AND approvata=1 ORDER BY timestamp DESC");
      selectWaitingByISBN = connection.prepareStatement("SELECT * FROM recensione WHERE isbn=? AND approvata=0 ORDER BY timestamp DESC");
      insert = connection.prepareStatement("INSERT INTO recensione (id_utente, isbn, testo) VALUES (?, ?, ?)");
      update = connection.prepareStatement("UPDATE recensione SET approvata=? WHERE id_utente=? AND isbn=?");
      delete = connection.prepareStatement("DELETE FROM recensione WHERE id_utente=? AND isbn=?");

    } catch (SQLException ex) {
      throw new DataLayerException("Errore inizializzazione RecensioneDAO", ex);
    }
  }

  @Override
  protected void destroy () throws DataLayerException {

    try {
      getPKs.close();
      selectByPK.close();
      selectByISBN.close();
      selectByUtente.close();
      selectApprovedByISBN.close();
      selectWaitingByISBN.close();
      insert.close();
      update.close();
      delete.close();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore finalizzazione RecensioneDAO", ex);
    }
  }

  @Override
  protected Recensione createFromRS (ResultSet rs) throws DataLayerException {

    Recensione recensione = new Recensione();
    try {
      recensione.setUtente(utenteDAO.getByPK(rs.getInt("id_utente")));
      recensione.setPubblicazione(pubblicazioneDAO.getByPK(rs.getString("isbn")));
      recensione.setTesto(rs.getString("testo"));
      recensione.setTimestamp(rs.getTimestamp("timestamp"));
      recensione.setApprovata(rs.getBoolean("approvata"));

    } catch (SQLException ex) {
      throw new DataLayerException("Errore creazione Recensione", ex);
    }
    return recensione;
  }

  @Override
  public void store (Recensione recensione) throws DataLayerException {

    try {
      if (recensione.getUtente().getID() > 0 && recensione.getPubblicazione().getIsbn() != null) { // UPDATE recensione SET approvata=? WHERE id_utente=? AND isbn=?
        update.setBoolean(1, recensione.isApprovata());
        update.setInt(2, recensione.getUtente().getID());
        update.setString(3, recensione.getPubblicazione().getIsbn());
        update.executeUpdate();

      } else { // INSERT INTO recensione (id_utente, isbn, testo) VALUES (?, ?, ?)
        insert.setInt(1, recensione.getUtente().getID());
        insert.setString(2, recensione.getPubblicazione().getIsbn());
        insert.setString(3, recensione.getTesto());
        insert.executeUpdate();
      }
      // aggiorno il timestamp del DTO
      // TODO recupero il timestamp

    } catch (SQLException ex) {
      throw new DataLayerException("Errore salvataggio Recensione", ex);
    }

  }

  @Override
  public Recensione getByPK (Object obj) throws DataLayerException {
    // SELECT id_utente, isbn FROM recensione ORDER BY timestamp DESC

    // TODO recupero PK da obj (Map?)
    try {
      selectByPK.setInt(1, 1);
      selectByPK.setString(2, "");

      try (ResultSet rs = selectByPK.executeQuery()) {
        if (rs.next()) {
          return createFromRS(rs);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Recensione", ex);
    }
    return null;
  }

  @Override
  public List<Recensione> getAll () throws DataLayerException {
    // SELECT id_utente, isbn FROM recensione ORDER BY timestamp DESC

    List<Recensione> recensioni = new ArrayList<>();
    try (ResultSet rs = getPKs.executeQuery()) {
      while (rs.next()) {
        Map<String, Object> pk = new HashMap<>();
        pk.put("id_utente", rs.getInt("id_utente"));
        pk.put("isbn", rs.getString("isbn"));
        recensioni.add(getByPK(pk));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Recensioni", ex);
    }
    return recensioni;
  }

  public List<Recensione> getByPubblicazione (Pubblicazione pubblicazione) throws DataLayerException {
    // SELECT * FROM recensione WHERE isbn=? ORDER BY timestamp DESC

    List<Recensione> recensioni = new ArrayList<>();
    try {
      selectByISBN.setString(1, pubblicazione.getIsbn());
      try (ResultSet rs = selectByISBN.executeQuery()) {
        while (rs.next()) {
          // TODO chiama getByPK
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Recensioni", ex);
    }
    return recensioni;
  }

  public List<Recensione> getApprovedByPubblicazione (Pubblicazione pubblicazione) throws DataLayerException {
    // SELECT * FROM recensione WHERE isbn=? AND approvata=1 ORDER BY timestamp DESC

    List<Recensione> recensioni = new ArrayList<>();
    try {
      selectByISBN.setString(1, pubblicazione.getIsbn());
      try (ResultSet rs = selectByISBN.executeQuery()) {
        while (rs.next()) {
          // TODO chiama getByPK
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Recensioni", ex);
    }
    return recensioni;
  }

  public List<Recensione> getWaitingByPubblicazione (Pubblicazione pubblicazione) throws DataLayerException {
    // SELECT * FROM recensione WHERE isbn=? AND approvata=0 ORDER BY timestamp DESC

    List<Recensione> recensioni = new ArrayList<>();
    try {
      selectByISBN.setString(1, pubblicazione.getIsbn());
      try (ResultSet rs = selectByISBN.executeQuery()) {
        while (rs.next()) {
          // TODO chiama getByPK
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Recensioni", ex);
    }
    return recensioni;
  }

  public List<Recensione> getByUtente (Utente utente) throws DataLayerException {
    // SELECT * FROM recensione WHERE id_utente=? ORDER BY timestamp DESC

    List<Recensione> recensioni = new ArrayList<>();
    try {
      selectByISBN.setInt(1, utente.getID());
      try (ResultSet rs = selectByISBN.executeQuery()) {
        while (rs.next()) {
          // TODO chiama getByPK
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Recensioni", ex);
    }
    return recensioni;
  }

  @Override
  public void delete (Recensione recensione) throws DataLayerException {
    // DELETE FROM recensione WHERE id_utente=? AND isbn=?

    try {
      delete.setInt(1, recensione.getUtente().getID());
      delete.setString(2, recensione.getPubblicazione().getIsbn());
      delete.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Recensione", ex);
    }
  }
}
