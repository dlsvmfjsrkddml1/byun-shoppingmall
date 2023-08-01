package com.byunmall.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public long getOffset(){
        return (long) (Math.max( 1, page) - 1) * Math.min(size, MAX_SIZE);
    }
}

// API 문서 생성

// GET /posts/{postId} -> 단건 조회
// POst /posts -> 게시글 등록

// 클라이언트 입장에서느 API가 무엇인지 모른다.

// Spring RestDocs
// 장점1 : 운영 코드에 영향이 없다. 단순히 테스트 케이스만으로 가능
// 장점2 : 변경된 기능에 대해서 최신문서  모드 수정 -> 문서를 수정 X
//         코드 기능과 문서가 일치 하지 않는다. 문서에 대한 신뢰가 떨어진다.

// Test 케이스 실행해서 문서를 생성해주는게 특징이다. 기능에 대한 문서가 최신본으로 유지가 된다.
//