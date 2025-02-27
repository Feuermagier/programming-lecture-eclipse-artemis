/* Licensed under EPL-2.0 2022-2023. */
package edu.kit.kastel.eclipse.common.core.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.kastel.eclipse.common.api.artemis.mapping.IExercise;
import edu.kit.kastel.eclipse.common.api.model.IAnnotation;
import edu.kit.kastel.eclipse.common.api.model.IMistakeType;
import edu.kit.kastel.eclipse.common.api.model.IRatingGroup;
import edu.kit.kastel.eclipse.common.core.model.rule.PenaltyRule;
import edu.kit.kastel.eclipse.common.core.model.rule.ThresholdPenaltyRule;

public class MistakeType implements IMistakeType {
	@JsonProperty("shortName")
	private String identifier;

	@JsonProperty("button")
	private String buttonText;
	// {"en" -> "Button Text in English"}
	@JsonProperty("additionalButtonTexts")
	private Map<String, String> additionalButtonTexts;

	@JsonProperty("message")
	private String message;
	// {"en" -> "Message in English"}
	@JsonProperty("additionalMessages")
	private Map<String, String> additionalMessages;

	@JsonProperty("appliesTo")
	private String appliesTo;

	private RatingGroup ratingGroup;
	@JsonProperty("penaltyRule")
	private PenaltyRule penaltyRule;

	@JsonProperty("enabledForExercises")
	private String enabledForExercises;
	@JsonProperty("enabledPenaltyForExercises")
	private String enabledPenaltyForExercises;

	private transient IExercise currentExercise = null;

	@Override
	public double calculate(List<IAnnotation> annotations) {
		assert annotations.stream().allMatch(a -> this.equals(a.getMistakeType()));
		return this.getPenaltyRule().calculate(annotations);
	}

	/**
	 * @return to which rating group this applies.
	 */
	public String getAppliesTo() {
		return this.appliesTo;
	}

	@Override
	public String getMessage(String languageKey) {
		if (languageKey == null || additionalMessages == null || !additionalMessages.containsKey(languageKey)) {
			return this.message;
		}
		return additionalMessages.get(languageKey);
	}

	@Override
	public String getButtonText(String languageKey) {
		if (languageKey == null || additionalButtonTexts == null || !additionalButtonTexts.containsKey(languageKey)) {
			return this.buttonText;
		}
		return additionalButtonTexts.get(languageKey);
	}

	@Override
	public void initialize(IExercise exercise) {
		currentExercise = exercise;
	}

	@Override
	public boolean isEnabledMistakeType() {
		if (enabledForExercises == null || currentExercise == null) {
			return true;
		}
		return this.currentExercise.getShortName().matches(this.enabledForExercises);
	}

	@Override
	public boolean isEnabledPenalty() {
		if (enabledPenaltyForExercises == null || currentExercise == null || penaltyRule.isCustomPenalty()) {
			return true;
		}
		return this.currentExercise.getShortName().matches(this.enabledPenaltyForExercises);
	}

	@Override
	public String getIdentifier() {
		return this.identifier;
	}

	public PenaltyRule getPenaltyRule() {
		if (isEnabledPenalty()) {
			return this.penaltyRule;
		}
		// Create penalty with zero points deduction
		return new ThresholdPenaltyRule(1, 0);
	}

	@Override
	public IRatingGroup getRatingGroup() {
		return this.ratingGroup;
	}

	@Override
	public String getTooltip(String languageKey, List<IAnnotation> annotations) {
		String penaltyText = getPenaltyRule().getTooltip(annotations);
		return getMessage(languageKey) + "\n" + penaltyText;
	}

	/**
	 * Sets a new rating group if there ain't already one. (Used for
	 * deserialization).
	 *
	 * @param ratingGroup the new rating group
	 */
	public void setRatingGroup(RatingGroup ratingGroup) {
		if (this.ratingGroup == null) {
			this.ratingGroup = ratingGroup;
		}
	}

	@Override
	public String toString() {
		return "MistakeType [identifier=" + this.identifier + ", name=" + this.buttonText + ", message=" + this.message + ", ratingGroup=" + this.ratingGroup
				+ ", penaltyRule=" + this.penaltyRule + "]";
	}

	@Override
	public boolean isCustomPenalty() {
		return this.penaltyRule.isCustomPenalty();
	}

	@Override
	public int hashCode() {
		return Objects.hash(appliesTo, penaltyRule, identifier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		MistakeType other = (MistakeType) obj;
		return Objects.equals(appliesTo, other.appliesTo) && Objects.equals(penaltyRule, other.penaltyRule) && Objects.equals(identifier, other.identifier);
	}

}
