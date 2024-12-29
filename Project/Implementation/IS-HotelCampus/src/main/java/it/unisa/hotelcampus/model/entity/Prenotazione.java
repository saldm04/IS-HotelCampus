package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
public class Prenotazione {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Date dataPrenotazione;

    @Column(nullable = false)
    private Date dataCheckIn;

    @Column(nullable = false)
    private Date dataCheckOut;

    @Column(nullable = false)
    private int numeroOspiti;

    @Column(nullable = false)
    private int importoTotale;

    @Column(nullable = false)
    private int costoUnitarioCamera;

    @ManyToOne
    @JoinColumn(name = "camera_id", nullable = false) // Foreign Key
    private Camera camera;

    @OneToMany
    private Set<ServizioPrenotato> serviziPrenotati;

    public Prenotazione() {}

    public Prenotazione(Date dataPrenotazione, Date dataCheckIn, Date dataCheckOut, int numeroOspiti, int importoTotale, int costoUnitarioCamera, Camera camera, Set<ServizioPrenotato> serviziPrenotati) {
        this.dataPrenotazione = dataPrenotazione;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.numeroOspiti = numeroOspiti;
        this.importoTotale = importoTotale;
        this.costoUnitarioCamera = costoUnitarioCamera;
        this.camera = camera;
        this.serviziPrenotati = serviziPrenotati;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(Date dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public Date getDataCheckIn() {
        return dataCheckIn;
    }

    public void setDataCheckIn(Date dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public Date getDataCheckOut() {
        return dataCheckOut;
    }

    public void setDataCheckOut(Date dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    public int getNumeroOspiti() {
        return numeroOspiti;
    }

    public void setNumeroOspiti(int numeroOspiti) {
        this.numeroOspiti = numeroOspiti;
    }

    public int getImportoTotale() {
        return importoTotale;
    }

    public void setImportoTotale(int importoTotale) {
        this.importoTotale = importoTotale;
    }

    public int getCostoUnitarioCamera() {
        return costoUnitarioCamera;
    }

    public void setCostoUnitarioCamera(int costoUnitarioCamera) {
        this.costoUnitarioCamera = costoUnitarioCamera;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Set<ServizioPrenotato> getServiziPrenotati() {
        return serviziPrenotati;
    }

    public void addServizioPrenotato(ServizioPrenotato servizioPrenotato) {
        this.serviziPrenotati.add(servizioPrenotato);
    }
}
