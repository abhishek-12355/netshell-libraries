package com.netshell.libraries.dbmodules.dbcommon.model.address;

import com.netshell.libraries.dbmodules.dbenum.initializers.Initializable;
import com.netshell.libraries.utilities.strategy.StrategyProvider;
import com.netshell.libraries.utilities.validation.ValidationException;
import com.netshell.libraries.utilities.validation.ValidationStrategy;

/**
 * Created by Abhishek
 * on 5/23/2016.
 */
public class PinCode implements Initializable<String> {

    private String code;

    @SuppressWarnings("unchecked")
    public PinCode(String code) throws ValidationException {
        this.code = (String) StrategyProvider.find(ValidationStrategy.class, PinCode.class.getName()).validate(code);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getId() {
        return code;
    }
}
