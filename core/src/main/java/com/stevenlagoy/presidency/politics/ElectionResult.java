package com.stevenlagoy.presidency.politics;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Map;

public record ElectionResult(@NotNull LocalDate electionDate, @NotNull Map<String, Integer> candidateVotes, @NotNull Map<String, Party> candidateParties) {

    public ElectionResult(LocalDate electionDate, Map<String, Integer> candidateVotes,
            Map<String, Party> candidateParties) {
        this.electionDate = electionDate;
        this.candidateVotes = candidateVotes;
        this.candidateParties = candidateParties;
    }

    public LocalDate getElectionDate() {
        return electionDate;
    }

    public Map<String, Integer> getCandidateVotes() {
        return candidateVotes;
    }

    public int getVotesForCandidate(String candidateName) {
        return candidateVotes.get(candidateName);
    }

    public Map<String, Party> getCandidateParties() {
        return candidateParties;
    }

    public Party getCandidateParty(String candidateName) {
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

}
