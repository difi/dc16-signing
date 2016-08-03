package no.difi.signing.repository;

import no.difi.signing.model.Conversation;
import org.springframework.data.repository.CrudRepository;

public interface ConversationRepository extends CrudRepository<Conversation, Long> {

    Conversation findByIdentifier(String identifier);

}
