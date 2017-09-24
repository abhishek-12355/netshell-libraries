package com.netshell.libraries.dbmodules.dbcommon.model.person;

import com.netshell.libraries.utilities.strategy.StrategyProvider;
import com.netshell.libraries.utilities.validation.ValidationException;
import com.netshell.libraries.utilities.validation.ValidationStrategy;

/**
 * Created by Abhishek
 * on 5/23/2016.
 */
public class Email {
    private String emailAddress;

    public static Email newInstance(final String emailAddress) throws ValidationException {
        final Email email = new Email();
        email.setEmailAddress(emailAddress);
        return email;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @SuppressWarnings("unchecked")
    public void setEmailAddress(String emailAddress) throws ValidationException {
        this.emailAddress =
                (String) StrategyProvider.find(ValidationStrategy.class, Email.class.getName()).validate(emailAddress);
    }
}
