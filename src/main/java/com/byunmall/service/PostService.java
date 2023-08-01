package com.byunmall.service;

import com.byunmall.domain.Post;
import com.byunmall.domain.PostEditor;
import com.byunmall.exception.PostNotFound;
import com.byunmall.repository.PostRepository;
import com.byunmall.request.PostCreate;
import com.byunmall.request.PostEdit;
import com.byunmall.request.PostSearch;
import com.byunmall.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch){
        return postRepository.getList(postSearch).stream()
                .map(post -> new PostResponse(post))
                .collect(Collectors.toList());
    }


    @Transactional
    public void edit(Long id, PostEdit postEdit){
        // 게시글이 존재하지 않는다면 "존재하지 않는 글입니다." 라고 예외를 발생시킨다.
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        // 게시글 수정하는 메서드
        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }
}
