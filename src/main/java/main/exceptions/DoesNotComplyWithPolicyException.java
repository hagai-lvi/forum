package main.exceptions;

/**
 * Mark that a message or a password does not comply with the forum policy
 * Created by hagai on 07/04/15.
 */
public class DoesNotComplyWithPolicyException extends Exception {
    public DoesNotComplyWithPolicyException(String message) {
        super(message);
    }
}
