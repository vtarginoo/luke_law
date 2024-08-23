package br.lukelaw.mvp_luke_law.messaging.service;

import br.lukelaw.mvp_luke_law.messaging.exception.MessageSendFailureException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    MovimentoService movimentoService;


    public void envioWhatsapp(String advWpp, String bodyMessage) {

            Twilio.init(accountSid, authToken);

            System.out.println(advWpp);

            Message message1 = Message.creator(
                    new com.twilio.type.PhoneNumber(advWpp),
                    new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                    bodyMessage).create();

            System.out.println("Message SID: " + message1.getSid());

            Message fetchedMessage = Message.fetcher(message1.getSid()).fetch();
            System.out.println("Fetched Message Status: " + fetchedMessage.getStatus());

    }
}




