package kr.objet.okrproject.domain.guest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import kr.objet.okrproject.domain.user.enums.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guest")
public class Guest {

	@Id
	@Column(name = "guset_uuid")
	private String guestUuid;

	@Column(name = "guest_id", length = 64, unique = true)
	@NotNull
	@Size(max = 64)
	private String guestId;

	@Column(name = "guest_name", length = 100)
	@NotNull
	@Size(max = 100)
	private String guestName;

	@Column(name = "email", length = 512, unique = true)
	@NotNull
	@Size(max = 512)
	private String email;

	@Column(name = "provider_type", length = 20)
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	@Column(name = "profile_image_url", length = 512)
	@Size(max = 512)
	private String profileImageUrl;

	@Builder
	public Guest(
		String uuid,
		@NotNull @Size(max = 64) String guestId,
		@NotNull @Size(max = 100) String guestName,
		@NotNull @Size(max = 512) String email,
		@Size(max = 512) String profileImageUrl,
		ProviderType providerType

	) {
		this.guestUuid = uuid;
		this.guestId = guestId;
		this.guestName = guestName;
		this.email = email != null ? email : "NO_EMAIL";
		this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
		this.providerType = providerType;
	}
}
