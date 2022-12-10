package kr.objet.okrproject.domain.team;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import kr.objet.okrproject.common.entity.BaseEntity;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(ProjectTeamMemberId.class)
@Getter
@Entity
public class ProjectTeamMember extends BaseEntity {
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq")
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private ProjectMaster projectMaster;

	@Column(name = "project_role_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private ProjectRoleType projectRoleType;

	@Column(name = "is_new")
	@NotNull
	private boolean isNew;

	public void setToNotNewProject() {
		this.isNew = false;
	}

	@Builder
	public ProjectTeamMember(User user, ProjectMaster projectMaster, ProjectRoleType projectRoleType, boolean isNew) {
		this.user = user;
		this.projectMaster = projectMaster;
		this.projectRoleType = projectRoleType;
		this.isNew = isNew;
	}

	public boolean isTeamLeader() {
		return this.projectRoleType.equals(ProjectRoleType.LEADER);
	}
}
