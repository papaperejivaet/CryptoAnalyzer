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

public class FileManager
{
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
           writer.write("*".repeat(50) + "\n");
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
