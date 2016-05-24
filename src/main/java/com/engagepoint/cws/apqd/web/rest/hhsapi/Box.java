package com.engagepoint.cws.apqd.web.rest.hhsapi;

/**
 * Created by dmitry.rudenko on 5/23/2016.
 */
public class Box {
    private Corner northwest;
    private Corner southeast;

    public Corner getNorthwest() {
        return northwest;
    }

    public void setNorthwest(Corner northwest) {
        this.northwest = northwest;
    }

    public Corner getSoutheast() {
        return southeast;
    }

    public void setSoutheast(Corner southeast) {
        this.southeast = southeast;
    }
}
