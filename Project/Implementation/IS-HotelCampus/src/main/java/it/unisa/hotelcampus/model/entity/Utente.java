package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Rappresenta un utente del sistema HotelCampus.
 * Contiene informazioni personali e credenziali di accesso.
 * Gestisce anche i dettagli specifici del cliente e il ruolo dell'utente.
 *
 * @version 1.0
 */
@Entity
public class Utente {

    /**
     * Email dell'utente, utilizzata come chiave primaria.
     */
    @Id
    @Column(nullable = false)
    private String email;

    /**
     * Password dell'utente per l'accesso al sistema.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Nome dell'utente.
     */
    @Column(nullable = false)
    private String nome;

    /**
     * Cognome dell'utente.
     */
    @Column(nullable = false)
    private String cognome;

    /**
     * Data di nascita dell'utente.
     */
    @Column(nullable = false)
    private LocalDate dataNascita;

    /**
     * Nazionalità dell'utente.
     */
    @Column(nullable = false)
    private String nazionalita;

    /**
     * Ruolo dell'utente all'interno del sistema, definito dall'enumerazione {@link Ruolo}.
     */
    @Column(nullable = false)
    private Ruolo ruolo;

    /**
     * Dettagli specifici del cliente associato a questo utente.
     */
    @OneToOne(mappedBy = "utente", cascade = CascadeType.ALL)
    private ClienteDettagli clienteDettagli;

    /**
     * Costruttore di default per la classe {@code Utente}.
     */
    public Utente() {
    }

    /**
     * Costruttore parametrizzato per la classe {@code Utente}.
     *
     * @param email        l'email dell'utente
     * @param password     la password dell'utente
     * @param nome         il nome dell'utente
     * @param cognome      il cognome dell'utente
     * @param dataNascita  la data di nascita dell'utente
     * @param nazionalita  la nazionalità dell'utente
     */
    public Utente(String email, String password, String nome, String cognome, LocalDate dataNascita, String nazionalita) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.nazionalita = nazionalita;
        this.ruolo = Ruolo.CLIENTE;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return il cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome il cognome da assegnare all'utente
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce la data di nascita dell'utente.
     *
     * @return la data di nascita
     */
    public LocalDate getDataNascita() {
        return dataNascita;
    }

    /**
     * Imposta la data di nascita dell'utente.
     *
     * @param dataNascita la data di nascita da assegnare
     */
    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
     * Restituisce l'email dell'utente.
     *
     * @return l'email dell'utente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email dell'utente.
     *
     * @param email l'email da assegnare all'utente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce la nazionalità dell'utente.
     *
     * @return la nazionalità dell'utente
     */
    public String getNazionalita() {
        return nazionalita;
    }

    /**
     * Imposta la nazionalità dell'utente.
     *
     * @param nazionalita la nazionalità da assegnare all'utente
     */
    public void setNazionalita(String nazionalita) {
        this.nazionalita = nazionalita;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return il nome dell'utente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'utente.
     *
     * @param nome il nome da assegnare all'utente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return la password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la password dell'utente.
     *
     * @param password la password da assegnare all'utente
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce il ruolo dell'utente.
     *
     * @return il ruolo dell'utente
     */
    public Ruolo getRuolo() {
        return ruolo;
    }

    /**
     * Imposta il ruolo dell'utente.
     *
     * @param ruolo il ruolo da assegnare all'utente
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Restituisce i dettagli specifici del cliente associato a questo utente.
     *
     * @return i dettagli del cliente
     */
    public ClienteDettagli getClienteDettagli() {
        return clienteDettagli;
    }

    /**
     * Imposta i dettagli specifici del cliente associato a questo utente.
     *
     * @param clienteDettagli i dettagli del cliente da assegnare
     */
    public void setClienteDettagli(ClienteDettagli clienteDettagli) {
        this.clienteDettagli = clienteDettagli;
    }

    /**
     * Restituisce una rappresentazione testuale dell'utente.
     *
     * @return una stringa che rappresenta l'utente
     */
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

    /**
     * Enumerazione dei ruoli possibili per un utente.
     */
    public enum Ruolo {
        CLIENTE,
        DIRETTORE,
        GESTOREPRENOTAZIONI
    }
}

