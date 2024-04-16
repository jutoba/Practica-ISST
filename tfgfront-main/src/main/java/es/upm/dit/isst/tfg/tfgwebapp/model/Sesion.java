package es.upm.dit.isst.tfg.tfgwebapp.model;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
public class Sesion {
private Long id;
@Future private Date fecha;
private String lugar;
@Size(min = 3, max = 3) private List<@Email @NotEmpty String> tribunal;
@JsonIgnore List<@Valid TFG> tfgs;
}