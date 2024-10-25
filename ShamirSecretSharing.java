import java.io.FileReader;
import java.util.*;
import java.util.regex.*;
import java.io.BufferedReader;
import java.math.BigInteger;

class ShamirSecretSharing {
    public static void main(String[] args) throws Exception {

        BufferedReader reader = new BufferedReader(new FileReader("input1.json"));
        StringBuilder jsonStringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonStringBuilder.append(line);
        }
        String jsonString = jsonStringBuilder.toString();
        
        // Extract keys (n and k)
        int n = Integer.parseInt(extractValue(jsonString, "\"n\""));
        int k = Integer.parseInt(extractValue(jsonString, "\"k\""));
        
        // Extract and decode the points (x, y)
        List<BigInteger[]> points = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"(\\d+)\":\\s*\\{\\s*\"base\":\\s*\"(\\d+)\",\\s*\"value\":\\s*\"(\\w+)\"");
        Matcher matcher = pattern.matcher(jsonString);
        
        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int base = Integer.parseInt(matcher.group(2));
            BigInteger y = new BigInteger(matcher.group(3), base); 
            points.add(new BigInteger[]{BigInteger.valueOf(x), y});
        }

        //Calculate the polynomial constant term (c) using Lagrange interpolation
        BigInteger constantTerm = lagrangeInterpolation(points, k);

        System.out.println("The constant term of the polynomial is: " + constantTerm);
    }
    
    // Utility method to extract a value from JSON string by key
    public static String extractValue(String json, String key) {
        Pattern pattern = Pattern.compile(key + "\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static BigInteger lagrangeInterpolation(List<BigInteger[]> points, int k) {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < k; i++) {
            BigInteger term = points.get(i)[1];
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    // Adjust term according to the Lagrange formula using BigInteger
                    term = term.multiply(BigInteger.ZERO.subtract(points.get(j)[0]))
                                .divide(points.get(i)[0].subtract(points.get(j)[0]));
                }
            }
            result = result.add(term);
        }
        return result;
    }
}
