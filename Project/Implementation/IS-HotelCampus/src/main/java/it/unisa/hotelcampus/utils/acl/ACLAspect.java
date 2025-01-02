package it.unisa.hotelcampus.utils.acl;

import it.unisa.hotelcampus.model.entity.Utente;
import it.unisa.hotelcampus.utils.UtenteInterceptor;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
public class ACLAspect {

  private final ACLService aclService;


  @Autowired
  public ACLAspect(ACLService aclService) {
    this.aclService = aclService;
  }

  @Around("@annotation(ControllaACL)")
  public Object controllaACL(ProceedingJoinPoint joinPoint) throws Throwable {


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