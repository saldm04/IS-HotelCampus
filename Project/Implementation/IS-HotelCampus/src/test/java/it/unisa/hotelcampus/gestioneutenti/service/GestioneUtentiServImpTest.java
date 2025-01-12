package it.unisa.hotelcampus.gestioneutenti.service;

import it.unisa.hotelcampus.model.dao.UtenteRepository;
import it.unisa.hotelcampus.model.entity.Utente;
import it.unisa.hotelcampus.utils.PasswordHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GestioneUtentiServImpTest {

  @Mock
  private UtenteRepository utenteRepository;

  @InjectMocks
  private GestioneUtentiServiceImpl gestioneUtentiService;


  private Utente utenteRossi;

  @BeforeEach
  public void setUp() {

    utenteRossi = new Utente();
    utenteRossi.setEmail("mariorossi@gmail.com");
    utenteRossi.setPassword(PasswordHash.toHash("Mario1234"));
  }

  @Test
  public void testAutentica_TC16_Success() {

    when(utenteRepository.findById("mariorossi@gmail.com")).thenReturn(Optional.ofNullable(utenteRossi));

    Utente risultato = gestioneUtentiService.autentica("mariorossi@gmail.com", "Mario1234");

    assertEquals(utenteRossi.getEmail(), risultato.getEmail(), "Autenticazione riuscita: utente autenticato");

  }

  @Test
  public void testAutentica_TC17_PasswordErrata() {

    when(utenteRepository.findById("mariorossi@gmail.com")).thenReturn(Optional.ofNullable(utenteRossi));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      gestioneUtentiService.autentica("mariorossi@gmail.com", "Password1234");
    });

    assertEquals("Credenziali errate!", exception.getMessage(), "Autenticazione fallita: credenziali errate");

  }

  @Test
  public void testAutentica_TC18_EmailNonPresente() {

    when(utenteRepository.findById("mariabianchi@gmail.com")).thenReturn(Optional.empty());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      gestioneUtentiService.autentica("mariabianchi@gmail.com", "Password1234");
    });

    assertEquals("L'email inserita non è associata ad alcun account!", exception.getMessage(), "Autenticazione fallita: credenziali errate");
  }

}
