package exceptions;

public class InvalidFileNameException extends Exception
{
    public InvalidFileNameException()
    {
        super("Введено некорректное имя файла!");
    }
}
