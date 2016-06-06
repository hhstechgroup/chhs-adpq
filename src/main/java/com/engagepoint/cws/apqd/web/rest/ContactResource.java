package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Authority;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.repository.AuthorityRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.security.AuthoritiesConstants;
import com.engagepoint.cws.apqd.security.SecurityUtils;
import com.engagepoint.cws.apqd.web.rest.dto.ContactDTO;
import com.engagepoint.cws.apqd.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ContactResource {

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @RequestMapping(value = "/contacts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ContactDTO>> getContactsForMailTo() throws URISyntaxException {

        Page<User> page = loadContacts4CurrentUser();
        List<ContactDTO> managedUserDTOs = page.getContent().stream()
            .map(ContactDTO::new)
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contacts");
        return new ResponseEntity<>(managedUserDTOs, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/contacts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ContactDTO>> getContactsList() throws URISyntaxException {
        User userFrom = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        List<ContactDTO> contactDTOs = userFrom.getMailBox().getContacts().stream()
            .map(ContactDTO::new)
            .collect(Collectors.toList());

        return new ResponseEntity<>(contactDTOs, HttpStatus.OK);
    }

    private Page<User> loadContacts4CurrentUser() {
        Page<User> page = null;

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        Authority worker = authorityRepository.findOne(AuthoritiesConstants.CASE_WORKER);
        Authority parent = authorityRepository.findOne(AuthoritiesConstants.PARENT);

        if (user.getAuthorities().contains(parent)) {
            Set<User> contacts = user.getMailBox().getContacts();
            page = new PageImpl<>(new ArrayList<>(contacts));
        } else if (user.getAuthorities().contains(worker)) {
            List<User> others = new ArrayList<>();
            for (User other : userRepository.findAll()) {
                if (other.getAuthorities().contains(parent) ||
                    other.getAuthorities().contains(worker)) {
                    others.add(other);
                }
            }

            page = new PageImpl<>(new ArrayList<>(others));
        }

        return page;
    }
}
