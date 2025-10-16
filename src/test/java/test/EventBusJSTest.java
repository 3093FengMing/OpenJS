package test;

import me.fengming.openjs.event.js.EventBusJS;
import me.fengming.openjs.event.js.EventGroupJS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.*;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/**
 * @author ZZZank
 */
public class EventBusJSTest {
    private static final EventGroupJS EVENT_GROUP = new EventGroupJS("TestEvents");
    private static final EventBusJS<IntSupplier, Void> CANCELLABLE =
        EVENT_GROUP.addBus("cancellable", IntSupplier.class, true);
    private static final EventBusJS<IntConsumer, Void> REGULAR =
        EVENT_GROUP.addBus("regular", IntConsumer.class);

    @Test
    public void testCancellable() {
        try (var cx = ContextFactory.getGlobal().enterContext()) {
            var scope = cx.initSafeStandardObjects();

            var binding = EVENT_GROUP.asBinding();
            ScriptableObject.putProperty(scope, binding.name(), binding.value());

            Assertions.assertFalse(CANCELLABLE.post(supplyInt(42)));
            Assertions.assertFalse(CANCELLABLE.post(supplyInt(0)));

            cx.evaluateString(
                scope, """
                    TestEvents.cancellable((e) => e.getAsInt() != 0);""", "test.js", 1, null
            );

            Assertions.assertTrue(CANCELLABLE.post(supplyInt(42)));
            Assertions.assertFalse(CANCELLABLE.post(supplyInt(0)));
        }
    }

    private static IntSupplier supplyInt(int value) {
        return () -> value;
    }

    @Test
    public void testRegular() {
        try (var cx = ContextFactory.getGlobal().enterContext()) {
            var scope = cx.initSafeStandardObjects();

            var binding = EVENT_GROUP.asBinding();
            ScriptableObject.putProperty(scope, binding.name(), binding.value());

            Assertions.assertFalse(REGULAR.post(null));
            Assertions.assertFalse(REGULAR.post(ignored -> {}));

            cx.evaluateString(
                scope, """
                    TestEvents.regular(e => e.accept(42));""", "test.js", 1, null
            );

            var holder = new int[1];
            Assertions.assertFalse(REGULAR.post(value -> holder[0] = value));
            Assertions.assertEquals(42, holder[0]);
        }
    }
}
