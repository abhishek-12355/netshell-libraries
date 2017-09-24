package com.netshell.libraries.dbmodules.dbcommon.model.person;

import com.netshell.libraries.dbmodules.dbenum.DBEnum;
import com.netshell.libraries.dbmodules.dbenum.datasource.IterableDataSource;
import com.netshell.libraries.dbmodules.dbenum.initializers.Initializable;

import java.util.Arrays;

/**
 * Created by Abhishek
 * on 5/22/2016.
 */
public class Sex implements Initializable<String> {

    private String code;

    public Sex(String code) {
        this.code = code;
    }

    public static void initDefaults() {
        DBEnum.initialize(Sex.class, (i, r) -> new Sex(r), new IterableDataSource<>(Arrays.asList("Male", "Female", "Other")));
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getId() {
        return code;
    }
}
