package com.stevenlagoy.presidency.core;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONSerializable;
import com.stevenlagoy.presidency.util.MatchingException;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An EntityManager is responsible for tracking entities of one specific type, including owning,
 * storing, and responding to queries for them. EntityManagers are leaves in the Manager hierarchy
 * tree and may not have submanagers.
 * @param <T> Type of the entities tracked by this EntityManager
 * @param <K> Type of the primary lookup key of the entity type
 *
 * @author Steven LaGoy
 */
public abstract class EntityManager<T, K> extends Manager implements Iterable<T> {

    protected final Set<T> entities = new HashSet<>();
    protected final Map<K, T> index = new HashMap<>();

    protected EntityManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    public final @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    @Override
    protected void doCleanup() {
        entities.clear();
        index.clear();
    }

    // Serialization

    @Override
    protected @NotNull JSONObject doToJson() {
        JSONObject json = new JSONObject();
        for (T entity : entities) {
            if (entity instanceof JSONSerializable<?>) {
                K key = keyOf(entity);
                if (key == null) continue;
                JSONObject serialized = ((JSONSerializable<?>) entity).toJson();
                json.put(key.toString(), serialized);
            }
            else {
                K key = keyOf(entity);
                if (key == null) continue;
                json.put(key.toString(), JSONSerializable.toJson(entity));
            }
        }
        return json;
    }

    // Entity Manager

    /**
     * This entity's primary lookup key. Return {@code null} if it has none (for example if it is
     * a composite-keyed or unkeyed type).
     */
    protected abstract @Nullable K keyOf(@NotNull T entity);

    // Registration

    /**
     * Register an entity for tracking by this manager. After this, the manager will be an owner of
     * the entity.
     * @param entity Entity to track
     */
    public void register(@NotNull T entity) {
        entities.add(entity);
        K key = keyOf(entity);
        if (key != null) index.put(key, entity);
    }

    /**
     * Unregister an entity for tracking by this manager. After this, the manager will not be an
     * owner of the entity.
     * @param entity Entity to stop tracking
     */
    protected void unregister(@NotNull T entity) {
        entities.remove(entity);
        index.remove(keyOf(entity));
    }

    /**
     * Stop tracking all entities.
     */
    protected void clearEntities() {
        entities.clear();
        index.clear();
    }

    // Iterable

    @Override
    public @NotNull Iterator<T> iterator() {
        return entities.iterator();
    }

    public @NotNull Stream<T> stream() {
        return entities.stream();
    }

    // Query

    public @NotNull Set<T> getAll() {
        requireOperational();
        return entities;
    }

    public int count() {
        requireOperational();
        return entities.size();
    }

    public @NotNull Optional<T> matchByKey(@NotNull K key) {
        requireOperational();
        return Optional.ofNullable(index.get(key));
    }

    public @NotNull Set<T> matchWhere(@NotNull Predicate<T> predicate) {
        requireOperational();
        return entities.stream().filter(predicate).collect(Collectors.toSet());
    }

    public @NotNull Optional<T> matchFirstWhere(@NotNull Predicate<T> predicate) {
        requireOperational();
        return entities.stream().filter(predicate).findFirst();
    }

    public @NotNull T requireWhere(@NotNull Predicate<T> predicate, @NotNull Supplier<String> failureMessageSupplier) throws MatchingException {
        return matchFirstWhere(predicate).orElseThrow(
            () -> new MatchingException(failureMessageSupplier.get(), () -> matchFirstWhere(predicate))
        );
    }

    // Selection

    public @NotNull T selectWeighted(@NotNull ToDoubleFunction<T> weightFunction) {
        return selectWeighted(entities, weightFunction);
    }

    public @NotNull T selectWeighted(@NotNull Collection<T> subset, @NotNull ToDoubleFunction<T> weightFunction) {
        requireOperational();
        Map<T, Double> weights = new HashMap<>();
        for (T entity : subset) weights.put(entity, weightFunction.applyAsDouble(entity));
        return Objects.requireNonNull(RandomUtils.weightedSelect(weights));
    }

    public @NotNull T selectRandom() {
        requireOperational();
        return Objects.requireNonNull(RandomUtils.randSelect(new ArrayList<>(entities)));
    }

    protected @NotNull String entityLabel() {
        return getClass().getSimpleName().replaceFirst("Manager$", "");
    }
}
