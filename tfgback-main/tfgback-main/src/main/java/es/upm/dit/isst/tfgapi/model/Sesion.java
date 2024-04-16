package es.upm.dit.isst.tfgapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Future;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Future
    private Date fecha;

    private String lugar;

    @Size(min = 3, max = 3)
    private List<@Email @NotEmpty String> tribunal;

    @JsonIgnore
    @OneToMany(mappedBy = "sesion")
    List<@Valid TFG> tfgs;

    // Constructor público sin parámetros
    public Sesion() {
        // Constructor vacío necesario para JPA y Spring
    }

    // Getters y setters para todos los atributos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public List<String> getTribunal() {
        return tribunal;
    }

    public void setTribunal(List<String> tribunal) {
        this.tribunal = tribunal;
    }

    // Anotación @JsonIgnore para evitar que Spring use automáticamente este método en las de/serializaciones
    @JsonIgnore
    public List<TFG> getTfgs() {
        return tfgs;
    }

    // Anotación @JsonGetter para indicar que este método se usará para la propiedad tfgs al serializar a JSON
    @JsonGetter("tfgs")
    public String[] getEmailsTfgs() {
        if (tfgs != null) {
            return tfgs.stream().map(TFG::getAlumno).toArray(String[]::new);
        } else {
            return new String[0];
        }
    }

    // Anotación @JsonProperty para indicar que la propiedad tfgs se establece a través de este método
    @JsonProperty("tfgs")
    public void setTfgs(List<TFG> tfgs) {
        this.tfgs = tfgs;
    }

    // Equals y hashCode excluyendo la propiedad tfgs
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sesion sesion = (Sesion) o;
        return Objects.equals(id, sesion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Método toString personalizado para excluir la propiedad tfgs
    @Override
    public String toString() {
        return "Sesion{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", lugar='" + lugar + '\'' +
                ", tribunal=" + tribunal +
                '}';
    }
}

