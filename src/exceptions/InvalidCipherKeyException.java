package exceptions;

public class InvalidCipherKeyException extends RuntimeException
{
    public InvalidCipherKeyException(String message)
    {
        super(message);
    }
}
