package model.dao.impl;

import model.DataLayerException;
import model.dao.BaseDAO;
import model.dto.Pubblicazione;
import model.dto.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 05/03/2020
 */

public class TagDAO extends BaseDAO<Tag> {
  /*
  Use Case:
  1- Creo una pubblicazione e le associo alcuni tag (tag che saranno già presenti nel sistema).
      -> insertTagPub (2+)
      -> insertTagPub (1)

  2- Cambio i tag associati a una pubblicazione
      -> deleteAllTagPub e poi insertTagPub

  3- Rimuovo una pubblicazione
      -> deleteAllTagFromPub

  4- Rimuovo un tag dalla pubblicazione
      -> deleteTagFromPub

  deleteTagPub (tag, pub) -> rimuove quel singolo tag dalla pubblicazione.
  deleteAllTagPub (pub) -> rimuove tutti i tag dalla pubblicazione


  */
  private PreparedStatement getIDs, selectByID, selectByISBN, insertTag, insertTagPub, updateTag, deleteTag, deleteTagPub, deleteAllTagPub;

  //Singleton
  private TagDAO () {
    try {
      init();

    } catch (DataLayerException dle) {
      //dle.printStackTrace();
    }
  } // impedisce l'instanziazione dall'esterno
  private static TagDAO instance = new TagDAO();
  public static TagDAO getInstance () {
    return instance;
  }


  @Override
  protected void init () throws DataLayerException {
    try (Connection connection = dataLayer.getConnection()) {
      getIDs = connection.prepareStatement("SELECT id FROM tag ORDER BY id");
      selectByID = connection.prepareStatement("SELECT * FROM tag WHERE id=?");
      selectByISBN = connection.prepareStatement("SELECT id_tag FROM tag_pubblicazione WHERE isbn=?");
      insertTag = connection.prepareStatement("INSERT INTO tag (nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      updateTag = connection.prepareStatement("UPDATE tag SET nome=? WHERE id=?"); // probabilmente non utilizzata
      deleteTag = connection.prepareStatement("DELETE FROM tag WHERE id=?"); // probabilmente non utilizzata
      insertTagPub = connection.prepareStatement("INSERT INTO tag_pubblicazione (id_tag, isbn) VALUES (?, ?)");
      deleteTagPub = connection.prepareStatement("DELETE FROM tag_pubblicazione WHERE id_tag=? AND isbn=?");
      deleteAllTagPub = connection.prepareStatement("DELETE FROM tag_pubblicazione WHERE isbn=?");

    } catch (SQLException ex) {
      throw new DataLayerException("Errore inizializzazione TagDAO", ex);
    }
  }

  @Override
  protected void destroy () throws DataLayerException {
    try {
      getIDs.close();
      selectByID.close();
      selectByISBN.close();
      insertTag.close();
      updateTag.close();
      deleteTag.close();
      insertTagPub.close();
      deleteTagPub.close();
      deleteAllTagPub.close();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore finalizzazione TagDAO", ex);
    }
  }

  @Override
  protected Tag createFromRS (ResultSet rs) throws DataLayerException {
    try {
      Tag tag = new Tag();

      tag.setID(rs.getInt("id"));
      tag.setNome(rs.getString("nome"));


      return tag;

    } catch (SQLException ex) {
      throw new DataLayerException("Errore creazione Tag", ex);
    }
  }

  @Override
  public void store (Tag tag) throws DataLayerException {
    int id = tag.getID();
    try {
      if (id > 0) { // UPDATE tag SET nome=? WHERE id=?
        updateTag.setString(1, tag.getNome());
        updateTag.setInt(2, tag.getID());
        updateTag.executeUpdate();

      } else { // INSERT INTO tag (nome) VALUES (?)
        insertTag.setString(1, tag.getNome());

        if (insertTag.executeUpdate() == 1) {
          // leggo la chiave primaria appena generata per l'INSERT
          try (ResultSet rs = insertTag.getGeneratedKeys()) {
            if (rs.next()) {
              id = rs.getInt("id");
            }
          }
          // aggiorno la chiave del DTO
          tag.setID(id);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore salvataggio Tag", ex);
    }
  }

  @Override
  public Tag getByPK (Object obj) throws DataLayerException {
    // SELECT * FROM tag WHERE id=?

    int id = (int) obj;
    try {
      selectByID.setInt(1, id);
      try (ResultSet rs = selectByID.executeQuery()) {
        if (rs.next()) {
          return createFromRS(rs);
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Tag", ex);
    }
    return null;
  }

  @Override
  public List<Tag> getAll () throws DataLayerException {
    // SELECT id FROM tag ORDER BY id

    List<Tag> tags = new ArrayList<>();

    try (ResultSet rs = getIDs.executeQuery()) {
      while (rs.next()) {
        tags.add(getByPK(rs.getInt("id")));
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore recupero Tags");
    }
    return tags;
  }

  @Override
  public void delete (Tag tag) throws DataLayerException {
    // DELETE FROM tag WHERE id=?

    try {
      deleteTag.setInt(1, tag.getID());
      deleteTag.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Tag");
    }
  }

  public void storeTagPub (List<Tag> tags, Pubblicazione pubblicazione) throws DataLayerException {
    for (Tag tag : tags) {
      storeTagPub(tag, pubblicazione);
    }
  }

  public void storeTagPub (Tag tag, Pubblicazione pubblicazione) throws DataLayerException {
    // INSERT INTO tag_pubblicazione (id_tag, isbn) VALUES (?, ?)

    try {
      insertTagPub.setInt(1, tag.getID());
      insertTagPub.setString(2, pubblicazione.getIsbn());
      insertTagPub.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore salvataggio Tag Pubblicazione", ex);
    }
  }

  public List<Tag> getTagPub (Pubblicazione pubblicazione) throws DataLayerException {
    // SELECT id_tag FROM tag_pubblicazione WHERE isbn=?

    List<Tag> tags = new ArrayList<>();

    try {
      selectByISBN.setString(1, pubblicazione.getIsbn());

      try (ResultSet rs = selectByISBN.executeQuery()) {
        while (rs.next()) {
          tags.add(getByPK(rs.getInt("id_tag")));
        }
      }
    } catch (SQLException ex) {
      throw new DataLayerException("Errore caricamento Tags Pubblicazione", ex);
    }
    return tags;
  }

  public void deleteTagPub (Tag tag, Pubblicazione pubblicazione) throws DataLayerException {
    // DELETE FROM tag_pubblicazione WHERE id_tag=? AND isbn=?

    try {
      deleteTagPub.setInt(1, tag.getID());
      deleteTagPub.setString(2, pubblicazione.getIsbn());
      deleteTagPub.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Tag Pubblicazione");
    }
  }

  public void deleteAllTagPub (Pubblicazione pubblicazione) throws DataLayerException {
    // DELETE FROM tag_pubblicazione WHERE isbn=?

    try {
      deleteAllTagPub.setString(1, pubblicazione.getIsbn());
      deleteAllTagPub.executeUpdate();

    } catch (SQLException ex) {
      throw new DataLayerException("Errore rimozione Tags Pubblicazione");
    }
  }
}
