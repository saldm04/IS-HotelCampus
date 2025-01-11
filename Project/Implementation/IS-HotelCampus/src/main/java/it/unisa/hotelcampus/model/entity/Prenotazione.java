package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

/**
 * Rappresenta una prenotazione effettuata da un cliente.
 * Contiene informazioni sulle date di prenotazione, check-in, check-out, numero di ospiti e costi associati.
 * Gestisce le relazioni con la camera prenotata e i servizi aggiuntivi.
 *
 * @version 1.0
 */
@Entity
public class Prenotazione {

    /**
     * Identificativo univoco della prenotazione.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    /**
     * Data in cui è stata effettuata la prenotazione.
     */
    @Column(nullable = false)
    private LocalDate dataPrenotazione;

    /**
     * Data di check-in prevista per la prenotazione.
     */
    @Column(nullable = false)
    private LocalDate dataCheckIn;

    /**
     * Data di check-out prevista per la prenotazione.
     */
    @Column(nullable = false)
    private LocalDate dataCheckOut;

    /**
     * Numero di ospiti inclusi nella prenotazione.
     */
    @Column(nullable = false)
    private int numeroOspiti;

    /**
     * Importo totale della prenotazione, calcolato in base ai costi della camera e dei servizi prenotati.
     */
    @Column(nullable = false)
    private int importoTotale;

    /**
     * Costo unitario della camera al momento della prenotazione.
     */
    @Column(nullable = false)
    private int costoUnitarioCamera;

    /**
     * Camera associata alla prenotazione.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    private Camera camera;

    /**
     * Insieme dei servizi prenotati associati a questa prenotazione.
     */
    @OneToMany(mappedBy = "prenotazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServizioPrenotato> serviziPrenotati;

    /**
     * Cliente che ha effettuato la prenotazione.
     */
    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private ClienteDettagli cliente;

    /**
     * Costruttore di default per la classe {@code Prenotazione}.
     */
    public Prenotazione() {}

    /**
     * Costruttore parametrizzato per la classe {@code Prenotazione}.
     *
     * @param dataPrenotazione     la data in cui è stata effettuata la prenotazione
     * @param dataCheckIn          la data di check-in
     * @param dataCheckOut         la data di check-out
     * @param numeroOspiti         il numero di ospiti
     * @param camera               la camera prenotata
     * @param serviziPrenotati     l'insieme dei servizi prenotati
     * @param cliente              il cliente che effettua la prenotazione
     */
    public Prenotazione(LocalDate dataPrenotazione, LocalDate dataCheckIn, LocalDate dataCheckOut, int numeroOspiti, Camera camera, Set<ServizioPrenotato> serviziPrenotati, ClienteDettagli cliente) {
        this.dataPrenotazione = dataPrenotazione;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.numeroOspiti = numeroOspiti;
        this.importoTotale = camera.getCosto() + serviziPrenotati.stream().mapToInt(s -> s.getNumeroServizi() * s.getCostoUnitario()).sum();
        this.costoUnitarioCamera = camera.getCosto();
        this.camera = camera;
        this.serviziPrenotati = serviziPrenotati;
        this.cliente = cliente;
    }

    /**
     * Restituisce l'identificativo della prenotazione.
     *
     * @return l'ID della prenotazione
     */
    public Long getId() {
        return id;
    }

    /**
     * Imposta l'identificativo della prenotazione.
     *
     * @param id l'ID da assegnare alla prenotazione
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce la data di prenotazione.
     *
     * @return la data di prenotazione
     */
    public LocalDate getDataPrenotazione() {
        return dataPrenotazione;
    }

    /**
     * Imposta la data di prenotazione.
     *
     * @param dataPrenotazione la data di prenotazione da assegnare
     */
    public void setDataPrenotazione(LocalDate dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    /**
     * Restituisce la data di check-in.
     *
     * @return la data di check-in
     */
    public LocalDate getDataCheckIn() {
        return dataCheckIn;
    }

    /**
     * Imposta la data di check-in.
     *
     * @param dataCheckIn la data di check-in da assegnare
     */
    public void setDataCheckIn(LocalDate dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    /**
     * Restituisce la data di check-out.
     *
     * @return la data di check-out
     */
    public LocalDate getDataCheckOut() {
        return dataCheckOut;
    }

    /**
     * Imposta la data di check-out.
     *
     * @param dataCheckOut la data di check-out da assegnare
     */
    public void setDataCheckOut(LocalDate dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    /**
     * Restituisce il numero di ospiti della prenotazione.
     *
     * @return il numero di ospiti
     */
    public int getNumeroOspiti() {
        return numeroOspiti;
    }

    /**
     * Imposta il numero di ospiti della prenotazione.
     *
     * @param numeroOspiti il numero di ospiti da assegnare
     */
    public void setNumeroOspiti(int numeroOspiti) {
        this.numeroOspiti = numeroOspiti;
    }

    /**
     * Restituisce l'importo totale della prenotazione.
     *
     * @return l'importo totale
     */
    public int getImportoTotale() {
        return importoTotale;
    }

    /**
     * Imposta l'importo totale della prenotazione.
     *
     * @param importoTotale l'importo totale da assegnare
     */
    public void setImportoTotale(int importoTotale) {
        this.importoTotale = importoTotale;
    }

    /**
     * Restituisce il costo unitario della camera al momento della prenotazione.
     *
     * @return il costo unitario della camera
     */
    public int getCostoUnitarioCamera() {
        return costoUnitarioCamera;
    }

    /**
     * Imposta il costo unitario della camera al momento della prenotazione.
     *
     * @param costoUnitarioCamera il costo unitario da assegnare
     */
    public void setCostoUnitarioCamera(int costoUnitarioCamera) {
        this.costoUnitarioCamera = costoUnitarioCamera;
    }

    /**
     * Restituisce la camera associata alla prenotazione.
     *
     * @return la camera prenotata
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Imposta la camera associata alla prenotazione.
     *
     * @param camera la camera da assegnare
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Restituisce il cliente che ha effettuato la prenotazione.
     *
     * @return il cliente che ha effettuato la prenotazione
     */
    public ClienteDettagli getCliente() {
        return cliente;
    }

    /**
     * Imposta il cliente che ha effettuato la prenotazione.
     *
     * @param cliente il cliente da assegnare
     */
    public void setCliente(ClienteDettagli cliente) {
        this.cliente = cliente;
    }

    /**
     * Restituisce l'insieme dei servizi prenotati associati a questa prenotazione.
     *
     * @return l'insieme dei servizi prenotati
     */
    public Set<ServizioPrenotato> getServiziPrenotati() {
        return serviziPrenotati;
    }

    /**
     * Aggiunge un servizio prenotato all'insieme dei servizi prenotati.
     *
     * @param servizioPrenotato il servizio prenotato da aggiungere
     */
    public void addServizioPrenotato(ServizioPrenotato servizioPrenotato) {
        this.serviziPrenotati.add(servizioPrenotato);
    }
}
