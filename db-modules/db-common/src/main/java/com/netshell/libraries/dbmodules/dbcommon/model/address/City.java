package com.netshell.libraries.dbmodules.dbcommon.model.address;

import com.netshell.libraries.dbmodules.dbenum.initializers.Initializable;

/**
 * Created by Abhishek
 * on 5/23/2016.
 */
public class City implements Initializable<Integer> {

    private String name;
    private int id;

    public City(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(String name) {
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
