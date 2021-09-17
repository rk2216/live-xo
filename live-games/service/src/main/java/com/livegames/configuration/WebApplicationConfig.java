package com.livegames.configuration;

import com.livegames.model.Room;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;

@Configuration
public class WebApplicationConfig {

    @Bean(name="RoomsMap")
    @Scope(value= ConfigurableBeanFactory.SCOPE_SINGLETON)
    HashMap<String, Room> getRoomsMap(){
        return new HashMap<>();
    }

}
