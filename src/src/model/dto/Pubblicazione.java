package model.dto;

import java.sql.Date;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 01/03/2020
 */

public class Pubblicazione {
  private String isbn;
  private String titolo;
  private String editore;
  private Date data;
  private String lingua;
  private int pagine;
  private String descrizione;
  private String indice;
  private List<Autore> autori;
  private List<Ristampa> ristampe;
  private List<Sorgente> sorgenti;
  private List<Tag> tags;
  private List<Operazione> operazioni;
  private List<Recensione> recensioni;
  private List<Like> likes;

  // Costruttori
  public Pubblicazione () {
  }

  public Pubblicazione (String isbn, String titolo, String editore, Date data, String lingua, int pagine, String descrizione, String indice, List<Autore> autori, List<Ristampa> ristampe, List<Sorgente> sorgenti, List<Tag> tags, List<Operazione> operazioni, List<Recensione> recensioni, List<Like> likes) {
    this.isbn = isbn;
    this.titolo = titolo;
    this.editore = editore;
    this.data = data;
    this.lingua = lingua;
    this.pagine = pagine;
    this.descrizione = descrizione;
    this.indice = indice;
    this.autori = autori;
    this.ristampe = ristampe;
    this.sorgenti = sorgenti;
    this.tags = tags;
    this.operazioni = operazioni;
    this.recensioni = recensioni;
    this.likes = likes;
  }

  // Getter & Setter
  public String getIsbn () {
    return isbn;
  }

  public void setIsbn (String isbn) {
    this.isbn = isbn;
  }

  public String getTitolo () {
    return titolo;
  }

  public void setTitolo (String titolo) {
    this.titolo = titolo;
  }

  public String getEditore () {
    return editore;
  }

  public void setEditore (String editore) {
    this.editore = editore;
  }

  public Date getData () {
    return data;
  }

  public void setData (Date data) {
    this.data = data;
  }

  public String getLingua () {
    return lingua;
  }

  public void setLingua (String lingua) {
    this.lingua = lingua;
  }

  public int getPagine () {
    return pagine;
  }

  public void setPagine (int pagine) {
    this.pagine = pagine;
  }

  public String getDescrizione () {
    return descrizione;
  }

  public void setDescrizione (String descrizione) {
    this.descrizione = descrizione;
  }

  public String getIndice () {
    return indice;
  }

  public void setIndice (String indice) {
    this.indice = indice;
  }

  public List<Autore> getAutori () {
    return autori;
  }

  public void setAutori (List<Autore> autori) {
    this.autori = autori;
  }

  public List<Ristampa> getRistampe () {
    return ristampe;
  }

  public void setRistampe (List<Ristampa> ristampe) {
    this.ristampe = ristampe;
  }

  public List<Sorgente> getSorgenti () {
    return sorgenti;
  }

  public void setSorgenti (List<Sorgente> sorgenti) {
    this.sorgenti = sorgenti;
  }

  public List<Tag> getTags () {
    return tags;
  }

  public void setTags (List<Tag> tags) {
    this.tags = tags;
  }

  public List<Operazione> getOperazioni () {
    return operazioni;
  }

  public void setOperazioni (List<Operazione> operazioni) {
    this.operazioni = operazioni;
  }

  public List<Recensione> getRecensioni () {
    return recensioni;
  }

  public void setRecensioni (List<Recensione> recensioni) {
    this.recensioni = recensioni;
  }

  public List<Like> getLikes () {
    return likes;
  }

  public void setLikes (List<Like> likes) {
    this.likes = likes;
  }

  // Utility
  public void addAutore (Autore autore) {
    this.autori.add(autore);
  }

  public void removeAutore (Autore autore) {
    this.autori.remove(autore);
  }

  public void addRistampa (Ristampa ristampa) {
    this.ristampe.add(ristampa);
  }

  public void removeRistampa (Ristampa ristampa) {
    this.ristampe.remove(ristampa);
  }

  public void addSorgente (Sorgente sorgente) {
    this.sorgenti.add(sorgente);
  }

  public void removeSorgente (Sorgente sorgente) {
    this.sorgenti.remove(sorgente);
  }

  public void addTag (Tag tag) {
    this.tags.add(tag);
  }

  public void removeTag (Tag tag) {
    this.tags.remove(tag);
  }

  public void addOperazione (Operazione operazione) {
    this.operazioni.add(operazione);
  }

  public void removeOperazione (Operazione operazione) {
    this.operazioni.remove(operazione);
  }

  public void addRecensione (Recensione recensione) {
    this.recensioni.add(recensione);
  }

  public void removeRecensione (Recensione recensione) {
    this.recensioni.remove(recensione);
  }

  public void addLike (Like like) {
    this.likes.add(like);
  }

  public void removeLike (Like like) {
    this.likes.remove(like);
  }
}
