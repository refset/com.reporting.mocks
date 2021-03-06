package com.reporting.mocks.generators.Risk;

import com.reporting.mocks.generators.IRiskGeneratorLite;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.risks.Sega;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

public class SegaGenerator implements IRiskGeneratorLite<Sega> {
    @Override
    public RiskType getRiskType() { return RiskType.SEGA; }

    @Override
    public Sega generate(MarketEnvId marketEnvId, Trade trade) {
        return null;
    }

    @Override
    public int calcTimeEstimate(TradeType tradeType) {
        return 0;
    }
}
