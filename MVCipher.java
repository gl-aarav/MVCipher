
/**
 * This program implements an MV Cipher machine for encrypting and decrypting text files.
 *
 * @author Aarav Goyal
 * @since September 15, 2025
 */
import java.io.PrintWriter;
import java.util.Scanner;

public class MVCipher {
   private String key; // The encryption/decryption key
   private int index = 0; // Current index in the key for character shifting
   private final boolean IS_DEBUG_MODE; // Flag for debug mode
   private final int ALPHABET_SIZE; // Number of letters in the alphabet
   private final int ASCII_UPPER_A; // ASCII value of 'A'
   private final int KEY_MIN_LENGTH; // Minimum length for the key

   public MVCipher() {
      key = ""; // Initialize key to an empty string
      IS_DEBUG_MODE = false; // Set debug mode to false
      ALPHABET_SIZE = 26; // There are 26 letters in the English alphabet
      ASCII_UPPER_A = 65; // ASCII value for 'A'
      KEY_MIN_LENGTH = 3; // Minimum key length is 3 characters
   }

   /**
    * Main method to run the MVCipher program.
    * 
    * @param args Command line arguments (not used).
    */
   public static void main(String[] args) {
      MVCipher cipherMachine = new MVCipher();
      cipherMachine.runCipher();
   }

   /**
    * Runs the MVCipher machine, handling user input for encryption/decryption,
    * file operations, and key input.
    */
   public void runCipher() {
      System.out.println("\nWelcome to the MV Cipher machine!\n");
      this.key = this.getKeyInput();

      if (this.IS_DEBUG_MODE) {
         System.out.println("The key is " + this.key);
      }

      boolean isEncrypting = true;
      int operationChoice = Prompt.getInt("\nEncrypt or decrypt?", 1, 2);

      if (operationChoice == 2) {
         isEncrypting = false;
      }

      System.out.print("\nName of file to ");

      if (isEncrypting) {
         System.out.print("encrypt");
      } else {
         System.out.print("decrypt");
      }

      String inFileName = Prompt.getString("");
      String outputFileName = Prompt.getString("Name of output file");

      try (Scanner fileScanner = FileUtils.openToRead(inFileName);
            PrintWriter fileWriter = FileUtils.openToWrite(outputFileName)) {

         while (fileScanner.hasNext()) {
            String currentLine = fileScanner.nextLine();
            String encryptedDecryptedLine = "";

            for (int characterIndex = 0; characterIndex < currentLine.length(); ++characterIndex) {
               char originalCharacter = currentLine.charAt(characterIndex);
               char processedCharacter = originalCharacter;

               if (Character.isLetter(originalCharacter) && Character.isLowerCase(originalCharacter)) {
                  processedCharacter = this.getEncryptDecryptLowerCase(
                        originalCharacter, isEncrypting);
               } else if (Character.isLetter(originalCharacter) && Character.isUpperCase(originalCharacter)) {
                  processedCharacter = this.getEncryptDecryptUpperCase(
                        originalCharacter, isEncrypting);
               }

               encryptedDecryptedLine = encryptedDecryptedLine + processedCharacter;
            }

            fileWriter.println(encryptedDecryptedLine);
         }
      }

      System.out.print("\nThe ");

      if (isEncrypting) {
         System.out.print("encrypted ");
      } else {
         System.out.print("decrypted ");
      }

      System.out.println("file " + outputFileName + " has been created using the keyword -> "
            + this.key);
      System.out.println();
   }

   /**
    * Prompts the user for a key and validates it.
    * The key must consist only of letters and be at least 3 characters long.
    * 
    * @return The validated key as an uppercase string.
    */
   public String getKeyInput() {
      boolean isValid = false;
      String keyInput = "";

      while (!isValid) {
         keyInput = Prompt.getString("Please input a word to use as key (letters only)");
         keyInput = keyInput.toUpperCase();

         if (this.isKeyValid(keyInput)) {
            isValid = true;
         } else {
            System.out.println("ERROR: Key must be all letters and at least 3 characters long");
         }
      }

      return keyInput;
   }

   /**
    * Checks if the provided key is valid.
    * A valid key must have a length of at least KEY_MIN_LENGTH and contain only
    * uppercase letters.
    * 
    * @param inputKey The key string to validate.
    * @return True if the key is valid, false otherwise.
    */
   public boolean isKeyValid(String inputKey) {
      if (inputKey.length() < KEY_MIN_LENGTH) {
         return false;
      } else {
         for (int charIndex = 0; charIndex < inputKey.length(); ++charIndex) {
            if (inputKey.charAt(charIndex) < ASCII_UPPER_A
                  || inputKey.charAt(charIndex) > ASCII_UPPER_A + ALPHABET_SIZE - 1) {
               return false;
            }
         }

         return true;
      }
   }

   /**
    * Encrypts or decrypts a single lowercase character using the current key
    * character.
    * The key character is advanced after each operation.
    * 
    * @param inputChar The lowercase character to encrypt or decrypt.
    * @param isEncrypt A boolean indicating whether to encrypt (true) or decrypt
    *                  (false).
    * @return The encrypted or decrypted lowercase character.
    */
   public char getEncryptDecryptLowerCase(char inputChar, boolean isEncrypt) {
      char keyCharacter = this.key.charAt(this.index);
      this.index = (this.index + 1) % this.key.length();
      int shiftAmount = keyCharacter - ASCII_UPPER_A + 1;
      char outputChar;

      if (isEncrypt) {
         outputChar = (char) (inputChar + shiftAmount);

         if (outputChar > 'z') {
            outputChar = (char) (outputChar - ALPHABET_SIZE);
         }
      } else {
         outputChar = (char) (inputChar - shiftAmount);

         if (outputChar < 'a') {
            outputChar = (char) (outputChar + ALPHABET_SIZE);
         }
      }

      return outputChar;
   }

   /**
    * Encrypts or decrypts a single uppercase character using the current key
    * character.
    * The key character is advanced after each operation.
    * 
    * @param inputChar The uppercase character to encrypt or decrypt.
    * @param isEncrypt A boolean indicating whether to encrypt (true) or decrypt
    *                  (false).
    * @return The encrypted or decrypted uppercase character.
    */
   public char getEncryptDecryptUpperCase(char inputChar, boolean isEncrypt) {
      char keyCharacter = this.key.charAt(this.index);
      this.index = (this.index + 1) % this.key.length();
      int shiftAmount = keyCharacter - ASCII_UPPER_A + 1;
      char outputChar;

      if (isEncrypt) {
         outputChar = (char) (inputChar + shiftAmount);

         if (outputChar > 'Z') {
            outputChar = (char) (outputChar - ALPHABET_SIZE);
         }
      } else {
         outputChar = (char) (inputChar - shiftAmount);

         if (outputChar < 'A') {
            outputChar = (char) (outputChar + ALPHABET_SIZE);
         }
      }

      return outputChar;
   }
}