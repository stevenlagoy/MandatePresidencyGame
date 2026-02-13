package com.stevenlagoy.presidency.demographics;

import java.util.ArrayList;
import java.util.List;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.Jsonic;
import com.stevenlagoy.presidency.data.Repr;
import com.stevenlagoy.presidency.demographics.DemographicsManager.DemographicCategory;
import com.stevenlagoy.presidency.util.Logger;

public class DemographicsJava implements Repr<DemographicsJava>, Jsonic<DemographicsJava> {

    private BlocJava generation;
    private BlocJava religion;
    private BlocJava raceEthnicity;
    private BlocJava presentation;

    public DemographicsJava() {
        this.generation = null;
        this.religion = null;
        this.raceEthnicity = null;
        this.presentation = null;
    }

    public DemographicsJava(DemographicsJava other) {
        this.generation = other.getGeneration();
        this.religion = other.getReligion();
        this.raceEthnicity = other.getRaceEthnicity();
        this.presentation = other.getPresentation();
    }

    public DemographicsJava(JSONObject demographicsJson) {
        fromJson(demographicsJson);
    }

    public DemographicsJava(DemographicsManager dm, String generation, String religion, String raceEthnicity,
                            String presentation) {
        this.generation = dm.matchBlocName(generation);
        if (!this.generation.getDemographicGroup().equals(DemographicCategory.GENERATION)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc \"%s\" of type %s was assigned to a demographic category of type %s.",
                            generation, this.generation.getDemographicGroup(), "Generation"),
                    new Exception());
            this.generation = null;
        }
        this.religion = dm.matchBlocName(religion);
        if (!this.religion.getDemographicGroup().equals(DemographicCategory.RELIGION)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", religion,
                            this.religion.getDemographicGroup(), "Religion"),
                    new Exception());
            this.religion = null;
        }
        this.raceEthnicity = dm.matchBlocName(raceEthnicity);
        if (!this.raceEthnicity.getDemographicGroup().equals(DemographicCategory.RACE_ETHNICITY)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc %s of type %s was assigned to a demographic category of type %s.",
                            raceEthnicity, this.raceEthnicity.getDemographicGroup(), "Race / Ethnicity"),
                    new Exception());
            this.raceEthnicity = null;
        }
        this.presentation = dm.matchBlocName(presentation);
        if (!this.presentation.getDemographicGroup().equals(DemographicCategory.PRESENTATION)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc %s of type %s was assigned to a demographic category of type %s.",
                            presentation, this.presentation.getDemographicGroup(), "Presentation"),
                    new Exception());
            this.presentation = null;
        }
    }

    public DemographicsJava(BlocJava generation, BlocJava religion, BlocJava raceEthnicity, BlocJava presentation) {
        this.generation = generation;
        if (!this.generation.getDemographicGroup().equals(DemographicCategory.GENERATION)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc %s of type %s was assigned to a demographic category of type %s.",
                            generation, this.generation.getDemographicGroup(), "Generation"),
                    new Exception());
            this.generation = null;
        }
        this.religion = religion;
        if (!this.religion.getDemographicGroup().equals(DemographicCategory.RELIGION)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc %s of type %s was assigned to a demographic category of type %s.", religion,
                            this.religion.getDemographicGroup(), "Religion"),
                    new Exception());
            this.religion = null;
        }
        this.raceEthnicity = raceEthnicity;
        if (!this.raceEthnicity.getDemographicGroup().equals(DemographicCategory.RACE_ETHNICITY)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc %s of type %s was assigned to a demographic category of type %s.",
                            raceEthnicity, this.raceEthnicity.getDemographicGroup(), "Race / Ethnicity"),
                    new Exception());
            this.raceEthnicity = null;
        }
        this.presentation = presentation;
        if (!this.presentation.getDemographicGroup().equals(DemographicCategory.PRESENTATION)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc %s of type %s was assigned to a demographic category of type %s.",
                            presentation, this.presentation.getDemographicGroup(), "Presentation"),
                    new Exception());
            this.presentation = null;
        }
    }

    public DemographicsJava(String buildstring) {
        this.fromRepr(buildstring);
    }

    public BlocJava getGeneration() {
        return this.generation;
    }

    public void setGeneration(BlocJava generation) {
        this.generation = generation;
        if (!this.generation.getDemographicGroup().equals(DemographicCategory.GENERATION)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc \"%s\" of type %s was assigned to a demographic category of type %s.",
                            generation, this.generation.getDemographicGroup(), "Generation"),
                    new Exception());
            this.generation = null;
        }
    }

    public BlocJava getReligion() {
        return this.religion;
    }

    public void setReligion(BlocJava religion) {
        this.religion = religion;
        if (!this.religion.getDemographicGroup().equals(DemographicCategory.RELIGION)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc \"%s\" of type %s was assigned to a demographic category of type %s.",
                            religion, this.religion.getDemographicGroup(), "Religion"),
                    new Exception());
            this.religion = null;
        }
    }

    public BlocJava getRaceEthnicity() {
        return this.raceEthnicity;
    }

    public void setRaceEthnicity(BlocJava raceEthnicity) {
        this.raceEthnicity = raceEthnicity;
        if (!this.raceEthnicity.getDemographicGroup().equals(DemographicCategory.RACE_ETHNICITY)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc \"%s\" of type %s was assigned to a demographic category of type %s.",
                            raceEthnicity, this.raceEthnicity.getDemographicGroup(), "Race / Ethnicity"),
                    new Exception());
            this.raceEthnicity = null;
        }
    }

    public BlocJava getPresentation() {
        return this.presentation;
    }

    public void setPresentation(BlocJava presentation) {
        this.presentation = presentation;
        if (!this.presentation.getDemographicGroup().equals(DemographicCategory.PRESENTATION)) {
            Logger.log("INVALID BLOC GROUP",
                    String.format("The bloc \"%s\" of type %s was assigned to a demographic category of type %s.",
                            presentation, this.presentation.getDemographicGroup(), "Presentation"),
                    new Exception());
            this.presentation = null;
        }
    }

    public BlocJava[] toBlocsArray() {
        return new BlocJava[] { generation, religion, raceEthnicity, presentation };
    }

    public String toRepr() {
        String repr = String.format(
                "%s:[generation=\"%s\";religion=\"%s\";raceEthnicity=\"%s\";presentation=\"%s\";];",
                this.getClass().toString().replace("class ", ""),
                this.generation.getName(),
                this.religion.getName(),
                this.raceEthnicity.getName(),
                this.presentation.getName());
        return repr;
    }

    public DemographicsJava fromRepr(String repr) {
        return this;
    }

    @Override
    public JSONObject toJson() {
        List<JSONObject> fields = new ArrayList<>();
        fields.add(new JSONObject("generation", this.generation.getName()));
        fields.add(new JSONObject("religion", this.religion.getName()));
        fields.add(new JSONObject("race_ethnicity", this.raceEthnicity.getName()));
        fields.add(new JSONObject("presentation", this.presentation.getName()));
        return new JSONObject("demographics", fields);
    }

    @Override
    public DemographicsJava fromJson(JSONObject demographicsJson) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
}
