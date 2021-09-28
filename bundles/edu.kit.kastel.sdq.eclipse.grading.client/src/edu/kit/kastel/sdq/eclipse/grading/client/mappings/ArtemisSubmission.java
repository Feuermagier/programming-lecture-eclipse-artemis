package edu.kit.kastel.sdq.eclipse.grading.client.mappings;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.kastel.sdq.eclipse.grading.api.artemis.mapping.ISubmission;

public class ArtemisSubmission implements ISubmission, Serializable {
    private static final long serialVersionUID = 4084879944629151733L;

    @JsonProperty(value = "id")
    private int submissionId;
    @JsonProperty
    private String commitHash;

    // for constructing hasSubmittedAssessment and hasSavedAssessment
    @JsonProperty
    private ResultsDTO[] results;

    // for getting participantIdentifier, participantName, repositoryUrl
    @JsonProperty(value = "participation", required = true)
    private ParticipationDummy participation;

    private transient Boolean hasSubmittedAssessment;
    private transient Boolean hasSavedAssessment;

    /**
     * For Auto-Deserialization Need to call this::init thereafter!
     */
    public ArtemisSubmission() {
    }

    protected String getCommitHash() {
        return this.commitHash;
    }

    @Override
    public String getParticipantIdentifier() {
        return this.participation.getParticipantIdentifier();
    }

    @Override
    public String getParticipantName() {
        return this.participation.getParticipantName();
    }

    @Override
    public String getRepositoryUrl() {
        String studentsUrl = this.participation.getRepositoryUrl();
        String studentId = this.participation.getParticipantIdentifier();

        int startIndexOfUID = studentsUrl.indexOf(studentId);
        int endIndexOfUID = studentsUrl.indexOf("@");

        assert startIndexOfUID < endIndexOfUID && startIndexOfUID >= 0 && endIndexOfUID >= 0;

        String newUrl = "";
        newUrl += studentsUrl.substring(0, startIndexOfUID);
        newUrl += studentsUrl.substring(endIndexOfUID + 1);
        return newUrl;
    }

    @Override
    public int getSubmissionId() {
        return this.submissionId;
    }

    @Override
    public boolean hasSavedAssessment() {
        return this.hasSavedAssessment;
    }

    @Override
    public boolean hasSubmittedAssessment() {
        return this.hasSubmittedAssessment;
    }

    public void init() {
        if (this.results.length > 0) {
            ResultsDTO lastResult = this.results[this.results.length - 1];

            this.hasSubmittedAssessment = lastResult.completionDate != null;
            this.hasSavedAssessment = lastResult.hasFeedback != null && lastResult.hasFeedback;
        }
    }

    /**
     *
     * @return a String like {@code toString}, but with fields not contained in ISubmission
     */
    public String toDebugString() {
        return "ArtemisSubmission [submissionId=" + this.submissionId + ", participantIdentifier="
                + this.getParticipantIdentifier() + ", participantName=" + this.getParticipantName()
                + ", repositoryUrl=" + this.getRepositoryUrl() + ", commitHash=" + this.commitHash + "]";
    }

    @Override
    public String toString() {
        return "ArtemisSubmission [submissionId=" + this.submissionId + ", participantIdentifier="
                + this.getParticipantIdentifier() + ", participantName=" + this.getParticipantName()
                + ", repositoryUrl=" + this.getRepositoryUrl() + ", commitHash=" + this.commitHash
                + ", hasSubmittedAssessment=" + this.hasSubmittedAssessment + ", hasSavedAssessment="
                + this.hasSavedAssessment + "]";
    }
}
