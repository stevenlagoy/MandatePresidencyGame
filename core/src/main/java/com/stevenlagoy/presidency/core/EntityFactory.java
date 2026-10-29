package com.stevenlagoy.presidency.core;

import com.stevenlagoy.presidency.util.CompletionMethodFor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Entity factories are stateless factory classes which create or select entities of one type. Entity factories
 * are not submanagers because they hold no state. They reference a manager and engine to allow selection
 * of registered entities from {@code T}'s entity manager class.
 * @param <T> Type which is selected and/or created by this type.
 * @param <C> Context object which provides context needed for selection and/or creation of entities.
 */
public abstract class EntityFactory<T, C extends EntityFactory.EntityContext> extends EngineBound {

    protected final EntityManager<T, ?> manager;

    protected EntityFactory(@NotNull Engine engine, @NotNull EntityManager<T, ?> manager) {
        super(engine);
        this.manager = manager;
    }

    public static abstract class EntityContext extends EngineBound {

        public EntityContext(@NotNull Engine engine) {
            super(engine);
        }

        abstract public @NotNull String getImputationFailureConditionsMessage() throws NoSuchFieldException;

        private Map<Field, Function<EntityContext, ?>> completionCache = null;

        /**
         * Reflect a subclass to find methods annotated with {@link CompletionMethodFor} and associate
         * them with the field they complete.
         * @return Mapping of fields to the methods which are defined to complete them in an {@code EntityContext}
         * subclass using the {@link CompletionMethodFor} annotation.
         */
        protected final Map<Field, Function<EntityContext, ?>> fieldCompletionMethods() {
            if (completionCache != null) {
                return completionCache;
            }

            Map<Field, Function<EntityContext, ?>> map = new HashMap<>();
            Class<?> clazz = this.getClass();

            // Loop over methods declared in subclass
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(CompletionMethodFor.class)) {
                    String fieldName = method.getAnnotation(CompletionMethodFor.class).value();
                    try {
                        // Match annotation string to an actual Field object
                        Field field = clazz.getDeclaredField(fieldName);
                        method.setAccessible(true);

                        // Wrap reflection invocation into a Function
                        Function<EntityContext, ?> function = contextInstance -> {
                            try {
                                return method.invoke(contextInstance);
                            }
                            catch (Exception e) {
                                throw new RuntimeException(
                                    "Failed to invoke completion method: " + method.getName(), e
                                );
                            }
                        };
                        map.put(field, function);
                    }
                    catch (NoSuchFieldException e) {
                        throw new IllegalStateException(String.format(
                            "Annotation @CompletionMethodFor(\"%s\") references a field that does not exist in %s",
                            fieldName, clazz.getSimpleName()
                        ), e);
                    }
                }
            }
            this.completionCache = Map.copyOf(map);
            return this.completionCache;
        }

        /**
         * Applies the functions returned by {@link #fieldCompletionMethods()} to any {@code null} fields
         * recursively until all possible fields have been filled.
         * @return {@code frue} if all fields were filled using the present values, or {@code false} if
         * there was at least one field which could not be filled.
         * @throws IllegalAccessException If the fields which are keys in {@link #fieldCompletionMethods()}
         * were not accessible, or not assignable to the type returned by their mapped function value
         * from the same method.
         */
        public final boolean impute() throws IllegalAccessException {
            boolean result = false;
            // Try to complete missing fields
            for (Field field : fieldCompletionMethods().keySet()) {
                if (field.get(this) != null) continue; // No need to fill it
                var completionMethod = fieldCompletionMethods().get(field);
                Object value = completionMethod.apply(this);
                if (value != null) { // We were able to compete a missing field
                    result = true;
                    field.set(this, value);
                }
            }
            // Determine if any fields were filled, and if any are still null
            for (Field field : fieldCompletionMethods().keySet()) {
                if (field.get(this) == null) { // A field is still missing
                    if (result) { // And we might be able to fill it
                        return impute(); // Recurse
                    } else { // And we weren't able to fill any fields
                        return false; // No possible way to fill the remaining empty fields
                    }
                }
            } // There are no missing values
            return true;
        }

        public final void assertAllFieldsPresent() throws AssertionError {
            for (Field field : this.getClass().getDeclaredFields()) {
                try {
                    assert field.get(this) != null;
                }
                catch (IllegalAccessException e) {
                    throw new AssertionError("Could not access a declared field.", e);
                }
            }
        }
    }

    /**
     * Create a new entity from the given context. Must return non-null, and may call the {@link #select(EntityContext)}
     * method to do this.
     * @param context Context from which to create the entity.
     * @return An entity "matching" the context, depending on the entity's type and guarantees, or {@code null}
     * if the context is not valid to create an entity.
     */
    public final @Nullable T create(@NotNull C context) {
        try {
            if (!context.impute()) {
                throw new IllegalStateException("There was not sufficient context to create an entity: " + context.getImputationFailureConditionsMessage());
            }
        }
        catch (IllegalAccessException | IllegalStateException | NoSuchFieldException e) {
            throw new IllegalArgumentException("Something went wrong when creating an entity.", e);
        }
        context.assertAllFieldsPresent();
        return this.doCreation(context);
    }

    /**
     * Create a new entity from the given context. Must return non-null, and may call the {@link #select(EntityContext)}
     * method to do this.
     * @param context Context from which to create the entity. It is guaranteed that all values are non-null
     *                when this method is called.
     * @return An entity "matching" the context, depending on the entity's type and guarantees, or {@code null}
     * if the context is not valid to create an entity.
     */
    protected abstract @Nullable T doCreation(@NotNull C context);

    /**
     * Select an existing entity from the given context. May return {@code null} if the entity manager
     * has no registered entities or if selection is not allowed for this entity type.
     * @param context Context to be used when selecting an entity. Missing ({@code null}) values will be imputed.
     * @return An entity "matching" the context, depending on the entity's type and guarantees, or {@code null}
     * if there are no sepectable "matching" entities.
     * @throws UnsupportedOperationException If selection is not allowed for the type being selected.
     */
    public final @Nullable T select(@NotNull C context) throws UnsupportedOperationException {
        try {
            if (!context.impute()) {
                throw new IllegalStateException("There was not sufficient context to select an entity: " + context.getImputationFailureConditionsMessage());
            }
        }
        catch (IllegalAccessException | IllegalStateException | NoSuchFieldException e) {
            throw new IllegalArgumentException("Something went wrong when creating an entity.", e);
        }
        context.assertAllFieldsPresent();
        return this.doSelection(context);
    }

    /**
     * Select an existing entity from the given context. May return {@code null} if the entity manager
     * has no registered entities or if selection is not allowed for this entity type.
     * @param context Context to be used when selecting an entity. It is guaranteed that all values are non-null
     *                when this method is called.
     * @return An entity "matching" the context, depending on the entity's type and guarantees, or {@code null}
     * if there are no sepectable "matching" entities.
     * @throws UnsupportedOperationException If selection is not allowed for the type being selected.
     */
    protected abstract @Nullable T doSelection(@NotNull C context) throws UnsupportedOperationException;

}
