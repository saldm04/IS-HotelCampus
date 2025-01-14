package it.unisa.hotelcampus.gestioneutenti.controller;

import it.unisa.hotelcampus.gestioneutenti.service.GestioneUtentiService;
import it.unisa.hotelcampus.model.entity.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(it.unisa.hotelcampus.gestioneutenti.controller.AutenticazioneController.class)
public class AuthentControlTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private GestioneUtentiService gestioneUtentiService;

  private Utente utenteRossi;

  MockHttpSession session = new MockHttpSession();

  @BeforeEach
  public void setUp() {

    utenteRossi = new Utente();
    utenteRossi.setEmail("mariorossi@gmail.com");
    session.setAttribute("utente", utenteRossi);
  }

  @Test
  public void testAutentica_TC16_Success() throws Exception {


    when(gestioneUtentiService.autentica("mariorossi@gmail.com", "Mario1234")).thenReturn(utenteRossi);

    mockMvc.perform(post("/utenti/login")
                    .param("email", "mariorossi@gmail.com")
                    .param("password", "Mario1234"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andExpect(request().sessionAttribute("utente", utenteRossi));
  }

  @Test
  public void testAutentica_TC17_PasswordErrata() throws Exception {

    when(gestioneUtentiService.autentica("mariorossi@gmail.com", "Password1234")).thenThrow(new IllegalArgumentException("Credenziali errate!"));

    mockMvc.perform(post("/utenti/login")
                    .param("email", "mariorossi@gmail.com")
                    .param("password", "Password1234"))
            .andExpect(status().isOk())
            .andExpect(view().name("login"))
            .andExpect(model().attribute("generalError", "Credenziali errate!"));

  }

  @Test
  public void testAutentica_TC18_EmailNonPresente() throws Exception {

    when(gestioneUtentiService.autentica("mariabianchi@gmail.com", "Password1234")).thenThrow(new IllegalArgumentException("L'email inserita non è associata ad alcun account!"));

    mockMvc.perform(post("/utenti/login")
                    .param("email", "mariabianchi@gmail.com")
                    .param("password", "Password1234"))
            .andExpect(status().isOk())
            .andExpect(view().name("login"))
            .andExpect(model().attribute("generalError", "L'email inserita non è associata ad alcun account!"));

  }
}

