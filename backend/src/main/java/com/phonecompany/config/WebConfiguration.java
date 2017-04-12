package com.phonecompany.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class WebConfiguration implements EmbeddedServletContainerCustomizer {

    /**
     * Spring application to be functioning on its own with no regard to Angular2
     * frontend server expects. That's why, any request to the route which cannot
     * be resolved by Spring controller will result in {@code HttpStatus.NOT_FOUND}
     * The following configuration allows Angular2 to take care of the routing
     * process
     *
     * @param configurableEmbeddedServletContainer container.
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
    }
}
