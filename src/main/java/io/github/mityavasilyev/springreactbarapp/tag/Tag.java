package io.github.mityavasilyev.springreactbarapp.tag;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Tag is used to group different cocktails on user's demand
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tag")
@Table(name = "tags")
public class Tag {

    @Id
    @SequenceGenerator(
            name = "tag_sequence",
            sequenceName = "tag_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tag_sequence"
    )
    @Column(updatable = false)
    private Long id;
    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
