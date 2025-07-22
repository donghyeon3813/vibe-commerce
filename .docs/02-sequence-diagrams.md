## 상품 목록 조회
```mermaid
sequenceDiagram
    participant 사용자
    participant PC as ProductController
    participant PS as ProductService
    participant PR as ProductRepository
    participant BS as BrandService
    participant BR as BrandRepository

    사용자->>PC: 상품 목록 요청(brandId, sort, page, size)
    PC->>PS: 상품 목록 조회
    PS->>PR: 상품 목록 조회

    alt 상품 없음
        PS-->>PC: 빈 상품 목록 반환
    else 상품 있음
        PR-->>PS: 상품 목록 반환
        PS->>BS: 브랜드 정보 조회(brandIds)
        BS-->>BR: 브랜드 정보 조회
        BR-->>BS: 브랜드 정보 반환
        alt 브랜드 정보 없음
            BS-->>PS: null 반환
            PS-->>PC: 상품 정보만 반환
        else 브랜드 정보 있음
            BR-->>BS: 브랜드 정보 반환
            BS-->>PS: 브랜드 정보 반환
            PS-->>PC: 상품 + 브랜드 정보 반환
        end
    end

    PC-->>사용자: 상품 목록 응답
```

## 상품 정보 조회
```mermaid
sequenceDiagram
    participant 사용자
    participant PC as ProductController
    participant PS as ProductService
    participant PR as ProductRepository
    participant BS as BrandService
    participant BR as BrandRepository

    사용자->>PC: 상품 정보 요청(productId)
    PC->>PS: 상품 정보 조회
    PS->>PR: 상품 정보 조회

    alt 상품 없음
        PS-->>PC: 404 반환
    else 상품 있음
        PR-->>PS: 상품 정보 반환
        PS->>BS: 브랜드 정보 조회(brandIds)
        BS-->>BR: 브랜드 정보 조회
        BR-->>BS: 브랜드 정보 반환
        alt 브랜드 정보 없음
            BS-->>PS: null 반환
            PS-->>PC: 상품 정보만 반환
        else 브랜드 정보 있음
            BR-->>BS: 브랜드 정보 반환
            BS-->>PS: 브랜드 정보 반환
            PS-->>PC: 상품 + 브랜드 정보 반환
        end
    end

    PC-->>사용자: 상품 정보 응답
```

## 브랜드 정보 조회
```mermaid
sequenceDiagram
participant 사용자
participant BC as BrandController
participant BS as BrandService
participant BR as BrandRepository

    사용자->>BC: 브랜드 정보 요청(brandId)
    BC->>BS: 브랜드 정보 조회
    BS->>BR: 브랜드 정보 조회
    BR-->>BS: 브랜드 정보 반환
    alt 브랜드 없음
        BS-->>BC: 404 반환
    else 브랜드 있음
        BR-->>BS: 브랜드 정보 반환
    end

    BC-->>사용자: 브랜드 정보 응답
```

## 좋아요 등록
```mermaid
sequenceDiagram
    participant 사용자
    participant LC as LikeController
    participant US as UserService
    participant UR as UserRepository
    participant LS as LikeService
    participant LR as LikeRepository
    participant PS as ProductService
    participant PR as ProductRepository
    
    
    사용자 ->> LC: 좋아요 등록 요청(USER-X-ID, productId)
    LC --> US: 사용자 확인
    US --> UR: 사용자 조회
    alt 사용자 없음
        US --> LC: 실패 처리
    else
        US --> LC: 유저 반환
    end
    LC ->> LS: 좋아요 등록 요청(userId, productId)
    LS ->> PS: 상품 정보 조회(productId)
    PS ->> PR: 상품 정보 조회
    PR -->> PS: 상품 정보 반환
    PS -->> LS: 상품 정보 반환
    alt 상품 없음
        PS -->> LC: 404 Not Found
    else 상품 있음
        LS ->> LR: 좋아요 등록 처리
        alt 등록 실패
        LR --> LS: 등록 실패 응답
        else 등록 성공
            LS -->> LC: 처리 결과 반환
            LC -->> 사용자: 성공 응답
        end
    end
    
        
```

