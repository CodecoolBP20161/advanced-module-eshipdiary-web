package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;
import java.time.Period;
import java.sql.Date;

@Projection(name="userOverview", types={User.class})
public interface UserOverviewProjection {
    String getId();
    @Value("#{target.lastName} #{target.firstName}")
    String getName();
    @Value("#{target.birthDate != null ? T(java.time.Period).between(target.birthDate.toLocalDate(), T(java.time.LocalDate).now()).getYears() : 'nincs adat' }")
    String getAge();
    String getKnowledgeLevel();
    @Value("#{target.active?'Aktív':'Inaktív'}")
    String getIsActive();


    //name, age, knowledge level, status(active/inactive)

}
