package com.byunmall.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC)
@Entity @Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob // 대용량 데이터를 저장하는 주석
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostEditor.PostEditorBuilder toEditor(){
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    // 하나의 인자만 와서 좋다.
    // PostEditor 안에 변수만 수정을 해야 하는구나! 라고 생각한다.
    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
