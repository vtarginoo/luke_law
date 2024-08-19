package br.lukelaw.mvp_luke_law.Legado.canais.whatsapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

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
       // Message message = Message.creator(
       //         new com.twilio.type.PhoneNumber("whatsapp:+5521978658787"), //
       //         new com.twilio.type.PhoneNumber(twilioPhoneNumber),bodyMessage) .create();

        Message message2 = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:+5521996800927"), //
                new com.twilio.type.PhoneNumber(twilioPhoneNumber),bodyMessage) .create();

        //System.out.println(message.getSid());
        System.out.println(message2.getSid());
    }
}


