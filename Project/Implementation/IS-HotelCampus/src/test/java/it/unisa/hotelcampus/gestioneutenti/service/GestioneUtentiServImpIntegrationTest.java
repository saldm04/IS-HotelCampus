package it.unisa.hotelcampus.gestioneutenti.service;

import it.unisa.hotelcampus.model.dao.UtenteRepository;
import it.unisa.hotelcampus.model.entity.Camera;
import it.unisa.hotelcampus.model.entity.ClienteDettagli;
import it.unisa.hotelcampus.model.entity.Servizio;
import it.unisa.hotelcampus.model.entity.Utente;
import it.unisa.hotelcampus.utils.PasswordHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class GestioneUtentiServImpIntegrationTest {

  @Autowired
  private UtenteRepository utenteRepository;

  @Autowired
  private GestioneUtentiServiceImpl gestioneUtentiService;

  private Utente utente;


  @BeforeEach
  public void setUp() {
    utente = new Utente("mariorossi@gmail.com", PasswordHash.toHash("Mario1234"), "mario", "rossi", LocalDate.of(1900, 1, 1), "Italia");
    utenteRepository.save(utente);
  }

  @Test
  public void testAutentica_TC16_Success() {
    Utente risultato = gestioneUtentiService.autentica("mariorossi@gmail.com", "Mario1234");

    assertEquals(utente.getEmail(), risultato.getEmail(), "Autenticazione riuscita: utente autenticato");
  }

  @Test
  public void testAutentica_TC17_PasswordErrata() {

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      gestioneUtentiService.autentica("mariorossi@gmail.com", "Password1234");
    });

    assertEquals("Credenziali errate!", exception.getMessage(), "Autenticazione fallita: credenziali errate");

  }

  @Test
  public void testAutentica_TC18_EmailNonPresente() {

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      gestioneUtentiService.autentica("mariabianchi@gmail.com", "Password1234");
    });

    assertEquals("L'email inserita non è associata ad alcun account!", exception.getMessage(), "Autenticazione fallita: credenziali errate");

    //contralla che non esiste un utente con quella email nel db
    assertEquals(utenteRepository.findById("mariabianchi@gmail.com"), Optional.empty());
  }
}
