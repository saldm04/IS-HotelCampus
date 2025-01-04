package it.unisa.hotelcampus.utils.acl;

import it.unisa.hotelcampus.model.entity.Utente;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servizio per la gestione delle regole di Access Control List (ACL).
 * Determina se un utente con un determinato ruolo può accedere a un metodo specifico.
 */
@Service
public class ACLService {

  /**
   * Mappa che associa il nome completo del metodo alle liste di ruoli autorizzati.
   */
  private static final Map<String, List<Utente.Ruolo>> ACL_MAP = new HashMap<>();

  static {
    //GestionePrenotazioniServImp
    ACL_MAP.put("GestionePrenotazioniServImp.getPrenotazioni(..)", List.of(Utente.Ruolo.CLIENTE, Utente.Ruolo.GESTOREPRENOTAZIONI));
    ACL_MAP.put("GestionePrenotazioniServImp.creaPrenotazione(..)", List.of(Utente.Ruolo.CLIENTE));
    ACL_MAP.put("GestionePrenotazioniServImp.eliminaPrenotazione(..)", List.of(Utente.Ruolo.GESTOREPRENOTAZIONI));
    ACL_MAP.put("GestionePrenotazioniServImp.cercaPrenotazioni(..)", List.of(Utente.Ruolo.GESTOREPRENOTAZIONI));
    //GestioneCamereServiceImpl
    ACL_MAP.put("GestioneCamereServiceImpl.creaCamera(..)", List.of(Utente.Ruolo.DIRETTORE));
    ACL_MAP.put("GestioneCamereServiceImpl.rimuoviCamera(..)", List.of(Utente.Ruolo.DIRETTORE));
    ACL_MAP.put("GestioneCamereServiceImpl.verificaDisponibilita(..)", List.of(Utente.Ruolo.CLIENTE));
    //GestioneServiziServiceImpl
    ACL_MAP.put("GestioneServiziServiceImpl.creaServizio(..)", List.of(Utente.Ruolo.DIRETTORE));
    ACL_MAP.put("GestioneServiziServiceImpl.rimuoviServizio(..)", List.of(Utente.Ruolo.DIRETTORE));
    //GestioneUtentiServiceImpl
    ACL_MAP.put("GestioneUtentiServiceImpl.elimina(..)", List.of(Utente.Ruolo.DIRETTORE));
    ACL_MAP.put("GestioneUtentiServiceImpl.getUtenti(..)", List.of(Utente.Ruolo.DIRETTORE));
    ACL_MAP.put("GestioneUtentiServiceImpl.getClienti(..)", List.of(Utente.Ruolo.GESTOREPRENOTAZIONI));
    ACL_MAP.put("GestioneUtentiServiceImpl.setRuolo(..)", List.of(Utente.Ruolo.DIRETTORE));
  }

  /**
   * Verifica se un utente con un determinato ruolo può accedere a un metodo specifico.
   *
   * @param fullMethodName il nome completo del metodo (classe.metodo(..))
   * @param role           il ruolo dell'utente
   * @return {@code true} se l'utente può accedere al metodo, {@code false} altrimenti
   */
  public boolean canAccess(String fullMethodName, Utente.Ruolo role) {
    List<Utente.Ruolo> allowedRoles = ACL_MAP.get(fullMethodName);
    return allowedRoles != null && allowedRoles.contains(role);
  }
}