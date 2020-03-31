package model.dto;

import java.sql.Timestamp;

/**
 * @author Primiano Medugno
 * @since 01/03/2020
 */

public class Like {
  private Utente utente;
  private Pubblicazione pubblicazione;
  private Timestamp timestamp;

  // Costruttori
  public Like () {
  }

  public Like (Utente utente, Pubblicazione pubblicazione, Timestamp timestamp) {
    this.utente = utente;
    this.pubblicazione = pubblicazione;
    this.timestamp = timestamp;
  }

  // Getter & Setter
  public Utente getUtente () {
    return utente;
  }

  public void setUtente (Utente utente) {
    this.utente = utente;
  }

  public Pubblicazione getPubblicazione () {
    return pubblicazione;
  }

  public void setPubblicazione (Pubblicazione pubblicazione) {
    this.pubblicazione = pubblicazione;
  }

  public Timestamp getTimestamp () {
    return timestamp;
  }

  public void setTimestamp (Timestamp timestamp) {
    this.timestamp = timestamp;
  }
}
