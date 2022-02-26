package io.github.mityavasilyev.springreactbarapp.tag;

import io.github.mityavasilyev.springreactbarapp.exceptions.ExceptionController;
import io.github.mityavasilyev.springreactbarapp.exceptions.InvalidIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ROLE_BARTENDER"})
@RequestMapping(path = "api/tags")
public class TagController extends ExceptionController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(tagService.getAll());
    }

    @GetMapping("{tagId}")
    public ResponseEntity<Tag> getTagById(@PathVariable("tagId") Long id) throws InvalidIdException {
        if (id <= 0) throw new InvalidIdException();
        return ResponseEntity.ok(tagService.getById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Tag>> getAllTagsByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(tagService.getAllByName(name));
    }

    @PostMapping
    public ResponseEntity<Tag> addNewTag(@RequestBody TagDTO tagDTO) {
        Tag tag = tagDTO.parseTagWithName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagService.addNew(tag));
    }

    @DeleteMapping(path = "{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable("tagId") Long id) throws InvalidIdException {
        if (id <= 0) throw new InvalidIdException();
        tagService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "{tagId}")
    public ResponseEntity<Tag> updateTag(@PathVariable("tagId") Long id,
                                         @RequestBody TagDTO tagDTO) throws InvalidIdException {
        if (id <= 0) throw new InvalidIdException();
        Tag tagPatch = tagDTO.parseTagWithName();
        Tag tag = tagService.getById(id);
        if (tag == null) return ResponseEntity.notFound().build();

        if (tagPatch.getName() != null) tag.setName(tagPatch.getName());
        tag = tagService.updateById(id, tag);

        return ResponseEntity.ok(tag);
    }
}
