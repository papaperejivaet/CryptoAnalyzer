package file_manager;

import exceptions.FileIsEmptyException;
import exceptions.InvalidFileNameException;
import validation.Validator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс FileManager предоставляет методы для чтения и записи данных в файлы.
 * Он использует валидацию имени файла и проверку размера файла перед чтением и записью.
 */
public class FileManager
{

    /**
     * Читает данные из файла и возвращает их в виде списка строк.
     * Выполняет валидацию имени файла и проверку на его пустоту перед чтением.
     *
     * @param fileName Имя файла для чтения.
     * @return Список строк, содержащих данные из файла.
     * @throws InvalidFileNameException если имя файла неверное.
     * @throws FileIsEmptyException если файл пустой.
     * @throws RuntimeException если файл не найден или произошла ошибка при чтении.
     */
    public List<String> getData(String fileName) throws InvalidFileNameException, FileIsEmptyException
    {
        ArrayList<String> fileData = new ArrayList<>();
        Validator.validateFileName(fileName);
        Path path = Path.of(fileName);
        Validator.validateFileSize(fileName);

        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            while (reader.ready())
            {
                fileData.add(reader.readLine());
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Файл не найден!");
        }

        return fileData;
    }

    /**
     * Записывает данные в файл.
     * Выполняет валидацию имени файла перед записью.
     *
     * @param fileName Имя файла для записи.
     * @param data Список строк для записи в файл.
     * @return true, если данные успешно записаны в файл, иначе false.
     */
   public boolean writeData(String fileName, List<String> data)
   {
       try
       {
           Validator.validateFileName(fileName);
       }
       catch (InvalidFileNameException e)
       {
           System.out.println(e.getMessage());
           return false;
       }
       Path path = Path.of(fileName);

       try (BufferedWriter writer = Files.newBufferedWriter(path))
       {
           for (String line : data)
           {
               writer.write(line + "\n");
           }
           writer.flush();
           return true;
       }
       catch (IOException e)
       {
           System.out.print("\nФайл не найден!");
           return false;
       }
   }
}
