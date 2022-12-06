package kr.objet.okrproject.domain.feedback;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import kr.objet.okrproject.common.entity.BaseEntity;
import kr.objet.okrproject.domain.initiative.ProjectInitiative;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Feedback extends BaseEntity {

    @Id
    @Column(name = "feedback_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_initiative_id" , updatable = false)
    private ProjectInitiative projectInitiative;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumns(value = {
            @JoinColumn(name = "user_seq", referencedColumnName = "user_seq", updatable = false),
            @JoinColumn(name = "project_id", referencedColumnName = "project_id", updatable = false)
    }, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ProjectTeamMember projectTeamMember;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "grade_mark")
    private FeedbackType grade;

    @NotNull
    @Column(name = "opinion")
    private String opinion;

    @NotNull
    @Column(name = "checked")
    private boolean isChecked;

    public void checkFeedback() {
        this.isChecked = true;
    }

    @Builder
    public Feedback(ProjectInitiative projectInitiative, ProjectTeamMember projectTeamMember, FeedbackType grade, String opinion) {
        this.projectInitiative = projectInitiative;
        this.projectTeamMember = projectTeamMember;
        this.grade = grade;
        this.opinion = opinion;
        this.isChecked = false;
    }

}


