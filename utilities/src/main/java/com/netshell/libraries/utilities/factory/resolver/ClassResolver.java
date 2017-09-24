package com.netshell.libraries.utilities.factory.resolver;

import com.netshell.libraries.utilities.common.CommonMethods;
import com.netshell.libraries.utilities.factory.ClassSubstitutionMap;

import java.util.Map;

/**
 * @author Abhishek
 * Created on 8/23/2015.
 */
public final class ClassResolver implements Resolver<String, String> {

    /**
     * Stores the substitution list of classes
     */
    private final ClassSubstitutionMap classSubstitutionMap = new ClassSubstitutionMap();

    /**
     * Given the input class name, it transitively tries to find the registered substitute.
     * If no substitute is present it returns the class itself.
     *
     * @param input used to compute the return
     * @return the Fully Qualified Class Name of the substituted class.
     */
    @Override
    public String resolve(String input) {

        String output = CommonMethods.checkInput(input, "Input Class Name cannot be null");

        do {
            input = output;
            output = classSubstitutionMap.findSubstitution(input);
        } while (!output.equals(input));

        return output;
    }

    /**
     * @param classMap to initialize substitutionMap from.
     */
    public void initialize(final Map<String, String> classMap) {
        this.classSubstitutionMap.addAll(classMap);
    }

    /**
     * This returns possibility of whether a substitution can be made.
     * It does not guarantee a substitution would be made when {@link  #resolve} is called.
     *
     * @return whether a substitution is possible
     */
    public boolean canResolve() {
        return this.classSubstitutionMap.canSubstitute();
    }
}
