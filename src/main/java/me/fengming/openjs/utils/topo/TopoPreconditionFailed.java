package me.fengming.openjs.utils.topo;

/**
 * @author ZZZank
 */
public class TopoPreconditionFailed extends RuntimeException {

    public TopoPreconditionFailed(String message) {
        super(message);
    }

    public TopoPreconditionFailed(String format, Object... args) {
        super(String.format(format, args));
    }
}
