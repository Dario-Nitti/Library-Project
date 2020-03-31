package model.dto;

import java.util.List;

/**
 * @author Primiano Medugno
 * @since 01/03/2020
 */

public class Autore {
  private int id;
  private String nome;
  private String cognome;
  private List<Pubblicazione> pubblicazioni;

  // Costruttori
  public Autore () {
  }

  public Autore (int id, String nome, String cognome, List<Pubblicazione> pubblicazioni) {
    this.id = id;
    this.nome = nome;
    this.cognome = cognome;
    this.pubblicazioni = pubblicazioni;
  }

  // Getter & Setter
  public int getID () {
    return id;
  }

  public void setID (int id) {
    this.id = id;
  }

  public String getNome () {
    return nome;
  }

  public void setNome (String nome) {
    this.nome = nome;
  }

  public String getCognome () {
    return cognome;
  }

  public void setCognome (String cognome) {
    this.cognome = cognome;
  }

  public List<Pubblicazione> getPubblicazioni () {
    return pubblicazioni;
  }

  public void setPubblicazioni (List<Pubblicazione> pubblicazioni) {
    this.pubblicazioni = pubblicazioni;
  }

  // Utility
  public void addPubblicazione (Pubblicazione pubblicazione) {
    this.pubblicazioni.add(pubblicazione);
  }

  public void removePubblicazione (Pubblicazione pubblicazione) {
    this.pubblicazioni.remove(pubblicazione);
  }
}
