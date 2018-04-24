package com.gd.orh.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Converter;

public abstract class BaseDTO<S, T> {

    public T convertTo() {
        return getConverter().convert((S) this);
    }

    public S convertFor(T t) {
        return getConverter().reverse().convert(t);
    }

    @JsonIgnore
    protected abstract Converter<S, T> getConverter();
}
