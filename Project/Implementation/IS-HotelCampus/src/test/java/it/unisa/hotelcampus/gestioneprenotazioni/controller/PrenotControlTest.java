package it.unisa.hotelcampus.gestioneprenotazioni.controller;

import it.unisa.hotelcampus.gestioneprenotazioni.service.GestionePrenotazioniService;
import it.unisa.hotelcampus.model.dao.CameraRepository;
import it.unisa.hotelcampus.model.dao.ServizioRepository;
import it.unisa.hotelcampus.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(it.unisa.hotelcampus.gestioneprenotazioni.controller.PrenotazioniController.class)
public class PrenotControlTest {


  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private GestionePrenotazioniService gestionePrenotazioniService;

  @MockitoBean
  private CameraRepository cameraRepository;

  @MockitoBean
  private ServizioRepository servizioRepository;

  private ClienteDettagli clienteAutenticato;
  private Camera camera;
  private Utente utente;

  @BeforeEach
  public void setUp() {
    clienteAutenticato = new ClienteDettagli();
    clienteAutenticato.setEmail("cliente@esempio.it");

    camera = new Camera();
    camera.setId(1L);
    camera.setNumeroMaxOspiti(2);
    camera.setCosto(100);

    utente = new Utente();
    utente.setClienteDettagli(clienteAutenticato);
    utente.setRuolo(Utente.Ruolo.CLIENTE);
  }

