package org.fase2.dwf2.util;

import org.fase2.dwf2.controller.*;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Registrar los recursos que manejarán las rutas
        register(UserController.class);
        register(AccountController.class);
        register(TransactionController.class);
        register(RouteController.class);
        register(TestController.class);
        register(LoanController.class);
        register(EmployeeActionController.class);
        register(BranchController.class);


        // Agregar un proveedor de Jackson para la serialización JSON
        register(SpringBeanAutowiringSupport.class);
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
        register(JacksonFeature.class);
        register(JacksonJaxbJsonProvider.class);
        register(OpenApiResource.class);
        register(OpenApiConfig.class);

    }

}
