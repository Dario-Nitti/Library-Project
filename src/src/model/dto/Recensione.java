package model.dto;

import java.sql.Timestamp;

/**
 * @author Primiano Medugno
 * @since 01/03/2020
 */

public class Recensione {
  private Utente utente;
  private Pubblicazione pubblicazione;
  private String testo;
  private Timestamp timestamp;
  private boolean approvata;

  // Costruttori
  public Recensione () {
  }

  public Recensione (Utente utente, Pubblicazione pubblicazione, String testo, Timestamp timestamp, boolean approvata) {
    this.utente = utente;
    this.pubblicazione = pubblicazione;
    this.testo = testo;
    this.timestamp = timestamp;
    this.approvata = approvata;
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

  public String getTesto () {
    return testo;
  }

  public void setTesto (String testo) {
    this.testo = testo;
  }

  public Timestamp getTimestamp () {
    return timestamp;
  }

  public void setTimestamp (Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public boolean isApprovata () {
    return approvata;
  }

  public void setApprovata (boolean approvata) {
    this.approvata = approvata;
  }
}
