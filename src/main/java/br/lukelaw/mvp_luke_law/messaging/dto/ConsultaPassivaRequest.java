package br.lukelaw.mvp_luke_law.messaging.dto;

import br.lukelaw.mvp_luke_law.webscraping.validator.ProcessNumberValidator;
import br.lukelaw.mvp_luke_law.webscraping.validator.ValidProcessNumber;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConsultaPassivaRequest {

    @NotBlank(message = "O número do processo é obrigatório")
    @ValidProcessNumber
    private String numProcesso;

    @NotBlank(message = "O número de celular é obrigatório")
    private String celular;

    public String getNumProcesso() {
        return formatarNumeroProcesso(this.numProcesso);
    }

    private String formatarNumeroProcesso(String numProcesso) {
        if (numProcesso.matches(ProcessNumberValidator.FORMAT_WITHOUT_DASHES)) {
            // Converte o número do processo para o formato com traços e pontos
            return numProcesso.replaceAll("^(\\d{7})(\\d{2})(\\d{4})(\\d{1})(\\d{2})(\\d{4})$", "$1-$2.$3.$4.$5.$6");
        }
        return numProcesso; // Se já estiver no formato correto, retorna como está
    }
}