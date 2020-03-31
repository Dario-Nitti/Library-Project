package model.dao.impl;

import model.DataLayerException;
import model.dao.BaseDAO;
import model.dto.Operazione;
import model.dto.Pubblicazione;

import java.sql.*;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 05/03/2020
 */

public class OperazioneDAO extends BaseDAO<Operazione> {

  private PreparedStatement getIDs, selectByID, selectByISBN, selectByUtenteInserimento, insert, update, delete;
  // TODO Estrazione elenco degli utenti più collaborativi (cioè quelli che hanno inserito più pubblicazioni)

  // Singleton
  private OperazioneDAO () {
    try {
      init();

    } catch (DataLayerException dle) {
      dle.printStackTrace();
    }
  } // impedisce l'instanziazione dall'esterno
  private static OperazioneDAO instance = new OperazioneDAO();
  public static OperazioneDAO getInstance () {
    return instance;
  }

  @Override
  protected void init () throws DataLayerException {

    try (Connection connection = dataLayer.getConnection()) {
      getIDs = connection.prepareStatement("SELECT id FROM operazione ORDER BY timestamp DESC");
      selectByID = connection.prepareStatement("SELECT * FROM operazione WHERE id=?");
      selectByISBN = connection.prepareStatement("SELECT id FROM operazione WHERE isbn=? ORDER BY timestamp DESC");
      insert = connection.prepareStatement("INSERT INTO operazione (tipo, descrizione, id_utente, isbn) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      update = connection.prepareStatement("UPDATE operazione SET tipo=?, descrizione=?, id_utente=?, isbn=? WHERE id=?");
      delete = connection.prepareStatement("DELETE FROM operazione WHERE id=?");

    } catch (SQLException ex) {
      throw new DataLayerException("Errore inizializzazione OperazioneDAO", ex);
    }
  }

  @Override
  protected void destroy () throws DataLayerException {

    try {
      getIDs.close();
      selectByID.close();
      selectByISBN.close();
      insert.close();
      update.close();
      delete.close();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore finalizzazione OperazioneDAO", ex);
    }
  }

  @Override
  protected Operazione createFromRS (ResultSet rs) throws DataLayerException {

    Operazione operazione = new Operazione();
    try {
      operazione.setID(rs.getInt("id"));
      operazione.setTimestamp(rs.getTimestamp("timestamp"));
      operazione.setTipo(rs.getString("tipo"));
      operazione.setDescrizione(rs.getString("descrizione"));
      UtenteDAO utenteDAO = UtenteDAO.getInstance();
      operazione.setUtente(utenteDAO.getByPK(rs.getInt("id_utente")));
      PubblicazioneDAO pubblicazioneDAO = PubblicazioneDAO.getInstance();
      operazione.setPubblicazione(pubblicazioneDAO.getByPK(rs.getString("isbn")));

    } catch (SQLException ex) {
      throw new DataLayerException("Errore creazione Operazione", ex);
    }
    return operazione;
  }

  @Override
  public void store (Operazione operazione) throws DataLayerException {

    int id = operazione.getID();
    try {
      if (id > 0) { // UPDATE operazione SET tipo=?, descrizione=?, id_utente=?, isbn=? WHERE id=?
        update.setString(1, operazione.getTipo());
        update.setString(2, operazione.getDescrizione());
        update.setInt(3, operazione.getUtente().getID());
        update.setString(4, operazione.getPubblicazione().getIsbn());
        update.setInt(5, operazione.getID());
        update.executeUpdate();

      } else { // INSERT INTO operazione (tipo, descrizione, id_utente, isbn) VALUES (?, ?, ?, ?)
        insert.setString(1, operazione.getTipo());
        insert.setString(2, operazione.getDescrizione());
        insert.setInt(3, operazione.getUtente().getID());
        insert.setString(4, operazione.getPubblicazione().getIsbn());
        if (insert.executeUpdate() == 1) {
          // leggo la chiave primaria appena generata per l'INSERT
          try (ResultSet rs = insert.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // aggiorno l'ID del DTO
          operazione.setID(id);
        }
      }
      // aggiorno il timestamp del DTO
      operazione.setTimestamp(getByPK(id).getTimestamp());

    } catch (SQLException ex) {
      throw new DateTimeException("Errore salvataggio Operazione", ex);
    }
  }

  @Override
  public Operazione getByPK (Object obj) throws DataLayerException {
    // SELECT * FROM operazione WHERE id=?

    int id = (int) obj;
    try {
      selectByID.setInt(1, id);
      try (ResultSet rs = selectByID.executeQuery()) {
        if (rs.next()) {
          return createFromRS(rs);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Operazione", ex);
    }
    return null;
  }

  @Override
  public List<Operazione> getAll () throws DataLayerException {
    // SELECT id FROM operazione ORDER BY timestamp DESC

    List<Operazione> operazioni = new ArrayList<>();
    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        operazioni.add(getByPK(rs.getInt("id")));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore caricamento Operazioni", ex);
    }
    return operazioni;
  }

  public List<Operazione> getByPubblicazione (Pubblicazione pubblicazione) throws DataLayerException {
    // SELECT id FROM operazione WHERE isbn=? ORDER BY timestamp DESC

    List<Operazione> operazioni = new ArrayList<>();
    try {
      selectByISBN.setString(1, pubblicazione.getIsbn());
      try (ResultSet rs = selectByISBN.executeQuery()) {
        while (rs.next()) {
          operazioni.add(getByPK(rs.getInt("id")));
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore caricamento Operazioni", ex);
    }
    return operazioni;
  }

  @Override
  public void delete (Operazione operazione) throws DataLayerException {
    // DELETE FROM operazione WHERE id=?

    try {
      delete.setInt(1, operazione.getID());
      delete.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Operazione", ex);
    }
  }
}
