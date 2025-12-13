package service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private static final String EMAIL = "your.email@gmail.com";       // ← غيرها
    private static final String PASSWORD = "your-app-password";       // ← غيرها (مش الباسوورد العادي!)

    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });
    }

    public static void sendAppointmentConfirmation(
            String toEmail,
            String patientName,
            String clinicName,
            String doctorName,
            String date,
            String time,
            double price,
            String address
    ) {
        try {
            Session session = getSession();
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("✅ تأكيد حجز موعد — " + clinicName);

            // ترميز العنوان لـ Google Maps
            String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8")
                    .replace("+", "%20");

            String url = "https://www.google.com/maps/search/?api=1&query=" + encodedAddress;

            String body = """
            <html dir="rtl">
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 10px; background: #f9f9f9;">
                    <h2 style="color: #15BF8F; text-align: center;">🎉 تم حجز موعدك بنجاح!</h2>
                    <p>مرحبًا <strong>%s</strong>،</p>
                    <p>نشكرك على ثقتك في <strong>%s</strong>.</p>
                    <hr>
                    <h3>تفاصيل الموعد:</h3>
                    <ul>
                        <li><strong>العيادة:</strong> %s</li>
                        <li><strong>الطبيب:</strong> د. %s</li>
                        <li><strong>التاريخ:</strong> %s</li>
                        <li><strong>الوقت:</strong> %s</li>
                        <li><strong>السعر:</strong> %.2f ج.م</li>
                        <li><strong>العنوان:</strong> %s</li>
                    </ul>
                    <p>
                        <a href="%s" 
                           style="display: inline-block; background: #15BF8F; color: white; text-decoration: none; padding: 10px 20px; border-radius: 5px; font-weight: bold;">
                            📍 افتح الموقع في خريطة جوجل
                        </a>
                    </p>
                    <hr>
                    <p style="font-size: 12px; color: #777;">
                        هذا الإيميل أُرسل تلقائيًا. يُرجى عدم الرد عليه.<br>
                        لو لم تطلب هذا الحجز، يُرجى تجاهله أو التواصل مع الدعم.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(
                    patientName,
                    clinicName,
                    clinicName,
                    doctorName,
                    date,
                    time,
                    price,
                    address,
                    url
            );

            message.setContent(body, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("✅ إيميل تأكيدي أُرسل إلى: " + toEmail);

        } catch (Exception e) {
            System.err.println("❌ فشل إرسال الإيميل: " + e.getMessage());
            e.printStackTrace();
        }
    }
}