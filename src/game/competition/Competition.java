package game.competition;

import game.arena.IArena;
import utilities.ValidationUtils;

import java.util.ArrayList;

/**
 * Created by itzhak on 24-Mar-19.
 */
public abstract class Competition {
    /**
     * Important note:
     * Those fields (and more in this project) are currently final due to them not changing in HW2.
     * If in future tasks you will need to change them you could remove the final modifier and add a setter.
     */
    private IArena arena;
    private final ArrayList<Competitor> activeCompetitors;
    private final ArrayList<Competitor> finishedCompetitors;
    private final int maxCompetitors;

    /**
     * Ctor for an abstract competition
     * @param arena Arena in which the competition takes place in
     * @param maxCompetitors Maximum number of competitor allowed in the competition
     */
    public Competition(IArena arena, int maxCompetitors) {
        this.maxCompetitors = maxCompetitors;
        this.activeCompetitors = new ArrayList<>();
        this.finishedCompetitors = new ArrayList<>();
        this.arena = arena;
    }

    /**
     * Validate if a competitor can compete
     * @param competitor contending competitor
     * @return true if competitor is validated else false
     */
    protected abstract boolean isValidCompetitor(Competitor competitor);

    /**
     * adds a valid competitor to the competition
     * @param competitor competitor to be added
     */
    public void addCompetitor(Competitor competitor){
        ValidationUtils.assertNotNull(competitor);
        if(maxCompetitors <= activeCompetitors.size()){
            throw new IllegalStateException("WinterArena is full max = "+ maxCompetitors);
        }
        if(isValidCompetitor(competitor)){
            competitor.initRace();
            activeCompetitors.add(competitor);
        }
        else{
            throw new IllegalArgumentException("Invalid competitor "+ competitor);
        }
    }

    /**
     * Play a single turn of the game
     */
    public void playTurn(){
        ArrayList<Competitor> tmp = new ArrayList<>(activeCompetitors);
        for(Competitor competitor: tmp){
            if(!arena.isFinished(competitor)){
                competitor.move(arena.getFriction());
                if(arena.isFinished(competitor)){
                    finishedCompetitors.add(competitor);
                    activeCompetitors.remove(competitor);
                }
            }
        }
    }

    /**
     * check if competition has active competitors
     * @return true if there are active competitors else false.
     */
    public boolean hasActiveCompetitors(){
        return activeCompetitors.size() > 0;
    }

    /**
     * Get competitors who have finished (used later so we could print them)
     * @return all finished competitors.
     */
    public ArrayList<Competitor> getFinishedCompetitors() {
        return new ArrayList<>(finishedCompetitors);
    }
}
