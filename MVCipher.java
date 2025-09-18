import java.io.PrintWriter;
import java.util.Scanner;

public class MVCipher {
   private String key = "";
   private int index = 0;
   private boolean DEBUG = false;

   public static void main(String[] var0) {
      MVCipher var1 = new MVCipher();
      var1.run();
   }

   public void run() {
      System.out.println("\n Welcome to the MV Cipher machine!\n");
      this.key = this.getKey();
      if (this.DEBUG) {
         System.out.println("The key is " + this.key);
      }

      boolean var1 = true;
      int var2 = Prompt.getInt("\nEncrypt or decrypt?", 1, 2);
      if (var2 == 2) {
         var1 = false;
      }

      System.out.print("\nName of file to ");
      if (var1) {
         System.out.print("encrypt");
      } else {
         System.out.print("decrypt");
      }

      String var3 = Prompt.getString("");
      Scanner var4 = FileUtils.openToRead(var3);
      String var5 = Prompt.getString("Name of output file");
      PrintWriter var6 = FileUtils.openToWrite(var5);

      while (var4.hasNext()) {
         String var7 = var4.nextLine();
         String var8 = "";

         for (int var9 = 0; var9 < var7.length(); ++var9) {
            char var10 = var7.charAt(var9);
            char var11 = var10;
            if (Character.isLetter(var10) && Character.isLowerCase(var10)) {
               var11 = this.encryptDecryptLowerCase(var10, var1);
            } else if (Character.isLetter(var10) && Character.isUpperCase(var10)) {
               var11 = this.encryptDecryptUpperCase(var10, var1);
            }

            var8 = var8 + var11;
         }

         var6.println(var8);
      }

      var6.close();
      System.out.print("\nThe ");
      if (var1) {
         System.out.print("encrypted ");
      } else {
         System.out.print("decrypted ");
      }

      System.out.println("file " + var5 + " has been created using the keyword -> " + this.key);
      System.out.println();
   }

   public String getKey() {
      boolean var1 = false;
      String var2 = "";

      while (!var1) {
         var2 = Prompt.getString("Please input a word to use as key (letters only)");
         var2 = var2.toUpperCase();
         if (this.isValidKey(var2)) {
            var1 = true;
         } else {
            System.out.println("ERROR: Key must be all letters and at least 3 characters long");
         }
      }

      return var2;
   }

   public boolean isValidKey(String var1) {
      if (var1.length() < 3) {
         return false;
      } else {
         for (int var2 = 0; var2 < var1.length(); ++var2) {
            if (var1.charAt(var2) < 'A' || var1.charAt(var2) > 'Z') {
               return false;
            }
         }

         return true;
      }
   }

   public char encryptDecryptLowerCase(char var1, boolean var2) {
      char var3 = this.key.charAt(this.index);
      this.index = (this.index + 1) % this.key.length();
      int var4 = var3 - 65 + 1;
      char var5;
      if (var2) {
         var5 = (char) (var1 + var4);
         if (var5 > 'z') {
            var5 = (char) (var5 - 26);
         }
      } else {
         var5 = (char) (var1 - var4);
         if (var5 < 'a') {
            var5 = (char) (var5 + 26);
         }
      }

      return var5;
   }

   public char encryptDecryptUpperCase(char var1, boolean var2) {
      char var3 = this.key.charAt(this.index);
      this.index = (this.index + 1) % this.key.length();
      int var4 = var3 - 65 + 1;
      char var5;
      if (var2) {
         var5 = (char) (var1 + var4);
         if (var5 > 'Z') {
            var5 = (char) (var5 - 26);
         }
      } else {
         var5 = (char) (var1 - var4);
         if (var5 < 'A') {
            var5 = (char) (var5 + 26);
         }
      }

      return var5;
   }

}