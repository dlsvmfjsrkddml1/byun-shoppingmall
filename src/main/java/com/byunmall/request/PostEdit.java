package com.byunmall.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@ToString
public class PostEdit {

    @NotBlank(message="타이틀을 입력해주세요.")
    private final String title;

    @NotBlank(message="콘텐츠를 입력해주세요.")
    private final String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
