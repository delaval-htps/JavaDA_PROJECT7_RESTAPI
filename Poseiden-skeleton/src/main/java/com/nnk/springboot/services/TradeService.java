package com.nnk.springboot.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private MessageSource messageSource;

    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    public Trade findById(Integer id) {

        Optional<Trade> existingTrade = tradeRepository.findById(id);
        if (existingTrade.isPresent()) {
            log.info(messageSource.getMessage("global.trade.find-by-id", new Object[] { id, existingTrade.get() }, LocaleContextHolder.getLocale()));
            return existingTrade.get();
        } else {
            throw new TradeNotFoundException(messageSource.getMessage("global.trade.not-found", new Object[] { id }, LocaleContextHolder.getLocale()));
        }
    }

    public Trade saveTrade(Trade trade) {
        if (trade != null) {
            Trade savedTrade = tradeRepository.save(trade);
            log.info(messageSource.getMessage("global.trade.creation", new Object[] { savedTrade }, LocaleContextHolder.getLocale()));
            return savedTrade;

        } else {
            throw new TradeNotFoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "trade" }, LocaleContextHolder.getLocale()));
        }
    }

    public Trade updateTrade(Trade trade) {

        if (trade != null && trade.getTradeId() != 0) {

            Optional<Trade> existingTrade = tradeRepository.findById(trade.getTradeId());

            if (existingTrade.isPresent() && Objects.equals(trade.getTradeId(), existingTrade.get().getTradeId())) {

                Trade updatedTrade = tradeRepository.save(trade);

                log.info(messageSource.getMessage("global.trade.update", new Object[] { updatedTrade }, LocaleContextHolder.getLocale()));

                return updatedTrade;

            } else {
                throw new TradeNotFoundException(messageSource.getMessage("global.trade.not-found", new Object[] { trade.getTradeId() }, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new TradeNotFoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "trade" }, LocaleContextHolder.getLocale()));
        }

    }
   
    public void deleteTrade(Trade trade) {
       if (trade != null && trade.getTradeId() != 0) {

           Optional<Trade> existedTrade = tradeRepository.findById(trade.getTradeId());

           if (existedTrade.isPresent()) {

               log.info(messageSource.getMessage("global.trade.delete", new Object[] { existedTrade }, LocaleContextHolder.getLocale()));

               tradeRepository.delete(existedTrade.get());

           } else {
               throw new TradeNotFoundException(messageSource.getMessage("global.trade.not-found", new Object[] { trade.getTradeId() }, LocaleContextHolder.getLocale()));
           }

       } else {
           throw new TradeNotFoundException(messageSource.getMessage("global.exception.not-null", new Object[] { "Trade" }, LocaleContextHolder.getLocale()));
       }
   }


}
