package br.lukelaw.mvp_luke_law.controller;


import br.lukelaw.mvp_luke_law.entity.Processo;
import br.lukelaw.mvp_luke_law.service.ProcessoService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/processos")
public class ProcessoController {

    @Autowired
    ProcessoService processoService;

    @GetMapping("/{numProcesso}")
    public ResponseEntity<String> capturarInfoProcesso (@PathVariable String numProcesso) {

        ResponseEntity<String> response = processoService.realizarRequisicao(numProcesso);
        System.out.println(response);

        return ResponseEntity.ok(response.getBody());

    }






}
