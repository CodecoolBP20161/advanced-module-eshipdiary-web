package com.codecool.eshipdiary.config;

import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.UserOverviewProjection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class SpringDataRestConfig {

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {

        return new RepositoryRestConfigurerAdapter() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
                config.exposeIdsFor(
                        User.class,
                        Ship.class,
                        Oar.class
                )
                        .getProjectionConfiguration()
                        .addProjection(UserOverviewProjection.class);
            }
        };
    }
}
