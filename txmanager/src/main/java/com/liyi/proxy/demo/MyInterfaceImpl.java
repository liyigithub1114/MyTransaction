package com.liyi.proxy.demo;

public class MyInterfaceImpl implements MyInterface {

    @Override
    public void print(String name) {
        System.out.println("Hello: " + name);
    }

}
