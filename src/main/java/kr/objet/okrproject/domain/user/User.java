package kr.objet.okrproject.domain.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.objet.okrproject.common.entity.BaseTimeEntity;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.enums.RoleType;
import kr.objet.okrproject.domain.user.enums.jobtype.JobFieldDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class User extends BaseTimeEntity implements UserDetails {

	@JsonIgnore
	@Id
	@Column(name = "user_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userSeq;

	@Column(name = "user_id", length = 64, unique = true)
	@NotNull
	@Size(max = 64)
	private String userId;

	@Column(name = "username", length = 100)
	@NotNull
	@Size(max = 100)
	private String username;

	@JsonIgnore
	@Column(name = "password", length = 128)
	@NotNull
	@Size(max = 128)
	private String password;

	@Column(name = "email", length = 512, unique = true)
	@NotNull
	@Size(max = 512)
	private String email;

	@Column(name = "email_verified_yn", length = 1)
	@NotNull
	@Size(min = 1, max = 1)
	private String emailVerifiedYn;

	@Column(name = "profile_image_url", length = 512)
	@Size(max = 512)
	private String profileImageUrl;

	@Column(name = "provider_type", length = 20)
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	@Column(name = "role_type", length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull
	private RoleType roleType;

	@NotNull
	@Column(name = "job", length = 50)
	@Enumerated(EnumType.STRING)
	private JobFieldDetail jobField;

	public User(
		@NotNull @Size(max = 64) String userId,
		@NotNull @Size(max = 100) String username,
		@NotNull @Size(max = 512) String email,
		@NotNull @Size(max = 1) String emailVerifiedYn,
		@Size(max = 512) String profileImageUrl,
		ProviderType providerType,
		@NotNull RoleType roleType,
		@NotNull LocalDateTime createdDate,
		@NotNull LocalDateTime lastModifiedDate,
		@NotNull String password,
		@NotNull JobFieldDetail jobField
	) {

		this.userId = userId;
		this.username = username;
		this.password = password;
		this.email = email != null ? email : "NO_EMAIL";
		this.emailVerifiedYn = emailVerifiedYn;
		this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
		this.providerType = providerType;
		this.roleType = roleType;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.jobField = jobField;
	}

	@Builder
	public User(Long userSeq, String userId, String username, String password, String email, String emailVerifiedYn,
		String profileImageUrl, ProviderType providerType, RoleType roleType, JobFieldDetail jobField) {
		this.userSeq = userSeq;
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.emailVerifiedYn = emailVerifiedYn;
		this.profileImageUrl = profileImageUrl;
		this.providerType = providerType;
		this.roleType = roleType;
		this.jobField = jobField;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.getRoleType().getCode()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
