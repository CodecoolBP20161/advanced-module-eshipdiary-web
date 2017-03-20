package com.codecool.eshipdiary.config;

import com.codecool.eshipdiary.model.*;
import com.codecool.eshipdiary.repository.OarOverviewProjection;
import com.codecool.eshipdiary.repository.RentalLogOverviewProjection;
import com.codecool.eshipdiary.repository.ShipOverviewProjection;
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
                        Oar.class,
                        ShipSize.class,
                        ShipType.class,
                        RentalLog.class
                )
                        .getProjectionConfiguration()
                        .addProjection(UserOverviewProjection.class)
                        .addProjection(OarOverviewProjection.class)
                        .addProjection(ShipOverviewProjection.class)
                        .addProjection(RentalLogOverviewProjection.class);
            }
        };
    }
}
