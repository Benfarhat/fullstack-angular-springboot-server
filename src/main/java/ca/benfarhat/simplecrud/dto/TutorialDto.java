package ca.benfarhat.simplecrud.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * TutorialDto: Dto pour les tutoriels 
 * 
 * @author Benfarhat Elyes
 * @since 2020-12-30
 * @version 1.0.0
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.ALWAYS)
public class TutorialDto {
	
	private long id;
	@NotNull
	@NotBlank(message = "Le titre ne doit pas être vide")
	@JsonProperty("titre")
	private String title;
	private String description;
	@JsonProperty("publie")
	@JsonPropertyDescription("valeur booleenne qui si vrai veut dire publié, sinon false")
	@Default
	private boolean published = false;
	
}
