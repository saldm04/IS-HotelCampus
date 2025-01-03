package it.unisa.hotelcampus.utils.acl;

import it.unisa.hotelcampus.model.entity.Utente;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ACLService {
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


  public boolean canAccess(String fullMethodName, Utente.Ruolo role) {
    List<Utente.Ruolo> allowedRoles = ACL_MAP.get(fullMethodName);
    return allowedRoles != null && allowedRoles.contains(role);
  }
}