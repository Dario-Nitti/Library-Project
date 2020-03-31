package model.dao.impl;

import model.DataLayer;
import model.DataLayerException;
import model.dao.BaseDAO;
import model.dto.Like;
import model.dto.Pubblicazione;

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

public class LikeDAO extends BaseDAO<Like> {

  private UtenteDAO utenteDAO = UtenteDAO.getInstance();
  private PubblicazioneDAO pubblicazioneDAO = PubblicazioneDAO.getInstance();
  private PreparedStatement getPKs, selectByPK, selectByISBN, selectByUtente, insert, delete;

  // Singleton
  private LikeDAO () {
    try {
      init();

    } catch (DataLayerException dle) {
      dle.printStackTrace();
    }
  } // impedisce l'instanziazione dall'esterno
  private static LikeDAO instance = new LikeDAO();
  public static LikeDAO getInstance () {
    return instance;
  }

  @Override
  protected void init () throws DataLayerException {

    try (Connection connection = dataLayer.getConnection()) {
      getPKs = connection.prepareStatement("SELECT id_utente, isbn FROM `like` ORDER BY timestamp DESC");
      selectByPK = connection.prepareStatement("SELECT * FROM `like` WHERE id_utente=? AND isbn=?");
      selectByISBN = connection.prepareStatement("SELECT * FROM `like` WHERE isbn=? ORDER BY timestamp DESC");
      selectByUtente = connection.prepareStatement("SELECT * FROM `like` WHERE id_utente=? ORDER BY timestamp DESC");
      insert = connection.prepareStatement("INSERT INTO `like` (id_utente, isbn) VALUES (?, ?)");
      delete = connection.prepareStatement("DELETE FROM `like` WHERE id_utente=? AND isbn=?");

    } catch (SQLException ex) {
      throw new DataLayerException("Errore inizializzazione LikeDAO", ex);
    }
  }

  @Override
  protected void destroy () throws DataLayerException {

    try {
      getPKs.close();
      selectByPK.close();
      selectByISBN.close();
      selectByUtente.close();
      insert.close();
      delete.close();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore finalizzazione LikeDAO", ex);
    }
  }

  @Override
  protected Like createFromRS (ResultSet rs) throws DataLayerException {

    Like like = new Like();
    try {
      like.setUtente(utenteDAO.getByPK(rs.getInt("id_utente")));
      like.setPubblicazione(pubblicazioneDAO.getByPK(rs.getString("isbn")));
      like.setTimestamp(rs.getTimestamp("timestamp"));

    } catch (SQLException ex) {
      throw new DataLayerException("Errore creazione Like", ex);
    }
    return like;
  }

  @Override
  public void store (Like like) throws DataLayerException {
    // INSERT INTO `like` (id_utente, isbn) VALUES (?, ?)

    try {
      insert.setInt(1, like.getUtente().getID());
      insert.setString(2, like.getPubblicazione().getIsbn());
      insert.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore salvataggio Like", ex);
    }
  }

  @Override
  public Like getByPK (Object obj) throws DataLayerException {
    // SELECT * FROM `like` WHERE id_utente=? AND isbn=?

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
      throw new DataLayerException("Errore recupero Like", ex);
    }
    return null;
  }

  @Override
  public List<Like> getAll () throws DataLayerException {
    // SELECT id_utente, isbn FROM `like` ORDER BY timestamp DESC

    List<Like> likes = new ArrayList<>();
    try (ResultSet rs = getPKs.executeQuery()) {
      while (rs.next()) {
        Map<String, Object> pk = new HashMap();
        pk.put("id_utente", rs.getInt("id_utente"));
        pk.put("isbn", rs.getString("isbn"));
        likes.add(getByPK(pk));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Likes", ex);
    }
    return null;
  }

  public List<Like> getByPubblicazione (Pubblicazione pubblicazione) throws DataLayerException {
    // SELECT * FROM `like` WHERE isbn=? ORDER BY timestamp DESC

    List<Like> likes = new ArrayList<>();
    try {
      selectByISBN.setString(1, pubblicazione.getIsbn());
      try (ResultSet rs = selectByISBN.executeQuery()) {
       while (rs.next()){
         // TODO chiama getByPK
       }
      }
    } catch (SQLException ex){
      throw new DataLayerException("Errore recupero Likes", ex);
    }
    return likes;
  }

  @Override
  public void delete (Like like) throws DataLayerException {
    // DELETE FROM `like` WHERE id_utente=? AND isbn=?

    try {
      delete.setInt(1, like.getUtente().getID());
      delete.setString(2, like.getPubblicazione().getIsbn());
      delete.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Like", ex);
    }
  }
}
