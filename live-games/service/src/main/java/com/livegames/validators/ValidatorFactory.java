package com.livegames.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ValidatorFactory {
    public static Map<String, Validator> validatorMap = new HashMap<>();
    @Autowired
    private TicTacToeValidator ticTacToeValidator;

    @PostConstruct
    private void buildValidatorMap(){
        validatorMap.put("TIC_TAC_TOE", ticTacToeValidator);
    }

    public static Validator getValidator(String gameName){
        return validatorMap.get(gameName);
    }
}
