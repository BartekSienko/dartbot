package org.tournament;

import java.util.*;
import org.match_engine.*;


public class WorldChamps extends Tournament{
    
    public WorldChamps(String name, int pCount, ArrayList<Deque<DartPlayer>> players,
                      List<MatchLogic> rulesets) {
        super(name, pCount, players, rulesets, new ArrayList<>(Arrays.asList(500, 200, 100, 50, 35, 25, 15)));
    }

}
