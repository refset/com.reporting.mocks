package com.reporting.mocks.configuration.defaults;

import com.reporting.mocks.configuration.*;
import com.reporting.mocks.model.PricingGroup;
import com.reporting.mocks.model.risks.IntradayRiskType;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.TradeType;

import java.util.*;

public class FXForwardsDeskDefaultPricingGroupConfig extends PricingGroupConfig {
    protected final int startingTradeCount = 200000;
    protected final int newTradeStart = 0;
    protected final int newTradePeriodicity = 1000;        // number of milliseconds between new tcnTrades (default: 10s)

    protected final int modifiedTradeStart = 0;
    protected final int modifiedTradePeriodicity = 60 * 1000;    // number of milliseconds between trade modifications (default: 30s)
    protected final boolean modifyPostReverse = false;           // a modify even does not create two values
    protected final boolean riskOnDelete = false;


    protected final int deleteTadeStart = 120 * 1000;
    protected final int deleteTradePeriodicity = 120 * 1000;

    protected final int eodMarketPeridocity = 20 * 60 * 60 * 1000;
    protected final int intradayMarketPeriodicity = 15 * 60 * 1000;

    public FXForwardsDeskDefaultPricingGroupConfig() {
        ArrayList<String> books = new ArrayList<>();
        UnderlyingConfig underlyings = new UnderlyingConfig();
        Map<TradeType, List<RiskType>> tradeTypeAndRisks = new HashMap<>();

        this.pricingGroupId = new PricingGroup("fxswapdesk");
        // - URN: book:<department pricingGroup>:<desk pricingGroup>:<book pricingGroup>
        //   e.g., book:fxdesk:fxspots:bookname
        books.add("bank:macro:fx:forwards:hedge");
        books.add("bank:macro:fx:forwards:stirt");
        books.add("bank:macro:fx:forwards:g10");
        books.add("bank:macro:fx:forwards:longdated");

        underlyings.addSet("EUR", Arrays.asList("USD", "CHF", "GBP", "MXN", "JPY", "AUD", "RBL"));
        underlyings.addSet("USD", Arrays.asList("CHF", "GBP", "MXN", "JPY", "AUD", "RBL"));

        tradeTypeAndRisks.put(TradeType.Payment, Arrays.asList(RiskType.PV));
        tradeTypeAndRisks.put(TradeType.Spot, Arrays.asList(RiskType.PV, RiskType.DELTA));
        tradeTypeAndRisks.put(TradeType.Forward, Arrays.asList(RiskType.PV, RiskType.DELTA));
        tradeTypeAndRisks.put(TradeType.Swap, Arrays.asList(RiskType.PV, RiskType.DELTA));
        tradeTypeAndRisks.put(TradeType.VanillaOption, Arrays.asList(RiskType.PV, RiskType.DELTA, RiskType.GAMMA, RiskType.VEGA));

        this.tradeConfig = new TradeConfig(books, underlyings, tradeTypeAndRisks);
        this.tradeConfig.setStartingTradeCount(startingTradeCount);
        this.tradeConfig.setNewTradeStart(newTradeStart);
        this.tradeConfig.setNewTradePeriodicity(newTradePeriodicity);
        this.tradeConfig.setModifiedTradeStart(modifiedTradeStart);
        this.tradeConfig.setModifiedTradePeriodicity(modifiedTradePeriodicity);
        this.tradeConfig.setDeleteTadeStart(deleteTadeStart);
        this.tradeConfig.setDeleteTradePeriodicity(deleteTradePeriodicity);

        ArrayList<RiskType> eodr = new ArrayList<>();
        eodr.add(RiskType.PV);
        eodr.add(RiskType.DELTA);
        eodr.add(RiskType.GAMMA);
        this.endofdayConfig = new EndofDayConfig(eodr, eodMarketPeridocity);

        ArrayList<IntradayRiskType> indr = new ArrayList<>();
        indr.add(new IntradayRiskType(RiskType.PV, 1));
        indr.add(new IntradayRiskType(RiskType.DELTA, 3));
        indr.add(new IntradayRiskType(RiskType.GAMMA, 3));
        this.intradayConfig = new IntradayConfig(indr, intradayMarketPeriodicity, modifyPostReverse, riskOnDelete);
    }}
