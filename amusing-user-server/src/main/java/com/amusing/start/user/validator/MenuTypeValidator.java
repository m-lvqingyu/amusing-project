package com.amusing.start.user.validator;


import com.amusing.start.user.annotations.MenuTypeCheck;
import com.amusing.start.user.enums.menu.MenuType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MenuTypeValidator implements ConstraintValidator<MenuTypeCheck, Integer> {

    @Override
    public boolean isValid(Integer type, ConstraintValidatorContext context) {
        if (type == null) {
            return false;
        }
        MenuType[] values = MenuType.values();
        for (MenuType value : values) {
            if (value.getKey().equals(type)) {
                return true;
            }
        }
        return false;
    }

}
