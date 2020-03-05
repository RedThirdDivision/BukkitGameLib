package de.jeter.bukkitgamelib.command;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

public class MethodContainer {

    private final HashMap<Sender, Method> methods;

    public MethodContainer(HashMap<Sender, Method> map) {
        methods = map;
    }

    protected Method getMethod(Sender s) {
        return methods.get(s);
    }

    protected Collection<Method> getMethods() {
        return methods.values();
    }

    protected HashMap<Sender, Method> getMethodMap() {
        return methods;
    }

    @Override
    public String toString() {
        String ret = "MethodContainer:";
        if (!methods.isEmpty()) {
            for (Sender sender : methods.keySet()) {
                Method method = methods.get(sender);

                ret += " Sender: " + sender.toString() + " Method: " + (method != null ? method.getName() : "null") + " || ";
            }
        } else {
            ret += " no methods registered";
        }
        return ret;
    }

}
