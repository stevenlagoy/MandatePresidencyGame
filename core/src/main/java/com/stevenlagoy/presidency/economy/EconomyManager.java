package com.stevenlagoy.presidency.economy;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EconomyManager extends Manager {

    // Submanagers

    public final AssetManager ASSET_MANAGER;
    public final BankManager BANK_MANAGER;
    public final CorporationManager CORPORATION_MANAGER;
    public final LiabilityManager LIABILITY_MANAGER;
    public final LoanManager LOAN_MANAGER;
    public final TransactionManager TRANSACTION_MANAGER;

    public EconomyManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        ASSET_MANAGER = new AssetManager(engine, this);
        BANK_MANAGER = new BankManager(engine, this);
        CORPORATION_MANAGER = new CorporationManager(engine, this);
        LIABILITY_MANAGER = new LiabilityManager(engine, this);
        LOAN_MANAGER = new LoanManager(engine, this);
        TRANSACTION_MANAGER = new TransactionManager(engine, this);
    }

    // Manager Methods

    @Override
    public @NotNull List<Manager> getSubManagers() {
        return List.of(ASSET_MANAGER, BANK_MANAGER, CORPORATION_MANAGER, LIABILITY_MANAGER, LOAN_MANAGER, TRANSACTION_MANAGER);
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected void doCleanup() {

    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject();
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }
}
