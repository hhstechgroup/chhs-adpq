package com.engagepoint.cws.apqd.service.util;

import org.junit.Test;

import static com.engagepoint.cws.apqd.APQDTestUtil.assertThatConstructorIsPrivate;
import static com.engagepoint.cws.apqd.service.util.RandomUtil.generateActivationKey;
import static com.engagepoint.cws.apqd.service.util.RandomUtil.generatePassword;
import static com.engagepoint.cws.apqd.service.util.RandomUtil.generateResetKey;
import static org.assertj.core.api.StrictAssertions.assertThat;

public class RandomUtilTest {

    @Test
    public void testConstructorIsPrivate() throws Exception {
        assertThatConstructorIsPrivate(RandomUtil.class);
    }

    @Test
    public void testGeneratePassword() throws Exception {
        final String password1 = generatePassword();
        assertThat(password1).isNotNull();
        assertThat(password1).isNotEqualTo(generatePassword());
    }

    @Test
    public void testGenerateActivationKey() throws Exception {
        final String activationKey1 = generateActivationKey();
        assertThat(activationKey1).isNotNull();
        assertThat(activationKey1).isNotEqualTo(generateActivationKey());
    }

    @Test
    public void testGenerateResetKey() throws Exception {
        final String resetKey = generateResetKey();
        assertThat(resetKey).isNotNull();
        assertThat(resetKey).isNotEqualTo(generateResetKey());
    }
}
