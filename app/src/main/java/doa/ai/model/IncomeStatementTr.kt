package doa.ai.model

import java.math.BigDecimal

data class IncomeStatementTr(
        val bold: Boolean,
        val label: String,
        val td: List<BigDecimal>
)

data class CashFlowTrX(
        val bold: Boolean,
        val label: String,
        val td: List<BigDecimal>
)