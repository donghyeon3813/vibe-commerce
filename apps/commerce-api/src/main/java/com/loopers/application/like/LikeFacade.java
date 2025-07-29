package com.loopers.application.like;

import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeFacade {

    private final LikeService likeService;
    private final UserService userService;
    private final ProductService productService;

    public void register(LikeCommand.RegisterDto registerDto) {

        UserModel user = userService.getUser(registerDto.userId());
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        productService.getProductInfo(registerDto.productId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

        likeService.resist(user.getId(), registerDto.productId());
    }

    public void delete(LikeCommand.DeleteDto deleteDto) {
        UserModel user = userService.getUser(deleteDto.userId());
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        likeService.unLike(user.getId(), deleteDto.productId());
    }
}
