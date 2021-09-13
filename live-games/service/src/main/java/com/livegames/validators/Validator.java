package com.livegames.validators;

import com.livegames.model.User;

public abstract class Validator {
    public abstract String validate(User user, String roomId);
}