  @Test
  public void testCreaPrenotazione_TC1_DataCheckInNull_ThrowsException() throws Exception {
    Long numeroCamera = 1L;
    Integer numeroOspiti = 2;
    String checkOut = LocalDate.now().plusDays(10).toString();


    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkOut", checkOut)
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().is4xxClientError());

  }

  @Test
  public void testCreaPrenotazione_TC2_DataCheckInPassata_ThrowsException() throws Exception {
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().minusDays(1);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    Mockito.when(gestionePrenotazioniService.creaPrenotazione(
            dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato
    )).thenThrow(new IllegalArgumentException("La data di check-in non può essere precedente alla data odierna"));

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    // Non aggiungiamo parametri per i servizi poiché la lista è vuota
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
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    Integer numeroOspiti = 2;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));


    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    // Non aggiungiamo parametri per i servizi poiché la lista è vuota
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().is4xxClientError());
  }


  @Test
  public void testCreaPrenotazione_TC4_DataCheckOutPrimaCheckIn_ThrowsException() throws Exception {
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(2);
    Integer numeroOspiti = 2;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    Mockito.when(gestionePrenotazioniService.creaPrenotazione(
            dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato
    )).thenThrow(new IllegalArgumentException("La data di check-out non può essere precedente alla data di check-in"));

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
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
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(5);
    Integer numeroOspiti = 2;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    Mockito.when(gestionePrenotazioniService.creaPrenotazione(
            dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato
    )).thenThrow(new IllegalArgumentException("La data di check-out non può essere uguale alla data di check-in"));

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
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
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 0;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    Mockito.when(gestionePrenotazioniService.creaPrenotazione(
            dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato
    )).thenThrow(new IllegalArgumentException("Il numero di ospiti deve essere maggiore di 0"));

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
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
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 3;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    Mockito.when(gestionePrenotazioniService.creaPrenotazione(
            dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato
    )).thenThrow(new IllegalArgumentException("Il numero di ospiti non può essere maggiore del numero massimo di ospiti della camera"));

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
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
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(null);

    Mockito.when(gestionePrenotazioniService.creaPrenotazione(
            dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato
    )).thenThrow(new IllegalArgumentException("La camera non è selezionata"));


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
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    Mockito.when(gestionePrenotazioniService.creaPrenotazione(
            dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato
    )).thenThrow(new IllegalArgumentException("La camera non è più disponibile per le date selezionate"));

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
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
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));
    Mockito.when(servizioRepository.findById(0L)).thenReturn(Optional.of(new Servizio()));
    Mockito.when(servizioRepository.findById(2L)).thenReturn(Optional.of(new Servizio()));

    when(gestionePrenotazioniService.creaPrenotazione(
            eq(dataCheckIn),
            eq(dataCheckOut),
            eq(numeroOspiti),
            eq(camera),
            anySet(),
            eq(clienteAutenticato)
    )).thenThrow(new IllegalArgumentException("Il numero di servizi non può essere maggiore del numero di ospiti"));

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("quantita[0]", "3")
                    .param("quantita[1]", "1")
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
  public void testCreaPrenotazione_TC11_ClienteNull_ThrowsException() throws Exception {
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;


    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
            )
            .andExpect(status().is4xxClientError());
  }


  @Test
  public void testCreaPrenotazione_TC12_PrenotazioneEffettuata() throws Exception {
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now();
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    // Crea una prenotazione con un ID specifico
    Prenotazione prenotazioneAttesa = new Prenotazione();
    Long idPrenotazioneAttesa = 123L;
    prenotazioneAttesa.setId(idPrenotazioneAttesa);

    Mockito.when(gestionePrenotazioniService.creaPrenotazione(
            dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato
    )).thenReturn(prenotazioneAttesa);

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("confermaPrenotazione"))
            .andExpect(model().attributeExists("prenotazione"))
            .andExpect(model().attribute("prenotazione", hasProperty("id", is(idPrenotazioneAttesa))));
  }

  @Test
  public void testCreaPrenotazione_TC13_PrenotazioneEffettuata() throws Exception {
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;
    Set<ServizioPrenotato> servizi = new HashSet<>();

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    // Crea una prenotazione con un ID specifico
    Prenotazione prenotazioneAttesa = new Prenotazione();
    Long idPrenotazioneAttesa = 123L;
    prenotazioneAttesa.setId(idPrenotazioneAttesa);

    Mockito.when(gestionePrenotazioniService.creaPrenotazione(
            dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato
    )).thenReturn(prenotazioneAttesa);

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("confermaPrenotazione"))
            .andExpect(model().attributeExists("prenotazione"))
            .andExpect(model().attribute("prenotazione", hasProperty("id", is(idPrenotazioneAttesa))));
  }

  @Test
  public void testCreaPrenotazione_TC14_PrenotazioneEffettuata() throws Exception {

    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;

    // Definizione dei servizi
    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    // Crea una prenotazione con un ID specifico
    Prenotazione prenotazioneAttesa = new Prenotazione();
    Long idPrenotazioneAttesa = 123L;
    prenotazioneAttesa.setId(idPrenotazioneAttesa);

    when(gestionePrenotazioniService.creaPrenotazione(
            eq(dataCheckIn),
            eq(dataCheckOut),
            eq(numeroOspiti),
            eq(camera),
            anySet(),
            eq(clienteAutenticato)
    )).thenReturn(prenotazioneAttesa);

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .param("quantita[0]", "1")
                    .param("quantita[1]", "2")
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("confermaPrenotazione"))
            .andExpect(model().attributeExists("prenotazione"))
            .andExpect(model().attribute("prenotazione", hasProperty("id", is(idPrenotazioneAttesa))));
  }

  @Test
  public void testCreaPrenotazione_TC15_PrenotazioneEffettuata() throws Exception{
    Long numeroCamera = 1L;
    LocalDate dataCheckIn = LocalDate.now().plusDays(5);
    LocalDate dataCheckOut = LocalDate.now().plusDays(10);
    Integer numeroOspiti = 2;

    Mockito.when(cameraRepository.findById(numeroCamera)).thenReturn(Optional.of(camera));

    // Crea una prenotazione con un ID specifico
    Prenotazione prenotazioneAttesa = new Prenotazione();
    Long idPrenotazioneAttesa = 123L;
    prenotazioneAttesa.setId(idPrenotazioneAttesa);

    when(gestionePrenotazioniService.creaPrenotazione(
            eq(dataCheckIn),
            eq(dataCheckOut),
            eq(numeroOspiti),
            eq(camera),
            anySet(),
            eq(clienteAutenticato)
    )).thenReturn(prenotazioneAttesa);

    mockMvc.perform(post("/prenotazioni/confermaPrenotazione")
                    .param("numeroCamera", numeroCamera.toString())
                    .param("numeroOspiti", numeroOspiti.toString())
                    .param("checkIn", dataCheckIn.toString())
                    .param("checkOut", dataCheckOut.toString())
                    .param("quantita[0]", "1")
                    .param("quantita[1]", "2")
                    .sessionAttr("utente", utente)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("confermaPrenotazione"))
            .andExpect(model().attributeExists("prenotazione"))
            .andExpect(model().attribute("prenotazione", hasProperty("id", is(idPrenotazioneAttesa))));
  }

}
