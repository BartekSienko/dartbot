package org.tournament;

import java.io.File;
import java.util.*;
import org.match_engine.*;

public class EuroChamps extends Tournament {
    
    public EuroChamps(String name, int pCount, ArrayList<Deque<DartPlayer>> players,
                      List<MatchLogic> rulesets) {
        super(name, pCount, players, rulesets, new ArrayList<>(Arrays.asList(120, 60, 40, 25, 10)));
    }

}