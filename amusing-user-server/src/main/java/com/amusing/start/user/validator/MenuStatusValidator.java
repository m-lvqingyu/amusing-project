package com.amusing.start.user.validator;

import com.amusing.start.user.annotations.MenuStatusCheck;
import com.amusing.start.user.enums.menu.MenuStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MenuStatusValidator implements ConstraintValidator<MenuStatusCheck, Integer> {

    @Override
    public boolean isValid(Integer status, ConstraintValidatorContext context) {
        if (status == null) {
            return false;
        }
        MenuStatus[] values = MenuStatus.values();
        for (MenuStatus value : values) {
            if (value.getKey().equals(status)) {
                return true;
            }
        }
        return false;
    }

}
