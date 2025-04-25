package exceptions;

public class InvalidFileNameException extends RuntimeException
{
    public InvalidFileNameException()
    {
        super("Введено некорректное имя файла!");
    }
}
