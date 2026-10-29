package com.stevenlagoy.presidency.citizens.attributes.names;

import com.stevenlagoy.presidency.citizens.attributes.Family;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Factory;
import com.stevenlagoy.presidency.demographics.Demographics;
import com.stevenlagoy.presidency.util.RandomUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.stevenlagoy.presidency.citizens.attributes.names.NameManager.NameForm.*;

public class PersonalNameFactory extends Factory<PersonalName, PersonalNameFactory.NameContext> {

    // Constants

    /** Percentage of Hispanic people who have a Hispanic-style name. */
    public static final float hispanicHispanicNamePercent = 0.80f;
    /** Percentage of Asian people who have an Eastern-style name. */
    public static final float asianEasternNamePercent = 0.30f;
    /** Percentage of people who use the Dr. honorific. */
    public static final float drHonorificPercent = 0.12f;
    /** Percentage of men who use the Mr. honorific. */
    public static final float manMrHonorificPercent = 0.85f;
    /** Percentage of women who use the Mrs. honorific. */
    public static final float womanMsHonorificPercent = 0.30f;
    /** Percentage of women who are not Ms. who are Mrs. */
    public static final float womanMrsHonorificPercent = 0.80f;
    /** Percentage of non-binary people who use the Mx. honorific. */
    public static final float nonBinaryMxHonorificPercent = 0.10f;
    /** Percentage of non-binary people who use the Mr. honorific. */
    public static final float nonBinaryMrHonorificPercent = 0.20f;
    /** Percentage of non-binary people who use the Ms. honorific. */
    public static final float nonBinaryMsHonorificPercent = 0.20f;
    /** Percentage of people with the MD suffix. */
    public static final float mdSuffixPercent = 0.001f;
    /** Percentage of people with the PhD suffix. */
    public static final float phdSuffixPercent = 0.0075f;
    /** Percentage of people with the Esq suffix. */
    public static final float esqSuffixPercent = 0.0025f;
    /** Percentage of people who commonly use a nickname. */
    public static final float nicknamePercent = 0.33f;
    /** Percentage of people with a nickname whose nickname is based on their middle name. */
    public static final float nicknameFromMiddleNamePercent = 0.10f;
    /** Percentage of people with a nickname based on the abbreviations of their names, like Thomas James -> TJ. */
    public static final float nicknameFromAbbreviationPercent = 0.15f;
    /** Percentage controlling length of nicknames based on abbreviations. This is the chance for each additional letter to be included. */
    public static final float nicknameFromAbbreviationLengthPercent = 0.6f;
    /** Percentage of people who commonly use a nickname which is not based on one of their given names. */
    public static final float nicknameNotFromGivenNamesPercent = 0.05f;

    // Instance Fields

    private final NameManager nameManager;

    PersonalNameFactory(@NotNull Engine engine, @NotNull NameManager nameManager) {
        super(engine);
        this.nameManager = nameManager;
    }

    public @NotNull NameManager getNameManager() {
        return nameManager;
    }

    /** Record class with context values for creating a PersonalName. */
    public record NameContext(Demographics demographics, int age, Family family) {}

    @Override
    public @NotNull PersonalName build(@NotNull NameContext context) {
        return nameManager.buildPersonalName(context);
    }

    public void decorate(@NotNull PersonalName name, @NotNull NameContext context) {
        fillHonorific(name, context);
        fillSuffixes(name, context);
    }

    public @NotNull NameManager.NameForm selectNameForm(@NotNull NameContext context) {
        if (context.demographics.getRaceEthnicity().getAncestorNames().contains("Asian")) {
            if (RandomUtils.randNextPercent() <= asianEasternNamePercent) {
                return EASTERN;
            }
        }
        else if (context.demographics.getRaceEthnicity().getAncestorNames().contains("Hispanic / Latino")) {
            if (context.demographics.getRaceEthnicity().getAncestorNames().contains("Argentinian")) {
                // Only the paternal apellido is inherited in the Argentinian custom
                return WESTERN;
            }
            if (RandomUtils.randNextPercent() <= hispanicHispanicNamePercent) {
                return HISPANIC;
            }
        }
        return WESTERN;
    }

