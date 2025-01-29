package org.tournament;


import java.util.*;
import org.match_engine.*;

public class GrandPrix extends Tournament {
    
    public GrandPrix(String name, int pCount, ArrayList<Deque<DartPlayer>> players,
                      List<MatchLogic> rulesets) {
        super(name, pCount, players, rulesets, new ArrayList<>(Arrays.asList(120, 60, 40, 25, 10)));
    }


}