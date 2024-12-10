package ca.gbc.notificationservice.service;

import ca.gbc.notificationservice.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender javaMailSender;


    @KafkaListener(topics="order-placed")
    public void listen(OrderPlacedEvent orderPlacedEvent){

        log.info("Received message from order-placed topic {}", orderPlacedEvent);

        // Send email to customer
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("aumzaveri06@gmail.com");
            mimeMessageHelper.setTo(orderPlacedEvent.getEmail());
            mimeMessageHelper.setSubject(String.format("Your Order (%s) was palced successfully", orderPlacedEvent.getOrderNumber()));
            mimeMessageHelper.setText(String.format("""
                    Good Day.
                    
                    Your order with order number %s was successfully placed
                    
                    Thank you for your business
                    COMP3095 Staff
                    """, orderPlacedEvent.getOrderNumber()));
        };
        try{
            javaMailSender.send(messagePreparator);
            log.info("Order notification successfully sent!!");
        }catch (MailException e){
            log.error("Exception occurred when sending email",e);
            throw new RuntimeException("Exception occurred when sending email",e);
        }
    };
}
