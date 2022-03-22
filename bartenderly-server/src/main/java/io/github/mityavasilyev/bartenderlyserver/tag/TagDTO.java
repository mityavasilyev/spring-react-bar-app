package io.github.mityavasilyev.bartenderlyserver.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {

    Long id;
    String name;

    /**
     * Security feature. Parses model to entity. Prevents from injection and misuse of new/update methods
     *
     * @return parsed entity
     */
    public Tag parseTagWithName() {
        return new Tag(this.name);
    }

    public Tag parseTagWithId() {
        return new Tag(this.id);
    }
}
