package kr.objet.okrproject.domain.initiative;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import kr.objet.okrproject.common.entity.BaseEntity;
import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProjectInitiative extends BaseEntity {

    @Id
    @Column(name = "project_initiative_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iniId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_key_result_id" , updatable = false)
    private ProjectKeyResult projectKeyResult;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumns(value = {
            @JoinColumn(name = "user_seq", referencedColumnName = "user_seq", updatable = false),
            @JoinColumn(name = "project_id", referencedColumnName = "project_id", updatable = false)
    }, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ProjectTeamMember projectTeamMember;

    @OneToMany(mappedBy = "projectInitiative" )
    private List<Feedback> feedback = new ArrayList<>();

    @Column(name = "initiative_name")
    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Column(name="initiative_edt")
    private LocalDate edt;

    @NotNull
    @Column(name="initiative_sdt")
    private LocalDate sdt;

    @Column(name = "initiative_detail")
    @NotNull
    @Size(max = 200)
    private String detail;

    @Column(name = "initiative_done")
    @NotNull
    private boolean done;

    @Builder
    public ProjectInitiative( ProjectKeyResult projectKeyResult, ProjectTeamMember projectTeamMember, String name, LocalDate edt, LocalDate sdt,String detail, boolean done) {
        this.projectKeyResult = projectKeyResult;
        this.projectTeamMember = projectTeamMember;
        this.name = name;
        this.edt = edt;
        this.sdt = sdt;
        this.detail = detail;
        this.done = done;
    }

    public void markInitiativeAsDone() {
        this.done = true;
    }

    public void updateIniDetail(String detail) {
        this.detail = detail;
    }
}
