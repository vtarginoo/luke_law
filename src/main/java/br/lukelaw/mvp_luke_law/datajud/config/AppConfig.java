package br.lukelaw.mvp_luke_law.datajud.config;

import br.lukelaw.mvp_luke_law.datajud.deserializer.MovimentoDeserializer;
import br.lukelaw.mvp_luke_law.datajud.deserializer.ProcessoDeserializer;
import br.lukelaw.mvp_luke_law.datajud.entity.Movimento;
import br.lukelaw.mvp_luke_law.datajud.entity.Processo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Registrar o módulo para suporte a tipos de data/hora Java 8
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // Registrar seus deserializers personalizados
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Processo.class, new ProcessoDeserializer());
        module.addDeserializer(Movimento.class, new MovimentoDeserializer());


        objectMapper.registerModule(module);

        return objectMapper;
    }
}