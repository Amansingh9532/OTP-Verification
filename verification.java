import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

public class verification {

    private static String generatedOtp = "";
    private static long otpTimestamp = 0;

    public static void main(String[] args) {
        JFrame f1 = new JFrame("OTP Verification");
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel l1 = new JLabel("Enter your mobile number:");
        l1.setBounds(30, 20, 150, 25);
        panel.add(l1);

        JTextField phoneNumber = new JTextField();
        phoneNumber.setBounds(200, 20, 150, 25);
        panel.add(phoneNumber);

        JLabel l2 = new JLabel("Enter OTP:");
        l2.setBounds(30, 60, 80, 25);
        panel.add(l2);

        JTextField otpField = new JTextField();
        otpField.setBounds(120, 60, 150, 25);
        panel.add(otpField);

        JButton otprequest = new JButton("Request OTP");
        otprequest.setBounds(120, 100, 140, 25);
        panel.add(otprequest);

        otprequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = phoneNumber.getText();
                String apiKey = "y7RsRE7rF8P6tbdQblmJ5q05572uKtBpm4Yk0KQptNHWSFPwUDLmB06VDSDG";
                generatedOtp = generateOTP();
                otpTimestamp = System.currentTimeMillis();
                String message = "Your OTP is: " + generatedOtp;

                try {
                    if (sendOtp(message, userId, apiKey)) {
                        JOptionPane.showMessageDialog(f1, "OTP Sent Successfully");
                    } else {
                        JOptionPane.showMessageDialog(f1, "Failed to Send OTP");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f1, "Error: " + ex.getMessage());
                }
            }
        });

        JButton verify = new JButton("Verify OTP");
        verify.setBounds(120, 130, 140, 25);
        panel.add(verify);

        verify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredOtp = otpField.getText();
                if (isOtpValid()) {
                    if (generatedOtp.equals(enteredOtp)) {
                        JOptionPane.showMessageDialog(f1, "OTP Verified Successfully");
                    } else {
                        JOptionPane.showMessageDialog(f1, "Invalid OTP");
                    }
                } else {
                    JOptionPane.showMessageDialog(f1, "OTP has expired. Please request a new one.");
                }
            }
        });

        f1.add(panel);
        f1.setSize(400, 250);
        f1.setLocationRelativeTo(null);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1.setVisible(true);
    }

    // Method to generate a 6-digit OTP
    private static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }


    private static boolean sendOtp(String message, String userId, String apiKey) {
        try {
            String senderId = "FSTSMS";
            String language = "english";
            String route = "dlt";

            message = URLEncoder.encode(message, "UTF-8");
            String myUrl = "https://www.fast2sms.com/dev/bulk?authorization=" + apiKey +"&sender_id=" + senderId +"&message=" + message +"&language=" + language +"&route=" + route +"&numbers=" + userId;

            URL url = new URL(myUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("cache-control", "no-cache");

            int responseCode = con.getResponseCode();
            if (responseCode == 200) { 
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("Response: " + response);
                return true;
            } else {
                System.out.println("Error Response Code: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }


    private static boolean isOtpValid() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - otpTimestamp) <= 300000;
    }
}
