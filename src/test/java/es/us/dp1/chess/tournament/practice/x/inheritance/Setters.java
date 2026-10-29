package es.us.dp1.chess.tournament.practice.x.inheritance;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

// Calls setters by name, so that the graders compile before the attributes are moved to the
// superclasses (ReflexiveTest.invokeMethodReflexively only finds methods without parameters)
final class Setters {

    private Setters() {
    }

    static void set(Object target, String property, Object value) {
        String name = "set" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
        for (Method m : target.getClass().getMethods())
            if (m.getName().equals(name) && m.getParameterCount() == 1) {
                try {
                    m.invoke(target, value);
                    return;
                } catch (ReflectiveOperationException | IllegalArgumentException e) {
                    fail("Could not call " + name + " on " + target.getClass().getSimpleName() + ": " + e);
                }
            }
        fail(target.getClass().getSimpleName() + " has no method " + name);
    }

}
