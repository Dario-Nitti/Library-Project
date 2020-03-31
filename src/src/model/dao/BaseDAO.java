package model.dao;

import model.DataLayer;
import model.DataLayerException;

import java.sql.ResultSet;

/**
 * @author Primiano Medugno
 * @since 15/03/2020
 */
public abstract class BaseDAO<T> implements DataAccessObject<T> {
  // Istanza del DataLayer condivisa con tutti i DAO che estendono la classe
  protected final DataLayer dataLayer = DataLayer.getInstance();

  // Utility d'inizializzazione e finalizzazione per i DAO
  protected abstract void init () throws DataLayerException;

  protected abstract void destroy () throws DataLayerException;

  // Metodo che permette a ogni DAO di estrarre un corrispettivo DTO dal ResultSet
  protected abstract T createFromRS (ResultSet rs) throws DataLayerException;
}
