package org.seongjki.sism.common;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class HttpException extends HttpStatusCodeException {

    public HttpException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

}
