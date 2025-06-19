package booking.data;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestTableRepository extends JpaRepository<RestTable, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM RestTable t WHERE t.id = :id")
    Optional<RestTable> findByIdForUpdate(@Param("id") Long id);

    @Query("SELECT t FROM RestTable t WHERE t.size >= :size ORDER BY t.size ASC")
    List<RestTable> findAllBySize(@Param("size") int size);

}
