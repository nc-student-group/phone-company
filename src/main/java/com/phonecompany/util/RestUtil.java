package com.phonecompany.util;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class RestUtil {

    public static HttpHeaders getResourceHeaders(String resourceName, Long resourceId) {
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/" + resourceName + "/{id}")
                .buildAndExpand(resourceId)
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriOfNewResource);

        return httpHeaders;
    }
}
