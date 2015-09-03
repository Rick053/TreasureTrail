package com.utsdev.treasuretrail.fields;

import tech.cocoon.Message.Field;

/**
 * Created by Rick on 03-Sep-15.
 */
public class User extends Field {
    public User(int length) {
        super(length);
    }

    @Override
    public String decode() {
        return null;
    }

    @Override
    public boolean encode(Object o) {
        return false;
    }
}
