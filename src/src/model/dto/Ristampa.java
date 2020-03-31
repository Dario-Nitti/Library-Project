package model.dto;

import java.sql.Date;

/**
 * @author Primiano Medugno
 * @since 01/03/2020
 */

public class Ristampa {
  private int id;
  private Pubblicazione pubblicazione;
  private Date data;

  // Costruttori
  public Ristampa () {
  }

  public Ristampa (int id, Pubblicazione pubblicazione, Date data) {
    this.id = id;
    this.pubblicazione = pubblicazione;
    this.data = data;
  }

  // Getter & Setter
  public Pubblicazione getPubblicazione () {
    return pubblicazione;
  }

  public void setPubblicazione (Pubblicazione pubblicazione) {
    this.pubblicazione = pubblicazione;
  }

  public Date getData () {
    return data;
  }

  public void setData (Date data) {
    this.data = data;
  }
}
