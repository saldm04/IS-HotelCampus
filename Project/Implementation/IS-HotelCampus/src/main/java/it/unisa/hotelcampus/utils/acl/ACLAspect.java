package it.unisa.hotelcampus.utils.acl;

import it.unisa.hotelcampus.model.entity.Utente;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Aspect per gestire il controllo degli accessi basato sull'annotazione {@link ControllaACL}.
 * Intercetta i metodi annotati con {@code @ControllaACL} e verifica se l'utente corrente ha i permessi necessari.
 */
@Aspect
@Component
public class ACLAspect {

  private final ACLService aclService;

  /**
   * Costruisce un'istanza di ACLAspect con il servizio ACLService iniettato.
   *
   * @param aclService il servizio per la gestione delle regole ACL
   */
  @Autowired
  public ACLAspect(ACLService aclService) {
    this.aclService = aclService;
  }

  /**
   * Intercetta l'esecuzione di metodi annotati con {@code @ControllaACL} e verifica i permessi dell'utente.
   *
   * @param joinPoint il punto di giunzione dell'esecuzione del metodo
   * @param controllaACL l'annotazione {@link ControllaACL}
   * @return il risultato dell'esecuzione del metodo intercettato
   * @throws IllegalAccessException se l'utente non ha i permessi per accedere al metodo
   */
  @Around("@annotation(controllaACL)")
  public Object controllaACL(ProceedingJoinPoint joinPoint, ControllaACL controllaACL) throws Throwable {
    // Recupera gli attributi della richiesta corrente
    RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
    HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);

    if (session == null) {
      throw new RuntimeException("Sessione non trovata");
    }


    Utente utente = (Utente) session.getAttribute("utente");

    if (utente == null) {
      throw new RuntimeException("Utente non autenticato");
    }


    if (!aclService.canAccess(joinPoint.getSignature().toShortString(), utente.getRuolo())) {
      throw new RuntimeException("Accesso negato");
    }

    return joinPoint.proceed();
  }
}