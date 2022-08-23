package data.candidate.exceptions;

import java.io.Serial;

public class NonExistentEntityException extends Exception {

    @Serial
    private static final long serialVersionUID = -3760558819369784286L;

    public NonExistentEntityException(String message) {
        super(message);
    }
}