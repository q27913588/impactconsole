package biz.mercue.impactweb.util;

public class CustomException {
    public static class TokenNullException extends RuntimeException {
        public TokenNullException() {
        }

        public TokenNullException(String message) {
            super(message);
        }
    }

    public static class TokenInvalidException extends RuntimeException {
        public TokenInvalidException() {
        }

        public TokenInvalidException(String message) {
            super(message);
        }
    }

    public static class TokenExpireException extends RuntimeException {
        public TokenExpireException() {
        }

        public TokenExpireException(String message) {
            super(message);
        }
    }

    public static class CanNotFindDataException extends RuntimeException {
        public CanNotFindDataException() {
        }

        public CanNotFindDataException(String message) {
            super(message);
        }
    }

    public static class DuplicateAccountException extends RuntimeException {
        public DuplicateAccountException() {
        }

        public DuplicateAccountException(String message) {
            super(message);
        }
    }

    public static class LostRequiredFieldException extends RuntimeException {
        public LostRequiredFieldException() {
        }

        public LostRequiredFieldException(String message) {
            super(message);
        }
    }

    public static class AccountDostNotExistException extends RuntimeException {
        public AccountDostNotExistException() {
        }

        public AccountDostNotExistException(String message) {
            super(message);
        }
    }
}
