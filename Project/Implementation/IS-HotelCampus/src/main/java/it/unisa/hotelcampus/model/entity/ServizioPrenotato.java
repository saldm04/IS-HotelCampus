package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

@Entity
public class ServizioPrenotato {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private int costoUnitario;
    @Column(nullable = false)
    private int numeroServizi;

    @ManyToOne
    @JoinColumn(name = "servizio_id", nullable = false) // Foreign Key
    private Servizio servizio;

    public ServizioPrenotato() {
    }

    public ServizioPrenotato(int costoUnitario, int numeroServizi, Servizio servizio) {
        this.costoUnitario = costoUnitario;
        this.numeroServizi = numeroServizi;
        this.servizio = servizio;
    }

    public int getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(int costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumeroServizi() {
        return numeroServizi;
    }

    public void setNumeroServizi(int numeroServizi) {
        this.numeroServizi = numeroServizi;
    }

    public Servizio getServizio() {
        return servizio;
    }

    public void setServizio(Servizio servizio) {
        this.servizio = servizio;
    }

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
