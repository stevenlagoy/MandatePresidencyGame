package com.stevenlagoy.presidency.characters.attributes.names;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName.DisplayOption;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager.ManagerState;
import com.stevenlagoy.presidency.demographics.Demographics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class PersonalNameTest {

    static final Engine ENGINE = new Engine();
    static List<PersonalName> names;

    public void createNames(int numNames) {
        createNames(numNames, ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER.getCommonDemographics());
    }

    public void createNames(int numNames, Demographics demographics) {
        if (names == null)
            names = new ArrayList<>();
        if (names != null && names.size() == numNames)
            return;
        if (numNames > names.size()) {
            while(names.size() < numNames) names.add(ENGINE.MANAGERS.NAME_MANAGER.buildPersonalName(demographics, 35, null, null));
        }
        else {
            while(names.size() > numNames) names.remove(0);
        }
    }

    @BeforeAll
    public static void initNameManager() {
        if (ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER.getState() != ManagerState.ACTIVE) {
            if (!ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER.init()) {
                fail("Failed to initialize DemographicsManager, necessary for testing.");
            }
        }
        if (ENGINE.MANAGERS.NAME_MANAGER.getState() != ManagerState.ACTIVE) {
            if (!ENGINE.MANAGERS.NAME_MANAGER.init()) {
                fail("Failed to initialize NameManager, necessary for testing.");
            }
        }
    }

    @Test
    public void namesAreGenerated() {
        int numNames = 100;
        createNames(numNames);
        assertEquals(names.size(), numNames);
    }

    @Test
    public void namesHaveGivenAndFamilyName() {
        createNames(1000);
        for (PersonalName name : names) {
            assertNotNull(name.getGivenName());
            assertNotNull(name.getFamilyName());
            assertFalse(name.getGivenName().isBlank());
            assertFalse(name.getFamilyName().isBlank());
            if (!name.getGivenName().matches("(?i)^[-'0-9a-zÀ-ÿ ]+")) {
                System.out.println("Not match");
            }
            assertTrue(name.getGivenName().matches("(?i)^[-'0-9a-zÀ-ÿ ]+"));
            assertTrue(name.getFamilyName().matches("(?i)^[-'0-9a-zÀ-ÿ ]+"));
        }
    }

    @Test
    public void namesCanHaveMiddleNames() {
        createNames(100);
        boolean found = false;
        for (PersonalName name : names) {
            if (name.getMiddleName() != null && !name.getMiddleName().isBlank()) {
                found = true;
                assertTrue(name.getMiddleName().matches("(?i)^[-'0-9a-zÀ-ÿ ]+"));
            }
        }
        assertTrue(found, "At least one name has a middle name");
    }

    @Test
    public void namesCanHaveMultipleFirstNames() {
        createNames(500);
        boolean found = false;
        for (PersonalName name : names) {
            if (name.getGivenName().trim().split("\\s+").length > 1) {
                found = true;
                break;
            }
        }
        assertTrue(found, "At least one name should have multiple first names");
    }

    @Test
    public void namesCanHaveMultipleLastNames() {
        createNames(500);
        boolean found = false;
        for (PersonalName name : names) {
            if (name.getFamilyName().trim().split("\\s+").length > 1) {
                found = true;
                break;
            }
        }
        assertTrue(found, "At least one name should have multiple last names");
    }

    @Test
    public void namesCanHaveNicknames() {
        createNames(200);
        boolean found = false;
        for (PersonalName name : names) {
            if (name.getNickname() != null && !name.getNickname().isBlank()) {
                found = true;
                assertTrue(name.getNickname().matches("[\\p{L}'\\-\\. ]+"));
            }
        }
        assertTrue(found, "At least one name should have a nickname");
    }

    @Test
    public void namesCanHaveHonorifics() {
        createNames(200);
        boolean found = false;
        for (PersonalName name : names) {
            String formal = name.getFormalName();
            if (formal.startsWith("Mr.") || formal.startsWith("Mrs.") || formal.startsWith("Ms.") || formal.startsWith("Dr.")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "At least one name should have an honorific");
    }

    @Test
    public void namesCanHaveOrdinals() {
        createNames(200);
        boolean found = false;
        for (PersonalName name : names) {
            String ord = name.getOrdinal();
            if (ord != null && !ord.isBlank()) {
                assertTrue(ord.matches("(Sr\\.|Jr\\.|I|II|III)"));
                found = true;
            }
        }
        assertTrue(found, "At least one name should have an ordinal");
    }

    @Test
    public void namesCanHaveSuffixes() {
        createNames(2000);
        boolean found = false;
        for (PersonalName name : names) {
            List<String> suffixes = name.getSuffixes();
            if (suffixes != null && !suffixes.isEmpty()) {
                for (String suffix : suffixes) {
                    assertTrue(suffix.matches("(PhD|MD|Esq\\.)"));
                }
                found = true;
            }
        }
        assertTrue(found, "At least one name should have a suffix");
    }

    @Test
    public void namesCanBeAbbreviated() {
        createNames(200);
        boolean found = false;
        for (PersonalName name : names) {
            name.addDisplayOption(PersonalName.DisplayOption.ABBREVIATE_FIRST);
            String abbreviated = name.getCommonName();
            if (abbreviated.matches("[A-Z](\\.| )+.*")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "At least one name should have an abbreviated first name");
    }

    @Test
    public void namesCanPreferMiddleName() {
        createNames(200);
        boolean found = false;
        for (PersonalName name : names) {
            if (name.getMiddleName() != null && !name.getMiddleName().isBlank()) {
                name.addDisplayOption(PersonalName.DisplayOption.PREFER_MIDDLE);
                String preferred = name.getCommonName();
                if (preferred.contains(name.getMiddleName())) {
                    found = true;
                    break;
                }
            }
        }
        assertTrue(found, "At least one name should prefer the middle name");
    }

    @Test
    public void namesDependOnDemographics() {
        String[] generations = {"Silent Generation", "Baby Boomer", "Generation X", "Millennial", "Generation Z", "Generation Alpha", "Generation Beta"};
        String[] religions = {
            "Christian", "Evangelical", "Baptist", "Lutheran", "Black Protestant",
            "Catholic", "Hispanic Catholic", "Mormon", "Irreligious", "Agnostic",
            "Jewish", "Muslim", "Buddhist", "Hindu"
        };
        String[] ethnicities = {
            "White", "Anglo", "Irish", "Australian", "American",
            "Norwegian", "Italian", "Spanish", "Portuguese", "German", "French",
            "Polish", "Ukrainian", "Black", "African American", "Native / Indian",
            "Cherokee", "Navajo", "Inuit", "Cree", "Nahuatl", "Chinese", "Japanese",
            "Korean", "Filipino", "Indonesian", "Indian", "Hawaiian",
            "Hispanic / Latino", "Mexican", "Brazilian", "Salvadoran", "Puerto Rican",
            "Arab", "Egyptian", "Lebanese", "Iranian", "Israeli"
        };
        String[] genders = {"Man", "Woman"};
        for (String gen : generations) {
            for (String rel : religions) {
                for (String eth : ethnicities) {
                    for (String gender : genders) {
                        Demographics demographics = new Demographics(ENGINE.MANAGERS.DEMOGRAPHICS_MANAGER, gen, rel, eth, gender);
                        createNames(5, demographics);
                        for (PersonalName name : names) {
                            assertNotNull(name.getGivenName());
                            assertNotNull(name.getFamilyName());
                        }
                        names.clear();
                    }
                }
            }
        }
    }

    @Test
    public void testNameConstructors() {
        createNames(1);
        PersonalName name1 = names.get(0);
        name1.addSuffix("PhD");
        PersonalName name2 = new PersonalName(name1);
        name2 = new PersonalName("Dwight", "David", "Eisenhower");
        NameForm nf = name2.getNameForm();
        assertEquals(NameForm.defaultForm, nf);
    }

    @Test
    public void testNameEasternForm() {
        PersonalName name = new PersonalName(NameForm.EASTERN, "Bo", "Dian", "Wang");
        String fullname = name.getLegalName();
        assertEquals("Dianbo Wang", fullname);
        name = new PersonalName(NameForm.EASTERN, "Jian", null, "Duanmu");
        fullname = name.getCommonName();
        assertEquals("Duanmu Jian", fullname);
    }

    @Test
    public void testNameHispanicForm() {
        PersonalName name = new PersonalName(NameForm.HISPANIC, "Ramón", null, "Marciano Yáñez");
        name.addDisplayOption(DisplayOption.PATERNAL_FIRST);
        String fullname = name.getFormalName();
        assertEquals("Ramón Yáñez Marciano", fullname);
        assertEquals("Yáñez", name.getPaternalName());
        assertEquals("Marciano", name.getMaternalName());
        name = new PersonalName(NameForm.HISPANIC, "Simón Tomás", "Darío", "Alvarez y Valentín Ramírez");
        name.addDisplayOption(DisplayOption.MATERNAL_FIRST);
        fullname = name.getCommonName();
        assertEquals("Simón Tomás Alvarez y Valentín Ramírez", fullname);
        assertEquals("Alvarez y Valentín", name.getMaternalName());
        assertEquals("Ramírez", name.getPaternalName());
    }

    @Test
    public void testNameNativeAmericanForm() {
        PersonalName name = new PersonalName(NameForm.NATIVE_AMERICAN, "Daniel", "Hiawatha", "Lees");
        String fullname = name.getInformalName();
        assertEquals("Daniel Lees", fullname);
    }

    @Test
    public void testSuffixes() {
        PersonalName name = new PersonalName("James", "Joseph", "Baker");
        name.setSuffix("PhD");
        List<String> suffixes = name.getSuffixes();
        assertTrue(suffixes.contains("PhD") && suffixes.size() == 1);
        boolean removed = name.removeSuffix("PhD");
        suffixes = name.getSuffixes();
        assertTrue(removed && suffixes.size() == 0);
        name.addSuffix("Esq.");
        String fullname = name.getBiographicalName();
        assertEquals("James Joseph Baker, Esq.", fullname);
    }

    @Test
    public void testNameToRepr() {
        PersonalName name = new PersonalName("Julia", "Theresa", "White");
        name.addSuffix("PhD");
        String repr = name.toRepr();
        assertTrue(repr.contains("nameForm=\"WESTERN\";"));
        assertTrue(repr.contains("givenName=\"Julia\";"));
        assertTrue(repr.contains("middleName=\"Theresa\";"));
        assertTrue(repr.contains("familyName=\"White\";"));
        assertTrue(repr.contains("suffixes=[0:\"PhD\";];"));
    }

    @Test
    public void testNameToJson() {
        createNames(500);
        for (PersonalName name : names) {
            JSONObject json = name.toJson();
            String key = json.getKey();
            String expectedKey = String.format("%s (%d)", name.getLegalName(), name.hashCode());
            assertEquals(expectedKey, key);
        }
    }

    @Test
    public void testNameHashCode() {
        PersonalName name1 = new PersonalName("Eli", null, "Johnson");
        PersonalName name2 = new PersonalName("Mary", "Josephine", "Bilodeau");
        int hash1 = name1.hashCode();
        int hash2 = name2.hashCode();
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void testNamesWithNullValues() {
        new PersonalName(null, null, null, null);
        new PersonalName(NameForm.WESTERN, null, null, null);
        new PersonalName(NameForm.EASTERN, null, null, null);
        new PersonalName(NameForm.HISPANIC, null, null, null);
        new PersonalName(NameForm.NATIVE_AMERICAN, null, null, null);
    }

    @Test
    public void testNicknames() {
        PersonalName name = new PersonalName("Tobias", "Edwin", "Jacobs");
        name.setNickname("Toby");
        assertEquals("Toby", name.getNickname());
        String fullname = name.toString();
        assertEquals("Tobias Edwin \"Toby\" Jacobs", fullname);
    }
}
