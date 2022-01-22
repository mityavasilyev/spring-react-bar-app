package io.github.mityavasilyev.springvertxreactbarapp.controllers;

import io.github.mityavasilyev.springvertxreactbarapp.model.Tag;
import io.github.mityavasilyev.springvertxreactbarapp.services.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
