package org.capps.news.web.controller;

import org.capps.news.model.Tag;
import org.capps.news.service.TagService;
import org.capps.news.service.interfaces.TagServiceInterface;
import org.capps.news.web.vm.mapper.TagVMMapper;
import org.capps.news.web.vm.request.TagReqVM;
import org.capps.news.web.vm.response.TagResVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagServiceInterface tagService;
    private final TagVMMapper tagVMMapper;

    @Autowired
    public TagController(TagService tagService, TagVMMapper tagVMMapper) {
        this.tagService = tagService;
        this.tagVMMapper = tagVMMapper;
    }

    @GetMapping
    public List<TagResVM> getAllTags() {
        return tagService.getAllTags().stream()
                .map(tagVMMapper::tagToTagResVM)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<TagResVM> createTag(@RequestBody TagReqVM tagReqVM) {
        Tag tag = tagVMMapper.tagReqVMToTag(tagReqVM);
        Tag createdTag = tagService.createTag(tag);
        TagResVM response = tagVMMapper.tagToTagResVM(createdTag);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResVM> updateTag(@PathVariable Long id, @RequestBody TagReqVM tagReqVM) {
        Tag tag = tagVMMapper.tagReqVMToTag(tagReqVM);
        Tag updatedTag = tagService.updateTag(id, tag);
        TagResVM response = tagVMMapper.tagToTagResVM(updatedTag);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}