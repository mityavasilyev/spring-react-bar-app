package io.github.mityavasilyev.springvertxreactbarapp.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tag is used to group different cocktails on user's demand
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    private Long id;
    private String name;
}
