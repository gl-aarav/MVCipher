import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Prompt {
    private static InputStreamReader streamReader;
    private static BufferedReader bufReader;

    public static String getString(String var0) {
        System.out.print(var0 + " -> ");
        String var1 = "";

        try {
            var1 = bufReader.readLine();
        } catch (IOException var3) {
            System.err.println("ERROR: BufferedReader could not read line");
        }

        return var1;
    }

    public static char getChar(String var0) {
        String var1;
        for (var1 = getString(var0); var1.length() != 1; var1 = getString(var0)) {
        }

        return var1.charAt(0);
    }

    public static int getInt(String var0) {
        int var1 = 0;
        boolean var2 = false;

        while (!var2) {
            String var3 = getString(var0);

            try {
                var1 = Integer.parseInt(var3);
                var2 = true;
            } catch (NumberFormatException var5) {
                var2 = false;
            }
        }

        return var1;
    }

    public static int getInt(String var0, int var1, int var2) {
        boolean var3 = false;

        int var4;
        do {
            do {
                var4 = getInt(var0 + " (" + var1 + ", " + var2 + ")");
            } while (var4 < var1);
        } while (var4 > var2);

        return var4;
    }

    public static double getDouble(String var0) {
        double var1 = 0.0D;
        boolean var3 = false;

        while (!var3) {
            String var4 = getString(var0);

            try {
                var1 = Double.parseDouble(var4);
                var3 = true;
            } catch (NumberFormatException var6) {
                var3 = false;
            }
        }

        return var1;
    }

    public static double getDouble(String var0, double var1, double var3) {
        double var5 = 0.0D;
        boolean var7 = false;

        do {
            do {
                var5 = getDouble(var0 + " (" + var1 + ", " + var3 + ")");
            } while (var5 < var1);
        } while (var5 > var3);

        return var5;
    }

    static {
        streamReader = new InputStreamReader(System.in);
        bufReader = new BufferedReader(streamReader);
    }
}
