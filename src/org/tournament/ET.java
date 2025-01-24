package org.tournament;

import java.io.File;
import java.util.*;
import org.match_engine.*;

public class ET extends Tournament {
    
    public ET(String name, int pCount, ArrayList<Deque<DartPlayer>> players,
                      List<MatchLogic> rulesets) {
        super(name, pCount, players, rulesets, new ArrayList<>(Arrays.asList(50, 30, 18, 12, 8, 5)));
    }


}