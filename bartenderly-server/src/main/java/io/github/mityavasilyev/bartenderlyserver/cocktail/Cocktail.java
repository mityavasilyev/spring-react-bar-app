package io.github.mityavasilyev.bartenderlyserver.cocktail;

import io.github.mityavasilyev.bartenderlyserver.extra.Ingredient;
import io.github.mityavasilyev.bartenderlyserver.extra.Recipe;
import io.github.mityavasilyev.bartenderlyserver.tag.Tag;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Cocktail class that contains Name, Description, Tags,
 * Ingredients, Recipe and Note
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(includeFieldNames = false)
@Entity(name = "cocktail")
@Table(name = "cocktails")
public class Cocktail {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(updatable = false)
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    @ManyToMany
    @JoinTable(
            name = "tags_cocktails",
            joinColumns = @JoinColumn(
                    name = "cocktail_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Tag> tags;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "cocktail_id",
            referencedColumnName = "id"
    )
    private Set<Ingredient> ingredients;

    @OneToOne(
            cascade = CascadeType.ALL
    )
    private Recipe recipe;

    @Column(
            name = "note",
            columnDefinition = "TEXT"
    )
    private String note;

}
