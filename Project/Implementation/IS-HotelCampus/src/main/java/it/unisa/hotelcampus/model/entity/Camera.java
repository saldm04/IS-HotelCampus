package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta una camera all'interno dell'hotel.
 * Contiene informazioni dettagliate sulla camera, come il numero, il tipo, la capacità e il costo.
 * Gestisce anche le prenotazioni associate alla camera.
 *
 * @version 1.0
 */
@Entity
public class Camera {

    /**
     * Identificativo univoco della camera.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    /**
     * Numero univoco della camera.
     */
    @Column(nullable = false, unique = true)
    private int numero;

    /**
     * Tipo della camera, definito dall'enumerazione {@link TipoCamera}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCamera tipo;

    /**
     * Numero massimo di ospiti che possono soggiornare nella camera.
     */
    @Column(nullable = false)
    private int numeroMaxOspiti;

    /**
     * Superficie della camera in metri quadrati.
     */
    @Column(nullable = false)
    private int quadratura;

    /**
     * Costo giornaliero della camera in euro.
     */
    @Column(nullable = false)
    private int costo;

    /**
     * Percorso dell'immagine rappresentativa della camera.
     */
    @Column(nullable = false)
    private String immagine;

    /**
     * Indica se la camera è stata eliminata (soft delete).
     */
    @Column(nullable = false)
    private boolean isDeleted;

    /**
     * Insieme delle prenotazioni associate a questa camera.
     */
    @OneToMany(mappedBy = "camera", cascade = CascadeType.ALL)
    private Set<Prenotazione> prenotazioni;

    /**
     * Tipi possibili di camera.
     */
    public enum TipoCamera {
        STANDARD,
        DELUXE,
        LUXURY,
        EXCLUSIVE
    }

    /**
     * Costruttore di default per la classe {@code Camera}.
     */
    public Camera() {
    }

    /**
     * Costruttore parametrizzato per la classe {@code Camera}.
     *
     * @param numero          il numero della camera
     * @param tipo            il tipo della camera
     * @param numeroMaxOspiti il numero massimo di ospiti
     * @param quadratura      la superficie della camera
     * @param costo           il costo giornaliero della camera
     * @param immagine        il percorso dell'immagine della camera
     */
    public Camera(int numero, TipoCamera tipo, int numeroMaxOspiti, int quadratura, int costo, String immagine) {
        this.numero = numero;
        this.tipo = tipo;
        this.numeroMaxOspiti = numeroMaxOspiti;
        this.quadratura = quadratura;
        this.costo = costo;
        this.immagine = immagine;
        this.isDeleted = false;
        this.prenotazioni = new HashSet<>();
    }

    /**
     * Restituisce l'identificativo della camera.
     *
     * @return l'ID della camera
     */
    public Long getId() {
        return id;
    }

    /**
     * Imposta l'identificativo della camera.
     *
     * @param id l'ID da assegnare alla camera
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce il numero della camera.
     *
     * @return il numero della camera
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Imposta il numero della camera.
     *
     * @param numero il numero da assegnare alla camera
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * Restituisce il tipo della camera.
     *
     * @return il tipo della camera
     */
    public TipoCamera getTipo() {
        return tipo;
    }

    /**
     * Imposta il tipo della camera.
     *
     * @param tipo il tipo da assegnare alla camera
     */
    public void setTipo(TipoCamera tipo) {
        this.tipo = tipo;
    }

    /**
     * Restituisce il numero massimo di ospiti.
     *
     * @return il numero massimo di ospiti
     */
    public int getNumeroMaxOspiti() {
        return numeroMaxOspiti;
    }

    /**
     * Imposta il numero massimo di ospiti.
     *
     * @param numeroMaxOspiti il numero massimo di ospiti da assegnare
     */
    public void setNumeroMaxOspiti(int numeroMaxOspiti) {
        this.numeroMaxOspiti = numeroMaxOspiti;
    }

    /**
     * Restituisce la superficie della camera.
     *
     * @return la quadratura della camera
     */
    public int getQuadratura() {
        return quadratura;
    }

    /**
     * Imposta la superficie della camera.
     *
     * @param quadratura la quadratura da assegnare alla camera
     */
    public void setQuadratura(int quadratura) {
        this.quadratura = quadratura;
    }

    /**
     * Restituisce il costo giornaliero della camera.
     *
     * @return il costo della camera
     */
    public int getCosto() {
        return costo;
    }

    /**
     * Imposta il costo giornaliero della camera.
     *
     * @param costo il costo da assegnare alla camera
     */
    public void setCosto(int costo) {
        this.costo = costo;
    }

    /**
     * Restituisce il percorso dell'immagine della camera.
     *
     * @return il percorso dell'immagine
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * Imposta il percorso dell'immagine della camera.
     *
     * @param immagine il percorso da assegnare all'immagine della camera
     */
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    /**
     * Indica se la camera è stata eliminata.
     *
     * @return {@code true} se la camera è eliminata, {@code false} altrimenti
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Imposta lo stato di eliminazione della camera.
     *
     * @param isDeleted {@code true} per eliminare la camera, {@code false} altrimenti
     */
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * Restituisce l'insieme delle prenotazioni associate a questa camera.
     *
     * @return l'insieme delle prenotazioni
     */
    public Set<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Aggiunge una prenotazione alla camera.
     *
     * @param prenotazione la prenotazione da aggiungere
     */
    public void addPrenotazione(Prenotazione prenotazione) {
        this.prenotazioni.add(prenotazione);
    }
}
