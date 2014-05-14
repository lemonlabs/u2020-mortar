package co.lemonlabs.mortar.example.core.anim;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME) @Target(TYPE)
public @interface Transition {
    int[] value();
}
