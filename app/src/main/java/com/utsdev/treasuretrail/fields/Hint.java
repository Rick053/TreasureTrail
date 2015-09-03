package com.utsdev.treasuretrail.fields;

import tech.cocoon.Message.Field;

public class Hint extends Field {
    public Hint(int length) {
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
