package com.utsdev.treasuretrail.fields;

import tech.cocoon.Message.Field;

public class CurrentLocation extends Field {
    public CurrentLocation(int length) {
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
