package io.github.mityavasilyev.springreactbarapp.tag;

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
    public void addNewTag(@RequestBody TagModel tagModel) {
        tagService.addNew(parseTag(tagModel));
    }

    @DeleteMapping(path = "{tagId}")
    public void deleteTag(@PathVariable("tagId") Long id) {
        tagService.deleteById(id);
    }

    // TODO: 31.01.2022 Update mappings to use ResponseEntity
    @PatchMapping(path = "{tagId}")
    public ResponseEntity<Tag> updateTag(@PathVariable("tagId") Long id,
                                         @RequestBody TagModel tagModel) {
        Tag tagPatch = parseTag(tagModel);
        Tag tag = tagService.getById(id);
        if (tag == null) return ResponseEntity.notFound().build();

        if (tagPatch.getName() != null) tag.setName(tagPatch.getName());
        tag = tagService.updateById(id, tag);

        return ResponseEntity.ok(tag);
    }

    /**
     * Security feature. Parses model to entity. Prevents from injection and misuse of new/update methods
     *
     * @param tagModel model that needs to parsed
     * @return parsed entity
     */
    private Tag parseTag(TagModel tagModel) {
        return new Tag(tagModel.id, tagModel.name);
    }

    class TagModel {
        Long id;
        String name;
    }
}
