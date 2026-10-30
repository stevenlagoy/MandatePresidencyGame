package com.stevenlagoy.presidency.core;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONSerializable;
import com.stevenlagoy.presidency.util.MatchingException;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
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

    protected EntityFactory<T, ? extends EntityFactory.EntityContext<T>> factory;

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
        clearEntities();
    }

    // Factory

    public @NotNull EntityFactory<T, ? extends EntityFactory.EntityContext<T>> getFactory() {
        return factory;
    }

    protected final void setFactory(@NotNull EntityFactory<T, ? extends EntityFactory.EntityContext<T>> factory) {
        if (this.factory != null) {
            throw new IllegalStateException("EntityFactory has already been set.");
        }
        this.factory = factory;
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

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
        for (Object obj : json.requireArray()) {
            if (obj instanceof JSONObject objJson) {
                register(entityFromJson(objJson));
            }
        }
    }

    /**
     * Construct a new entity from the given JSONObject representation. May delegate to a constructor
     * which accepts a JSONObject.
     * @param json JSONObject representing the entity to be constructed.
     * @return New entity with fields specified by the given JSONObject.
     */
    protected abstract @NotNull T entityFromJson(JSONObject json);

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
    public final void register(@NotNull T entity) {
        entities.add(entity);
        K key = keyOf(entity);
        if (key != null) index.put(key, entity);
    }

    /**
     * Unregister an entity for tracking by this manager. After this, the manager will not be an
     * owner of the entity.
     * @param entity Entity to stop tracking
     */
    protected final void unregister(@NotNull T entity) {
        entities.remove(entity);
        index.remove(keyOf(entity));
    }

    /**
     * Stop tracking all entities.
     */
    protected final void clearEntities() {
        entities.clear();
        index.clear();
    }

    // Iterable

    @Override
    public final @NotNull Iterator<T> iterator() {
        return entities.iterator();
    }

    public final @NotNull Stream<T> stream() {
        return entities.stream();
    }

    // Query

    public final @NotNull Set<T> getAll() {
        requireOperational();
        return entities;
    }

    public final int count() {
        requireOperational();
        return entities.size();
    }

    public final @NotNull Optional<T> matchByKey(@NotNull K key) {
        requireOperational();
        return Optional.ofNullable(index.get(key));
    }

    /**
     * Find a registered entity by comparing its key with a target key.
     * @param key Target key for the desired entity
     * @param keyTransformer Transformer which is applied to the target key and each entity's key for comparison
     * @return Optional of a registered entity with a key matching the target key, after keyTransformer
     *         is applied to each, or emtpy if no such entity is found.
     */
    public @NotNull Optional<T> matchByKey(@NotNull K key, @NotNull Function<K, K> keyTransformer) {
        for (T entity : entities) {
            if (keyTransformer.apply(key).equals(keyTransformer.apply(keyOf(entity)))) {
                return Optional.ofNullable(index.get(key));
            }
        }
        return Optional.empty();
    }

    public @NotNull T requireByKey(@NotNull K key) throws MatchingException {
        return requireByKey(key, () -> "Failed to find a required object with key: " + key);
    }

    public @NotNull T requireByKey(@NotNull K key, @NotNull Supplier<String> failureMessageSupplier) throws MatchingException {
        return Optional.ofNullable(index.get(key)).orElseThrow(() -> new MatchingException(failureMessageSupplier.get(), () -> matchByKey(key)));
    }

    public @NotNull Set<T> matchWhere(@NotNull Predicate<T> predicate) {
        return entities.stream().filter(predicate).collect(Collectors.toSet());
    }

    public final @NotNull Optional<T> matchFirstWhere(@NotNull Predicate<T> predicate) {
        requireOperational();
        return entities.stream().filter(predicate).findFirst();
    }

    public final @NotNull T requireWhere(@NotNull Predicate<T> predicate) throws MatchingException {
        return requireWhere(predicate, () -> getClass().getSimpleName() + " failed to match a required object.");
    }

    public final @NotNull T requireWhere(@NotNull Predicate<T> predicate, @NotNull Supplier<String> failureMessageSupplier) throws MatchingException {
        requireOperational();
        return matchFirstWhere(predicate).orElseThrow(
            () -> new MatchingException(failureMessageSupplier.get(), () -> matchFirstWhere(predicate))
        );
    }

    public final @NotNull Set<T> matchWhereAll(@NotNull Collection<Predicate<T>> predicates) {
        requireOperational();
        return entities.stream().filter(
            predicates.stream().reduce(Predicate::and).orElse(x -> true)
        ).collect(Collectors.toSet());
    }

    public final @NotNull Optional<T> matchFirstWhereAll(@NotNull Collection<Predicate<T>> predicates) {
        requireOperational();
        return entities.stream().filter(
            predicates.stream().reduce(Predicate::and).orElse(x -> true)
        ).findFirst();
    }

    public final @NotNull T requireWhereAll(@NotNull Collection<Predicate<T>> predicates) throws MatchingException {
        return requireWhereAll(predicates, () -> getClass().getSimpleName() + " failed to match a required object.");
    }

    public final @NotNull T requireWhereAll(@NotNull Collection<Predicate<T>> predicates, @NotNull Supplier<String> failureMessageSupplier) throws MatchingException {
        requireOperational();
        return matchFirstWhereAll(predicates).orElseThrow(
            () -> new MatchingException(failureMessageSupplier.get(), () -> matchFirstWhereAll(predicates))
        );
    }

    public final @NotNull Set<T> matchWhereAny(@NotNull Collection<Predicate<T>> predicates) {
        requireOperational();
        return entities.stream().filter(
            predicates.stream().reduce(Predicate::or).orElse(x -> true)
        ).collect(Collectors.toSet());
    }

    public final @NotNull Optional<T> matchFirstWhereAny(@NotNull Collection<Predicate<T>> predicates) {
        requireOperational();
        return entities.stream().filter(
            predicates.stream().reduce(Predicate::or).orElse(x -> true)
        ).findFirst();
    }

    public final @NotNull T requireWhereAny(@NotNull Collection<Predicate<T>> predicates) throws MatchingException {
        return requireWhereAny(predicates, () -> getClass().getSimpleName() + " failed to match a required object.");
    }

    public final @NotNull T requireWhereAny(@NotNull Collection<Predicate<T>> predicates, @NotNull Supplier<String> failureMessageSupplier) throws MatchingException {
        requireOperational();
        return matchFirstWhereAny(predicates).orElseThrow(
            () -> new MatchingException(failureMessageSupplier.get(), () -> matchFirstWhereAny(predicates))
        );
    }

    // Selection

    public final @NotNull T selectWeighted(@NotNull ToDoubleFunction<T> weightFunction) {
        return selectWeighted(entities, weightFunction);
    }

    public final @NotNull T selectWeighted(@NotNull Collection<T> subset, @NotNull ToDoubleFunction<T> weightFunction) {
        requireOperational();
        Map<T, Double> weights = new HashMap<>();
        for (T entity : subset) weights.put(entity, weightFunction.applyAsDouble(entity));
        return Objects.requireNonNull(RandomUtils.weightedSelect(weights));
    }

    public final @NotNull T selectRandom() {
        requireOperational();
        return Objects.requireNonNull(RandomUtils.randSelect(new ArrayList<>(entities)));
    }

    protected @NotNull String entityLabel() {
        return getClass().getSimpleName().replaceFirst("Manager$", "");
    }
}
