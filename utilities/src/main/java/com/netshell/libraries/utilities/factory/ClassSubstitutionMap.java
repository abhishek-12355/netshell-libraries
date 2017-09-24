package com.netshell.libraries.utilities.factory;

import com.netshell.libraries.utilities.common.CommonMethods;
import com.netshell.libraries.utilities.factory.exception.FactoryException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhishek
 * Created on 8/23/2015.
 */
public final class ClassSubstitutionMap {

    /**
     * Map to store the substitution.
     * HashMap: because large size is not expected.
     */
    private final Map<String, String> classMap = new HashMap<>();

    /**
     * find the substitution or return the input itself.
     *
     * @param input to be substituted
     * @return substituted item.
     */
    public String findSubstitution(final String input) {
        final String output = classMap.get(input);
        return output == null ? input : output;
    }

    /**
     * @param source of substitution
     * @param target of substitution
     */
    public void add(final String source, final String target) {
        this.classMap.put(CommonMethods.checkInput(source), CommonMethods.checkInput(target));
    }

    /**
     * @param classMap to store substitutions
     */
    public void addAll(final Map<String, String> classMap) {
        if (CommonMethods.isEmpty(classMap)) {
            return;
        }

        if (classMap.containsKey(null) || classMap.containsKey("") ||
                classMap.containsValue(null) || classMap.containsValue("")) {
            throw new FactoryException("classMap cannot contain null as Class Substitution");
        }

        for (Map.Entry<String, String> entry : classMap.entrySet()) {
            CommonMethods.checkInput(entry.getKey());
            CommonMethods.checkInput(entry.getValue());
        }

        this.classMap.putAll(classMap);
    }

    /**
     * This returns possibility of whether a substitution can be made.
     * It does not guarantee a substitution would be made when {@link  #findSubstitution} is called.
     *
     * @return whether a substitution is possible
     */
    public boolean canSubstitute() {
        return !this.classMap.isEmpty();
    }
}
