package it.unisa.hotelcampus.model.entity;

import jakarta.persistence.*;
import lombok.*;

/*
 * @author Luca
 */

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private int numero;

    @Enumerated(EnumType.STRING) @NonNull
    private TipoCamera tipo;

    @NonNull
    private int numeroMaxOspiti;

    @NonNull
    private int quadratura;

    @NonNull
    private int costo;

    @NonNull
    private String immagine;

    @Column(nullable = false)
    private boolean isDeleted;

    public enum TipoCamera{
        STANDARD,
        DELUXE,
        LUXURY,
        EXCLUSIVE
    }


    public @NonNull TipoCamera getTipo() {
        return tipo;
    }

    public void setTipo(@NonNull TipoCamera tipo) {
        this.tipo = tipo;
    }

    public @NonNull int getNumeroMaxOspiti() {
        return numeroMaxOspiti;
    }

    public void setNumeroMaxOspiti(@NonNull int numeroMaxOspiti) {
        this.numeroMaxOspiti = numeroMaxOspiti;
    }

    public @NonNull int getQuadratura() {
        return quadratura;
    }

    public void setQuadratura(@NonNull int quadratura) {
        this.quadratura = quadratura;
    }

    public @NonNull int getCosto() {
        return costo;
    }

    public void setCosto(@NonNull int costo) {
        this.costo = costo;
    }

    public @NonNull String getImmagine() {
        return immagine;
    }

    public void setImmagine(@NonNull String immagine) {
        this.immagine = immagine;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
