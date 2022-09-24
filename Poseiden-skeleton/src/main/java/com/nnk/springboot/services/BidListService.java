package com.nnk.springboot.services;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;

@Service
public class BidListService {

    @Autowired
    private BidListRepository bidListRepository;

    @Autowired
    private MessageSource messageSource;

    public BidList saveBidList(BidList bid) {
        if (bid != null) {
            return bidListRepository.save(bid);
        } else {
            throw new BidListNotFoundException(
                    messageSource.getMessage("global.exception.not-found", new Object[] { "bid" }, Locale.FRANCE));
        }

    }

    public BidList updateBidList(BidList bid) {
        if (bid != null && bid.getBidListId() != 0) {

            Optional<BidList> existedBidList = bidListRepository.findById(bid.getBidListId());
            
            if (existedBidList.isPresent()) {

                // TODO save field of bid

            } else {
                throw new BidListNotFoundException(
                        messageSource.getMessage("global.exception.not-found", new Object[] { "bid" }, Locale.FRANCE));
            }

        } else {
            throw new BidListNotFoundException(
                    messageSource.getMessage("global.exception.not-null", new Object[] { "bid" }, Locale.FRANCE));
        }
        return bid;
    }
}
