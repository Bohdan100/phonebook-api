package corp.phonebook.validation;

public final class Validation {

    public static <T> boolean requireNull(T value) {
        return value == null;
    }

    public static <T> boolean requireNonNull(T value) {
        return value != null;
    }
}

