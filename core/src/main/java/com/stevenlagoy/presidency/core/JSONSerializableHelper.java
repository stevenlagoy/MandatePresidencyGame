package com.stevenlagoy.presidency.core;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONSerializable;
import org.jetbrains.annotations.NotNull;

/**
 * Interface indicating that JSON serialization and deserialization is possible and requires an
 * {@link Engine} parameter to be passed. Use identically to {@link JSONSerializable}, but add an
 * {@link Engine} parameter when calling {@link #toJson(Engine)} or
 * {@link #fromJson(JSONObject, Engine)}. Implementing classes are recommended to extend {@link
 * EngineBound}.
 * <p>
 * This class does not extend {@link JSONSerializable} as its methods are not contravariant in
 * their parameters. Implementations of this class are intended to be interfaces or implementers of
 * {@link JSONSerializable}.
 *
 * @param <T> the implementing class itself; used to type the return value of
 *            {@link #fromJson(JSONObject, Engine)}
 */
public interface JSONSerializableHelper<T extends JSONSerializableHelper<T>> {

    /**
     * Converts this object into a {@link JSONObject} representation. Implementing
     * classes should include all fields that are meaningful for serialization.
     *
     * @param engine an {@link Engine} which will help with serialization,
     *               especially when this object is composed of other
     *               {@link JSONSerializable} objects.
     * @return a {@code JSONObject} representing this object's state
     */
    @NotNull JSONObject toJson(@NotNull Engine engine);

    /**
     * Reconstructs this object's state from the given {@link JSONObject} and
     * returns {@code this}. Fields present in {@code json} should be applied to
     * the object; fields absent in {@code json} may be left at their defaults.
     * <p>
     * The method both mutates {@code this} and returns it, enabling use as a
     * factory-style call on a fresh instance:
     *
     * <pre>{@code
     * MyClass obj = new MyClass().fromJson(json, engine);
     * }</pre>
     *
     * @param json a {@link JSONObject} containing key-value pairs corresponding
     *             to fields of this object; may be partial
     * @param engine an {@link Engine} which will help with deserialization,
     *               especially when this object is composed of other
     *               {@link JSONSerializable} objects.
     * @return this object, after applying fields from {@code json}
     */
    @NotNull T fromJson(@NotNull JSONObject json, @NotNull Engine engine);

}
