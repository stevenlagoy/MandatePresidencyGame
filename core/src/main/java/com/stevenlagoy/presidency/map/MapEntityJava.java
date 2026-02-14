/*
 * MapEntity.java
 * Steven LaGoy
 * Created: 10 June 2025 at 10:26 PM
 * Modified: 10 June 2025
 */

package com.stevenlagoy.presidency.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import com.stevenlagoy.presidency.demographics.BlocJava;
import com.stevenlagoy.presidency.demographics.DemographicsManager;

/**
 * Any Entity (Nation, State, Municipality, &c) which appears on the Map is a
 * MapEntity. This is a blank interface to allow for polymorphism.
 */
public interface MapEntityJava {

    /** Get a descriptive identifying name for the MapEntity. */
    public String getName();

    /** Get the total population. */
    public int getPopulation();

    /**
     * Set the total population.
     * <p>
     * Minimum population is zero. If the population is found to be lower, it will
     * be set to zero.
     *
     * @param population New population.
     */
    public void setPopulation(int population);

    /**
     * Add to the total population.
     * <p>
     * Minimum population is zero. If the population is found to be lower, it will
     * be set to zero.
     *
     * @param population Amount of population to be added.
     */
    public void addPopulation(int population);

    /** Get the total land area. */
    public double getLandArea();

    /**
     * Set the total land area.
     * <p>
     * Minimum land area is zero. If the land area is found to be lower, it will be
     * set to zero.
     *
     * @param area New land area
     */
    public void setLandArea(double area);

    public Set<String> getDescriptors();

    public void setDescriptors(DemographicsManager dm,Set<String> descriptors);

    public boolean hasDescriptor(String descriptor);

    public boolean addDescriptor(DemographicsManager dm,String descriptor);

    public boolean addAllDescriptors(DemographicsManager dm,Collection<String> descriptors);

    public boolean removeDescriptor(DemographicsManager dm,String descriptor);

    public boolean removeAllDescriptors(DemographicsManager dm,Collection<String> descriptors);

    public Map<BlocJava, Float> getDemographics();

    /**
     * Get the percentage of a demographic bloc's share within its category.
     *
     * @param bloc The bloc to evaluate.
     * @return Float between 0.0 and 1.0 for the bloc's category share.
     * @see #getDemographicPopulation(BlocJava)
     */
    public float getDemographicPercentage(BlocJava bloc);

    /**
     * Get the population of a demographic bloc's membership.
     *
     * @param bloc The bloc to evaluate.
     * @return int between zero and the population.
     * @see #getPopulation()
     * @see #getDemographicPercentage(BlocJava)
     */
    public int getDemographicPopulation(BlocJava bloc);

    public void evaluateDemographics(DemographicsManager dm);

}
