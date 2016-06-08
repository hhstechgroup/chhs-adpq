package com.engagepoint.cws.apqd.security;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.repository.AuthorityRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.Collection;

import static com.engagepoint.cws.apqd.APQDTestUtil.addUserRole;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareUser;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserDetailsServiceTest {
    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private AuthorityRepository authorityRepository;

    private UserDetailsService userDetailsService;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new UserDetailsService();

        ReflectionTestUtils.setField(userDetailsService, "userRepository", userRepository);
    }


    @Test(expected = UsernameNotFoundException.class)
    public void testUserNotFound() {
        userDetailsService.loadUserByUsername("NoSuchUser");
    }

    @Test(expected = UserNotActivatedException.class)
    public void testUserNotActivated() {
        final String INACTIVE_LOGIN = "inactiveUser".toLowerCase();
        prepareUser(userRepository, passwordEncoder, INACTIVE_LOGIN);
        userDetailsService.loadUserByUsername(INACTIVE_LOGIN);
    }

    @Test
    @Transactional
    public void testUserDetails() {
        final String LOGIN = "activeUser".toLowerCase();
        User user = prepareUser(null, passwordEncoder, LOGIN);
        user.setActivated(true);
        addUserRole(authorityRepository, user, AuthoritiesConstants.USER);

        userRepository.saveAndFlush(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(LOGIN);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(LOGIN);
        assertThat(userDetails.getPassword()).isNotEmpty();
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();

        Collection<? extends GrantedAuthority> testAuthorities = userDetails.getAuthorities();
        assertThat(testAuthorities).isNotNull();
        assertThat(testAuthorities.iterator().hasNext()).isTrue();
        assertThat(testAuthorities.iterator().next().getAuthority()).isEqualTo(AuthoritiesConstants.USER);
    }
}
