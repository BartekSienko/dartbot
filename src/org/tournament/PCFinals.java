package org.tournament;

import java.io.File;
import java.util.*;
import org.match_engine.*;

public class PCFinals extends Tournament {
    
    public PCFinals(String name, int pCount, ArrayList<Deque<DartPlayer>> players,
                      List<MatchLogic> rulesets) {
        super(name, pCount, players, rulesets, new ArrayList<>(Arrays.asList(120, 60, 30, 20, 12, 6, 0)));
    }


}