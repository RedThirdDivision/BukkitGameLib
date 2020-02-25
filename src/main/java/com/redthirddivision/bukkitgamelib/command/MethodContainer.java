package com.redthirddivision.bukkitgamelib.command;

import com.redthirddivision.bukkitgamelib.command.BaseCommand.Sender;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

public class MethodContainer {

    private final HashMap<Sender, Method> methods;

    public MethodContainer(HashMap<Sender, Method> map) {
        methods = map;
    }

    public Method getMethod(Sender s) {
        return methods.get(s);
    }

    public Collection<Method> getMethods() {
        return methods.values();
    }

    public HashMap<Sender, Method> getMethodMap() {
        return methods;
    }
    
}
