import org.junit.Test;

import com.stevenlagoy.presidency.characters.attributes.names.Name;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager.ManagerState;
import com.stevenlagoy.presidency.demographics.Bloc;
import com.stevenlagoy.presidency.demographics.Demographics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before; // Add this import for JUnit 4

public final class NameTest {

    public final Engine ENGINE = new Engine();
    public List<Name> names;

    public void createNames(int numNames,Demographics demographics) {
        if (!names.isEmpty())
            return;
        names = new ArrayList<>();
        for (int i = 0; i < numNames; i++) {
            names.add(ENGINE.NameManager().generateName(demographics));
        }
    }

    @Before
    public void initNameManager() {
        if (ENGINE.getState() != ManagerState.ACTIVE && !ENGINE.init())
            fail("Could not initialize Game Engine.");
    }

    @Test
    public void testGenerateNames() {
        int numNames = 100;
        Demographics demographics = new Demographics(ENGINE.DemographicsManager(), "Baby Boomer", "Christian", "Anglo", "Man");

        createNames(numNames,demographics);
        assertSame(names.size(),numNames);
    }

}
