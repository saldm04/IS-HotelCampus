package it.unisa.hotelcampus.gestioneprenotazioni.controller;

import it.unisa.hotelcampus.gestioneprenotazioni.service.GestionePrenotazioniService;
import it.unisa.hotelcampus.model.dao.*;
import it.unisa.hotelcampus.model.entity.*;
import it.unisa.hotelcampus.utils.PasswordHash;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PrenotControlIntTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  GestionePrenotazioniService gestionePrenotazioniService;

  @Autowired
  CameraRepository cameraRepository;

  @Autowired
  PrenotazioneRepository prenotazioneRepository;

  @Autowired
  ClienteDettagliRepository clienteDettagliRepository;

  @Autowired
  private UtenteRepository utenteRepository;

  @Autowired
  private ServizioRepository servizioRepository;

  private Camera camera;
  private Utente utente;
  private ClienteDettagli clienteDettagli;
  private Set<Servizio> servizi;
  Servizio servizio1;
  Servizio servizio2;


  @BeforeEach
  public void setUp() {

    servizi = new HashSet<>();
    servizio1 = new Servizio( "Colazione", "descrizione", 10, "Images/Demo/Servizi/colazione1.jpg");
    servizio2 = new Servizio( "Spa", "descrizione", 20, "Images/Demo/Servizi/colazione1.jpg");
    servizi.add(servizio1);
    servizi.add(servizio2);
    servizioRepository.saveAll(servizi);

    utente = new Utente("mariorossi@gmail.com", PasswordHash.toHash("Mario1234"), "mario", "rossi", LocalDate.of(1900,1,1), "Italia");
    clienteDettagli = new ClienteDettagli(utente);
    utente.setClienteDettagli(clienteDettagli);
    clienteDettagliRepository.save(clienteDettagli);
    utenteRepository.save(utente);

    camera = new Camera(101, Camera.TipoCamera.STANDARD, 2, 20,100, "Images/Demo/Camere/101.JPEG");
    cameraRepository.save(camera);
  }

  @Test
  public void testCreaPrenotazione_TC1_DataCheckInNull_ThrowsException() throws Exception {

    Integer numeroOspiti = 2;
    String checkOut = LocalDate.now().plusDays(10).toString();


    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkOut", checkOut)
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().is4xxClientError());


  }

  @Test
  public void testCreaPrenotazione_TC2_DataCheckInPassata_ThrowsException() throws Exception {

      LocalDate dataCheckIn = LocalDate.now().minusDays(1);
      LocalDate dataCheckOut = LocalDate.now().plusDays(10);
      Integer numeroOspiti = 2;

      mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                      .param("numeroCamera",camera.getId().toString())
                      .param("numeroOspiti", numeroOspiti.toString())
                      .param("checkIn", dataCheckIn.toString())
                      .param("checkOut", dataCheckOut.toString())
                      .sessionAttr("utente", utente)
              )
              .andExpect(status().isOk())
              .andExpect(view().name("error"))
              .andExpect(model().attributeExists("generalError"))
              .andExpect(model().attribute("generalError", "La data di check-in non può essere precedente alla data odierna"));
  }

  @Test
  public void testCreaPrenotazione_TC3_DataCheckOutNull_ThrowsException() throws Exception {

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    Integer numeroOspiti = 2;

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().is4xxClientError());
  }

  @Test
  public void testCreaPrenotazione_TC4_DataCheckOutPrimaCheckIn_ThrowsException() throws Exception {

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(2);
    Integer numeroOspiti = 2;

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("error"))
            .andExpect(model().attributeExists("generalError"))
            .andExpect(model().attribute("generalError", "La data di check-out non può essere precedente alla data di check-in"));
  }

  @Test
  public void testCreaPrenotazione_TC5_DataCheckOutUgualeCheckIn_ThrowsException() throws Exception {

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(5);
    Integer numeroOspiti = 2;


    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("error"))
            .andExpect(model().attributeExists("generalError"))
            .andExpect(model().attribute("generalError", "La data di check-out non può essere uguale alla data di check-in"));
  }

  @Test
  public void testCreaPrenotazione_TC6_NumeroOspitiMinoreUgualeZero_ThrowsException() throws Exception {

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 0;

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("error"))
            .andExpect(model().attributeExists("generalError"))
            .andExpect(model().attribute("generalError", "Il numero di ospiti deve essere maggiore di 0"));
  }

  @Test
  public void testCreaPrenotazione_TC7_NumeroOspitiMaggioreNumeroMaxOspiti_ThrowsException() throws Exception {

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 3;

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("error"))
            .andExpect(model().attributeExists("generalError"))
            .andExpect(model().attribute("generalError", "Il numero di ospiti non può essere maggiore del numero massimo di ospiti della camera"));
  }

  @Test
  public void testCreaPrenotazione_TC8_CameraNull_ThrowsException() throws Exception {

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().is4xxClientError());
  }

  @Test
  public void testCreaPrenotazione_TC9_CameraNonDisponibile_ThrowsException() throws Exception {

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Prenotazione prenotazione = new Prenotazione(
            LocalDate.of(2024, 12, 12),
            dataCheckIn,
            dataCheckOut,
            2,
            camera,
            servizi,
            clienteDettagli
    );

    clienteDettagli.creaPrenotazione(prenotazione);
    prenotazioneRepository.save(prenotazione);

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("error"))
            .andExpect(model().attributeExists("generalError"))
            .andExpect(model().attribute("generalError", "La camera non è più disponibile per le date selezionate"));
  }

  @Test
  public void testCreaPrenotazione_TC10_NumeroServiziMaggioreNumeroOspiti_ThrowsException() throws Exception {

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("quantita[" + servizio1.getId() + "]", "3")
                    .param("quantita[" + servizio2.getId() + "]", "1")
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("error"))
            .andExpect(model().attributeExists("generalError"))
            .andExpect(model().attribute("generalError", "Il numero di servizi non può essere maggiore del numero di ospiti"));
  }

  @Test
  public void testCreaPrenotazione_TC12_PrenotazioneEffettuata() throws Exception {
    LocalDate dataCheckIn = LocalDate.now();
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("confermaPrenotazione"))
            .andExpect(model().attributeExists("prenotazione"))
            .andExpect(model().attribute("prenotazione", prenotazioneRepository.findByCamera(camera)));
  }

  @Test
  public void testCreaPrenotazione_TC13_PrenotazioneEffettuata() throws Exception {

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("confermaPrenotazione"))
            .andExpect(model().attributeExists("prenotazione"))
            .andExpect(model().attribute("prenotazione", prenotazioneRepository.findByCamera(camera)));
  }

  @Test
  public void testCreaPrenotazione_TC14_PrenotazioneEffettuata() throws Exception {

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .param("quantita[" + servizio1.getId() + "]", "2")
                    .param("quantita[" + servizio2.getId() + "]", "1")
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("confermaPrenotazione"))
            .andExpect(model().attributeExists("prenotazione"))
            .andExpect(model().attribute("prenotazione", prenotazioneRepository.findByCamera(camera)));
  }

  @Test
  public void testCreaPrenotazione_TC15_PrenotazioneEffettuata() throws Exception{

    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", camera.getId().toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .param("quantita[" + servizio1.getId() + "]", "2")
                    .param("quantita[" + servizio2.getId() + "]", "1")
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("confermaPrenotazione"))
            .andExpect(model().attributeExists("prenotazione"))
            .andExpect(model().attribute("prenotazione", prenotazioneRepository.findByCamera(camera)));
  }

}
