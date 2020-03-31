package model.dto;

/**
 * @author Primiano Medugno
 * @since 01/03/2020
 */

public class Sorgente {
  private int id;
  private String uri;
  private String tipo;
  private String formato;
  private String descrizione;
  private Pubblicazione pubblicazione;

  // Costruttori
  public Sorgente () {
  }

  public Sorgente (int id, String uri, String tipo, String formato, String descrizione, Pubblicazione pubblicazione) {
    this.id = id;
    this.uri = uri;
    this.tipo = tipo;
    this.formato = formato;
    this.descrizione = descrizione;
    this.pubblicazione = pubblicazione;
  }

  // Getter & Setter
  public int getID () {
    return id;
  }

  public void setID (int id) {
    this.id = id;
  }

  public String getURI () {
    return uri;
  }

  public void setURI (String uri) {
    this.uri = uri;
  }

  public String getTipo () {
    return tipo;
  }

  public void setTipo (String tipo) {
    this.tipo = tipo;
  }

  public String getFormato () {
    return formato;
  }

  public void setFormato (String formato) {
    this.formato = formato;
  }

  public String getDescrizione () {
    return descrizione;
  }

  public void setDescrizione (String descrizione) {
    this.descrizione = descrizione;
  }

  public Pubblicazione getPubblicazione () {
    return pubblicazione;
  }

  public void setPubblicazione (Pubblicazione pubblicazione) {
    this.pubblicazione = pubblicazione;
  }
}
