package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

/**
 * Rappresenta un servizio aggiuntivo offerto dall'hotel.
 * Contiene informazioni sul nome, descrizione, costo e immagine del servizio.
 * Gestisce anche lo stato di eliminazione (soft delete) del servizio.
 *
 * @version 1.0
 */
@Entity
public class Servizio {

    /**
     * Identificativo univoco del servizio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    /**
     * Nome del servizio.
     */
    @Column(nullable = false)
    private String nome;

    /**
     * Descrizione dettagliata del servizio.
     */
    @Column(nullable = false, length = 600)
    private String descrizione;

    /**
     * Costo del servizio in euro.
     */
    @Column(nullable = false)
    private int costo;

    /**
     * Percorso dell'immagine rappresentativa del servizio.
     */
    @Column(nullable = false)
    private String immagine;

    /**
     * Indica se il servizio è stato eliminato (soft delete).
     */
    @Column(nullable = false)
    private boolean isDeleted;

    /**
     * Costruttore di default per la classe {@code Servizio}.
     */
    public Servizio() {
    }

    /**
     * Costruttore parametrizzato per la classe {@code Servizio}.
     *
     * @param nome        il nome del servizio
     * @param descrizione la descrizione del servizio
     * @param costo       il costo del servizio
     * @param immagine    il percorso dell'immagine del servizio
     */
    public Servizio(String nome, String descrizione, int costo, String immagine) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.costo = costo;
        this.immagine = immagine;
        this.isDeleted = false;
    }

    /**
     * Restituisce il costo del servizio.
     *
     * @return il costo del servizio
     */
    public int getCosto() {
        return costo;
    }

    /**
     * Imposta il costo del servizio.
     *
     * @param costo il costo da assegnare al servizio
     */
    public void setCosto(int costo) {
        this.costo = costo;
    }

    /**
     * Restituisce la descrizione del servizio.
     *
     * @return la descrizione del servizio
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione del servizio.
     *
     * @param descrizione la descrizione da assegnare al servizio
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce l'identificativo del servizio.
     *
     * @return l'ID del servizio
     */
    public Long getId() {
        return id;
    }

    /**
     * Imposta l'identificativo del servizio.
     *
     * @param id l'ID da assegnare al servizio
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce il percorso dell'immagine del servizio.
     *
     * @return il percorso dell'immagine
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * Imposta il percorso dell'immagine del servizio.
     *
     * @param immagine il percorso da assegnare all'immagine del servizio
     */
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    /**
     * Indica se il servizio è stato eliminato.
     *
     * @return {@code true} se il servizio è eliminato, {@code false} altrimenti
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Imposta lo stato di eliminazione del servizio.
     *
     * @param deleted {@code true} per eliminare il servizio, {@code false} altrimenti
     */
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    /**
     * Restituisce il nome del servizio.
     *
     * @return il nome del servizio
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del servizio.
     *
     * @param nome il nome da assegnare al servizio
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce una rappresentazione testuale del servizio.
     *
     * @return una stringa che rappresenta il servizio
     */
    @Override
    public String toString() {
        return "Servizio{" +
                "costo=" + costo +
                ", id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", immagine='" + immagine + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
