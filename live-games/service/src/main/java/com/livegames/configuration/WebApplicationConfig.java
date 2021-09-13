package com.livegames.configuration;

import com.livegames.model.Room;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebApplicationConfig {

    @Bean(name="RoomsMap")
    @Scope(value= ConfigurableBeanFactory.SCOPE_SINGLETON)
    HashMap<String, Room> getRoomsMap(){
        return new HashMap<>();
    }

}
