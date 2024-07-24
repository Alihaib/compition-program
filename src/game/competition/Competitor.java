package game.competition;

import game.arena.IArena;
import game.entities.IMobileEntity;

/**
 * Created by itzhak on 24-Mar-19.
 */
public interface Competitor extends IMobileEntity {
    /**
     * start the race for this competitor
     */
    void initRace();
}
