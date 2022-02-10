package io.github.mityavasilyev.springreactbarapp.tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAll();
    }

    @GetMapping("{tagId}")
    public Tag getTagById(@PathVariable("tagId") Long id) {
        return tagService.getById(id);
    }

    @GetMapping("/name/{name}")
    public List<Tag> getAllTagsByName(@PathVariable("name") String name) {
        return tagService.getAllByName(name);
    }

    @PostMapping
    public ResponseEntity<Tag> addNewTag(@RequestBody TagDTO tagDTO) {
        Tag tag = tagDTO.parseTagWithName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagService.addNew(tag));
    }

    @DeleteMapping(path = "{tagId}")
    public void deleteTag(@PathVariable("tagId") Long id) {
        tagService.deleteById(id);
    }

    // TODO: 31.01.2022 Update mappings to use ResponseEntity
    @PatchMapping(path = "{tagId}")
    public ResponseEntity<Tag> updateTag(@PathVariable("tagId") Long id,
                                         @RequestBody TagDTO tagDTO) {
        Tag tagPatch = tagDTO.parseTagWithName();
        Tag tag = tagService.getById(id);
        if (tag == null) return ResponseEntity.notFound().build();

        if (tagPatch.getName() != null) tag.setName(tagPatch.getName());
        tag = tagService.updateById(id, tag);

        return ResponseEntity.ok(tag);
    }
}
