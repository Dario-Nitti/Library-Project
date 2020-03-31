package model.dao.impl;

import model.DataLayerException;
import model.dao.BaseDAO;
import model.dto.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 05/03/2020
 */

public class UtenteDAO extends BaseDAO<Utente> {

  private PreparedStatement getIDs, selectByID, selectByEmail, selectByEmailPassword, selectByRuolo, insert, update, delete;

  // Singleton
  private UtenteDAO () {
    try {
      init();

    } catch (DataLayerException dle) {
      dle.printStackTrace();
    }
  } // impedisce l'instanziazione dall'esterno
  private static UtenteDAO instance = new UtenteDAO();
  public static UtenteDAO getInstance () {
    return instance;
  }

  @Override
  protected void init () throws DataLayerException {

    try (Connection connection = dataLayer.getConnection()) {
      getIDs = connection.prepareStatement("SELECT id FROM utente ORDER BY id");
      selectByID = connection.prepareStatement("SELECT * FROM utente WHERE id=?");
      selectByEmail = connection.prepareStatement("SELECT * FROM utente WHERE email=?");
      selectByEmailPassword = connection.prepareStatement("SELECT * FROM utente WHERE email=? AND hashPassword=?");
      selectByRuolo = connection.prepareStatement("SELECT * FROM utente WHERE ruolo=? ORDER BY id");
      insert = connection.prepareStatement("INSERT INTO utente (nome, cognome, email, hashPassword) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      update = connection.prepareStatement("UPDATE utente SET ruolo=? WHERE id=?");
      delete = connection.prepareStatement("DELETE FROM utente WHERE id=?");

    } catch (SQLException ex) {
      throw new DataLayerException("Errore inizializzazione UtenteDAO", ex);
    }
  }

  @Override
  protected void destroy () throws DataLayerException {

    try {
      getIDs.close();
      selectByID.close();
      selectByEmail.close();
      selectByEmailPassword.close();
      selectByRuolo.close();
      insert.close();
      update.close();
      delete.close();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore finalizzazione UtenteDAO", ex);
    }
  }

  @Override
  protected Utente createFromRS (ResultSet rs) throws DataLayerException {

    Utente utente = new Utente();
    try {
      utente.setID(rs.getInt("id"));
      utente.setNome(rs.getString("nome"));
      utente.setCognome(rs.getString("cognome"));
      utente.setEmail(rs.getString("email"));
      utente.setHashPassword(rs.getString("hashPassword"));
      utente.setRuolo(rs.getString("ruolo"));

    } catch (SQLException ex) {
      throw new DataLayerException("Errore creazione Utente", ex);
    }
    return utente;
  }

  @Override
  public void store (Utente utente) throws DataLayerException {

    int id = utente.getID();
    try {
      if (id > 0) { // UPDATE utente SET ruolo=? WHERE id=?
        update.setString(1, utente.getRuolo());
        update.setInt(2, utente.getID());
        update.executeUpdate();

      } else { // INSERT INTO utente (nome, cognome, email, hashPassword) VALUES (?, ?, ?, ?)
        insert.setString(1, utente.getNome());
        insert.setString(2, utente.getCognome());
        insert.setString(3, utente.getEmail());
        insert.setString(4, utente.getHashPassword());

        if (insert.executeUpdate() == 1) {
          // leggo la chiave primaria appena generata per l'INSERT
          try (ResultSet rs = insert.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // aggiorno la chiave del DTO
          utente.setID(id);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore salvataggio Utente", ex);
    }
  }

  @Override
  public Utente getByPK (Object obj) throws DataLayerException {
    // SELECT * FROM utente WHERE id=?

    int id = (int) obj;
    try {
      selectByID.setInt(1, id);
      try (ResultSet rs = selectByID.executeQuery()) {
        if (rs.next()) {
          return createFromRS(rs);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Utente", ex);
    }
    return null;
  }

  @Override
  public List<Utente> getAll () throws DataLayerException {
    // SELECT id FROM utente ORDER BY id

    List<Utente> utenti = new ArrayList<>();
    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        utenti.add(getByPK(rs.getInt("id")));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Utenti", ex);
    }
    return utenti;
  }

  public List<Utente> getByRuolo (String ruolo) throws DataLayerException {
    // SELECT * FROM utente WHERE ruolo=? ORDER BY id

    List<Utente> utenti = new ArrayList<>();
    try {
      selectByRuolo.setString(1, ruolo);
      try (ResultSet rs = selectByRuolo.executeQuery()) {
        while (rs.next()) {
          utenti.add(getByPK(rs.getInt("id")));
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Utenti", ex);
    }
    return utenti;
  }

  public boolean checkEmail (String email) throws DataLayerException {
    // SELECT * FROM utente WHERE email=?

    try {
      selectByEmail.setString(1, email);
      try (ResultSet rs = selectByEmail.executeQuery()) {
        if (rs.next()) {
          return true;
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore controllo email Utente", ex);
    }
    return false;
  }

  public int loginUtente (String email, String hashPassword) throws DataLayerException {
    // SELECT * FROM utente WHERE email=? AND hashPassword=?

    try {
      selectByEmailPassword.setString(1, email);
      selectByEmailPassword.setString(2, hashPassword);

      try (ResultSet rs = selectByEmailPassword.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("id");
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore autenticazione Utente", ex);
    }
    return 0;
  }

  @Override
  public void delete (Utente utente) throws DataLayerException {
// DELETE FROM utente WHERE id=?

    try {
      delete.setInt(1, utente.getID());
      delete.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Utente", ex);
    }
  }

  // TODO query che estrai gli utenti più attivi (con maggior inserimenti)
}
