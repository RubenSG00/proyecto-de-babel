package es.us.dp1.chess.tournament.practice.x.customvalidation;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

public class TestX40 {

    Validator v = Validation.buildDefaultValidatorFactory().getValidator();

    static WorkoutPlan plan(Integer minutes) {
        WorkoutPlan p = new WorkoutPlan();
        p.setName("Morning routine");
        p.setMinutes(minutes);
        return p;
    }

    @Test
    public void testX40TheAnnotationIsAConstraint() {
        Constraint c = MultipleOf.class.getAnnotation(Constraint.class);
        assertNotNull(c, "MultipleOf must be annotated with @Constraint");
        assertTrue(Arrays.asList(c.validatedBy()).contains(MultipleOfValidator.class),
                "MultipleOf must be validated by MultipleOfValidator");
        Retention r = MultipleOf.class.getAnnotation(Retention.class);
        assertTrue(r != null && r.value() == RetentionPolicy.RUNTIME, "MultipleOf needs RUNTIME retention");
        Target t = MultipleOf.class.getAnnotation(Target.class);
        assertTrue(t != null && Arrays.asList(t.value()).contains(ElementType.FIELD), "MultipleOf must be applicable to fields");
    }

    @Test
    public void testX40MinutesIsMandatoryAndAMultipleOf5() throws NoSuchFieldException {
        MultipleOf m = WorkoutPlan.class.getDeclaredField("minutes").getAnnotation(MultipleOf.class);
        assertNotNull(m, "minutes must be annotated with @MultipleOf");
        assertEquals(5, m.value());
        assertNotNull(WorkoutPlan.class.getDeclaredField("minutes").getAnnotation(NotNull.class), "minutes is mandatory");
        for (Integer ok : new Integer[] { 0, 5, 30, 45 })
            assertTrue(v.validate(plan(ok)).isEmpty(), ok + " minutes must be valid");
        for (Integer bad : new Integer[] { 1, 12, 44, null })
            assertFalse(v.validate(plan(bad)).isEmpty(), bad + " minutes must be invalid");
    }

    @Test
    public void testX40DefaultMessage() {
        Set<ConstraintViolation<WorkoutPlan>> violations = v.validate(plan(7));
        assertEquals(1, violations.size());
        assertEquals("must be a multiple of 5", violations.iterator().next().getMessage());
    }

    @Test
    public void testX40TheValidatorAcceptsNull() {
        MultipleOfValidator validator = new MultipleOfValidator();
        try {
            validator.initialize(WorkoutPlan.class.getDeclaredField("minutes").getAnnotation(MultipleOf.class));
        } catch (NoSuchFieldException | NullPointerException e) {
            fail("minutes must be annotated with @MultipleOf");
        }
        assertTrue(validator.isValid(null, null), "null must be valid for @MultipleOf: @NotNull decides about nulls");
        assertFalse(validator.isValid(3, null));
    }

}
