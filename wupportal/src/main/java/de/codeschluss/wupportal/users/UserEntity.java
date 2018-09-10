package de.codeschluss.wupportal.users;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.hateoas.core.Relation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.codeschluss.wupportal.model.BaseEntity;
import de.codeschluss.wupportal.model.Provider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * The persistent class for the users database table.
 * 
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Entity
@Table(name = "users")
@Relation(collectionRelation = "data")
public class UserEntity extends BaseEntity {
	
	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	private static final long serialVersionUID = 1L;

	private String fullname;

	private String password;

	private String phone;

	private boolean superuser;

	private String username;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Provider> providers;
	
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = PASSWORD_ENCODER.encode(password);
	}

}