package corp.phonebook.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import corp.phonebook.data.entity.PhoneBook;

@Repository
public interface PhoneBookRepository extends JpaRepository<PhoneBook, Long> {
    @Query("SELECT pb FROM PhoneBook pb JOIN pb.owner o WHERE o.id = :ownerId")
    PhoneBook findPhoneBookByOwner_Id(@Param("ownerId") Long ownerId);

    @Query("SELECT pb FROM PhoneBook pb WHERE pb.id = :id")
    PhoneBook findPhoneBookById(@Param("id") long id);
}
