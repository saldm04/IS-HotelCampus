package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

@Entity
public class Servizio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, length = 600)
    private String descrizione;
    @Column(nullable = false)
    private int costo;
    @Column(nullable = false)
    private String immagine;
    @Column(nullable = false)
    private boolean isDeleted;

    public Servizio() {
    }

    public Servizio(String nome, String descrizione, int costo, String immagine) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.costo = costo;
        this.immagine = immagine;
        this.isDeleted = false;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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
