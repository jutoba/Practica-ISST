package es.upm.dit.isst.tfg.tfgwebapp.model;
import java.net.URI;
import java.util.Arrays;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.DecimalMax;
import com.fasterxml.jackson.annotation.*;
public class TFG {
@Email private String alumno;
@Email private String tutor;
@NotEmpty private String titulo;
private String resumen;
private Estado estado;
private URI memoria;
@PositiveOrZero @DecimalMax("10.0") private Double calificacion;
private Boolean matriculaHonor;
private Sesion sesion;
}
