package it.unisa.hotelcampus.gestioneprenotazioni.service;

import it.unisa.hotelcampus.model.dao.*;
import it.unisa.hotelcampus.model.entity.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.unisa.hotelcampus.utils.PasswordHash.toHash;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GestPrenotServImpIntegTest {
    @Autowired
    private GestionePrenotazioniService gestionePrenotazioniService;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ServizioRepository servizioRepository;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private ServizioPrenotatoRepository servizioPrenotatoRepository;

    @Autowired
    private ClienteDettagliRepository clienteDettagliRepository;

    private Utente utente;
    private ClienteDettagli cliente;
    private Camera camera;

    @BeforeEach
    void setUp() {
        utente = new Utente("mariorossi@gmail.com", toHash("Mario1234"), "Mario", "Rossi", LocalDate.of(1990, 1, 1), "Italia");
        cliente = new ClienteDettagli(utente);
        utente.setClienteDettagli(cliente);
        utenteRepository.save(utente);

        camera = new Camera(101, Camera.TipoCamera.STANDARD, 2, 20, 100, "Images/Demo/Camere/101.JPEG");
        cameraRepository.save(camera);

        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);
        session.setAttribute("utente", utente);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testCreaPrenotazione_TC1_DataCheckInNull_ThrowsException() {
        LocalDate dataCheckIn = null;
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, cliente
            );
        });

        assertEquals("Le date di check-in e check-out non possono essere nulle", exception.getMessage());

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertTrue(prenotazioni.isEmpty());
        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertTrue(cliente.getPrenotazioni().isEmpty());
        camera = cameraRepository.findByNumero(101);
        assertTrue(camera.getPrenotazioni().isEmpty());
    }

    @Test
    public void testCreaPrenotazione_TC2_DataCheckInPassata_ThrowsException() {
        LocalDate dataCheckIn = LocalDate.now().minusDays(1);
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, cliente
            );
        });

        assertEquals("La data di check-in non può essere precedente alla data odierna", exception.getMessage());

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertTrue(prenotazioni.isEmpty());
        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertTrue(cliente.getPrenotazioni().isEmpty());
        camera = cameraRepository.findByNumero(101);
        assertTrue(camera.getPrenotazioni().isEmpty());
    }

    @Test
    public void testCreaPrenotazione_TC3_DataCheckOutNull_ThrowsException() {
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = null;
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, cliente
            );
        });

        assertEquals("Le date di check-in e check-out non possono essere nulle", exception.getMessage());

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertTrue(prenotazioni.isEmpty());
        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertTrue(cliente.getPrenotazioni().isEmpty());
        camera = cameraRepository.findByNumero(101);
        assertTrue(camera.getPrenotazioni().isEmpty());
    }

    @Test
    public void testCreaPrenotazione_TC4_DataCheckOutPrimaCheckIn_ThrowsException() {
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = LocalDate.now().plusDays(2);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, cliente
            );
        });

        assertEquals("La data di check-out non può essere precedente alla data di check-in", exception.getMessage());

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertTrue(prenotazioni.isEmpty());
        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertTrue(cliente.getPrenotazioni().isEmpty());
        camera = cameraRepository.findByNumero(101);
        assertTrue(camera.getPrenotazioni().isEmpty());
    }

    @Test
    public void testCreaPrenotazione_TC5_DataCheckOutUgualeCheckIn_ThrowsException(){
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = LocalDate.now().plusDays(5);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, cliente
            );
        });

        assertEquals("La data di check-out non può essere uguale alla data di check-in", exception.getMessage());

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertTrue(prenotazioni.isEmpty());
        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertTrue(cliente.getPrenotazioni().isEmpty());
        camera = cameraRepository.findByNumero(101);
        assertTrue(camera.getPrenotazioni().isEmpty());
    }

    @Test
    public void testCreaPrenotazione_TC6_NumeroOspitiMinoreUgualeZero_ThrowsException(){
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 0;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, cliente
            );
        });

        assertEquals("Il numero di ospiti deve essere maggiore di 0", exception.getMessage());

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertTrue(prenotazioni.isEmpty());
        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertTrue(cliente.getPrenotazioni().isEmpty());
        camera = cameraRepository.findByNumero(101);
        assertTrue(camera.getPrenotazioni().isEmpty());
    }

    @Test
    public void testCreaPrenotazione_TC7_NumeroOspitiMaggioreNumeroMaxOspiti_ThrowsException(){
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 3;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, cliente
            );
        });

        assertEquals("Il numero di ospiti non può essere maggiore del numero massimo di ospiti della camera", exception.getMessage());

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertTrue(prenotazioni.isEmpty());
        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertTrue(cliente.getPrenotazioni().isEmpty());
        camera = cameraRepository.findByNumero(101);
        assertTrue(camera.getPrenotazioni().isEmpty());
    }

    @Test
    public void testCreaPrenotazione_TC8_CameraNull_ThrowsException(){
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, null, servizi, cliente
            );
        });

        assertEquals("La camera non è selezionata", exception.getMessage());

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertTrue(prenotazioni.isEmpty());
        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertTrue(cliente.getPrenotazioni().isEmpty());
        camera = cameraRepository.findByNumero(101);
        assertTrue(camera.getPrenotazioni().isEmpty());
    }

    @Test
    public void testCreaPrenotazione_TC9_CameraNonDisponibile_ThrowsException(){
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        ClienteDettagli clienteDettagli = clienteDettagliRepository.findById(utente.getEmail()).get();
        Prenotazione prenotazione = new Prenotazione(LocalDate.now(), dataCheckIn, dataCheckOut, 2, camera, servizi, clienteDettagli);
        clienteDettagli.creaPrenotazione(prenotazione);
        clienteDettagliRepository.save(clienteDettagli);
        Camera cameraDB = cameraRepository.findByNumero(camera.getNumero());
        cameraDB.addPrenotazione(prenotazione);
        cameraRepository.save(cameraDB);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, cliente
            );
        });

        assertEquals("La camera non è più disponibile per le date selezionate", exception.getMessage());

        clienteDettagli = clienteDettagliRepository.findById(utente.getEmail()).get();
        camera = cameraRepository.findByNumero(101);
        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertEquals(1, prenotazioni.size());
        assertEquals(1, clienteDettagli.getPrenotazioni().size());
        assertEquals(1, camera.getPrenotazioni().size());
    }

    @Test
    public void testCreaPrenotazione_TC10_NumeroServiziMaggioreNumeroOspiti_ThrowsException(){
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();
        Servizio colazione = new Servizio("Colazione", "descrizione", 10, "Images/Demo/Servizi/colazione1.jpg");
        Servizio spa = new Servizio("Spa", "descrizione", 40, "Images/Demo/Servizi/spa1.jpg");
        servizioRepository.save(colazione);
        servizioRepository.save(spa);
        ServizioPrenotato colazionePrenotata = new ServizioPrenotato(colazione.getCosto(), 3, colazione);
        ServizioPrenotato spaPrenotata = new ServizioPrenotato(spa.getCosto(), 1, spa);
        servizi.add(colazionePrenotata);
        servizi.add(spaPrenotata);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, cliente
            );
        });

        assertEquals("Il numero di servizi non può essere maggiore del numero di ospiti", exception.getMessage());

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertTrue(prenotazioni.isEmpty());
        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertTrue(cliente.getPrenotazioni().isEmpty());
        camera = cameraRepository.findByNumero(101);
        assertTrue(camera.getPrenotazioni().isEmpty());
        assertTrue(servizioPrenotatoRepository.findAll().isEmpty());
    }

    @Test
    public void testCreaPrenotazione_TC11_ClienteNull_ThrowsException(){
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniService.creaPrenotazione(
                    dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, null
            );
        });

        assertEquals("Il cliente deve essere autenticato", exception.getMessage());

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertTrue(prenotazioni.isEmpty());
        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertTrue(cliente.getPrenotazioni().isEmpty());
        camera = cameraRepository.findByNumero(101);
        assertTrue(camera.getPrenotazioni().isEmpty());
    }

    @Test
    public void testCreaPrenotazione_TC12_PrenotazioneEffettuata(){
        LocalDate dataCheckIn = LocalDate.now();
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        ClienteDettagli clienteDettagli = clienteDettagliRepository.findById(utente.getEmail()).get();
        Prenotazione risultato = gestionePrenotazioniService.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteDettagli);
        assertNotNull(risultato);

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertEquals(1, prenotazioni.size());
        Prenotazione prenotazione = prenotazioni.getFirst();
        assertEquals(dataCheckIn, prenotazione.getDataCheckIn());
        assertEquals(dataCheckOut, prenotazione.getDataCheckOut());
        assertEquals(numeroOspiti, prenotazione.getNumeroOspiti());
        assertEquals(camera.getId(), prenotazione.getCamera().getId());
        assertEquals(100, risultato.getCostoUnitarioCamera());
        assertEquals(100, risultato.getImportoTotale());
        assertTrue(prenotazione.getServiziPrenotati().isEmpty());

        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertEquals(1, cliente.getPrenotazioni().size());
        prenotazione = cliente.getPrenotazioni().iterator().next();
        assertEquals(cliente.getEmail(), prenotazione.getCliente().getEmail());

        camera = cameraRepository.findByNumero(101);
        assertEquals(1, camera.getPrenotazioni().size());
    }

    @Test
    public void testCreaPrenotazione_TC13_PrenotazioneEffettuata(){
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        ClienteDettagli clienteDettagli = clienteDettagliRepository.findById(utente.getEmail()).get();
        Prenotazione risultato = gestionePrenotazioniService.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteDettagli);
        assertNotNull(risultato);

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertEquals(1, prenotazioni.size());
        Prenotazione prenotazione = prenotazioni.getFirst();
        assertEquals(dataCheckIn, prenotazione.getDataCheckIn());
        assertEquals(dataCheckOut, prenotazione.getDataCheckOut());
        assertEquals(numeroOspiti, prenotazione.getNumeroOspiti());
        assertEquals(camera.getId(), prenotazione.getCamera().getId());
        assertEquals(100, risultato.getCostoUnitarioCamera());
        assertEquals(100, risultato.getImportoTotale());
        assertTrue(prenotazione.getServiziPrenotati().isEmpty());

        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertEquals(1, cliente.getPrenotazioni().size());
        prenotazione = cliente.getPrenotazioni().iterator().next();
        assertEquals(cliente.getEmail(), prenotazione.getCliente().getEmail());

        camera = cameraRepository.findByNumero(101);
        assertEquals(1, camera.getPrenotazioni().size());
    }

    @Test
    public void testCreaPrenotazione_TC14_PrenotazioneEffettuata(){
        LocalDate dataCheckIn = LocalDate.now().plusDays(5);
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();
        Servizio colazione = new Servizio("Colazione", "descrizione", 10, "Images/Demo/Servizi/colazione1.jpg");
        Servizio spa = new Servizio("Spa", "descrizione", 40, "Images/Demo/Servizi/spa1.jpg");
        servizioRepository.save(colazione);
        servizioRepository.save(spa);
        ServizioPrenotato colazionePrenotata = new ServizioPrenotato(colazione.getCosto(), 2, colazione);
        ServizioPrenotato spaPrenotata = new ServizioPrenotato(spa.getCosto(), 1, spa);
        servizi.add(colazionePrenotata);
        servizi.add(spaPrenotata);

        ClienteDettagli clienteDettagli = clienteDettagliRepository.findById(utente.getEmail()).get();
        Prenotazione risultato = gestionePrenotazioniService.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteDettagli);
        assertNotNull(risultato);

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertEquals(1, prenotazioni.size());
        Prenotazione prenotazione = prenotazioni.getFirst();
        assertEquals(dataCheckIn, prenotazione.getDataCheckIn());
        assertEquals(dataCheckOut, prenotazione.getDataCheckOut());
        assertEquals(numeroOspiti, prenotazione.getNumeroOspiti());
        assertEquals(camera.getId(), prenotazione.getCamera().getId());
        assertEquals(100, risultato.getCostoUnitarioCamera());
        assertEquals(160, risultato.getImportoTotale());
        assertEquals(2, prenotazione.getServiziPrenotati().size());

        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertEquals(1, cliente.getPrenotazioni().size());
        prenotazione = cliente.getPrenotazioni().iterator().next();
        assertEquals(cliente.getEmail(), prenotazione.getCliente().getEmail());

        camera = cameraRepository.findByNumero(101);
        assertEquals(1, camera.getPrenotazioni().size());

        List<ServizioPrenotato> serviziPrenotati = servizioPrenotatoRepository.findAll();
        assertEquals(2, serviziPrenotati.size());
    }

    @Test
    public void testCreaPrenotazione_TC15_PrenotazioneEffettuata(){
        LocalDate dataCheckIn = LocalDate.now();
        LocalDate dataCheckOut = LocalDate.now().plusDays(10);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();
        Servizio colazione = new Servizio("Colazione", "descrizione", 10, "Images/Demo/Servizi/colazione1.jpg");
        Servizio spa = new Servizio("Spa", "descrizione", 40, "Images/Demo/Servizi/spa1.jpg");
        servizioRepository.save(colazione);
        servizioRepository.save(spa);
        ServizioPrenotato colazionePrenotata = new ServizioPrenotato(colazione.getCosto(), 2, colazione);
        ServizioPrenotato spaPrenotata = new ServizioPrenotato(spa.getCosto(), 1, spa);
        servizi.add(colazionePrenotata);
        servizi.add(spaPrenotata);

        ClienteDettagli clienteDettagli = clienteDettagliRepository.findById(utente.getEmail()).get();
        Prenotazione risultato = gestionePrenotazioniService.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteDettagli);
        assertNotNull(risultato);

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        assertEquals(1, prenotazioni.size());
        Prenotazione prenotazione = prenotazioni.getFirst();
        assertEquals(dataCheckIn, prenotazione.getDataCheckIn());
        assertEquals(dataCheckOut, prenotazione.getDataCheckOut());
        assertEquals(numeroOspiti, prenotazione.getNumeroOspiti());
        assertEquals(camera.getId(), prenotazione.getCamera().getId());
        assertEquals(100, risultato.getCostoUnitarioCamera());
        assertEquals(160, risultato.getImportoTotale());
        assertEquals(2, prenotazione.getServiziPrenotati().size());

        cliente = clienteDettagliRepository.findById(utente.getEmail()).get();
        assertEquals(1, cliente.getPrenotazioni().size());
        prenotazione = cliente.getPrenotazioni().iterator().next();
        assertEquals(cliente.getEmail(), prenotazione.getCliente().getEmail());

        camera = cameraRepository.findByNumero(101);
        assertEquals(1, camera.getPrenotazioni().size());

        List<ServizioPrenotato> serviziPrenotati = servizioPrenotatoRepository.findAll();
        assertEquals(2, serviziPrenotati.size());
    }
}