package me.fengming.openjs.utils.codec;

import com.mojang.serialization.DataResult;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public final class CodecUtils {

    public static <I, O> UnsafeFunction<I, O> unsafeFn(UnsafeFunction<I, O> fn) {
        return Objects.requireNonNull(fn);
    }

    public interface UnsafeFunction<I, O> extends Function<I, DataResult<O>> {
        O applyUnsafe(I o) throws Exception;

        @Override
        default DataResult<O> apply(I i) {
            try {
                return DataResult.success(applyUnsafe(i));
            } catch (Exception e) {
                return DataResult.error(e::toString);
            }
        }
    }
}
