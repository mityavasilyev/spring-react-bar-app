package io.github.mityavasilyev.springreactbarapp.tag;

import io.github.mityavasilyev.springreactbarapp.exceptions.DataNotFoundException;
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
    public List<Tag> getAllTagsById(@PathVariable("name") String name) {
        return tagService.getAllByName(name);
    }

    @PostMapping
    public void addNewTag(@RequestBody Tag tag) {
        tagService.addNew(tag);
    }

    @DeleteMapping(path = "{tagId}")
    public void deleteTag(@PathVariable("tagId") Long id) {
        tagService.deleteById(id);
    }

    // TODO: 31.01.2022 Update mappings to use ResponseEntity
    @PatchMapping(path = "{tagId}")
    public ResponseEntity<Tag> updateTag(@PathVariable("tagId") Long id,
                                         @RequestBody Tag tagPatch) throws DataNotFoundException {
        Tag tag = tagService.getById(id);
        if (tag == null) return ResponseEntity.notFound().build();

        if (tagPatch.getName() != null) tag.setName(tagPatch.getName());
        tag = tagService.updateById(id, tag);

        return ResponseEntity.ok(tag);
    }
}