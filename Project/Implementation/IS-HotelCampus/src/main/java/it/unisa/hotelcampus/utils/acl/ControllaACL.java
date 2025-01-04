package it.unisa.hotelcampus.utils.acl;

import it.unisa.hotelcampus.model.entity.Utente;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotazione per indicare che un metodo deve essere controllato per i permessi di accesso.
 * Utilizzata insieme all'aspect {@link it.unisa.hotelcampus.utils.acl.ACLAspect}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ControllaACL {
}
