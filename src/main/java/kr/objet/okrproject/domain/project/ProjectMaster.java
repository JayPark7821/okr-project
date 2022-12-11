package kr.objet.okrproject.domain.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import kr.objet.okrproject.common.entity.BaseEntity;
import kr.objet.okrproject.common.utils.TokenGenerator;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.project.enums.ProjectType;
import kr.objet.okrproject.domain.team.TeamMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProjectMaster extends BaseEntity {

	private static final String PROJECT_MASTER_PREFIX = "mst_";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Long id;

	private String projectMasterToken;

	@OneToMany(mappedBy = "projectMaster")
	private List<TeamMember> teamMember = new ArrayList<>();

	@OneToMany(mappedBy = "projectMaster")
	private List<KeyResult> keyResults = new ArrayList<>();

	@Column(name = "project_name")
	@NotNull
	@Size(max = 50)
	private String name;

	@Column(name = "project_sdt")
	private LocalDate startDate;

	@Column(name = "project_edt")
	private LocalDate endDate;

	@Column(name = "project_type")
	@Enumerated(EnumType.STRING)
	private ProjectType type;

	@Column(name = "project_object")
	@NotNull
	@Size(max = 50)
	private String object;

	private double progress;

	public void updateProgress(double progress) {
		this.progress = progress;
	}

	public void changeProjectTypeToTeam() {
		this.type = ProjectType.TEAM;
	}

	@Builder
	public ProjectMaster(String name, LocalDate startDate, LocalDate endDate, ProjectType type, String object, double progress) {
		this.projectMasterToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_MASTER_PREFIX);
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = ProjectType.TEAM;
		this.object = object;
		this.progress = progress;
	}

}
