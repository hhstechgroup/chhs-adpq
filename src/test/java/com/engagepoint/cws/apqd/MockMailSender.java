package com.engagepoint.cws.apqd;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.AsyncResult;

import javax.mail.internet.MimeMessage;
import java.util.concurrent.Future;

public class MockMailSender extends JavaMailSenderImpl {
    // only one send/getFutureMimeMessage cycle at a time
    private Future<MimeMessage> futureMimeMessage;

    private boolean simulateFailure;

    public MockMailSender() {
        this(false);
    }

    public MockMailSender(boolean simulateFailure) {
        this.simulateFailure = simulateFailure;
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        futureMimeMessage = new AsyncResult<>(mimeMessage);

        if (simulateFailure) {
            throw new MailSendException("send failed");
        }
    }

    public Future<MimeMessage> getFutureMimeMessage() {
        return futureMimeMessage;
    }
}
