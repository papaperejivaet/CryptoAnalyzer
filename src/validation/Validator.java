package validation;

import exceptions.FileIsEmptyException;
import exceptions.InvalidCipherKeyException;
import exceptions.InvalidFileNameException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Validator
{
    private Validator()
    {
    }

    public static void validateFileName(String fileName)
    {
        if (fileName == null || fileName.isEmpty())
        {
            throw new InvalidFileNameException();
        }

    }

//Валидирует ключ шифра. Отсеивает пустые строки и null, проверяет является ли строка числом.
    public static void validateCipherKey(String key)
    {
        if (key == null || key.isEmpty())
        {
            throw new InvalidCipherKeyException("ВВедено неверное значение!");
        }
        else if(!key.matches("\\d"))
        {
            throw new InvalidCipherKeyException("ВВедено не число!");
        }

    }

    //Валидирует размер файла. Если пустой - бросает исключение
    public static void validateFileSize(String fileName)
    {
        try
        {
            if (Files.size(Path.of(fileName)) == 0)
            {
                throw new FileIsEmptyException();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Файл не найден!");
        }
    }
}
