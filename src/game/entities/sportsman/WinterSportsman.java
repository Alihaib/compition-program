package game.entities.sportsman;

import game.arena.IArena;
import game.competition.Competitor;
import game.enums.Discipline;
import game.enums.Gender;
import game.enums.League;
import utilities.Point;
import utilities.ValidationUtils;

/**
 * Created by itzhak on 24-Mar-19.
 */
public class WinterSportsman extends Sportsman implements Competitor {
    /**
     * Important note:
     * Those fields (and more in this project) are currently final due to them not changing in HW2.
     * If in future tasks you will need to change them you could remove the final modifier and add a setter.
     */
    private final Discipline discipline;

    /**
     * Ctor
     * @param name
     * @param age
     * @param gender
     * @param acceleration
     * @param maxSpeed
     * @param discipline
     */
    public WinterSportsman(String name, double age, Gender gender, double acceleration, double maxSpeed, Discipline discipline) {
        super(name, age, gender, acceleration, maxSpeed);
        ValidationUtils.assertNotNull(discipline);
        this.discipline = discipline;
    }
    @Override
    public void initRace() {
        this.setLocation(new Point(0,this.getLocation().getY()));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getName();
    }

    //region Getters & setters

    /**
     * @return winter sportsman's discipline
     */
    public Discipline getDiscipline() {
        return discipline;
    }

    @Override
    protected double getAcceleration() {
        return super.getAcceleration()+ League.calcAccelerationBonus(this.getAge());
    }
    //endregion
}
