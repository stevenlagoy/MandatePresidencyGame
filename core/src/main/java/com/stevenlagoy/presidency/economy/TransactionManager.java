package com.stevenlagoy.presidency.economy;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.EntityManager;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TransactionManager extends EntityManager<Transaction, Integer> {

    public TransactionManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
    }

    // Manager Methods

    @Override
    protected void doInit() {

    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }

    @Override
    protected @NotNull Integer keyOf(@NotNull Transaction entity) {
        return entity.hashCode();
    }

    // Query

    public @NotNull Set<Transaction> matchByReceiver(@NotNull FinancialEntity receiver) {
        return matchWhere(entity ->
            entity.getReceiver().equals(receiver)
        );
    }

    public @NotNull Set<Transaction> matchBySender(@NotNull FinancialEntity sender) {
        return matchWhere(entity ->
            entity.getSender().equals(sender)
        );
    }

    public @NotNull Set<Transaction> matchBySenderAndReceiver(@NotNull FinancialEntity sender, @NotNull FinancialEntity recipient) {
        return matchWhereAll(Set.of(
            entity -> entity.getSender().equals(sender),
            entity -> entity.getReceiver().equals(recipient)
        ));
    }
}
