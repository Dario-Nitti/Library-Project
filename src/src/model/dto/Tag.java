package model.dto;

import java.util.List;

/**
 * @author Primiano Medugno
 * @since 01/03/2020
 */

public class Tag {
  private int id;
  private String nome;
  private List<Pubblicazione> pubblicazioni;

  // Costruttori
  public Tag () {
  }

  public Tag (int id, String nome, List<Pubblicazione> pubblicazioni) {
    this.id = id;
    this.nome = nome;
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
