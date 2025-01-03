package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta i dettagli specifici di un cliente all'interno del sistema.
 * Estende le informazioni base dell'utente con dettagli aggiuntivi e gestisce le prenotazioni del cliente.
 *
 * @version 1.0
 */
@Entity
public class ClienteDettagli {

    /**
     * Email del cliente, utilizzata come chiave primaria.
     */
    @Id
    private String email;

    /**
     * Relazione one-to-one con l'entità {@link Utente}.
     * Utilizza la stessa chiave primaria dell'utente.
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "email", nullable = false)
    private Utente utente;

    /**
     * Insieme delle prenotazioni effettuate dal cliente.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Prenotazione> prenotazioni = new HashSet<>();

    /**
     * Costruttore di default per la classe {@code ClienteDettagli}.
     */
    public ClienteDettagli() {}

    /**
     * Costruttore parametrizzato per la classe {@code ClienteDettagli}.
     *
     * @param utente l'utente associato a questi dettagli
     */
    public ClienteDettagli(Utente utente) {
        this.utente = utente;
    }

    /**
     * Restituisce l'email del cliente.
     *
     * @return l'email del cliente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email del cliente.
     *
     * @param email l'email da assegnare al cliente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce l'utente associato ai dettagli del cliente.
     *
     * @return l'utente
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Imposta l'utente associato ai dettagli del cliente.
     *
     * @param utente l'utente da associare
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    /**
     * Restituisce l'insieme delle prenotazioni del cliente.
     *
     * @return l'insieme delle prenotazioni
     */
    public Set<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Aggiunge una prenotazione all'insieme delle prenotazioni del cliente.
     *
     * @param prenotazione la prenotazione da aggiungere
     * @return {@code true} se la prenotazione è stata aggiunta con successo, {@code false} altrimenti
     */
    public boolean creaPrenotazione(Prenotazione prenotazione) {
        return prenotazioni.add(prenotazione);
    }

    /**
     * Rimuove una prenotazione dall'insieme delle prenotazioni del cliente.
     *
     * @param prenotazione la prenotazione da rimuovere
     * @return {@code true} se la prenotazione è stata rimossa con successo, {@code false} altrimenti
     */
    public boolean eliminaPrenotazione(Prenotazione prenotazione) {
        return prenotazioni.remove(prenotazione);
    }
}
