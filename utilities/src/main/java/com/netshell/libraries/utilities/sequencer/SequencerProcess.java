package com.netshell.libraries.utilities.sequencer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Abhishek
 * on 12/4/2016.
 */
public final class SequencerProcess<T, R> {

    private final BiFunction<Map<String, ?>, T, R> function;
    private final Map<String, ?> context;
    private SequencerProcess<?, T> before;
    private boolean terminal;

    private SequencerProcess(final Map<String, ?> context,
                             final BiFunction<Map<String, ?>, T, R> function) {
        Objects.requireNonNull(function);
        this.context = context;
        this.function = function;
    }

    private SequencerProcess(final Function<T, R> function) {
        Objects.requireNonNull(function);
        this.context = new HashMap<>();
        this.function = (c, r) -> function.apply(r);
    }

    public static <R> SequencerProcess<Void, R> createProcess(
            final Map<String, ?> context,
            final BiFunction<Map<String, ?>, Void, R> function) {
        return new SequencerProcess<>(context, function);
    }

    public static <R> SequencerProcess<Void, R> createProcess(
            final Function<Void, R> function) {
        return new SequencerProcess<>(function);
    }

    public R execute() {
        return this.function.apply(context, before == null ? null : before.execute());
    }

    public <U> SequencerProcess<R, U> andThan(final Function<R, U> function) {
        Objects.requireNonNull(function);
        return andThan((c, r) -> function.apply(r));
    }

    public <U> SequencerProcess<R, U> andThan(final BiFunction<Map<String, ?>, R, U> function) {
        verifyTerminal();

        final SequencerProcess<R, U> sequencerProcess = new SequencerProcess<>(context, function);
        sequencerProcess.before = this;
        return sequencerProcess;
    }

    private void verifyTerminal() {
        if (terminal) {
            throw new RuntimeException("Attempt to add a process after terminal process");
        }
    }

    public SequencerProcess<R, Void> last(final Consumer<R> consumer) {
        Objects.requireNonNull(consumer);
        return last((c, r) -> consumer.accept(r));
    }

    public SequencerProcess<R, Void> last(final BiConsumer<Map<String, ?>, R> consumer) {
        verifyTerminal();
        final SequencerProcess<R, Void> sequencerProcess = new SequencerProcess<>(context, (c, r) -> {
            consumer.accept(c, r);
            return null;
        });
        sequencerProcess.before = this;
        this.terminal = true;
        return sequencerProcess;
    }
}
