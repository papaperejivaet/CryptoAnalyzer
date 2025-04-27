package exceptions.answer_exceptions;

public class IncorrectAnswerException extends RuntimeException
{
    public IncorrectAnswerException()
    {
        super("Такого варианта нет!");
    }
}
