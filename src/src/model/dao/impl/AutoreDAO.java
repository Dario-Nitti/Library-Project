package model.dao.impl;

import model.DataLayerException;
import model.dao.BaseDAO;
import model.dto.Autore;
import model.dto.Pubblicazione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 05/03/2020
 */

public class AutoreDAO extends BaseDAO<Autore> {

  private PreparedStatement getIDs, selectByID, selectByISBN, insert, update, delete, insertAutPub;

  // Singleton
  private AutoreDAO () {
    try {
      init();

    } catch (DataLayerException dle) {
      dle.printStackTrace();
    }
  } // impedisce l'instanziazione dall'esterno
  private static AutoreDAO instance = new AutoreDAO();
  public static AutoreDAO getInstance () {
    return instance;
  }

  @Override
  protected void init () throws DataLayerException {

    try (Connection connection = dataLayer.getConnection()) {
      getIDs = connection.prepareStatement("SELECT id FROM autore ORDER BY id");
      selectByID = connection.prepareStatement("SELECT * FROM autore WHERE id=?");
      selectByISBN = connection.prepareStatement("SELECT id_autore FROM autore_pubblicazione WHERE isbn=? ORDER BY id_autore");
      insert = connection.prepareStatement("INSERT INTO autore (nome, cognome) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
      update = connection.prepareStatement("UPDATE autore SET nome=?, cognome=? WHERE id=?");
      delete = connection.prepareStatement("DELETE FROM autore WHERE id=?");
      insertAutPub = connection.prepareStatement("INSERT INTO autore_pubblicazione (id_autore, isbn) VALUES (?, ?)");

    } catch (SQLException ex) {
      throw new DataLayerException("Errore inizializzazione AutoreDAO", ex);
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
      insertAutPub.close();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore finalizzazione AutoreDAO", ex);
    }
  }

  @Override
  protected Autore createFromRS (ResultSet rs) throws DataLayerException {

    Autore autore = new Autore();
    try {
      autore.setID(rs.getInt("id"));
      autore.setNome(rs.getString("nome"));
      autore.setCognome(rs.getString("cognome"));

    } catch (SQLException ex) {
      throw new DataLayerException("Errore creazione Autore", ex);
    }
    return autore;
  }

  @Override
  public void store (Autore autore) throws DataLayerException {

    int id = autore.getID();
    try {
      if (id > 0) { // UPDATE autore SET nome=?, cognome=? WHERE id=?
        update.setString(1, autore.getNome());
        update.setString(2, autore.getCognome());
        update.setInt(3, autore.getID());
        update.executeUpdate();

      } else { // INSERT INTO autore (nome, cognome) VALUES (?, ?)
        insert.setString(1, autore.getNome());
        insert.setString(2, autore.getCognome());
        if (insert.executeUpdate() == 1) {
          // leggo la chiave primaria appena generata per l'INSERT
          try (ResultSet rs = insert.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // aggiorno l'ID del DTO
          autore.setID(id);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore salvataggio Autore", ex);
    }
  }

  @Override
  public Autore getByPK (Object obj) throws DataLayerException {
    // SELECT * FROM autore WHERE id=?

    int id = (int) obj;
    try {
      selectByID.setInt(1, id);
      try (ResultSet rs = selectByID.executeQuery()) {
        if (rs.next()) {
          return createFromRS(rs);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Autore", ex);
    }
    return null;
  }

  @Override
  public List<Autore> getAll () throws DataLayerException {
    // SELECT id_autore FROM autore_pubblicazione WHERE isbn=? ORDER BY id_autore

    List<Autore> autori = new ArrayList<>();
    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        autori.add(getByPK(rs.getInt("id")));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore caricamento Autori", ex);
    }
    return autori;
  }

  public List<Autore> getByPubblicazione (Pubblicazione pubblicazione) throws DataLayerException {
    // SELECT id_autore FROM autore_pubblicazione WHERE isbn=? ORDER BY id_autore

    List<Autore> autori = new ArrayList<>();
    try {
      selectByISBN.setString(1, pubblicazione.getIsbn());
      try (ResultSet rs = selectByISBN.executeQuery()) {
        while (rs.next()) {
          autori.add(getByPK(rs.getInt("id_autore")));
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore caricamento Autori", ex);
    }
    return autori;
  }

  @Override
  public void delete (Autore autore) throws DataLayerException {
    // DELETE FROM autore WHERE id=?

    try {
      delete.setInt(1, autore.getID());
      delete.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Autore", ex);
    }
  }

  public void storeAutPub (List<Autore> autori, Pubblicazione pubblicazione) throws DataLayerException {

    for (Autore autore : autori) {
      storeAutPub(autore, pubblicazione);
    }
  }

  public void storeAutPub (Autore autore, Pubblicazione pubblicazione) throws DataLayerException {
    // INSERT INTO autore_pubblicazione (id_autore, isbn) VALUES (?, ?)

    try {
      insertAutPub.setInt(1, autore.getID());
      insertAutPub.setString(2, pubblicazione.getIsbn());
      insertAutPub.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore salvataggio Autore Pubblicazione");
    }
  }
}
