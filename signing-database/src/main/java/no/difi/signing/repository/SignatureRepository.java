package no.difi.signing.repository;

import no.difi.signing.model.Signature;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SignatureRepository extends CrudRepository<Signature, Long> {

    Signature findByIdentifier(String identifier);

    List<Signature> findByPid(String pid);

}
