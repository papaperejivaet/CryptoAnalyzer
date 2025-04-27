package exceptions;

public class FileIsEmptyException extends Exception
{
    public FileIsEmptyException()
    {
        super("Файл пустой!");
    }
}
