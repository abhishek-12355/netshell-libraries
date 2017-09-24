package com.netshell.libraries.utilities.initializer;

import com.netshell.libraries.utilities.common.CommonUtils;

import java.util.Iterator;

/**
 * Created by Abhishek
 * on 10/16/2016.
 */
public final class InitializerImpl implements Initializer {

    public static void initializeAll() {
        new InitializerImpl().initialize();
    }

    @Override
    public void initialize() {
        final Iterator<Initializable> list = CommonUtils.loadList(Initializable.class);
        boolean loadSuccess = false;
        while (list.hasNext()) {
            loadSuccess = true;
            list.next().initialize();
        }

        if (!loadSuccess) {
            Initializable.initializeDefaults();
        }
    }
}
