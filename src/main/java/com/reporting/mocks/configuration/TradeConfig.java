package com.reporting.mocks.configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.TradeType;

public class TradeConfig {
    protected int startingTradeCount = 100;
    protected int newTradeStart = 0;
    protected int newTradePeriodicity = 1000;        // number of milliseconds between new tcnTrades (default: 10s)

    protected int modifiedTradeStart = 60 * 1000;
    protected int modifiedTradePeriodicity = 60 * 1000;    // number of milliseconds between trade modifications (default: 30s)

    protected int deleteTadeStart = 120 * 1000;
    protected int deleteTradePeriodicity = 120 * 1000;     // number of milliseconds between deletion of tcnTrades (default: 60s)

    List<String> books;
    UnderlyingConfig underlyings;
    Map<TradeType, List<RiskType>> tradeTypesAndRisks;           // defines list of trade types and their risks

    public TradeConfig() {
    }

    public TradeConfig(List<String> books, UnderlyingConfig underlyings, Map<TradeType,List<RiskType>> tradeTypesAndRisks) {
        this.books = books;
        this.underlyings = underlyings;
        this.tradeTypesAndRisks = tradeTypesAndRisks;
    }

    public int getStartingTradeCount() {
        return startingTradeCount;
    }

    public void setStartingTradeCount(int startingTradeCount) {
        this.startingTradeCount = startingTradeCount;
    }

    public List<String> getBooks() {
        return books;
    }

    public UnderlyingConfig getUnderlyings() {
        return underlyings;
    }

    public int getNewTradePeriodicity() {
        return newTradePeriodicity;
    }

    public int getModifiedTradePeriodicity() {
        return modifiedTradePeriodicity;
    }

    public int getDeleteTradePeriodicity() {
        return deleteTradePeriodicity;
    }

    public int getNewTradeStart() {
        return newTradeStart;
    }

    public int getModifiedTradeStart() {
        return modifiedTradeStart;
    }

    public int getDeleteTadeStart() {
        return deleteTadeStart;
    }

    public List<TradeType> findAllTradeTypes() {
        return this.tradeTypesAndRisks.keySet().stream().collect(Collectors.toList());
    }

    public void setNewTradeStart(int newTradeStart) {
        this.newTradeStart = newTradeStart;
    }

    public void setNewTradePeriodicity(int newTradePeriodicity) {
        this.newTradePeriodicity = newTradePeriodicity;
    }

    public void setModifiedTradeStart(int modifiedTradeStart) {
        this.modifiedTradeStart = modifiedTradeStart;
    }

    public void setModifiedTradePeriodicity(int modifiedTradePeriodicity) {
        this.modifiedTradePeriodicity = modifiedTradePeriodicity;
    }

    public void setDeleteTadeStart(int deleteTadeStart) {
        this.deleteTadeStart = deleteTadeStart;
    }

    public void setDeleteTradePeriodicity(int deleteTradePeriodicity) {
        this.deleteTradePeriodicity = deleteTradePeriodicity;
    }

    public List<RiskType> findRiskByTradeType(TradeType tradeType) {
        if (this.tradeTypesAndRisks.containsKey(tradeType)) {
            return this.tradeTypesAndRisks.get(tradeType);
        }
        else {
            return null;
        }
    }

    public Map<TradeType, List<RiskType>> getTradeTypesAndRisks() {
        return tradeTypesAndRisks;
    }
}
