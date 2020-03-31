package model.dao.impl;

import model.DataLayerException;
import model.dao.BaseDAO;
import model.dto.Pubblicazione;
import model.dto.Ristampa;

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

public class RistampaDAO extends BaseDAO<Ristampa> {

  private PubblicazioneDAO pubblicazioneDAO = PubblicazioneDAO.getInstance();
  private PreparedStatement getPKs, selectByPK, selectByISBN, insert, delete;

  // Singleton
  private RistampaDAO () {
    try {
      init();

    } catch (DataLayerException dle) {
      dle.printStackTrace();
    }
  } // impedisce l'instanziazione dall'esterno
  private static RistampaDAO instance = new RistampaDAO();
  public static RistampaDAO getInstance () {
    return instance;
  }

  @Override
  protected void init () throws DataLayerException {

    try (Connection connection = dataLayer.getConnection()) {

      getPKs = connection.prepareStatement("SELECT isbn, data FROM ristampa ORDER BY isbn");
      selectByPK = connection.prepareStatement("SELECT * FROM ristampa where isbn=? AND data=?");
      selectByISBN = connection.prepareStatement("SELECT * FROM ristampa WHERE isbn=? ORDER BY data DESC");
      insert = connection.prepareStatement("INSERT INTO ristampa (isbn, data) VALUES (?, ?)");
      delete = connection.prepareStatement("DELETE FROM ristampa WHERE isbn=? AND data=?");

    } catch (SQLException ex) {
      throw new DataLayerException("Errore inizializzazione RistampaDAO", ex);
    }
  }

  @Override
  protected void destroy () throws DataLayerException {

    try {
      getPKs.close();
      selectByPK.close();
      selectByISBN.close();
      insert.close();
      delete.close();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore finalizzazione RistampaDAO", ex);
    }
  }

  @Override
  protected Ristampa createFromRS (ResultSet rs) throws DataLayerException {

    Ristampa ristampa = new Ristampa();
    try {
      ristampa.setPubblicazione(pubblicazioneDAO.getByPK(rs.getString("isbn")));
      ristampa.setData(rs.getDate("data"));

    } catch (SQLException ex) {
      throw new DataLayerException("Errore creazione Ristampa", ex);
    }
    return ristampa;
  }

  @Override
  public void store (Ristampa ristampa) throws DataLayerException {
    // INSERT INTO ristampa (isbn, data) VALUES (?, ?)

    try {
      insert.setString(1, ristampa.getPubblicazione().getIsbn());
      insert.setDate(2, ristampa.getData());
      insert.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore salvataggio Ristampa", ex);
    }
  }

  @Override
  public Ristampa getByPK (Object obj) throws DataLayerException {
    // SELECT * FROM ristampa where isbn=? AND data=?

    // TODO recupero PK da obj (Map?)
    try {
      selectByPK.setString(1, "");
      selectByPK.setDate(2, null);

      try (ResultSet rs = selectByPK.executeQuery()) {
        if (rs.next()) {
          return createFromRS(rs);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Ristampa", ex);
    }
    return null;
  }

  @Override
  public List<Ristampa> getAll () throws DataLayerException {
    // SELECT isbn, data FROM ristampa ORDER BY isbn

    List<Ristampa> ristampe = new ArrayList<>();
    try (ResultSet rs = getPKs.executeQuery()) {
      while (rs.next()) {
        Map<String, Object> pk = new HashMap<>();
        pk.put("isbn", rs.getString("isbn"));
        pk.put("data", rs.getDate("data"));
        ristampe.add(getByPK(pk));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Ristampe", ex);
    }
    return ristampe;
  }

  public List<Ristampa> getByPubblicazione (Pubblicazione pubblicazione) throws DataLayerException {
    // SELECT * FROM ristampa WHERE isbn=? ORDER BY data DESC

    List<Ristampa> ristampe = new ArrayList<>();
    try {
      selectByISBN.setString(1, pubblicazione.getIsbn());
      try (ResultSet rs = selectByISBN.executeQuery()) {
        while (rs.next()) {
          // TODO chiama getByPK
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Ristampe", ex);
    }
    return ristampe;
  }

  @Override
  public void delete (Ristampa ristampa) throws DataLayerException {
    // DELETE FROM ristampa WHERE isbn=? AND data=?

    try {
      delete.setString(1, ristampa.getPubblicazione().getIsbn());
      delete.setDate(2, ristampa.getData());
      delete.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Ristampa", ex);
    }
  }
}
