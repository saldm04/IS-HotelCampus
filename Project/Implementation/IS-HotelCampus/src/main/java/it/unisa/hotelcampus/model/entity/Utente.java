package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class Utente {
    @Id
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;
    @Column(nullable = false)
    private Date dataNascita;
    @Column(nullable = false)
    private String nazionalita;
    @Column(nullable = false)
    private Ruolo ruolo;

    @OneToOne(mappedBy = "utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ClienteDettagli clienteDettagli;

    public Utente() {
    }

    public Utente(String email, String password, String nome, String cognome, Date dataNascita, String nazionalita) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.nazionalita = nazionalita;
        this.ruolo = Ruolo.CLIENTE;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNazionalita() {
        return nazionalita;
    }

    public void setNazionalita(String nazionalita) {
        this.nazionalita = nazionalita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nome='" + nome + '\'' +
                ", dataNascita=" + dataNascita +
                ", nazionalita='" + nazionalita + '\'' +
                ", ruolo=" + ruolo +
                '}';
    }

    public enum Ruolo {
        CLIENTE, DIRETTORE, GESTOREPRENOTAZIONI
    }
}
