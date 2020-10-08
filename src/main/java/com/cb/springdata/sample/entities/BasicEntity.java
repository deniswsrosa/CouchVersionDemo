package com.cb.springdata.sample.entities;


import lombok.Getter;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

public class BasicEntity {

    @Getter(PROTECTED)
    @Setter(PROTECTED)
    protected String _class;
}
