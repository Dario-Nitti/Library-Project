package model.dao;

import model.DataLayerException;

import java.util.List;

/**
 * @author Primiano Medugno
 * @since 05/03/2020
 */

public interface DataAccessObject<T> {

  // Operazioni CRUD di base che ogni DAO deve implementare
  // (indipendentemente dalle tecnologie usate)
  void store (T t) throws DataLayerException;

  T getByPK (Object obj) throws DataLayerException;

  List<T> getAll () throws DataLayerException;

  void delete (T t) throws DataLayerException;
}
