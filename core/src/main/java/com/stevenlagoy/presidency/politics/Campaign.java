package com.stevenlagoy.presidency.politics;
import java.util.ArrayList;
import java.util.List;

import com.stevenlagoy.presidency.characters.CharacterJava;

public class Campaign extends Operation
{
    public static List<Operation> instances = new ArrayList<>();

    public Campaign(CharacterJava campaigner, CharacterJava[] agents)
    {
        super(campaigner, agents);
    }
}
