package com.realtime.statistics.utils;

public interface TestUtils {

    public static String createURLWithPort(final String uri, final String host,
                                           final String port) {
        return "http://" + host + ":" + port + uri;
    }

}
