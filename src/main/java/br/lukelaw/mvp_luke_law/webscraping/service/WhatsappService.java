package br.lukelaw.mvp_luke_law.webscraping.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsappService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String twilioPhoneNumber;

    public void notificacaoWhatsapp (String bodyMessage){

        Twilio.init(accountSid, authToken);

        Message message1 = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:+5521996066505"),
                new com.twilio.type.PhoneNumber(twilioPhoneNumber),bodyMessage).create();

        Message message2 = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:+5521996800927"),
                new com.twilio.type.PhoneNumber(twilioPhoneNumber),bodyMessage).create();

        System.out.println(message1.getSid());
        System.out.println(message2.getSid());

        Message fetchedMessage = Message.fetcher(message1.getSid()).fetch();
        System.out.println("Fetched Message Status: " + fetchedMessage.getStatus());

        Message fetchedMessage2 = Message.fetcher(message2.getSid()).fetch();
        System.out.println("Fetched Message Status: " + fetchedMessage2.getStatus());


    }
}


