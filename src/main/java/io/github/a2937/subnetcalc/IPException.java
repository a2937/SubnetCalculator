package io.github.a2937.subnetcalc;

/**
 * An exception to be thrown if an IP
 * address is malformed.
 */
public class IPException extends RuntimeException
{
    /**
     * Instantiates a new Ip exception.
     */
    public IPException() { super(); }

    /**
     * Instantiates a new Ip exception.
     *
     * @param message the message
     */
    public IPException(String message) { super(message); }

    /**
     * Instantiates a new Ip exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public IPException(String message, Throwable cause) { super(message, cause); }

    /**
     * Instantiates a new Ip exception.
     *
     * @param cause the cause
     */
    public IPException(Throwable cause) { super(cause); }
}
