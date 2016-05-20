package com.engagepoint.cws.apqd.service.usps;

/**
 * Created by dmytro.palczewski on 2/10/2016.
 */
class UspsRequestException extends Exception {
    UspsRequestException() {
        super();
    }

    UspsRequestException(String msg) {
        super(msg);
    }

    UspsRequestException(Throwable t) {
        super(t);
    }

    UspsRequestException(String msg, Throwable t) {
        super(msg, t);
    }
}
