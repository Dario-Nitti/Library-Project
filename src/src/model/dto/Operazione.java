package model.dto;

import java.sql.Timestamp;

/**
 * @author Primiano Medugno
 * @since 01/03/2020
 */

public class Operazione {
  private int id;
  private Timestamp timestamp;
  private String tipo;
  private String descrizione;
  private Utente utente;
  private Pubblicazione pubblicazione;

  // Costruttori
  public Operazione () {
  }

  public Operazione (int id, Timestamp timestamp, String tipo, String descrizione, Utente utente, Pubblicazione pubblicazione) {
    this.id = id;
    this.timestamp = timestamp;
    this.tipo = tipo;
    this.descrizione = descrizione;
    this.utente = utente;
    this.pubblicazione = pubblicazione;
  }

  // Getter & Setter
  public int getID () {
    return id;
  }

  public void setID (int id) {
    this.id = id;
  }

  public Timestamp getTimestamp () {
    return timestamp;
  }

  public void setTimestamp (Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public String getTipo () {
    return tipo;
  }

  public void setTipo (String tipo) {
    this.tipo = tipo;
  }

  public String getDescrizione () {
    return descrizione;
  }

  public void setDescrizione (String descrizione) {
    this.descrizione = descrizione;
  }

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
}
