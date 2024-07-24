package game.arena;

import game.entities.IMobileEntity;
import game.enums.WeatherCondition;
import game.enums.SnowSurface;
import utilities.ValidationUtils;

/**
 * Created by itzhak on 07-Mar-19.
 */
public class WinterArena implements IArena {
    /**
     * Important note:
     * Those fields (and more in this project) are currently final due to them not changing in HW2.
     * If in future tasks you will need to change them you could remove the final modifier and add a setter.
     */
    private final double length;
    private final SnowSurface surface;
    private final WeatherCondition condition;


    /**
     * Ctor for a generic arena
     * @param length the length of the arena
     * @param surface the snow surface of the arena
     * @param condition the weather condition in the arena
     */
    public WinterArena(double length, SnowSurface surface, WeatherCondition condition) {
        this.length = length;
        this.surface = surface;
        this.condition = condition;
    }

    @Override
    public double getFriction(){
        return surface.getFriction();
    }

    @Override
    public boolean isFinished(IMobileEntity mobileEntity) {
        ValidationUtils.assertNotNull(mobileEntity);
        return mobileEntity.getLocation().getX() >= length;
    }

}
