/**
 * This program implements an MV Cipher machine for encrypting and decrypting text files.
 *
 * @author Aarav Goyal
 * @since September 17, 2025
 */

// Import for writing to files
import java.io.PrintWriter;
import java.util.Scanner;

public class MVCipher {
   private String key; // The encryption/decryption key
   private int index; // Current index in the key for character shifting
   private final int ALPHABET_SIZE; // Number of letters in the alphabet
   private final int ASCII_UPPER_A; // ASCII value of 'A'
   private final int KEY_MIN_LENGTH; // Minimum length for the key

   public MVCipher() {
      key = ""; // Initialize key to an empty string
      index = 0; // Initialize index to 0
      ALPHABET_SIZE = 26; // Number of letters in the alphabet
      ASCII_UPPER_A = 65; // ASCII value of 'A'
      KEY_MIN_LENGTH = 3; // Minimum length for the key
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
      System.out.println("\n Welcome to the MV Cipher machine!\n");
      this.key = this.getKeyInput();

      // Determine if the user wants to encrypt or decrypt
      boolean isEncrypting = true;
      int operationChoice = Prompt.getInt("\nEncrypt or decrypt?", 1, 2);

      if (operationChoice == 2) {
         isEncrypting = false;
      }

      System.out.print("\nName of file to ");

      // Update prompt based on user's choice
      if (isEncrypting) {
         System.out.print("encrypt");
      } else {
         System.out.print("decrypt");
      }

      String inFileName = Prompt.getString("");
      String outputFileName = Prompt.getString("Name of output file");

      Scanner fileScanner = FileUtils.openToRead(inFileName);
      if (fileScanner == null) {
         System.out.println("ERROR: Input file not found: " + inFileName);
         this.index = 0; // Reset the key index even on error
         return;
      }

      PrintWriter fileWriter = FileUtils.openToWrite(outputFileName);
      if (fileWriter == null) {
         System.out.println("ERROR: Output file could not be created: " + outputFileName);
         fileScanner.close(); // Close the scanner if writer can't be opened
         this.index = 0; // Reset the key index even on error
         return;
      }

      // Loop through each line of the input file
      while (fileScanner.hasNext()) {
         String currentLine = fileScanner.nextLine();
         StringBuilder encryptedDecryptedLine = new StringBuilder();

         // If a line is empty, it will be written as an empty line in the output file.
         // This behavior is intentional.

         // Loop through each character of the current line
         for (int characterIndex = 0; characterIndex < currentLine.length(); ++characterIndex) {
            char originalCharacter = currentLine.charAt(characterIndex);
            char processedCharacter = originalCharacter;

            // Check if the character is a lowercase letter
            if (Character.isLetter(originalCharacter) && Character.isLowerCase(originalCharacter)) {
               processedCharacter = this.getEncryptDecryptLowerCase(originalCharacter, isEncrypting);
            } else if (Character.isLetter(originalCharacter) && Character.isUpperCase(originalCharacter)) {
               processedCharacter = this.getEncryptDecryptUpperCase(
                     originalCharacter, isEncrypting);
            }

            encryptedDecryptedLine.append(processedCharacter);
         }

         fileWriter.println(encryptedDecryptedLine.toString());
      }
      fileScanner.close();
      fileWriter.close();
      this.index = 0; // Reset the key index after processing

      System.out.print("\nThe ");

      // Update completion message based on user's choice
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

      // Loop until a valid key is entered
      while (!isValid) {
         keyInput = Prompt.getString("Please input a word to use as key (letters only)");

         // Check if the key contains only letters
         if (!keyInput.matches("[a-zA-Z]+")) {
            System.out.println("ERROR: Key must contain only letters.");
            continue;
         }

         // Convert to uppercase and validate length
         keyInput = keyInput.toUpperCase();
         if (this.isKeyValid(keyInput)) {
            isValid = true;
         } else {
            System.out.println("ERROR: Key must be at least " + KEY_MIN_LENGTH + " characters long.");
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
      // Check if the key meets the minimum length requirement
      if (inputKey.length() < KEY_MIN_LENGTH) {
         return false;
      } else {
         for (int charIndex = 0; charIndex < inputKey.length(); ++charIndex) {
            // Check if the character is an uppercase letter
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

      // Encrypt the character
      if (isEncrypt) {
         outputChar = (char) (inputChar + shiftAmount);

         // Wrap around the alphabet if necessary
         if (outputChar > 'z') {
            outputChar = (char) (outputChar - ALPHABET_SIZE);
         }
      } else {
         outputChar = (char) (inputChar - shiftAmount);

         // Wrap around the alphabet if necessary
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

      // Encrypt the character
      if (isEncrypt) {
         outputChar = (char) (inputChar + shiftAmount);

         // Wrap around the alphabet if necessary
         if (outputChar > 'Z') {
            outputChar = (char) (outputChar - ALPHABET_SIZE);
         }
      } else {
         outputChar = (char) (inputChar - shiftAmount);

         // Wrap around the alphabet if necessary
         if (outputChar < 'A') {
            outputChar = (char) (outputChar + ALPHABET_SIZE);
         }
      }

      return outputChar;
   }
}