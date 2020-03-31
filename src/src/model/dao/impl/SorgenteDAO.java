package model.dao.impl;

import model.DataLayerException;
import model.dao.BaseDAO;
import model.dto.Pubblicazione;
import model.dto.Sorgente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 05/03/2020
 */

public class SorgenteDAO extends BaseDAO<Sorgente> {

  private PubblicazioneDAO pubblicazioneDAO = PubblicazioneDAO.getInstance();
  private PreparedStatement getIDs, selectByID, selectByISBN, selectByTipo, insert, delete;

  // Singleton
  private SorgenteDAO () {
    try {
      init();

    } catch (DataLayerException dle) {
      dle.printStackTrace();
    }
  } // impedisce l'instanziazione dall'esterno
  private static SorgenteDAO instance = new SorgenteDAO();
  public static SorgenteDAO getInstance () {
    return instance;
  }

  @Override
  protected void init () throws DataLayerException {

    try (Connection connection = dataLayer.getConnection()) {

      getIDs = connection.prepareStatement("SELECT id FROM sorgente ORDER BY isbn");
      selectByID = connection.prepareStatement("SELECT * FROM sorgente WHERE id=?");
      selectByISBN = connection.prepareStatement("SELECT * FROM sorgente WHERE isbn=? ORDER BY tipo");
      selectByTipo = connection.prepareStatement("SELECT * FROM sorgente WHERE tipo=? ORDER BY isbn");
      insert = connection.prepareStatement("INSERT INTO sorgente (uri, tipo, formato, descrizione, isbn) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      delete = connection.prepareStatement("DELETE FROM sorgente WHERE id=?");


    } catch (SQLException ex) {
      throw new DataLayerException("Errore inizializzazione SorgenteDAO", ex);
    }
  }

  @Override
  protected void destroy () throws DataLayerException {

    try {
      getIDs.close();
      selectByID.close();
      selectByISBN.close();
      selectByTipo.close();
      insert.close();
      delete.close();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore finalizzazione SorgenteDAO", ex);
    }
  }

  @Override
  protected Sorgente createFromRS (ResultSet rs) throws DataLayerException {

    Sorgente sorgente = new Sorgente();
    try {
      sorgente.setID(rs.getInt("id"));
      sorgente.setURI(rs.getString("uri"));
      sorgente.setTipo(rs.getString("tipo"));
      sorgente.setFormato(rs.getString("formato"));
      sorgente.setDescrizione(rs.getString("descrizione"));
      sorgente.setPubblicazione(pubblicazioneDAO.getByPK(rs.getString("isbn")));

    } catch (SQLException ex) {
      throw new DataLayerException("Errore creazione Sorgente", ex);
    }
    return sorgente;
  }

  @Override
  public void store (Sorgente sorgente) throws DataLayerException {
    // INSERT INTO sorgente (uri, tipo, formato, descrizione, isbn) VALUES (?, ?, ?, ?, ?)

    try {
      insert.setString(1, sorgente.getURI());
      insert.setString(2, sorgente.getTipo());
      insert.setString(3, sorgente.getFormato());
      insert.setString(4, sorgente.getDescrizione());
      insert.setString(5, sorgente.getPubblicazione().getIsbn());

      if (insert.executeUpdate() == 1) {
        int id = 0;
        // leggo la chiave primaria appena generata per l'INSERT
        try (ResultSet rs = insert.getGeneratedKeys()) {
          if (rs.next()) {
            id = rs.getInt("id");
          }
        }
        // aggiorno l'ID del DTO
        sorgente.setID(id);
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore salvataggio Sorgente", ex);
    }
  }

  @Override
  public Sorgente getByPK (Object obj) throws DataLayerException {
    // SELECT * FROM sorgente WHERE id=?

    int id = (int) obj;
    try {
      selectByID.setInt(1, id);
      try (ResultSet rs = selectByID.executeQuery()) {
        if (rs.next()) {
          return createFromRS(rs);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Sorgente", ex);
    }
    return null;
  }

  @Override
  public List<Sorgente> getAll () throws DataLayerException {
    // SELECT id FROM sorgente ORDER BY isbn

    List<Sorgente> sorgenti = new ArrayList<>();
    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        sorgenti.add(getByPK(rs.getInt("id")));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Sorgenti", ex);
    }
    return sorgenti;
  }

  public List<Sorgente> getByPubblicazione (Pubblicazione pubblicazione) throws DataLayerException {
    // SELECT * FROM sorgente WHERE isbn=? ORDER BY tipo

    List<Sorgente> sorgenti = new ArrayList<>();
    try {
      selectByISBN.setString(1, pubblicazione.getIsbn());
      try (ResultSet rs = selectByISBN.executeQuery()) {
        while (rs.next()) {
          sorgenti.add(getByPK(rs.getInt("id")));
        }
      }

    } catch (SQLException ex) {
      throw new DataLayerException("Errore caricamento Sorgenti", ex);
    }
    return sorgenti;
  }

  public List<Sorgente> getByTipo (String tipo) throws DataLayerException {
    // SELECT * FROM sorgente WHERE tipo=? ORDER BY isbn

    List<Sorgente> sorgenti = new ArrayList<>();
    try {
      selectByTipo.setString(1, tipo);
      try (ResultSet rs = selectByTipo.executeQuery()) {
        while (rs.next()) {
          sorgenti.add(getByPK(rs.getInt("id")));
        }
      }

    } catch (SQLException ex) {
      throw new DataLayerException("Errore caricamento Sorgenti", ex);
    }
    return sorgenti;
  }

  @Override
  public void delete (Sorgente sorgente) throws DataLayerException {
    // DELETE FROM sorgente WHERE id=?

    try {
      delete.setInt(1, sorgente.getID());
      delete.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Sorgente", ex);
    }
  }
}
