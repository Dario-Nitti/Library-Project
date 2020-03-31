package model;

import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Primiano Medugno
 * @since 05/03/2020
 */

public class DataLayer {
  private static final MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource("jdbc:mariadb://localhost:3306/librarydb?user=root&maxPoolSize=10");
  private static final DataLayer instance = new DataLayer();

  // Singleton
  private DataLayer () {
    init();
  } // Impedisce l'istanziazione all'esterno

  public static DataLayer getInstance () {
    return instance;
  }

  /**
   * Recupero una connessione, tramite Connection Pool, dal DataSource
   *
   * @return Connessione al DB
   * @throws SQLException in caso di errori nel recupero della connessione
   */
  public Connection getConnection () throws SQLException {
    return dataSource.getConnection();
  }

  // Registro i DAO all'interno del DataLayer
  private void init () {
    // TODO associo i DAO al DataLayer
  }

  // Chiudo il DataSource all'uscita dell'applicazione
  public void close () {
    dataSource.close();
  }
}