    protected void fillNickname(@NotNull PersonalName name, @NotNull PersonalNameFactory.NameContext context) {
        // Determine whether to have a nickname
        if (!RandomUtils.chance(nicknamePercent)) {
            name.setNickname(null);
            return;
        }
        // Determine if nickname is not based on given names
        if (RandomUtils.chance(nicknameNotFromGivenNamesPercent)) {
            // Choose a given name as nickname
            String nickname = selectGivenName(context);
            // Determine whether to shorten that nickname
            if (nameManager.getNicknames().containsKey(nickname) && RandomUtils.chance(nicknamePercent)) {
                nickname = RandomUtils.randSelect(nameManager.getNicknames().get(nickname));
            }
            name.setNickname(nickname);
        }
        // Determine if nickname comes from middle name
        else if (name instanceof WesternPersonalName && ((WesternPersonalName) name).getMiddleName() != null && RandomUtils.chance(nicknameFromMiddleNamePercent)) {
            // Choose a middle name to nick
            String nickedMiddle;
            do {
                nickedMiddle = RandomUtils.randSelect(((WesternPersonalName) name).getMiddleName().split("\\s+"));
            } while (!nameManager.getNicknames().containsKey(nickedMiddle));
            String nickname = RandomUtils.randSelect(nameManager.getNicknames().get(nickedMiddle));
            name.setNickname(nickname);
        }
        // Determine if nickname comes from initials
        else if (RandomUtils.chance(nicknameFromAbbreviationPercent)) {
            String initials = name.getInitials();
            int length = RandomUtils.probabilisticCount(nicknameFromAbbreviationLengthPercent);
            String nickname = initials.substring(0, length);
            name.setNickname(nickname);
        }
        // Nickname comes from first name
        else {
            // Choose a first name to nick
            String nickedFirst;
            do {
                String givenNames = name instanceof WesternPersonalName ? ((WesternPersonalName) name).getFirstName() : name instanceof HispanicPersonalName ? ((HispanicPersonalName) name).getGivenName() : null;
                if (givenNames == null) return;
                nickedFirst = RandomUtils.randSelect(givenNames.split("\\s+"));
            } while (!nameManager.getNicknames().containsKey(nickedFirst));
            String nickname = RandomUtils.randSelect(nameManager.getNicknames().get(nickedFirst));
            name.setNickname(nickname);
        }
    }

    private void fillHonorific(@NotNull PersonalName name, @NotNull NameContext context) {
        if (RandomUtils.chance(drHonorificPercent)) {
            name.setHonorific("Dr.");
        }
        switch (context.demographics.getPresentation().getName()) {
            case "Man" :
                if (RandomUtils.chance(manMrHonorificPercent)) {
                    name.setHonorific("Mr.");
                }
                break;
            case "Woman" :
                if (RandomUtils.chance(womanMsHonorificPercent)) {
                    name.setHonorific("Ms.");
                }
                else if (RandomUtils.chance(womanMrsHonorificPercent)) {
                    name.setHonorific("Mrs.");
                }
                break;
            case "Non-Binary" :
                if (RandomUtils.chance(nonBinaryMxHonorificPercent)) {
                    name.setHonorific("Mx.");
                }
                else if (RandomUtils.chance(nonBinaryMrHonorificPercent)) {
                    name.setHonorific("Mr.");
                }
                else if (RandomUtils.chance(nonBinaryMsHonorificPercent)) {
                    name.setHonorific("Ms.");
                }
                break;
        }
    }

    private void fillSuffixes(@NotNull PersonalName name, @NotNull NameContext context) {
        if (RandomUtils.chance(esqSuffixPercent)) {
            name.getSuffixes().add("Esq.");
        }
        if (RandomUtils.chance(mdSuffixPercent)) {
            name.getSuffixes().add("MD");
        }
        if (RandomUtils.chance(phdSuffixPercent)) {
            name.getSuffixes().add("PhD");
        }
    }

    // Selection

    protected String selectGivenName(@NotNull PersonalNameFactory.NameContext context) {
        return RandomUtils.weightedSelect(nameManager.getGivenNamesDistribution(context));
    }

    protected @NotNull String selectDistinctGivenNames(@NotNull PersonalNameFactory.NameContext context, int n) {
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String selected;
            do {
                selected = selectGivenName(context);
            } while (nameBuilder.toString().contains(selected));
            nameBuilder.append(selected).append(" ");
        }
        return nameBuilder.toString().strip();
    }

    protected String selectFamilyName(@NotNull PersonalNameFactory.NameContext context) {
        return RandomUtils.weightedSelect(nameManager.getFamilyNamesDistribution(context));
    }

    protected @NotNull String selectDistinctFamilyNames(@NotNull PersonalNameFactory.NameContext context, int n, String... conjoinerPattern) {
        StringBuilder nameBuilder = new StringBuilder();
        Iterator<String> conjoinerStream = Arrays.stream(conjoinerPattern).iterator();
        for (int i = 0; i < n; i++) {
            String selected;
            do {
                selected = selectFamilyName(context);
            } while (nameBuilder.toString().contains(selected));
            nameBuilder.append(selected);
            try {
                nameBuilder.append(conjoinerStream.next());
            } catch (NoSuchElementException e) { // Ran out - fallback with first conjoiner
                nameBuilder.append(conjoinerPattern[0]);
            }
        }
        return nameBuilder.toString().strip();
    }


}
