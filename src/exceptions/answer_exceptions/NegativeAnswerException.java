package exceptions.answer_exceptions;

public class NegativeAnswerException extends RuntimeException
{
    public NegativeAnswerException()
    {
        super("Введено отрицательное число!");
    }
}
