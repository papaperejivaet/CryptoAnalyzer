package cipher;

import exceptions.NoCoincidenceException;

import java.util.*;

public class DecryptByAnalytics
{

    private static final Map<Character, Double> RUSSIAN_FREQUENCIES = Map.ofEntries(
            Map.entry('а', 8.66),
            Map.entry('б', 1.59),
            Map.entry('в', 4.54),
            Map.entry('г', 1.70),
            Map.entry('д', 2.98),
            Map.entry('е', 8.72),
            Map.entry('ё', 0.13),
            Map.entry('ж', 0.94),
            Map.entry('з', 1.65),
            Map.entry('и', 7.34),
            Map.entry('й', 1.21),
            Map.entry('к', 3.49),
            Map.entry('л', 4.40),
            Map.entry('м', 3.21),
            Map.entry('н', 6.97),
            Map.entry('о', 9.28),
            Map.entry('п', 2.81),
            Map.entry('р', 4.73),
            Map.entry('с', 5.47),
            Map.entry('т', 6.26),
            Map.entry('у', 2.62),
            Map.entry('ф', 0.26),
            Map.entry('х', 0.97),
            Map.entry('ц', 0.48),
            Map.entry('ч', 1.48),
            Map.entry('ш', 0.73),
            Map.entry('щ', 0.36),
            Map.entry('ъ', 0.04),
            Map.entry('ы', 1.90),
            Map.entry('ь', 1.74),
            Map.entry('э', 0.32),
            Map.entry('ю', 0.64),
            Map.entry('я', 2.01),
            Map.entry(' ', 15.0),
            Map.entry(',', 4.0),
            Map.entry('.', 3.0),
            Map.entry('-', 1.0),
            Map.entry('!', 0.1),
            Map.entry('?', 0.1),
            Map.entry(':', 0.1),
            Map.entry(';', 0.05),
            Map.entry('«', 0.01),
            Map.entry('»', 0.01)
    ); //Было TreeMap
    private final List<Character> alphabet;
    private static final Encrypter encrypter = new Encrypter();

    public DecryptByAnalytics(List<Character> alphabet)
    {
        this.alphabet = alphabet;
    }

    public List<List<String>> decrypt(List<String> encryptedData) throws NoCoincidenceException
    {
        Map<Character, Integer> currentValues = valuesForCurrentText(countTotalCharacters(encryptedData));
        List<List<String>> variations = createVariations(encryptedData);

        List<List<String>> decryptedData = new ArrayList<>();

        Map<Integer, Integer> deviationSquareSum = deviationSquareSumMap(currentValues, variations); // 1 - Номер варианта; 2 - сумма квадратов отклонений

        int minSum = Collections.min(deviationSquareSum.values());

        for (Map.Entry<Integer, Integer> entry : deviationSquareSum.entrySet())
        {
            int sum = entry.getValue();

            if (sum == minSum)
            {
                List<String> variation = variations.get(entry.getKey());

                decryptedData.add(variation);
            }
        }

        return decryptedData;
    }

    private int countTotalCharacters(List<String> data)
    {
        int count = 0;
        for (String line : data)
        {
            count += line.length();
        }
        return count;
    }

    private Map<Integer, Integer> deviationSquareSumMap(Map<Character, Integer> currentValues, List<List<String>> variations) throws NoCoincidenceException
    {
        Map<Integer, Integer> deviationSquareSum = new HashMap<>();


        int variationNumber = 0;
        for (List<String> variation : variations)
        {
            int sum = calculateDeviationSquareSum(currentValues, getLettersCount(variation));
            deviationSquareSum.put(variationNumber, sum);
            variationNumber++;
        }

        return deviationSquareSum;

    }

    private int calculateDeviationSquareSum(Map<Character, Integer> statisticLettersCount, Map<Character, Integer> currentLettersCount) throws NoCoincidenceException
    {

        int sum = 0;
        int check = 0;
        int deviation = 0;

        for (Character letter : alphabet)
        {
            Character lowLetter = Character.toLowerCase(letter);
            if (currentLettersCount.containsKey(lowLetter))
            {
                deviation = currentLettersCount.get(lowLetter) - statisticLettersCount.get(lowLetter);
                check++;
            }
            else
            {
                deviation = statisticLettersCount.get(lowLetter) * -1;
            }
            sum += deviation * deviation;

        }

        if (check == 0)
        {
            throw new NoCoincidenceException();
        }
        else
        {
            return sum;
        }
    }


    private Map<Character, Integer> getLettersCount(List<String> data)
    {
        Map<Character, Integer> lettersCount = new HashMap<>(); //Было TreeMap

        for (String line : data)
        {
            putLetters(lettersCount, line);
        }

        return lettersCount;

    }


    private void putLetters(Map<Character, Integer> lettersCount, String line)
    {
        Character[] letters = toCharacterArray(line);
        for (Character letter : letters)
        {
            if (alphabet.contains(letter))
            {
                letter = Character.toLowerCase(letter);
                lettersCount.put(letter, lettersCount.getOrDefault(letter, 0) + 1);
            }
        }
    }

    private Character[] toCharacterArray(String line)
    {
        Character[] characters = new Character[line.length()];

        for (int i = 0; i < line.length(); i++)
        {
            characters[i] = line.charAt(i);
        }

        return characters;
    }


    private Map<Character, Integer> valuesForCurrentText(int textSize)
    {
        Map<Character, Integer> currentValues = new HashMap<>();//было TreeMap

        for (Map.Entry<Character, Double> entry : RUSSIAN_FREQUENCIES.entrySet())
        {
            currentValues.put(entry.getKey(), (int) Math.round(((double) textSize / 100) * entry.getValue()));
        }
        return currentValues;
    }


    private List<List<String>> createVariations(List<String> data)
    {
        List<List<String>> variations = new ArrayList<>();
        int key = 0;

        while (key < alphabet.size())
        {
            variations.add(encrypter.encrypt(alphabet, data, key));
            key++;
        }
        return variations;
    }

}


