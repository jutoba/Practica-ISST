import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpHeaders;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import es.upm.dit.isst.tfgapi.model.Estado;
import es.upm.dit.isst.tfgapi.model.Sesion;
import es.upm.dit.isst.tfgapi.model.TFG;
import main.java.es.upm.dit.isst.tfgapi.repository.SesionRepository;
import main.java.es.upm.dit.isst.tfgapi.repository.TFGRepository;

@RestController
@RequestMapping("/myApi")
public class TFGController {

    private final TFGRepository tfgRepository;
    private final SesionRepository sesionRepository;

    public static final Logger log =
            LoggerFactory.getLogger(TFGController.class);

    @Autowired
    public TFGController(TFGRepository t, SesionRepository s) {
        this.tfgRepository = t;
        this.sesionRepository = s;
    }

    @GetMapping("/tfgs")
    List<TFG> readAll(
            @RequestParam(name = "tutor", required = false) String tutor) {
        if (tutor != null && !tutor.isEmpty()) {
            return (List<TFG>) tfgRepository.findByTutor(tutor);
        } else {
            return (List<TFG>) tfgRepository.findAll();
        }
    }

    @PostMapping("/tfgs")
    ResponseEntity<TFG> create(@RequestBody TFG newTFG)
            throws URISyntaxException {
        if (tfgRepository.findById(newTFG.getAlumno()).isPresent()) {
            return new ResponseEntity<TFG>(HttpStatus.CONFLICT);
        }
        TFG result = tfgRepository.save(newTFG);
        return ResponseEntity.created(new URI("/tfgs/" + result.getAlumno())).body(result);
    }

    @GetMapping("/tfgs/{id}")
    ResponseEntity<TFG> readOne(@PathVariable String id) {
        return tfgRepository.findById(id).map(tfg ->
                ResponseEntity.ok().body(tfg)
        ).orElse(new ResponseEntity<TFG>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/tfgs/{id}")
    ResponseEntity<TFG> update(@RequestBody TFG newTFG, @PathVariable String id) {
        return tfgRepository.findById(id).map(tfg -> {
            tfg.setAlumno(newTFG.getAlumno());
            tfg.setTutor(newTFG.getTutor());
            tfg.setTitulo(newTFG.getTitulo());
            tfg.setResumen(newTFG.getResumen());
            tfg.setEstado(newTFG.getEstado());
            tfg.setMemoria(newTFG.getMemoria());
            tfg.setCalificacion(newTFG.getCalificacion());
            tfg.setMatriculaHonor(newTFG.getMatriculaHonor());
            tfg.setSesion(newTFG.getSesion());
            tfgRepository.save(tfg);
            return ResponseEntity.ok().body(tfg);
        }).orElse(new ResponseEntity<TFG>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/tfgs/{id}")
    ResponseEntity<TFG> partialUpdate(@RequestBody TFG newTFG, @PathVariable String id) {
        return tfgRepository.findById(id).map(tfg -> {
            if (newTFG.getAlumno() != null) {
                tfg.setAlumno(newTFG.getAlumno());
            }
            if (newTFG.getTutor() != null) {
                tfg.setTutor(newTFG.getTutor());
            }
            if (newTFG.getTitulo() != null) {
                tfg.setTitulo(newTFG.getTitulo());
            }
            if (newTFG.getResumen() != null) {
                tfg.setResumen(newTFG.getResumen());
            }
            if (newTFG.getEstado() != null) {
                tfg.setEstado(newTFG.getEstado());
            }
            if (newTFG.getMemoria() != null) {
                tfg.setMemoria(newTFG.getMemoria());
            }
            if (newTFG.getCalificacion() != null) {
                tfg.setCalificacion(newTFG.getCalificacion());
            }
            if (newTFG.getMatriculaHonor() != null) {
                tfg.setMatriculaHonor(newTFG.getMatriculaHonor());
            }
            if (newTFG.getSesion() != null) {
                tfg.setSesion(newTFG.getSesion());
            }
            tfgRepository.save(tfg);
            return ResponseEntity.ok().body(tfg);
        }).orElse(new ResponseEntity<TFG>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/tfgs/{id}")
    ResponseEntity<TFG> delete(@PathVariable String id) {
        tfgRepository.deleteById(id);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/tfgs/{id}/estado/{estado}")
    @Transactional
    public ResponseEntity<?> actualizaEstado(@PathVariable String id, @PathVariable Estado estado) {
        return tfgRepository.findById(id).map(tfg -> {
            if (!tfg.getEstado().canTransitionTo(estado)) {
                return ResponseEntity.badRequest().body(
                        "No se puede pasar del estado "
                                + tfg.getEstado() + " a " + estado);
            }
            tfg.setEstado(estado);
            tfgRepository.save(tfg);
            return ResponseEntity.ok().body(tfg);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/tfgs/{id}/memoria", consumes = "application/pdf")
    public ResponseEntity<?> subeMemoria(@PathVariable String id, @RequestBody byte[] fileContent) {
        return tfgRepository.findById(id).map(tfg -> {
            tfg.setMemoria(fileContent);
            tfgRepository.save(tfg);
            return ResponseEntity.ok("Documento subido correctamente");
        }).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "TFG no encontrado")
        );
    }

    @GetMapping(value = "/tfgs/{id}/memoria", produces = "application/pdf")
    public ResponseEntity<?> descargaMemoria(@PathVariable String id) {
        TFG tfg = tfgRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "TFG no encontrado"));
        if (tfg.getMemoria() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"tfg_document_" + id + ".pdf" + "\"")
                .body(new ByteArrayResource(tfg.getMemoria()));
    }

    @PostMapping("/sesiones")
    ResponseEntity<Sesion> createSesion(@RequestBody Sesion newSesion)
            throws URISyntaxException {
        Sesion result = sesionRepository.save(newSesion);
        return ResponseEntity.created(new URI("/sesiones/" + result.getId())).body(result);
    }

    @PostMapping("/sesiones/{id}/tfgs")
    ResponseEntity<?> asignaTFG(@PathVariable Long id,
                                @RequestBody String alumno) {
        return sesionRepository.findById(id).map(sesion -> {
            TFG tfg = tfgRepository.findById(alumno).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TFG no encontrado"));
            tfg.setSesion(sesion);
            tfgRepository.save(tfg);
            return ResponseEntity.ok().body(tfg);
        }).orElse(ResponseEntity.notFound().build());
    }
}
