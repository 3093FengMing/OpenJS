package test;

import me.fengming.openjs.binding.base.Binding;
import me.fengming.openjs.event.js.EventBusJS;
import me.fengming.openjs.event.js.EventGroupJS;
import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.*;

import java.util.function.IntSupplier;

/**
 * @author ZZZank
 */
public class EventBusJSTest {
    private static final EventGroupJS EVENT_GROUP = new EventGroupJS("TestEvents");
    private static final EventBusJS.Cancellable<IntSupplier> EVENT_BUS =
        EVENT_GROUP.addBus("supply", CancellableEventBus.create(IntSupplier.class));

    private static final Binding BINDING = EVENT_GROUP.asBinding();

    private static final IntSupplier EVENT = () -> 42;

    @Test
    public void test() {
        try (var cx = ContextFactory.getGlobal().enterContext()) {
            var scope = cx.initSafeStandardObjects();
            ScriptableObject.putProperty(scope, BINDING.name(), BINDING.value());

            Assertions.assertFalse(EVENT_BUS.bus().post(EVENT));

            cx.evaluateString(
                scope, """
                    TestEvents.supply((e) => {
                        return true; // cancel the event
                    });""", "EventBusJSTest.js", 1, null
            );

            Assertions.assertTrue(EVENT_BUS.bus().post(EVENT));
        }
    }
}
