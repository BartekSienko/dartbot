package org.tournament;


import java.util.*;
import org.match_engine.*;

public class PC extends Tournament {
    
    public PC(String name, int pCount, ArrayList<Deque<DartPlayer>> players,
                      List<MatchLogic> rulesets) {
        super(name, pCount, players, rulesets, new ArrayList<>(Arrays.asList(30, 20, 16, 10, 6, 3, 0)));
    }


}
