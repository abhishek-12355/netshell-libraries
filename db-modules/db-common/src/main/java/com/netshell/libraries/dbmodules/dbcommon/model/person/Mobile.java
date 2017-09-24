package com.netshell.libraries.dbmodules.dbcommon.model.person;

import com.netshell.libraries.utilities.strategy.StrategyProvider;
import com.netshell.libraries.utilities.validation.ValidationException;
import com.netshell.libraries.utilities.validation.ValidationStrategy;

/**
 * Created by Abhishek
 * on 5/22/2016.
 */
public class Mobile {
    private String number;

    public static Mobile newInstance(final String mobileNumber) throws ValidationException {
        final Mobile mobile = new Mobile();
        mobile.setNumber(mobileNumber);
        return mobile;
    }

    public String getNumber() {
        return number;
    }

    @SuppressWarnings("unchecked")
    public void setNumber(String number) throws ValidationException {
        this.number =
                (String) StrategyProvider.find(ValidationStrategy.class, Mobile.class.getName()).validate(number);
    }
}
