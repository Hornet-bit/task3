import javax.crypto.Mac;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {

        checkParams(args);

        Random secureRandom = new SecureRandom();
        int computerMoveIndex = secureRandom.nextInt(args.length);

        String computerMove = args[computerMoveIndex];

        byte[] aesKey = new byte[16];
        secureRandom.nextBytes(aesKey);

        StringBuilder secretKey = new StringBuilder();
        for (byte a : aesKey) {
            secretKey.append(String.format("%02x", a).toUpperCase());
        }

        final String HMAC_SHA256 = "HmacSHA256";
        Mac sha256HMAC = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec keySpec = new SecretKeySpec(aesKey, HMAC_SHA256);
        sha256HMAC.init(keySpec);

        byte[] macData = sha256HMAC.doFinal(computerMove.getBytes());
        StringBuilder hmac = new StringBuilder();
        for (byte a : macData) {
            hmac.append(String.format("%02x", a).toUpperCase());
        }

        System.out.println("Hmac :\n" + hmac);

        for (int i = 0; i < args.length; i++) {
            System.out.println(i + 1 + " - " + args[i]);

        }
        System.out.println(0 + " - exit");
        System.out.println("Enter your move:");

        Scanner userInput = new Scanner(System.in);

        int idMove = Integer.parseInt(userInput.next());
        if (idMove == 0) {
            System.exit(0);
        }
        if (idMove > args.length) {
            System.out.println("Out of bounds, please, choose available move\n");
            System.exit(0);
        }
        System.out.println("You're move :" + args[idMove - 1]);

        List<String> computerWinMoves = findComputerWinMoves(args, idMove);

        System.out.println("Computer's move " + computerMove);

        if (computerWinMoves.contains(computerMove)) {
            System.out.println("computer win");
        } else if (computerMove.equals(args[idMove - 1])) {
            System.out.println("Nobody won, it was a draw");
        } else {
            System.out.println("User win!");
        }
        System.out.println("Secret key: " + secretKey + "\n");
    }

    public static void checkParams(String[] params) {
        if (params.length % 2 == 0) {
            System.out.println("an odd number of arguments is required");
            System.out.println("For example: rock paper scissors\n");
            System.exit(0);
        }
        if (params.length < 3) {
            System.out.println("at least 3 parameters are required");
            System.out.println("For example: rock paper scissors\n");
            System.exit(0);
        }
        Set<String> set = new HashSet<>();

        for (String a : params) {
            if (!set.add(a)) {
                System.out.println("The strings must be different. You have duplicates in your input");
                System.out.println("For example: rock paper scissors\n");
                System.exit(0);
            }
        }
    }

    public static List<String> findComputerWinMoves(String[] availableMove, int idMove) {

        List<String> computerWinMoves = new ArrayList<>();
        int i = 1;
        while (true) {

            if (idMove >= availableMove.length) {
                idMove = 0;
            }
            computerWinMoves.add(availableMove[idMove]);

            if (i >= availableMove.length / 2) {
                break;
            }
            i++;
            idMove++;
        }
        return computerWinMoves;
    }
}
