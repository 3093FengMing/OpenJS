package me.fengming.openjs.utils.codec;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public final class CodecUtils {

    public static <K, V> Codec<V> biMapTransform(Codec<K> original, BiMap<K, V> transformer) {
        Objects.requireNonNull(original);
        Objects.requireNonNull(transformer);
        return original.flatXmap(
            key -> {
                var got = transformer.get(key);
                return got == null
                    ? DataResult.error(() -> "No value for key: " + key)
                    : DataResult.success(got);
            }, value -> {
                var got = transformer.inverse().get(value);
                return got == null
                    ? DataResult.error(() -> "No key for value: " + value)
                    : DataResult.success(got);
            }
        );
    }

    public static <T extends Enum<T>> Codec<T> forEnum(Class<T> type, boolean ignoreCase) {
        Function<String, DataResult<T>> mapper;
        if (ignoreCase) {
            var map = new HashMap<String, T>();
            for (var enumConstant : type.getEnumConstants()) {
                map.put(enumConstant.name().toLowerCase(Locale.ROOT), enumConstant);
            }

            mapper = (name) -> {
                var got = map.get(name.toLowerCase(Locale.ROOT));
                return got == null
                    ? DataResult.error(() -> "No enum constant " + type.getCanonicalName() + '.' + name)
                    : DataResult.success(got);
            };
        } else {
            mapper = (UnsafeFunction<String, T>) name -> Enum.valueOf(type, name);
        }
        return Codec.STRING.comapFlatMap(mapper, Enum::name);
    }

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
