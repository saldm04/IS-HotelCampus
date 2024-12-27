package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Prenotazione {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Camera camera;

    public Prenotazione() {}

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

}
