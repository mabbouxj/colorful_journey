package net.mabbouxj.colorful_journey.utils;

import java.util.Optional;
import java.util.function.Function;

public class CastingUtils {
    public static <T, U> Function<T, Optional<U>> attemptCast(Class<U> clazz) {
        return input -> {
            if (clazz.isInstance(input)) {
                return Optional.of(clazz.cast(input));
            } else {
                return Optional.empty();
            }
        };
    }
}
