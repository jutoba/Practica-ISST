package main.java.es.upm.dit.isst.tfgapi.repository;
import java.util.List;

public interface TFGRepository extends CrudRepository<TFG, String> {
    List<TFG> findByTutor(String tutor);
}
