package model.dto;

import java.util.List;

/**
 * @author Primiano Medugno
 * @since 01/03/2020
 */

public class Utente {
  private int id;
  private String nome;
  private String cognome;
  private String email;
  private String hashPassword;
  private String ruolo;
  private List<Like> likes;
  private List<Recensione> recensioni;
  private List<Operazione> operazioni;

  // Costruttori
  public Utente () {
  }

  public Utente (int id, String nome, String cognome, String email, String hashPassword, String ruolo, List<Like> likes, List<Recensione> recensioni, List<Operazione> operazioni) {
    this.id = id;
    this.nome = nome;
    this.cognome = cognome;
    this.email = email;
    this.hashPassword = hashPassword;
    this.ruolo = ruolo;
    this.likes = likes;
    this.recensioni = recensioni;
    this.operazioni = operazioni;
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

  public String getEmail () {
    return email;
  }

  public void setEmail (String email) {
    this.email = email;
  }

  public String getHashPassword () {
    return hashPassword;
  }

  public void setHashPassword (String hashPassword) {
    this.hashPassword = hashPassword;
  }

  public String getRuolo () {
    return ruolo;
  }

  public void setRuolo (String ruolo) {
    this.ruolo = ruolo;
  }

  public List<Like> getLikes () {
    return likes;
  }

  public void setLikes (List<Like> likes) {
    this.likes = likes;
  }

  public List<Recensione> getRecensioni () {
    return recensioni;
  }

  public void setRecensioni (List<Recensione> recensioni) {
    this.recensioni = recensioni;
  }

  public List<Operazione> getOperazioni () {
    return operazioni;
  }

  public void setOperazioni (List<Operazione> operazioni) {
    this.operazioni = operazioni;
  }

  // Utility
  public void addLike (Like like) {
    this.likes.add(like);
  }

  public void removeLike (Like like) {
    this.likes.remove(like);
  }

  public void addRecensione (Recensione recensione) {
    this.recensioni.add(recensione);
  }

  public void removeRecensione (Recensione recensione) {
    this.recensioni.remove(recensione);
  }

  public void addOperazione (Operazione operazione) {
    this.operazioni.add(operazione);
  }

  public void removeOperazione (Operazione operazione) {
    this.operazioni.remove(operazione);
  }
}
