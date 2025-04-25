package exceptions;

public class FileIsEmptyException extends RuntimeException
{
    public FileIsEmptyException()
    {
        super("Файл пустой!");
    }
}
