package test;

import me.fengming.openjs.event.js.EventBusJS;
import me.fengming.openjs.event.js.EventGroupJS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.*;

import java.util.function.IntSupplier;

/**
 * @author ZZZank
 */
public class EventBusJSTest {
    private static final EventGroupJS EVENT_GROUP = new EventGroupJS("TestEvents");
    private static final EventBusJS<IntSupplier, Void> EVENT_BUS =
        EVENT_GROUP.createBus("supply", IntSupplier.class, true);

    private static final IntSupplier EVENT = () -> 42;

    @Test
    public void test() {
        try (var cx = ContextFactory.getGlobal().enterContext()) {
            var scope = cx.initSafeStandardObjects();
            ScriptableObject.putProperty(scope, "TestEvents", EVENT_GROUP);

            Assertions.assertFalse(EVENT_BUS.bus().post(EVENT));

            cx.evaluateString(
                scope, """
                    TestEvents.supply((e) => {
                        return true; // cancel the event
                    });""", "test.js", 1, null
            );

            Assertions.assertTrue(EVENT_BUS.bus().post(EVENT));
        }
    }
}
