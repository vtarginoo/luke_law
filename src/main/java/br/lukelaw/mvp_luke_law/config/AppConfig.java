package br.lukelaw.mvp_luke_law.config;

import br.lukelaw.mvp_luke_law.deserializer.ProcessoDeserializer;
import br.lukelaw.mvp_luke_law.entity.Processo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Processo.class, new ProcessoDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}