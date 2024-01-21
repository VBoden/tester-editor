package ua.vboden.tester.entities;

import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String text;

	@OneToOne(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Type type;

	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Answer> answers;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "question_categories", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	@Fetch(FetchMode.SUBSELECT)
	private List<Category> categories;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return text;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		return id == other.id;
	}

}
