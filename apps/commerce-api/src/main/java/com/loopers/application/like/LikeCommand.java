package com.loopers.application.like;

public class LikeCommand {

    public record RegisterDto(String userId, Long productId) {
        public static RegisterDto of(String userId, Long productId) {
            return new RegisterDto(userId, productId);
        }
    }

    public record DeleteDto(String userId, Long productId) {
        public static DeleteDto of(String userId, Long productId) {
            return new DeleteDto(userId, productId);
        }
    }
}
