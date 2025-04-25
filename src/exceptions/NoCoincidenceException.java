package exceptions;

public class NoCoincidenceException extends Exception
{
    public NoCoincidenceException()
    {
        super("К сожалению, попытка неудачная! Попробуйте задать другой репрезентативный или зашифрованный текст");
    }
}
