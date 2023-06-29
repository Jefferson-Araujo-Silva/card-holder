package cardholder.api;

import cardholder.api.analysisdto.CreditAnalysisDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "analysis", url = "${url.analysis.host}")
public interface CardHolderApiAnalysis {
    @GetMapping(path = "/{id}")
    List<CreditAnalysisDto> getCreditAnalysis(@PathVariable(value = "id") UUID id);

    @GetMapping(path = "?cpf={cpf}")
    List<CreditAnalysisDto> getCreditAnalysisByCpf(@PathVariable(value = "cpf") String cpf);

}

