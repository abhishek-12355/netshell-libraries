package com.netshell.libraries.dbmodules.dbcommon.model.language;

import com.netshell.libraries.dbmodules.dbenum.initializers.Initializable;

/**
 * Created by Abhishek
 * on 5/22/2016.
 */
public class LanguageCode implements Initializable<String> {

    private String code;

    public LanguageCode(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getId() {
        return code;
    }
}
