package it.unisa.hotelcampus.gestioneprenotazioni.service;

import it.unisa.hotelcampus.gestionecamere.service.GestioneCamereServiceImpl;
import it.unisa.hotelcampus.model.dao.ClienteDettagliRepository;
import it.unisa.hotelcampus.model.dao.PrenotazioneRepository;
import it.unisa.hotelcampus.model.entity.Camera;
import it.unisa.hotelcampus.model.entity.ClienteDettagli;
import it.unisa.hotelcampus.model.entity.Prenotazione;
import it.unisa.hotelcampus.model.entity.ServizioPrenotato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GestionePrenotServImpTest {
    @Mock
    private PrenotazioneRepository prenotazioneRepository;

    @Mock
    private GestioneCamereServiceImpl gestioneCamereService;

    @Mock
    private ClienteDettagliRepository clienteDettagliRepository;

    @InjectMocks
    private GestionePrenotazioniServImp gestionePrenotazioniServImp;

    private ClienteDettagli clienteAutenticato;
    private Camera camera;

    @BeforeEach
    public void setUp() {
        clienteAutenticato = new ClienteDettagli();
        clienteAutenticato.setEmail("cliente@esempio.it");

        camera = new Camera();
        camera.setId(1L);
        camera.setNumeroMaxOspiti(2);
        camera.setCosto(100);
    }

    @Test
    public void testCreaPrenotazione_TC1_DataCheckInNull_ThrowsException() {
        Date dataCheckIn = null;
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        });

        assertEquals("Le date di check-in e check-out non possono essere nulle", exception.getMessage());

        verifyNoInteractions(gestioneCamereService);
        verifyNoInteractions(prenotazioneRepository);
        verifyNoInteractions(clienteDettagliRepository);
    }

    @Test
    public void testCreaPrenotazione_TC2_DataCheckInPassata_ThrowsException(){
        Date dataCheckIn = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        });

        assertEquals("La data di check-in non può essere precedente alla data odierna", exception.getMessage());

        verifyNoInteractions(gestioneCamereService);
        verifyNoInteractions(prenotazioneRepository);
        verifyNoInteractions(clienteDettagliRepository);
    }

    @Test
    public void testCreaPrenotazione_TC3_DataCheckOutNull_ThrowsException(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = null;
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        });

        assertEquals("Le date di check-in e check-out non possono essere nulle", exception.getMessage());

        verifyNoInteractions(gestioneCamereService);
        verifyNoInteractions(prenotazioneRepository);
        verifyNoInteractions(clienteDettagliRepository);
    }

    @Test
    public void testCreaPrenotazione_TC4_DataCheckOutPrimaCheckIn_ThrowsException(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        });

        assertEquals("La data di check-out non può essere precedente alla data di check-in", exception.getMessage());

        verifyNoInteractions(gestioneCamereService);
        verifyNoInteractions(prenotazioneRepository);
        verifyNoInteractions(clienteDettagliRepository);
    }

    @Test
    public void testCreaPrenotazione_TC5_DataCheckOutUgualeCheckIn_ThrowsException(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        });

        assertEquals("La data di check-out non può essere uguale alla data di check-in", exception.getMessage());

        verifyNoInteractions(gestioneCamereService);
        verifyNoInteractions(prenotazioneRepository);
        verifyNoInteractions(clienteDettagliRepository);
    }

    @Test
    public void testCreaPrenotazione_TC6_NumeroOspitiMinoreUgualeZero_ThrowsException(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 0;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        });

        assertEquals("Il numero di ospiti deve essere maggiore di 0", exception.getMessage());

        verifyNoInteractions(gestioneCamereService);
        verifyNoInteractions(prenotazioneRepository);
        verifyNoInteractions(clienteDettagliRepository);
    }

    @Test
    public void testCreaPrenotazione_TC7_NumeroOspitiMaggioreNumeroMaxOspiti_ThrowsException(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 3;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        });

        assertEquals("Il numero di ospiti non può essere maggiore del numero massimo di ospiti della camera", exception.getMessage());

        verifyNoInteractions(gestioneCamereService);
        verifyNoInteractions(prenotazioneRepository);
        verifyNoInteractions(clienteDettagliRepository);
    }

    @Test
    public void testCreaPrenotazione_TC8_CameraNull_ThrowsException(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, null, servizi, clienteAutenticato);
        });

        assertEquals("La camera non è selezionata", exception.getMessage());

        verifyNoInteractions(gestioneCamereService);
        verifyNoInteractions(prenotazioneRepository);
        verifyNoInteractions(clienteDettagliRepository);
    }

    @Test
    public void testCreaPrenotazione_TC9_CameraNonDisponibile_ThrowsException(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        when(gestioneCamereService.verificaDisponibilita(camera, dataCheckIn, dataCheckOut)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        });

        assertEquals("La camera non è più disponibile per le date selezionate", exception.getMessage());

        verifyNoInteractions(prenotazioneRepository);
        verifyNoInteractions(clienteDettagliRepository);
    }

    @Test
    public void testCreaPrenotazione_TC10_NumeroServiziMaggioreNumeroOspiti_ThrowsException(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();
        ServizioPrenotato servizio1 = new ServizioPrenotato();
        servizio1.setNumeroServizi(3);
        ServizioPrenotato servizio2 = new ServizioPrenotato();
        servizio2.setNumeroServizi(1);
        servizi.add(servizio1);
        servizi.add(servizio2);

        when(gestioneCamereService.verificaDisponibilita(camera, dataCheckIn, dataCheckOut)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        });

        assertEquals("Il numero di servizi non può essere maggiore del numero di ospiti", exception.getMessage());

        verify(gestioneCamereService, times(1)).verificaDisponibilita(camera, dataCheckIn, dataCheckOut);
        verify(prenotazioneRepository, never()).save(any(Prenotazione.class));
        verify(clienteDettagliRepository, never()).save(any(ClienteDettagli.class));
    }

    @Test
    public void testCreaPrenotazione_TC11_ClienteNull_ThrowsException(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, null);
        });

        assertEquals("Il cliente deve essere autenticato", exception.getMessage());

        verifyNoInteractions(gestioneCamereService);
        verifyNoInteractions(prenotazioneRepository);
        verifyNoInteractions(clienteDettagliRepository);
    }

    @Test
    public void testCreaPrenotazione_TC12_PrenotazioneEffettuata(){
        Date dataCheckIn = new Date(System.currentTimeMillis());
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        when(gestioneCamereService.verificaDisponibilita(camera, dataCheckIn, dataCheckOut)).thenReturn(true);

        Prenotazione prenotazione = new Prenotazione(new Date() , dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        when(prenotazioneRepository.save(any(Prenotazione.class))).thenReturn(prenotazione);

        Prenotazione risultato = gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);

        assertNotNull(risultato);
        assertEquals(camera, risultato.getCamera());
        assertEquals(numeroOspiti, risultato.getNumeroOspiti());
        assertEquals(100, risultato.getCostoUnitarioCamera());
        assertEquals(100, risultato.getImportoTotale());

        ArgumentCaptor<Prenotazione> prenotazioneCaptor = ArgumentCaptor.forClass(Prenotazione.class);
        verify(prenotazioneRepository, times(1)).save(prenotazioneCaptor.capture());
        Prenotazione catturata = prenotazioneCaptor.getValue();
        assertEquals(dataCheckIn, catturata.getDataCheckIn());
        assertEquals(dataCheckOut, catturata.getDataCheckOut());
        assertEquals(numeroOspiti, catturata.getNumeroOspiti());
        assertEquals(camera, catturata.getCamera());
        assertEquals(servizi, catturata.getServiziPrenotati());
        assertEquals(0, catturata.getServiziPrenotati().size());

        verify(clienteDettagliRepository, times(1)).save(clienteAutenticato);
    }

    @Test
    public void testCreaPrenotazione_TC13_PrenotazioneEffettuata(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();

        when(gestioneCamereService.verificaDisponibilita(camera, dataCheckIn, dataCheckOut)).thenReturn(true);

        Prenotazione prenotazione = new Prenotazione(new Date() , dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        when(prenotazioneRepository.save(any(Prenotazione.class))).thenReturn(prenotazione);

        Prenotazione risultato = gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);

        assertNotNull(risultato);
        assertEquals(camera, risultato.getCamera());
        assertEquals(numeroOspiti, risultato.getNumeroOspiti());
        assertEquals(100, risultato.getCostoUnitarioCamera());
        assertEquals(100, risultato.getImportoTotale());

        ArgumentCaptor<Prenotazione> prenotazioneCaptor = ArgumentCaptor.forClass(Prenotazione.class);
        verify(prenotazioneRepository, times(1)).save(prenotazioneCaptor.capture());
        Prenotazione catturata = prenotazioneCaptor.getValue();
        assertEquals(dataCheckIn, catturata.getDataCheckIn());
        assertEquals(dataCheckOut, catturata.getDataCheckOut());
        assertEquals(numeroOspiti, catturata.getNumeroOspiti());
        assertEquals(camera, catturata.getCamera());
        assertEquals(servizi, catturata.getServiziPrenotati());
        assertEquals(0, catturata.getServiziPrenotati().size());

        verify(clienteDettagliRepository, times(1)).save(clienteAutenticato);
    }

    @Test
    public void testCreaPrenotazione_TC14_PrenotazioneEffettuata(){
        Date dataCheckIn = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();
        ServizioPrenotato servizio1 = new ServizioPrenotato();
        ServizioPrenotato servizio2 = new ServizioPrenotato();
        servizio1.setNumeroServizi(1);
        servizio1.setCostoUnitario(20);
        servizio2.setNumeroServizi(2);
        servizio2.setCostoUnitario(30);
        servizi.add(servizio1);
        servizi.add(servizio2);

        when(gestioneCamereService.verificaDisponibilita(camera, dataCheckIn, dataCheckOut)).thenReturn(true);

        Prenotazione prenotazione = new Prenotazione(new Date() , dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        when(prenotazioneRepository.save(any(Prenotazione.class))).thenReturn(prenotazione);

        Prenotazione risultato = gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);

        assertNotNull(risultato);
        assertEquals(camera, risultato.getCamera());
        assertEquals(numeroOspiti, risultato.getNumeroOspiti());
        assertEquals(100, risultato.getCostoUnitarioCamera());
        assertEquals(180, risultato.getImportoTotale());
        assertEquals(servizi, risultato.getServiziPrenotati());

        ArgumentCaptor<Prenotazione> prenotazioneCaptor = ArgumentCaptor.forClass(Prenotazione.class);
        verify(prenotazioneRepository, times(1)).save(prenotazioneCaptor.capture());
        Prenotazione catturata = prenotazioneCaptor.getValue();
        assertEquals(dataCheckIn, catturata.getDataCheckIn());
        assertEquals(dataCheckOut, catturata.getDataCheckOut());
        assertEquals(numeroOspiti, catturata.getNumeroOspiti());
        assertEquals(camera, catturata.getCamera());
        assertEquals(servizi, catturata.getServiziPrenotati());
        assertEquals(2, catturata.getServiziPrenotati().size());
        assertTrue(catturata.getServiziPrenotati().contains(servizio1));
        assertTrue(catturata.getServiziPrenotati().contains(servizio2));

        verify(clienteDettagliRepository, times(1)).save(clienteAutenticato);
    }

    @Test
    public void testCreaPrenotazione_TC15_PrenotazioneEffettuata(){
        Date dataCheckIn = new Date(System.currentTimeMillis());
        Date dataCheckOut = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        int numeroOspiti = 2;
        Set<ServizioPrenotato> servizi = new HashSet<>();
        ServizioPrenotato servizio1 = new ServizioPrenotato();
        ServizioPrenotato servizio2 = new ServizioPrenotato();
        servizio1.setNumeroServizi(1);
        servizio1.setCostoUnitario(20);
        servizio2.setNumeroServizi(2);
        servizio2.setCostoUnitario(30);
        servizi.add(servizio1);
        servizi.add(servizio2);

        when(gestioneCamereService.verificaDisponibilita(camera, dataCheckIn, dataCheckOut)).thenReturn(true);

        Prenotazione prenotazione = new Prenotazione(new Date() , dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);
        when(prenotazioneRepository.save(any(Prenotazione.class))).thenReturn(prenotazione);

        Prenotazione risultato = gestionePrenotazioniServImp.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, servizi, clienteAutenticato);

        assertNotNull(risultato);
        assertEquals(camera, risultato.getCamera());
        assertEquals(numeroOspiti, risultato.getNumeroOspiti());
        assertEquals(100, risultato.getCostoUnitarioCamera());
        assertEquals(180, risultato.getImportoTotale());
        assertEquals(servizi, risultato.getServiziPrenotati());

        ArgumentCaptor<Prenotazione> prenotazioneCaptor = ArgumentCaptor.forClass(Prenotazione.class);
        verify(prenotazioneRepository, times(1)).save(prenotazioneCaptor.capture());
        Prenotazione catturata = prenotazioneCaptor.getValue();
        assertEquals(dataCheckIn, catturata.getDataCheckIn());
        assertEquals(dataCheckOut, catturata.getDataCheckOut());
        assertEquals(numeroOspiti, catturata.getNumeroOspiti());
        assertEquals(camera, catturata.getCamera());
        assertEquals(servizi, catturata.getServiziPrenotati());
        assertEquals(2, catturata.getServiziPrenotati().size());
        assertTrue(catturata.getServiziPrenotati().contains(servizio1));
        assertTrue(catturata.getServiziPrenotati().contains(servizio2));

        verify(clienteDettagliRepository, times(1)).save(clienteAutenticato);
    }
}
