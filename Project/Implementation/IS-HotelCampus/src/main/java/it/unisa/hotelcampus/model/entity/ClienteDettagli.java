package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class ClienteDettagli {
    @Id
    private String email;

    @OneToOne
    @MapsId
    @JoinColumn(name = "email", nullable = false)
    private Utente utente;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Set<Prenotazione> prenotazioni = new HashSet<>();

    public ClienteDettagli() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Set<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public boolean creaPrenotazione(Prenotazione prenotazione) {
        return prenotazioni.add(prenotazione);
    }

    public boolean eliminaPrenotazione(Prenotazione prenotazione) {
        return prenotazioni.remove(prenotazione);
    }

}