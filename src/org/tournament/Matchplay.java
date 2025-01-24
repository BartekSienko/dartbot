package org.tournament;

import java.io.File;
import java.util.*;
import org.match_engine.*;

public class Matchplay extends Tournament {
    
    public Matchplay(String name, int pCount, ArrayList<Deque<DartPlayer>> players,
                      List<MatchLogic> rulesets) {
        super(name, pCount, players, rulesets, new ArrayList<>(Arrays.asList(200, 100, 50, 30, 15)));
    }

}