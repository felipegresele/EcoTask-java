package com.example.demo.service.ia;
import com.example.demo.domain.model.dto.gemini.PlanRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeminiPlanoService {

    private final WebClient webClient;

    public GeminiPlanoService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getSustainabilityPlan(String context, String goal) {

        // Usa a classe DTO externa
        PlanRequest requestBody = new PlanRequest(context, goal);

        try {
            // Adicionamos o .header("Content-Type", "application/json") como garantia,
            // embora o bodyValue já o defina na maioria dos casos.
            return webClient.post()
                    .uri("/api/generate-plan")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(PlanResponse.class) // Mapeia o JSON para o Record PlanResponse
                    .map(PlanResponse::plan)        // Extrai o valor do campo 'plan' do Record
                    .block();

        } catch (Exception e) {
            // O log de erro agora será mais claro se for um problema de comunicação real
            System.err.println("Erro ao chamar a API Flask: " + e.getMessage());
            return "Falha na comunicação com o serviço de IA.";
        }
    }
}