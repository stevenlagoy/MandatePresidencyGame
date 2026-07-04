package com.stevenlagoy.presidency.politics;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.jsonic.JSONSerializable;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ElectionResult implements JSONSerializable<ElectionResult> {

    public LocalDate electionDate;
    public Map<String, Integer> candidateVotes;
    public Map<String, Party> candidateParties;

    public ElectionResult(@NotNull LocalDate electionDate, @NotNull Map<String, Integer> candidateVotes, @NotNull Map<String, Party> candidateParties) {
        this.electionDate = electionDate;
        this.candidateVotes = candidateVotes;
        this.candidateParties = candidateParties;
    }

    public ElectionResult(@NotNull JSONObject json) {
        fromJson(json);
    }

    public @NotNull LocalDate getElectionDate() {
        return electionDate;
    }

    public @NotNull Map<String, Integer> getCandidateVotes() {
        return candidateVotes;
    }

    public int getVotesForCandidate(String candidateName) {
        return candidateVotes.get(candidateName);
    }

    public @NotNull Map<String, Party> getCandidateParties() {
        return candidateParties;
    }

    public @NotNull Party getCandidateParty(String candidateName) {
        return candidateParties.get(candidateName);
    }

    public double getMarginForParty(Party party) {
        double votesForParty = 0.0, votesForOthers = 0.0;
        for (String candidate : candidateVotes.keySet()) {
            int votes = candidateVotes.get(candidate);
            Party candidateParty = candidateParties.get(candidate);
            if (candidateParty.equals(party)) votesForParty += votes;
            else votesForOthers += votes;
        }
        return votesForParty / votesForOthers;
    }

    public @NotNull JSONObject toJson() {
        return new JSONObject(String.valueOf(hashCode()), List.of(
            new JSONObject("electionDate", electionDate.toString()),
            new JSONObject("candidateVotes", candidateVotes.entrySet().stream().map(entry -> new JSONObject(entry.getKey(), entry.getValue()))),
            new JSONObject("candidateParties", candidateParties.entrySet().stream().map(entry -> new JSONObject(entry.getKey(), entry.getValue().getName())))
        ));
    }

    public @NotNull ElectionResult fromJson(@NotNull JSONObject json) {
        this.electionDate = LocalDate.parse(json.requireString("electionDate"));
        // this.candidateVotes = json.requireArray("candidateVotes").stream();
        // TODO
        return this;
    }

}