## 좋아요 해제
```mermaid
sequenceDiagram
    participant 사용자
    participant LC as LikeController
    participant US as UserService
    participant UR as UserRepository
    participant LS as LikeService
    participant LR as LikeRepository
    

    사용자 ->> LC: 좋아요 등록 요청(USER-X-ID, likeId)
    LC --> US: 사용자 확인
    US --> UR: 사용자 조회
    alt 사용자 없음
        US --> LC: 실패 처리
    else 사용자 있음
        US --> LC: 유저 반환
    end
    LC ->> LS: 좋아요 해제 요청
    LS ->> LR: 좋아요 조회(likeID)
    LR ->> LS: 좋아요 결과 반환
    alt 정보 없음
        LS -->> LC: 요청 실패 응답
    else
        LS -->> LR: 좋아요 삭제 요청
        alt 삭제 실패
            LR -->> LC: 삭제 실패 응답
        else 삭제 성공
            LS -->> LC: 좋아요 해제 처리 결과 반환
            LC -->> 사용자: 해제 성공 응답
        end
    end
    

```

## 내가 좋아요한 물품 목록
```mermaid
sequenceDiagram
    participant 사용자
    participant LC as LikeController
    participant US as UserService
    participant UR as UserRepository
    participant LS as LikeService
    participant LR as LikeRepository
    participant PS as ProductService
    participant PR as ProductRepository
    

    사용자 ->> LC: 좋아요 등록 요청(USER-X-ID, likeId)
    LC ->> US: 사용자 확인
    US ->> UR: 사용자 조회
    UR -->> US: 조회 결과 반환
    alt 사용자 없음
        US -->> LC: 실패 처리
    else 사용자 있음
        US -->> LC: 유저 반환
    end
    LC ->> LS: 좋아요 목록 조회 요청
    LS -->> LR: 좋아요 목록 조회
    LR -->> LS: 좋아요 목록 반환
    alt 좋아요 목록 없음
        LS -->> LC: 빈 목록 반환
    end
    LS -->> PS: 상품 조회 요청
    PS -->> PR: 상품 조회
    PR -->> LC: 상품 반환
```

### 주문 / 결재
```mermaid
sequenceDiagram
    participant 사용자
    participant OC as OrderController
    participant OS as OrderService
    participant OR as OrderRepository
    participant US as UserService
    participant UR as UserRepository
    participant PS as ProductService
    participant PR as ProductRepository
    participant PtS as PointService
    participant PtR as PointRepository
    participant PayS as PaymentService
    participant PayR as PaymentRepository

    사용자 ->> OC: 주문 요청(USER-X-ID, OrderItems)
    OC ->> US: 사용자 확인 요청(userId)
    US ->> UR: 사용자 조회
    UR -->> US: 사용자 정보 반환
    alt 사용자 없음
        US -->> OC: 사용자 없음 오류 반환
        OC -->> 사용자: 실패 응답
    else 사용자 있음
        US -->> OC: 사용자 반환
        OC ->> PS: 상품 유효성 확인 및 재고 차감(OrderItems)
        PS ->> PR: 상품 조회
        PR -->> PS: 상품 정보 반환
        alt 재고 부족 
            PS -->> OC: 재고 부족 실패 반환
        else
            PS --> PR: 재고 차감 요청
        end
        OC ->> OS: 주문 저장 요청 (User, OrderItems)
        OS ->> OR: 주문 저장
        OR -->> OS: 저장된 주문 반환
        OS ->> PtS: 포인트 조회
        alt 포인트 부족
            Pts --> OC: 포인트 차감 실패 반환
        end
        PtS ->> PtR: 포인트 차감 요청
        PtR -->> PtS: 차감 완료
        PtS -->> OS: 포인트 차감 완료
        OS ->> PayS: 결제 저장 요청(Order, 결제수단)
        PayS ->> PayR: 결제 정보 저장
        PayR -->> PayS: 저장 완료
        PayS -->> OS: 결제 저장 완료
        OS ->> OR: 주문 상태 변경 (결제 완료)
        OR -->> OS: 상태 변경 완료
        OS -->> OC: 주문 처리 완료
        OC -->> 사용자: 주문 성공 응답
    end
    
```