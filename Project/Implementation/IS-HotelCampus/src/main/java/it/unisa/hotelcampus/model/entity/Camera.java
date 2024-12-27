package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCamera tipo;

    @Column(nullable = false)
    private int numeroMaxOspiti;

    @Column(nullable = false)
    private int quadratura;

    @Column(nullable = false)
    private int costo;

    @Column(nullable = false)
    private String immagine;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany
    private Set<Prenotazione> prenotazioni;

    public enum TipoCamera {
        STANDARD,
        DELUXE,
        LUXURY,
        EXCLUSIVE
    }


    public Camera() {
    }


    public Camera(int numero, TipoCamera tipo, int numeroMaxOspiti, int quadratura, int costo, String immagine) {
        this.numero = numero;
        this.tipo = tipo;
        this.numeroMaxOspiti = numeroMaxOspiti;
        this.quadratura = quadratura;
        this.costo = costo;
        this.immagine = immagine;
        this.isDeleted = false;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public TipoCamera getTipo() {
        return tipo;
    }

    public void setTipo(TipoCamera tipo) {
        this.tipo = tipo;
    }

    public int getNumeroMaxOspiti() {
        return numeroMaxOspiti;
    }

    public void setNumeroMaxOspiti(int numeroMaxOspiti) {
        this.numeroMaxOspiti = numeroMaxOspiti;
    }

    public int getQuadratura() {
        return quadratura;
    }

    public void setQuadratura(int quadratura) {
        this.quadratura = quadratura;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
