package com.inovagab.backend.dto;

import java.util.Map;

public record DashboardResumo(
        long totalProjetos,
        double totalInvestimento,
        double totalRetornoFinanceiro,
        double lucro,
        double roi,
        double progressoMedio,
        double ganhoProdutividade,
        double reducaoCustos,
        Map<String, Long> projetosPorStatus,
        long projetosNoPrazo,
        long projetosAtrasados
) {
}
