package com.inovagab.backend.service;

import com.inovagab.backend.dto.DashboardResumo;
import com.inovagab.backend.model.Projeto;
import com.inovagab.backend.model.StatusProjeto;
import com.inovagab.backend.repository.ProjetoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final ProjetoRepository projetoRepository;

    public DashboardService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    public DashboardResumo gerarResumo() {
        return montarResumo(projetoRepository.findAll());
    }

    public DashboardResumo gerarResumoPorEstrategia(String estrategiaId) {
        return montarResumo(projetoRepository.findByEstrategiaId(estrategiaId));
    }

    private DashboardResumo montarResumo(List<Projeto> projetos) {

        long totalProjetos = projetos.size();

        double totalInvestimento = projetos.stream()
                .mapToDouble(p -> p.getInvestimento() != null ? p.getInvestimento() : 0)
                .sum();

        double totalRetornoFinanceiro = projetos.stream()
                .mapToDouble(p -> p.getRetornoFinanceiro() != null ? p.getRetornoFinanceiro() : 0)
                .sum();

        double lucro = totalRetornoFinanceiro - totalInvestimento;

        double roi = totalInvestimento > 0
                ? (lucro / totalInvestimento) * 100
                : 0;

        double progressoMedio = projetos.stream()
                .mapToDouble(p -> p.getProgresso() != null ? p.getProgresso() : 0)
                .average()
                .orElse(0);

        double ganhoProdutividade = projetos.stream()
                .mapToDouble(p -> p.getGanhoProdutividade() != null ? p.getGanhoProdutividade() : 0)
                .sum();

        double reducaoCustos = projetos.stream()
                .mapToDouble(p -> p.getReducaoCustos() != null ? p.getReducaoCustos() : 0)
                .sum();

        // Status dos Projetos: contagem por status (todos os status aparecem, mesmo com zero)
        Map<String, Long> projetosPorStatus = new LinkedHashMap<>();
        for (StatusProjeto status : StatusProjeto.values()) {
            projetosPorStatus.put(status.name(), 0L);
        }
        for (Projeto projeto : projetos) {
            if (projeto.getStatus() != null) {
                projetosPorStatus.merge(projeto.getStatus().name(), 1L, Long::sum);
            }
        }

        // Prazo dos Projetos: atrasado = prazo vencido e projeto não concluído
        LocalDate hoje = LocalDate.now();
        long projetosAtrasados = projetos.stream()
                .filter(p -> p.getPrazo() != null
                        && p.getPrazo().isBefore(hoje)
                        && p.getStatus() != StatusProjeto.CONCLUIDO)
                .count();
        long projetosNoPrazo = totalProjetos - projetosAtrasados;

        return new DashboardResumo(
                totalProjetos,
                totalInvestimento,
                totalRetornoFinanceiro,
                lucro,
                roi,
                progressoMedio,
                ganhoProdutividade,
                reducaoCustos,
                projetosPorStatus,
                projetosNoPrazo,
                projetosAtrasados
        );
    }
}
