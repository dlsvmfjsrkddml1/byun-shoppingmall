package com.byunmall.service;

import com.byunmall.domain.Post;
import com.byunmall.exception.PostNotFound;
import com.byunmall.repository.PostRepository;
import com.byunmall.request.PostCreate;
import com.byunmall.request.PostEdit;
import com.byunmall.request.PostSearch;
import com.byunmall.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        // When
        postService.write(postCreate);
        // Then
        assertThat(postRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post post = Post.builder().title("foo").content("bar").build();
        postRepository.save(post);
        // When
        PostResponse postResponse = postService.get(post.getId());
        // Then
        assertThat(postResponse.getTitle()).isEqualTo("foo");
        assertThat(postResponse.getContent()).isEqualTo("bar");
        assertThat(postRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("글 1개 조회")
    void test3() {
        // given
        Post post = Post.builder().title("foo").content("bar").build();
        postRepository.save(post);

        // 클라이언트의 요구사항 : json응답에서 title 값 길이를 10글자로 해주세요.
        //                       (이런 처리는 클라이언트에서 하는게 좋다.)
        // When
        PostResponse postResponse = postService.get(post.getId());
        // Then
        assertThat(postResponse.getTitle()).isEqualTo("foo");
        assertThat(postResponse.getContent()).isEqualTo("bar");
        assertThat(postRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("글 아이디 변경후 조회하면 PostNotFound.class 이 발생한다.")
    void test3_1() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        // when
        postRepository.save(post);

        // expected
        assertThatThrownBy(() -> postService.get(post.getId() + 1L))
                .isInstanceOf(PostNotFound.class);
        PostNotFound postNotFound = assertThrows(PostNotFound.class, () -> postService.get(post.getId() + 1L));
        assertThat(postNotFound.getMessage()).isEqualTo("존재하지 않는 글입니다.");
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test4() {
        List<Post> requestPosts = IntStream.range(0, 20)
                // requestEntity로 저장해서 조회해야 하기에...
                .mapToObj(i ->
                        Post.builder()
                                .title("제목" + i)
                                .content("내용" + i)
                                .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        List<PostResponse> posts = postService.getList(postSearch);

        assertThat(posts.size()).isEqualTo(10);
        assertThat(posts.get(0).getTitle()).isEqualTo("제목19");
    }

    @Test
    @DisplayName("글 제목만 수정")
    void test5() {
        Post post = Post.builder()
                .title("제목1")
                .content("내용2")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목2")
                .content("내용2")
                .build();
        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertThat(changedPost.getTitle()).isEqualTo("제목2");
        assertThat(changedPost.getContent()).isEqualTo("내용2");
    }

    @Test
    @DisplayName("글 내용만 수정")
    void test6() {
        Post post = Post.builder()
                .title("제목")
                .content("내용1")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목")
                .content("내용2")
                .build();
        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertThat(changedPost.getContent()).isEqualTo("내용2");
    }

    @Test
    @DisplayName("글 내용만 수정 ")
    void test7() {
        Post post = Post.builder()
                .title("제목")
                .content("내용1")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .content("내용2")
                .build();
        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertThat(changedPost.getContent()).isEqualTo("내용2");
        assertThat(changedPost.getTitle()).isEqualTo("제목");
    }

    @Test
    @DisplayName("글 내용만 수정 - 존재하지 않는 글 ")
    void test7_1() {
        Post post = Post.builder()
                .title("제목")
                .content("내용1")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목1")
                .content("내용2")
                .build();

        assertThatThrownBy(() -> postService.edit(post.getId() + 1L, postEdit))
                .isInstanceOf(PostNotFound.class);
    }

    @Test
    @DisplayName("글 내용 삭제 ")
    void test8() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertThat(postRepository.count()).isEqualTo(0);
    }
}
