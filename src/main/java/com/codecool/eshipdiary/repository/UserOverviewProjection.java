package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name="userOverview", types={User.class})
public interface UserOverviewProjection {
    @Value("#{target.firstName} #{target.lastName}")
    String getName();
//    String getAge();
    String getKnowledgeLevel();
    String getIsActive();


    //name, age, knowledge level, status(active/inactive)

}
