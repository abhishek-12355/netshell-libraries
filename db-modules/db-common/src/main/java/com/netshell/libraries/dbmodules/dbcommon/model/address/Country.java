package com.netshell.libraries.dbmodules.dbcommon.model.address;

import com.netshell.libraries.dbmodules.dbenum.initializers.Initializable;

/**
 * Created by Abhishek
 * on 5/23/2016.
 */
public class Country implements Initializable<Integer> {

    private int id;
    private String name;

    public Country(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Country(String name) {
        this(0, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
