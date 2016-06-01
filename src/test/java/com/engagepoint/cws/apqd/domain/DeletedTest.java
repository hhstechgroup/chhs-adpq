package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.DeletedRepository;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static com.engagepoint.cws.apqd.APQDTestUtil.assertIdentity;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DeletedTest {
    @Inject
    private DeletedRepository deletedRepository;

    @Inject
    private MailBoxRepository mailBoxRepository;

    private Deleted createEntity() {
        Deleted deleted = new Deleted();
        deleted.setMailBox(mailBoxRepository.saveAndFlush(new MailBox()));
        return deletedRepository.saveAndFlush(deleted);
    }

    @Test
    @Transactional
    public void testEntityFields() throws Exception {
        Deleted deleted = createEntity();

        Deleted testDeleted = deletedRepository.findOne(deleted.getId());
        assertThat(testDeleted).isNotNull();
        assertThat(testDeleted.getMailBox()).isNotNull();
    }

    @Test
    @Transactional
    public void testIdentity() throws Exception {
        Deleted deleted1 = createEntity();
        Deleted deleted2 = createEntity();
        Deleted foundEntity = deletedRepository.findOne(deleted2.getId());

        assertIdentity(deleted1, deleted2, foundEntity, null);
    }
}
