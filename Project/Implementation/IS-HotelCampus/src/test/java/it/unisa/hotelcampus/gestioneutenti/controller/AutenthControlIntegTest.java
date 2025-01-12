package it.unisa.hotelcampus.gestioneutenti.controller;

import it.unisa.hotelcampus.gestioneutenti.service.GestioneUtentiService;
import it.unisa.hotelcampus.model.dao.UtenteRepository;
import it.unisa.hotelcampus.model.entity.Utente;
import it.unisa.hotelcampus.utils.PasswordHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AutenthControlIntegTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GestioneUtentiService gestioneUtentiService;

    @Autowired
    private UtenteRepository utenteRepository;

    private Utente utenteRossi;

    MockHttpSession session = new MockHttpSession();

    @BeforeEach
    public void setUp() {
        utenteRossi = new Utente("mariorossi@gmail.com", PasswordHash.toHash("Mario1234"), "Mario", "Rossi", LocalDate.of(1990, 1, 1), "Italia");
        utenteRepository.save(utenteRossi);
        session.setAttribute("utente", utenteRossi);
    }

    @Test
    public void testAutentica_TC16_Success() throws Exception {
        mockMvc.perform(post("/utenti/login")
                        .param("email", "mariorossi@gmail.com")
                        .param("password", "Mario1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(request().sessionAttribute("utente",
                        hasProperty("email", is("mariorossi@gmail.com"))));

    }

    @Test
    public void testAutentica_TC17_PasswordErrata() throws Exception {
        mockMvc.perform(post("/utenti/login")
                        .param("email", "mariorossi@gmail.com")
                        .param("password", "Password1234"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("generalError", "Credenziali errate!"));
    }

    @Test
    public void testAutentica_TC18_EmailNonPresente() throws Exception {
        mockMvc.perform(post("/utenti/login")
                        .param("email", "mariabianchi@gmail.com")
                        .param("password", "Password1234"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("generalError", "L'email inserita non è associata ad alcun account!"));
    }
}
