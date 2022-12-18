package kr.objet.okrproject.domain.project;

import kr.objet.okrproject.common.entity.BaseEntity;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.common.utils.TokenGenerator;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.project.enums.ProjectType;
import kr.objet.okrproject.domain.team.TeamMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

	@Column(name = "project_objective")
	@NotNull
	@Size(max = 50)
	private String objective;

	private double progress;

	public void updateProgress(double progress) {
		this.progress = progress;
	}

	public void changeProjectTypeToTeam() {
		this.type = ProjectType.TEAM;
	}

	public ProjectMaster addTeamMember(TeamMember teamMember) {
		this.teamMember.add(teamMember);
		return this;
	}

	public void validateProjectDueDate() {
		if (LocalDate.now().isAfter(this.endDate)) {
			throw new OkrApplicationException(ErrorCode.INVALID_PROJECT_END_DATE);
		}
	}


	@Builder
	public ProjectMaster(String name, LocalDate startDate, LocalDate endDate, ProjectType type, String objective,
		double progress) {
		this.projectMasterToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_MASTER_PREFIX);
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = ProjectType.TEAM;
		this.objective = objective;
		this.progress = progress;
	}

}
