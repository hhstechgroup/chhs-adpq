package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.domain.enumeration.MessageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Message entity.
 */
public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("SELECT COUNT(m) FROM Message m " +
           " WHERE m.draft IS NOT NULL " +
           "   AND m.from = :from " +
           "   AND NOT EXISTS (" +
           "       SELECT d FROM Deleted d " +
           "        WHERE d.deletedBy = :from " +
           "          AND d.message = m)")
    Long countByDrafts(@Param("from") User from);

    @Query("SELECT COUNT(m) FROM Message m " +
           " WHERE m.inbox IS NOT NULL " +
           "   AND m.status = :status " +
           "   AND m.to = :to " +
           "   AND NOT EXISTS (" +
           "       SELECT d FROM Deleted d " +
           "        WHERE d.deletedBy = :to " +
           "          AND d.message = m)")
    Long countByInbox(@Param("to") User to, @Param("status") MessageStatus status);

    @Query("SELECT COUNT(d) FROM Deleted d " +
           " WHERE d.deletedBy = :deletedBy")
    Long countByDeleted(@Param("deletedBy") User deletedBy);

    @Query("SELECT m FROM Message m " +
           " WHERE m.inbox IS NOT NULL " +
           "   AND ((m.replyOn IS NULL AND m.to = :to) OR " +
           "        (m.biDirectional IS NOT NULL AND m.from = :to))" +
           "   AND NOT EXISTS (" +
           "       SELECT d FROM Deleted d " +
           "        WHERE d.deletedBy = :to " +
           "          AND d.message = m)" +
           " ORDER BY m.dateUpdated DESC")
    Page<Message> findAllInbox(@Param("to") User to, Pageable pageable);

    @Query("SELECT m FROM Message m " +
           " WHERE m.outbox IS NOT NULL " +
           "   AND m.from = :from " +
           "   AND NOT EXISTS (" +
           "       SELECT d FROM Deleted d " +
           "        WHERE d.deletedBy = :from " +
           "          AND d.message = m)" +
           " ORDER BY m.dateUpdated DESC")
    Page<Message> findAllOutbox(@Param("from") User from, Pageable pageable);

    @Query("SELECT m FROM Message m " +
           " WHERE m.draft IS NOT NULL " +
           "   AND m.from = :from " +
           "   AND NOT EXISTS (" +
           "       SELECT d FROM Deleted d " +
           "        WHERE d.deletedBy = :from " +
           "          AND d.message = m)" +
           " ORDER BY m.dateCreated DESC")
    Page<Message> findAllDrafts(@Param("from") User from, Pageable pageable);

    @Query("SELECT m FROM Deleted d " +
           "  JOIN d.message m " +
           " WHERE d.deletedBy = :deletedBy " +
           " ORDER BY d.deletedDate DESC")
    Page<Message> findAllDeleted(@Param("deletedBy") User deletedBy, Pageable pageable);

}
