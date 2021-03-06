package com.netshell.libraries.utilities.services.correlation;

import java.io.Serializable;

public final class CorrelationToken implements Comparable<CorrelationToken>, Serializable {

    private String name;
    private String value;

    public static CorrelationToken from(String name, String value) {
        final CorrelationToken token = new CorrelationToken();
        token.name = name;
        token.value = value;
        return token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(CorrelationToken o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CorrelationToken that = (CorrelationToken) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return value;
    }
}
