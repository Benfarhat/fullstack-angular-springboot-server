package ca.benfarhat.simplecrud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Tutorial: Entité pour les tutoriels 
 * 
 * @author Benfarhat Elyes
 * @since 2020-12-28
 * @version 1.0.0
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor 
@Entity
@Table(name = "tutorials")
public class Tutorial {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "published")
	private boolean published;
	
}
