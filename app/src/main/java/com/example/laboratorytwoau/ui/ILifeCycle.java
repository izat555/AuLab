package com.example.laboratorytwoau.ui;

public interface ILifeCycle<T> {
    void bind(T view);
    void unbind();
}
