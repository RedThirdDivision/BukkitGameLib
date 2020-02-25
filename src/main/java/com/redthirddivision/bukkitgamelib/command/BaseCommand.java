package com.redthirddivision.bukkitgamelib.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BaseCommand {

    public enum Sender {

        PLAYER,
        CONSOLE;
    }
    
    Sender sender();

    String command();
    
    String permission() default "";
    
    String subCommand() default "";
    
    String aliases() default "";
}


