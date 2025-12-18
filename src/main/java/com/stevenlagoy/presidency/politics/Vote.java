/*
 * Vote.java
 * Steven LaGoy
 * Created: 20 October 2025
 * Modified: 20 October 2025
 */

package com.stevenlagoy.presidency.politics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.stevenlagoy.presidency.characters.Character;
import com.stevenlagoy.presidency.map.MapEntity;

public class Vote {

    private boolean mailInVotingAllowed;
    private boolean earlyVotingAllowed;
    private LocalTime pollsOpenTime;
    private LocalTime pollsCloseTime;
    private LocalDateTime earlyDateTime;
    private LocalDateTime closeDateTime;
    private LocalDateTime mailInCloseDateTime;
    private LocalDateTime overseasCloseDateTime;
    private MapEntity location;
    private Map<Character, Integer> recipients;
    private int turnout;

    public Vote(Collection<Character> recipients, LocalDate date) {
        this.turnout = 0;
        this.recipients = recipients.stream().collect(Collectors.toMap(k -> k, v -> 0, (a, b) -> a, HashMap::new)); // Turn
                                                                                                                    // collection
                                                                                                                    // into
                                                                                                                    // a
                                                                                                                    // map
                                                                                                                    // of
                                                                                                                    // Character
                                                                                                                    // to
                                                                                                                    // 0.
        this.earlyDateTime = LocalDateTime.of(date, LocalTime.of(23, 59));
        this.closeDateTime = LocalDateTime.of(date, LocalTime.of(23, 59));
        this.overseasCloseDateTime = LocalDateTime.of(date, LocalTime.of(23, 59));
        this.mailInVotingAllowed = false;
        this.earlyVotingAllowed = false;
    }

    public boolean isMailInVotingAllowed() {
        return mailInVotingAllowed;
    }

    public void setMailInVotingAllowed(boolean allowed) {
        this.mailInVotingAllowed = allowed;
    }

    public LocalTime getPollsOpenTime() {
        return pollsOpenTime;
    }

    public void setPollsOpenTime(LocalTime time) {
        this.pollsOpenTime = time;
    }

    public LocalTime getPollsCloseTime() {
        return pollsCloseTime;
    }

    public void setPollsCloseTime(LocalTime time) {
        this.pollsCloseTime = time;
    }

    public boolean isEarlyVotingAllowed() {
        return earlyVotingAllowed;
    }

    public void setEarlyVotingAllowed(boolean allowed) {
        this.earlyVotingAllowed = allowed;
    }

    public LocalDateTime getDate() {
        return closeDateTime;
    }

    public void setDate(LocalDate date) {
        this.earlyDateTime = LocalDateTime.of(date, LocalTime.of(23, 59));
        this.closeDateTime = LocalDateTime.of(date, LocalTime.of(23, 59));
        this.overseasCloseDateTime = LocalDateTime.of(date, LocalTime.of(23, 59));
    }

    public LocalDateTime getEarlyDateTime() {
        return earlyDateTime;
    }

    public void setEarlyDateTime(LocalDateTime dateTime) {
        this.earlyDateTime = dateTime;
    }

    public LocalDateTime getCloseDateTime() {
        return closeDateTime;
    }

    public void setCloseDateTime(LocalDateTime dateTime) {
        this.closeDateTime = dateTime;
    }

    public LocalDateTime getMailInCloseDateTime() {
        return mailInCloseDateTime;
    }

    public void setMailInCloseDateTime(LocalDateTime dateTime) {
        this.mailInCloseDateTime = dateTime;
    }

    public LocalDateTime getOverseasCloseDateTime() {
        return overseasCloseDateTime;
    }

    public void setOverseasCloseDateTime(LocalDateTime dateTime) {
        this.overseasCloseDateTime = dateTime;
    }

    public MapEntity getLocation() {
        return location;
    }

    public void setLocation(MapEntity location) {
        this.location = location;
    }

    public int getTurnout() {
        turnout = 0;
        for (int value : recipients.values()) {
            turnout += value;
        }
        return turnout;
    }

    public Map<Character, Integer> getResults() {
        return recipients;
    }

    public Set<Character> getRecipients() {
        return recipients.keySet();
    }

    public int addRecipient(Character recipient) {
        return recipients.put(recipient, 0);
    }

    /**
     * Removes a recipient (without any votes) from this vote.
     * 
     * @param toRemove Character to remove as a possible recipient
     * @return The former number of votes for this recipient (always 0)
     * @throws Exception If the recipient has counted votes. In this case, use
     *                   {@link #removeRecipient(Character, Character)}
     * @see #removeRecipient(Character, Character)
     */
    public int removeRecipient(Character toRemove) throws Exception {
        if (recipients.get(toRemove) != 0) {
            throw new Exception("Removing vote recipient " + toRemove.getName().getCommonName()
                    + " will result in vote wasting. Call with removeRecipient(Character, Character) instead.");
        }
        return recipients.remove(toRemove);
    }

    /**
     * Remove a recipient and transfer votes to another endorsed recipient. Call
     * will {@code null} to waste the votes.
     * 
     * @param toRemove  Character to be removed from the vote.
     * @param toEndorse Character to transfer the votes to from the removed
     *                  Character.
     * @return Total votes of the removed Character
     */
    public int removeRecipient(Character toRemove, Character toEndorse) {
        if (toEndorse == null) {
            return recipients.remove(toRemove);
        } else {
            int removedVotes = recipients.remove(toRemove);
            recipients.put(toEndorse, recipients.get(toEndorse) + removedVotes);
            return removedVotes;
        }
    }

    public boolean participated(Character participant) {
        return recipients.get(participant) != null;
    }

    public int getVotesFor(Character recipient) {
        return recipients.getOrDefault(recipient, 0);
    }

}
