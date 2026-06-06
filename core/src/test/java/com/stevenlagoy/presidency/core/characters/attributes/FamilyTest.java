package com.stevenlagoy.presidency.core.characters.attributes;

import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class FamilyTest {

    static final Engine ENGINE = new Engine();

    @BeforeEach
    public void initFamilyManager() {
        if (ENGINE.CHARACTER_MANAGER.FAMILY_MANAGER.getState() != Manager.ManagerState.ACTIVE) {
            ENGINE.CHARACTER_MANAGER.FAMILY_MANAGER.init();
            if (ENGINE.CHARACTER_MANAGER.FAMILY_MANAGER.getState() != Manager.ManagerState.ACTIVE) {
                fail("Failed to initialize FamilyManager, necessary for testing");
            }
        }
    }

    @AfterEach
    public void resetFamilyManager() {
        ENGINE.CHARACTER_MANAGER.FAMILY_MANAGER.cleanup();
    }

    @Test
    public void testChooseFamilySize() {
        Map<Integer, Integer> returns = new HashMap<>();
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            int _return = ENGINE.CHARACTER_MANAGER.FAMILY_MANAGER.chooseFamilySize();
            returns.merge(_return, 1, Integer::sum);
        }
        System.out.println(returns);
        returns.forEach((familySize, occurrences) -> {
            System.out.printf("%s\t - %s\t(%.3f%%)\t", familySize, occurrences, (float) occurrences / iterations * 100);
            for (int i = 0; i < occurrences; i += iterations / 100) System.out.print("#");
            System.out.println();
        });
        assert(returns.keySet().stream().max(Comparator.naturalOrder()).isPresent());
        int maxKey = returns.keySet().stream().max(Comparator.naturalOrder()).get();
        assert(maxKey <= 36);
    }

}
