package es.upm.dit.isst.tfgapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.DecimalMax;
import java.util.Objects;

@Entity
public class TFG {

    @Id
    @Email
    private String alumno;

    @Email
    private String tutor;

    private String titulo;
    private String resumen;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @JsonIgnore
    @Lob
    private byte[] memoria;

    @PositiveOrZero
    @DecimalMax("10.0")
    private Double calificacion;

    private Boolean matriculaHonor;

    @ManyToOne
    private Sesion sesion;

    // Constructor público sin parámetros
    public TFG() {
        // Constructor vacío necesario para JPA y Spring
    }

    // Getters y setters para todos los atributos
    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    // Anotación @JsonIgnore para evitar que Spring use automáticamente este método en las de/serializaciones
    @JsonIgnore
    public byte[] getMemoria() {
        return memoria;
    }

    // Anotación @JsonProperty para indicar que la memoria se crea a través de este método
    @JsonProperty
    public void setMemoria(byte[] memoria) {
        this.memoria = memoria;
    }

    // Anotación @JsonGetter para indicar que este método se usará para la propiedad memoria al serializar a JSON
    @JsonGetter("memoria")
    public String getMemoriaUrl() {
        // Aquí puedes retornar la URL relativa al TFG para la memoria
        // Por ejemplo:
        // return "/tfgs/" + alumno + "/memoria";
        return "URL de memoria";
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public Boolean getMatriculaHonor() {
        return matriculaHonor;
    }

    public void setMatriculaHonor(Boolean matriculaHonor) {
        this.matriculaHonor = matriculaHonor;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    // Equals y hashCode excluyendo el atributo memoria
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFG tfg = (TFG) o;
        return Objects.equals(alumno, tfg.alumno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alumno);
    }

    // Método toString personalizado para excluir el atributo memoria
    @Override
    public String toString() {
        return "TFG{" +
                "alumno='" + alumno + '\'' +
                ", tutor='" + tutor + '\'' +
                ", titulo='" + titulo + '\'' +
                ", resumen='" + resumen + '\'' +
                ", estado=" + estado +
                ", calificacion=" + calificacion +
                ", matriculaHonor=" + matriculaHonor +
                ", sesion=" + sesion +
                '}';
    }
}

