package cipher;


import java.util.*;

/**
 * Класс Decrypter отвечает за расшифровку данных, используя алгоритм шифрования Цезаря
 */
public class Decrypter
{
    /**
     * Экземпляр класса Encrypter, используемый для выполнения операции шифрования и дешифрования.
     */
    private final Encrypter encrypter = new Encrypter();

    /**
     * Список символов алфавита, используемый для шифрования и дешифрования.
     */
    private final List<Character> alphabet;

    /**
     * Конструктор класса Decrypter.
     *
     * @param alphabet Список символов алфавита, используемого для шифрования/дешифрования.
     */
    public Decrypter(List<Character> alphabet)
    {
        this.alphabet = alphabet;
    }

    /**
     * Метод для расшифровки данных.
     * Для расшифровки используется ключ шифрования.
     *
     * @param encryptedData Список строк, содержащих зашифрованные данные.
     * @param key Ключ для дешифровки (сдвига).
     * @return Список строк с расшифрованными данными.
     */
    public List<String> decrypt(List<String> encryptedData, int key)
    {

        return encrypter.encrypt(alphabet, encryptedData, key * -1);

    }


}
