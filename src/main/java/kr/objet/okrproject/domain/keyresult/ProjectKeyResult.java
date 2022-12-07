package kr.objet.okrproject.domain.keyresult;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import kr.objet.okrproject.common.entity.BaseEntity;
import kr.objet.okrproject.common.utils.TokenGenerator;
import kr.objet.okrproject.domain.project.ProjectMaster;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProjectKeyResult extends BaseEntity {

    private static final String PROJECT_KEYRESULT_PREFIX = "key_";
    @Id
    @Column(name = "project_key_result_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectKeyResultToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id" , updatable = false)
    private ProjectMaster projectMaster;

    @Column(name = "key_result_name")
    @NotNull
    @Size(max = 50)
    private String name;

    @Builder
    public ProjectKeyResult(ProjectMaster projectMaster, String name) {
        this.projectKeyResultToken =  TokenGenerator.randomCharacterWithPrefix(PROJECT_KEYRESULT_PREFIX);
        this.projectMaster = projectMaster;
        this.name = name;
    }
}
