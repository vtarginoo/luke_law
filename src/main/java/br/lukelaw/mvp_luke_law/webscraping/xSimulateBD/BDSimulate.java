package br.lukelaw.mvp_luke_law.webscraping.xSimulateBD;


import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BDSimulate {

    // Simulando o banco de dados de usuários cadastrados
    public List<User> usuarios;

    // Simulando a associação de processos aos IDs dos usuários
    public Map<String, List<Integer>> processosAssociados;

    public BDSimulate() {
        this.usuarios = new ArrayList<>();
        this.processosAssociados = new HashMap<>();

        // Mockando alguns usuários
        usuarios.add(new User(1, "Youssef Y.", "+5521996800927"));
        usuarios.add(new User(2, "Victor M.", "+5521996800927"));
        usuarios.add(new User(3, "Saul Goodman", "+5521996800927"));

        // Mockando algumas associações de processos aos IDs dos usuários
        processosAssociados.put("0838717-06.2024.8.19.0001", List.of(1, 2));
        processosAssociados.put("0809129-51.2024.8.19.0001", List.of(3, 2));
        //processosAssociados.put("0947617-20.2023.8.19.0001", List.of(1, 3));
        //processosAssociados.put("0938160-61.2023.8.19.0001", List.of(1, 2));
    }
}
