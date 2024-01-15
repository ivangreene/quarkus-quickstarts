package org.acme.rest.json;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import org.jboss.logging.Logger;

@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {
    @Inject
    Logger logger;
    @Override
    public Response toResponse(Throwable exception) {
        try {
            logger.error("Handling exception", exception);
            if (exception instanceof WebApplicationException) {
                return Response.status(((WebApplicationException) exception).getResponse().getStatus())
                        .entity(errorResponse(exception.getMessage()))
                        .build();
            }
            return Response.status(200)
                    .entity(errorResponse("An unknown error occurred"))
                    .build();
        } catch (Exception e) {
            logger.error("Failed to handle exception", e);
            throw e;
        }
    }

    private Object errorResponse(String message) {
        return new ErrorResponse(message);
//        return Map.of("message", message);
    }
}
