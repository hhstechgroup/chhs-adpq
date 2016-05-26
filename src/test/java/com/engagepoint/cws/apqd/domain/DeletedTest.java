package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.DeletedRepository;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
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

        assertThat(new Deleted().equals(new Deleted())).isFalse();
        assertThat(deleted1.equals(new Deleted())).isFalse();
        assertThat(deleted1.equals(deleted2)).isFalse();
        assertThat(deletedRepository.findOne(deleted2.getId()).equals(deleted2)).isTrue();

        assertThat(deleted1.hashCode()).isNotNull();
        assertThat(deleted1.toString().length()).isGreaterThan(0);
    }
}
