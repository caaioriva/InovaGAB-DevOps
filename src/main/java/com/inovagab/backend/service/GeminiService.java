package com.inovagab.backend.service;

import com.inovagab.backend.dto.AnaliseIaResponse;
import com.inovagab.backend.exception.RegraNegocioException;
import com.inovagab.backend.model.Ideia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Integração simples com a API Gemini (Google Generative Language).
 * Recebe uma ideia e devolve uma sugestão de prioridade + justificativa.
 * Requer a variável de ambiente GEMINI_API_KEY.
 */
@Service
public class GeminiService {

    private static final String BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/";

    private final RestClient restClient = RestClient.create();

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    public AnaliseIaResponse analisarIdeia(Ideia ideia) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new RegraNegocioException(
                    "IA não configurada: defina a variável de ambiente GEMINI_API_KEY.");
        }

        String prompt = montarPrompt(ideia);

        Map<String, Object> corpo = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        String url = BASE_URL + model + ":generateContent?key=" + apiKey;

        Map<String, Object> resposta;
        try {
            resposta = restClient.post()
                    .uri(url)
                    .body(corpo)
                    .retrieve()
                    .body(Map.class);
        } catch (Exception exception) {
            throw new RegraNegocioException(
                    "Falha ao consultar a IA (Gemini): " + exception.getMessage());
        }

        return interpretar(extrairTexto(resposta));
    }

    private String montarPrompt(Ideia ideia) {
        return """
                Você é um analista de inovação corporativa do Grupo Águia Branca.
                Analise a ideia abaixo considerando impacto potencial, viabilidade
                e alinhamento estratégico.

                Título: %s
                Descrição: %s

                Responda EXATAMENTE neste formato, sem markdown:
                PONTUACAO: <número inteiro de 0 a 100>
                PRIORIDADE: <ALTA|MEDIA|BAIXA>
                JUSTIFICATIVA: <no máximo 2 frases>
                """.formatted(
                ideia.getTitulo() != null ? ideia.getTitulo() : "",
                ideia.getDescricao() != null ? ideia.getDescricao() : ""
        );
    }

    @SuppressWarnings("unchecked")
    private String extrairTexto(Map<String, Object> resposta) {
        try {
            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) resposta.get("candidates");
            Map<String, Object> content =
                    (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception exception) {
            throw new RegraNegocioException("Resposta inesperada da IA (Gemini).");
        }
    }

    private AnaliseIaResponse interpretar(String texto) {

        int pontuacao = 50;
        String prioridade = "MEDIA";
        String justificativa = texto != null ? texto.trim() : "";

        if (texto != null) {
            for (String linha : texto.split("\\r?\\n")) {
                String atual = linha.trim();
                String maiuscula = atual.toUpperCase();

                if (maiuscula.startsWith("PONTUACAO:") || maiuscula.startsWith("PONTUAÇÃO:")) {
                    String valor = atual.substring(atual.indexOf(':') + 1).replaceAll("[^0-9]", "");
                    if (!valor.isBlank()) {
                        try {
                            pontuacao = Math.max(0, Math.min(100, Integer.parseInt(valor)));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                } else if (maiuscula.startsWith("PRIORIDADE:")) {
                    String valor = atual.substring(atual.indexOf(':') + 1).trim().toUpperCase();
                    if (valor.contains("ALTA")) {
                        prioridade = "ALTA";
                    } else if (valor.contains("BAIXA")) {
                        prioridade = "BAIXA";
                    } else {
                        prioridade = "MEDIA";
                    }
                } else if (maiuscula.startsWith("JUSTIFICATIVA:")) {
                    justificativa = atual.substring(atual.indexOf(':') + 1).trim();
                }
            }
        }

        return new AnaliseIaResponse(pontuacao, prioridade, justificativa);
    }
}
