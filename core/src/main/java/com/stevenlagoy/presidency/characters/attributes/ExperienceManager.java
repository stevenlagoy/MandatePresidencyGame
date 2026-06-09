package com.stevenlagoy.presidency.characters.attributes;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONProcessor;
import com.stevenlagoy.presidency.characters.attributes.experiences.Experience;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.FilePaths;
import com.stevenlagoy.presidency.util.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ExperienceManager extends Manager {

    // Instance Fields

    Set<Experience> experiences;

    // Constructors

    public ExperienceManager(@NotNull Engine engine, @Nullable Manager superManager) {
        super(engine, superManager);
        experiences = new HashSet<>();
    }

    // Manager Methods

    @Override
    public @NotNull Set<Manager> getSubManagers() {
        return Set.of();
    }

    @Override
    protected void doInit() {
        readExperiences();
    }

    @Override
    protected void doCleanup() {
        experiences.clear();
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName());
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {

    }

    // Instance Methods

    private void readExperiences() {
        JSONObject experiencesJson = JSONProcessor.processJson(FilePaths.EXPERINECES);
        for (Object obj : experiencesJson.getAsList()) {
            if (obj instanceof JSONObject experienceJson) {
//                Logger.log(experienceJson.toString());
                experiences.add(new Experience(ENGINE, experienceJson));
            }
        }
    }

    public @NotNull Optional<Experience> matchExperience(@NotNull String experienceName) {
        Optional<Experience> res;
        res = experiences.stream().filter(experience -> experience.getLabel().equalsIgnoreCase(experienceName)).findFirst();
        if (res.isEmpty()) res = experiences.stream().filter(experience -> experience.getName().replace(" ", "_").equalsIgnoreCase(experienceName.replace(" ", "_"))).findFirst();
        return res;
    }

    public @NotNull Set<Experience> getExperiences() {
        return experiences;
    }

    public @NotNull Set<Experience> getExperiences(@NotNull String track) {
        return experiences.stream().filter(experience -> experience.getTrack().equals(track)).collect(Collectors.toSet());
    }

    public @NotNull Set<Experience> getExperiences(int age) {
        return experiences.stream().filter(experience -> experience.getMinAge() >= age).collect(Collectors.toSet());
    }

    public @NotNull Set<Experience> getExperiences(@NotNull Collection<Experience> priors) {
        return experiences.stream().filter(experience -> experience.prerequisitsMet(priors)).collect(Collectors.toSet());
    }

    public @NotNull Set<Experience> getExperiences(@NotNull Collection<Experience> priors, int age) {
        return getExperiences(priors).stream().filter(experience -> experience.getMinAge() >= age).collect(Collectors.toSet());
    }

}
