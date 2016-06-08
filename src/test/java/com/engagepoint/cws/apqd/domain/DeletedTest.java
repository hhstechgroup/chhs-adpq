package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.AuthorityRepository;
import com.engagepoint.cws.apqd.repository.DeletedRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.time.ZonedDateTime;

import static com.engagepoint.cws.apqd.APQDTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DeletedTest {
    @Inject
    private DeletedRepository deletedRepository;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private AuthorityRepository authorityRepository;

    private Deleted createEntity(String messageSubject, String messageBody, User deletedBy) {
        Deleted deleted = new Deleted();
        deleted.setDeletedBy(deletedBy);
        deleted.setDeletedDate(ZonedDateTime.now());

        Message message = prepareMessage(messageRepository, messageSubject, messageBody, null, null);
        deleted.setMessage(message);

        return deletedRepository.saveAndFlush(deleted);
    }

    @Test
    @Transactional
    public void testEntityFields() throws Exception {
        User deletedBy = userRepository.save(newUserAnnaBrown(passwordEncoder, authorityRepository));
        Deleted deleted = createEntity("message subject", "message body", deletedBy);

        Deleted testDeleted = deletedRepository.findOne(deleted.getId());
        assertThat(testDeleted).isNotNull();
        assertThat(testDeleted.getMessage()).isEqualTo(deleted.getMessage());
        assertThat(testDeleted.getDeletedBy()).isEqualTo(deleted.getDeletedBy());
        assertThat(testDeleted.getDeletedDate()).isEqualTo(deleted.getDeletedDate());
    }

    @Test
    @Transactional
    public void testIdentity() throws Exception {
        User deletedBy1 = userRepository.save(newUserAnnaBrown(passwordEncoder, authorityRepository));
        Deleted deleted1 = createEntity("message subject 1", "message body 1", deletedBy1);

        User deletedBy2 = userRepository.save(newUserJohnWhite(passwordEncoder, authorityRepository));
        Deleted deleted2 = createEntity("message subject 2", "message body 2", deletedBy2);
        Deleted foundEntity = deletedRepository.findOne(deleted2.getId());

        assertObjectIdentity(deleted1, deleted2, foundEntity, null);
    }
}
