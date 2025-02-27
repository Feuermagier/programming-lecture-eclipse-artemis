/* Licensed under EPL-2.0 2022-2023. */
package edu.kit.kastel.eclipse.common.api.model;

import java.util.List;

import edu.kit.kastel.eclipse.common.api.util.Pair;

/**
 * {@link IMistakeType}s belong to a {@link IRatingGroup}. Rating Groups may
 * introduce penalty limits for calculation, capping the maximum penalty that
 * all {@link IMistakeType}s belonging to one {@link IRatingGroup} can reach in
 * sum.
 *
 */
public interface IRatingGroup {
	String getIdentifier();

	/**
	 * Get the display name of the rating group.
	 * 
	 * @param languageKey the key of the language that is selected. (e.g., "de").
	 *                    Can be null to use the default language.
	 * @return the human readable name of the rating group.
	 */
	String getDisplayName(String languageKey);

	/**
	 *
	 * @return the MistakeTypes that define this RatingGroup as its rating group.
	 */
	List<IMistakeType> getMistakeTypes();

	/**
	 *
	 * @return the minimum or maximum penalty that all {@link IMistakeType}
	 *         belonging to one {@link IRatingGroup} can reach in sum
	 */
	double setToRange(double points);

	/**
	 * @return [negative_limit, positive_limit]
	 */
	Pair<Double, Double> getRange();

}
