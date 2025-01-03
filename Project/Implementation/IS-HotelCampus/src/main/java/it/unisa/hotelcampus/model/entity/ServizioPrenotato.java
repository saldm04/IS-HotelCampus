package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

/**
 * Rappresenta un servizio aggiuntivo prenotato all'interno di una prenotazione.
 * Contiene informazioni sul costo unitario, il numero di servizi prenotati e le relazioni con il servizio e la prenotazione.
 *
 * @version 1.0
 */
@Entity
public class ServizioPrenotato {

    /**
     * Identificativo univoco del servizio prenotato.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    /**
     * Costo unitario del servizio al momento della prenotazione.
     */
    @Column(nullable = false)
    private int costoUnitario;

    /**
     * Numero di unità del servizio prenotato.
     */
    @Column(nullable = false)
    private int numeroServizi;

    /**
     * Servizio associato a questa prenotazione.
     */
    @ManyToOne
    @JoinColumn(name = "servizio_id", nullable = false)
    private Servizio servizio;

    /**
     * Prenotazione alla quale è associato questo servizio prenotato.
     */
    @ManyToOne
    @JoinColumn(name = "prenotazione_id", nullable = false)
    private Prenotazione prenotazione;

    /**
     * Costruttore di default per la classe {@code ServizioPrenotato}.
     */
    public ServizioPrenotato() {
    }

    /**
     * Costruttore parametrizzato per la classe {@code ServizioPrenotato}.
     *
     * @param costoUnitario il costo unitario del servizio
     * @param numeroServizi  il numero di servizi prenotati
     * @param servizio       il servizio associato
     */
    public ServizioPrenotato(int costoUnitario, int numeroServizi, Servizio servizio) {
        this.costoUnitario = costoUnitario;
        this.numeroServizi = numeroServizi;
        this.servizio = servizio;
    }

    /**
     * Restituisce il costo unitario del servizio prenotato.
     *
     * @return il costo unitario
     */
    public int getCostoUnitario() {
        return costoUnitario;
    }

    /**
     * Imposta il costo unitario del servizio prenotato.
     *
     * @param costoUnitario il costo unitario da assegnare
     */
    public void setCostoUnitario(int costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    /**
     * Restituisce l'identificativo del servizio prenotato.
     *
     * @return l'ID del servizio prenotato
     */
    public Long getId() {
        return id;
    }

    /**
     * Imposta l'identificativo del servizio prenotato.
     *
     * @param id l'ID da assegnare
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce il numero di servizi prenotati.
     *
     * @return il numero di servizi prenotati
     */
    public int getNumeroServizi() {
        return numeroServizi;
    }

    /**
     * Imposta il numero di servizi prenotati.
     *
     * @param numeroServizi il numero di servizi da assegnare
     */
    public void setNumeroServizi(int numeroServizi) {
        this.numeroServizi = numeroServizi;
    }

    /**
     * Restituisce il servizio associato a questo servizio prenotato.
     *
     * @return il servizio
     */
    public Servizio getServizio() {
        return servizio;
    }

    /**
     * Imposta il servizio associato a questo servizio prenotato.
     *
     * @param servizio il servizio da associare
     */
    public void setServizio(Servizio servizio) {
        this.servizio = servizio;
    }

    /**
     * Restituisce la prenotazione associata a questo servizio prenotato.
     *
     * @return la prenotazione
     */
    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    /**
     * Imposta la prenotazione associata a questo servizio prenotato.
     *
     * @param prenotazione la prenotazione da associare
     */
    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    /**
     * Restituisce una rappresentazione testuale del servizio prenotato.
     *
     * @return una stringa che rappresenta il servizio prenotato
     */
    @Override
    public String toString() {
        return "ServizioPrenotato{" +
                "costoUnitario=" + costoUnitario +
                ", id=" + id +
                ", numeroServizi=" + numeroServizi +
                ", servizio=" + (servizio != null ? servizio.getId() : "null") +
                '}';
    }
}
