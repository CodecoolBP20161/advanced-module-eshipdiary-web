package com.codecool.eshipdiary.model;

import lombok.Data;

/**
 * Created by hamargyuri on 2017. 02. 19..
 */
@Data
public class RemoteLoginResponse {
    private User user;

    public RemoteLoginResponse(User user) {
        this.user = user;
    }

    public RemoteLoginResponse() {
        this.user = null;
    }
}
