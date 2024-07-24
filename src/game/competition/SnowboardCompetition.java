package game.competition;

import game.arena.WinterArena;
import game.entities.sportsman.Snowboarder;
import game.enums.Discipline;
import game.enums.Gender;
import game.enums.League;

/**
 * Created by itzhak on 25-Mar-19.
 */
public class SnowboardCompetition extends WinterCompetition{
    /**
     * Ctor for the competition
     * @param arena Winter arena in which the competition takes place in
     * @param maxCompetitors max competitors in the competition
     * @param discipline discipline
     * @param league age league
     * @param gender gender
     */
    public SnowboardCompetition(WinterArena arena, int maxCompetitors, Discipline discipline, League league, Gender gender) {
        super(arena, maxCompetitors, discipline, league, gender);
    }
    @Override
    protected boolean isValidCompetitor(Competitor competitor) {
        return competitor instanceof Snowboarder && super.isValidCompetitor(competitor);
    }
}
