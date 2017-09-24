package com.netshell.libraries.utilities.strategy;

/**
 * Created by Abhishek
 * on 5/22/2016.
 */
public interface Strategy {

    /**
     * It is recommended to override this method as empty name can cause issues.
     *
     * @return name of the validation strategy
     */
    String getName();
}
