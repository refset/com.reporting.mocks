package com.reporting.mocks.process.risks;

import com.reporting.mocks.configuration.PricingGroupConfig;
import com.reporting.mocks.generators.IRiskGeneratorLite;
import com.reporting.mocks.generators.RiskGeneratorFactory;
import com.reporting.mocks.generators.process.streaming.RiskStreamMessage;
import com.reporting.mocks.interfaces.persistence.ICalculationContextStore;
import com.reporting.mocks.interfaces.persistence.ITradeStore;
import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.model.CalculationContext;
import com.reporting.mocks.model.TradePopulation;
import com.reporting.mocks.model.id.MarketEnvId;
import com.reporting.mocks.model.id.RiskRunId;
import com.reporting.mocks.model.risks.Risk;
import com.reporting.mocks.model.risks.RiskType;
import com.reporting.mocks.model.trade.Trade;
import com.reporting.mocks.model.trade.TradeType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradePopulationRiskProducerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(TradePopulationRiskProducerThread.class.getName());
    protected BlockingQueue<TradePopulationRiskRunRequest> tradePopulationRiskRunRequests;
    protected BlockingQueue<RiskStreamMessage<? extends Risk>> riskResultPubisherQueue;
    protected ICalculationContextStore calculationContextStore;
    protected ITradeStore tradeStore;
    protected IResultPublisher resultPublisher;
    protected PricingGroupConfig pricingGroupConfig;


    public TradePopulationRiskProducerThread(BlockingQueue<TradePopulationRiskRunRequest> riskRunRequestQueue,
                                             BlockingQueue<RiskStreamMessage<? extends Risk>> riskResultPubisherQueue,
                                             PricingGroupConfig pricingGroupConfig,
                                             ICalculationContextStore calculationContextStore,
                                             ITradeStore tradeStore,
                                             IResultPublisher resultPublisher) {
        this.tradePopulationRiskRunRequests = riskRunRequestQueue;
        this.riskResultPubisherQueue = riskResultPubisherQueue;
        this.calculationContextStore = calculationContextStore;
        this.tradeStore = tradeStore;
        this.resultPublisher = resultPublisher;
        this.pricingGroupConfig = pricingGroupConfig;
    }


    @Override
    public void run() {
        try {
            while (true) {
                TradePopulationRiskRunRequest tradePopulationRiskRunRequest = this.tradePopulationRiskRunRequests.take();

                CalculationContext calculationContext = this.calculationContextStore.getCalculationContextById(tradePopulationRiskRunRequest.getCalculationId());
                TradePopulation tradePopulation = this.tradeStore.getTradePopulationById(tradePopulationRiskRunRequest.getTradePopulationId());
                Map<TradeType, List<Trade>> tradeTypeToTradeMapping = tradePopulation.tradeTypeToTradeMapping();
                List<RiskType> risksToRun = tradePopulationRiskRunRequest.getRisksToRun();

                int riskCount = 0;
                for(Map.Entry<TradeType, List<Trade>> entry : tradeTypeToTradeMapping.entrySet()) {
                    if (!entry.getValue().isEmpty()) {
                        int tradeCount = entry.getValue().size();
                        TradeType tradeType = entry.getKey();
                        List<RiskType> tradeRisks = this.pricingGroupConfig.getTradeConfig().findRiskByTradeType(tradeType);
                        for(RiskType riskType : tradeRisks) {
                            if (risksToRun.contains(riskType)) {
                                riskCount += tradeCount;
                            }
                        }
                    }
                }

                RiskRunId riskRunId = new RiskRunId(this.pricingGroupConfig.getPricingGroupId().getName());
                int riskNo = 0;
                for(TradeType tradeType : tradePopulation.getAllTradeTypes()) {
                    List<RiskType> tradeRisks = this.pricingGroupConfig.getTradeConfig().findRiskByTradeType(tradeType);
                    for(RiskType riskType : tradeRisks) {
                        if (risksToRun.contains(riskType)) {
                            IRiskGeneratorLite<? extends Risk> riskGenerator = RiskGeneratorFactory.getGeneratorLite(riskType);
                            MarketEnvId marketEnvId = calculationContext.get(riskType);
                            for(Trade trade : tradePopulation.getByTradeType(tradeType)) {
                                riskNo++;
                                Risk risk = riskGenerator.generate(marketEnvId, trade);
                                RiskStreamMessage<? extends Risk> riskStreamMsg = new RiskStreamMessage<>(
                                        calculationContext.getCalculationContextId(),
                                        riskRunId,
                                        RiskRunType.Intraday,
                                        riskCount,
                                        riskNo,
                                        risk,
                                        false);

                                this.riskResultPubisherQueue.add(riskStreamMsg);
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            // LOGGER.log( Level.FINE, "processing {0} entries in loop", list.size() );
            LOGGER.log(Level.FINE, "thread interrupted");
        }
    }
}
