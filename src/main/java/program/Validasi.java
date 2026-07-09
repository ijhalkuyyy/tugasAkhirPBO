package program;
import javax.swing.JOptionPane;

public class Validasi {

    public String inputNonEmpty(String message) {
        String input;
        do {
            input = JOptionPane.showInputDialog(message);
            if (input == null) System.exit(0);
        } while (input.trim().isEmpty());
        return input.trim();
    }

    public double inputDouble(String message) {
        String input;
        double value = 0;
        boolean valid = false;
        do {
            input = inputNonEmpty(message);
            try {
                value = Double.parseDouble(input);
                valid = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input harus angka. Silakan coba lagi.");
            }
        } while (!valid);
        return value;
    }

    public int inputInt(String message) {
        String input;
        int value = 0;
        boolean valid = false;
        do {
            input = inputNonEmpty(message);
            try {
                value = Integer.parseInt(input);
                valid = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input harus angka bulat. Silakan coba lagi.");
            }
        } while (!valid);
        return value;
    }






}
