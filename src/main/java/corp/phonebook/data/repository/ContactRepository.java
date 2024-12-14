package corp.phonebook.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import corp.phonebook.data.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c WHERE c.id = :id")
    Contact findContactById(@Param("id") Long id);

    @Query("SELECT c FROM Contact c WHERE c.number = :number")
    Contact findContactByNumber(@Param("number") String number);

    @Query("SELECT COUNT(c) > 0 FROM Contact c WHERE c.id = :id")
    boolean existsContactById(@Param("id") Long id);
}
