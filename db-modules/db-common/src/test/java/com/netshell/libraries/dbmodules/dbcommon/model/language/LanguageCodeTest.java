package com.netshell.libraries.dbmodules.dbcommon.model.language;

import com.netshell.libraries.dbmodules.dbenum.DBEnum;
import com.netshell.libraries.dbmodules.dbenum.datasource.IterableDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Abhishek
 * on 5/22/2016.
 */
public class LanguageCodeTest {

    private static final List<String> langs = Arrays.asList("en-us", "en-uk", "en-in", "hi-in");

    @BeforeAll
    public static void beforeClass() {
        DBEnum.initialize(LanguageCode.class, (i, r) -> new LanguageCode(r), new IterableDataSource<>(langs));
    }

    @Test
    public void TestEnUS() {
        assertEquals("en-us", DBEnum.findById(LanguageCode.class, "en-us").get().getCode());
    }
}