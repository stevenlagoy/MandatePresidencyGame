package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class IssueManager extends Manager {

    // Instance Fields

    private final @NotNull Set<Issue> issues;

    // Constructors

    public IssueManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        issues = new HashSet<>();
    }

    // Manager Methods

    @Override
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
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
        return new JSONObject(getClass().getSimpleName());
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }

    public @NotNull Set<Issue> getIssues() {
        return issues;
    }

    public @NotNull Optional<Issue> matchIssue(String title) {
        return issues.stream().filter(issue -> issue.getTitle().equals(title)).findFirst();
    }

    public @NotNull Optional<IssuePosition> matchIssuePosition(String title) {
        for (Issue issue : issues) {
            for (IssuePosition position : issue.getPositions()) {
                if (position.getTitle().equals(title)) {
                    return Optional.of(position);
                }
            }
        }
        return Optional.empty();
    }
}
