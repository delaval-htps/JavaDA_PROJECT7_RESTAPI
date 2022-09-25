package com.nnk.springboot.services;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;

/**
 * Service class for {@link BidList}
 */
@Service
public class BidListService {

    @Autowired
    private BidListRepository bidListRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * save a {@link BidList} in db.
     * 
     * @param bid the bidlist to save
     * @return the bidlist saved if it was correctly save
     * @throws BidListNotFoundException if bidList is null
     */
    public BidList saveBidList(BidList bid) {
        if (bid != null) {
            return bidListRepository.save(bid);
        } else {
            throw new BidListNotFoundException(
                    messageSource.getMessage("global.exception.not-found", new Object[] { "bid" }, Locale.FRANCE));
        }

    }

    /**
     * update a Bidlist given in parameter.
     * 
     * @param bid the bidlist updated
     * @return the updated existed bidlist according to bid given in parameter
     * @throws BidListNotFoundException if bidlist is null or not existant
     */
    public BidList updateBidList(BidList bid) {

        if (bid != null && bid.getBidListId() != 0) {

            Optional<BidList> existedBidList = bidListRepository.findById(bid.getBidListId());

            if (existedBidList.isPresent()) {

                return bidListRepository.save(bid);

            } else {
                throw new BidListNotFoundException(
                        messageSource.getMessage("global.exception.not-found", new Object[] { "bid" }, Locale.FRANCE));
            }

        } else {
            throw new BidListNotFoundException(
                    messageSource.getMessage("global.exception.not-null", new Object[] { "bid" }, Locale.FRANCE));
        }
    }

    /**
     * Retrieve all bidlist in db.
     * 
     * @return List of all registred BidList(empty if there were not)
     */
    public List<BidList> findBidLists() {
        return bidListRepository.findAll();
    }

    /**
     * Delete a BidList given in parameter.
     * 
     * @param bid the bid to delete.
     * @throws BidListNotFoundException if bid does not exist or if it is null
     */
    public void deleteBidList(BidList bid) {
        if (bid != null && bid.getBidListId() != 0) {
            Optional<BidList> existedBisList = bidListRepository.findById(bid.getBidListId());

            if (existedBisList.isPresent()) {
                bidListRepository.delete(existedBisList.get());
            } else {
                throw new BidListNotFoundException(
                        messageSource.getMessage("global.exception.not-found", new Object[] { "bid" }, Locale.FRANCE));
            }
        } else {
            throw new BidListNotFoundException(
                    messageSource.getMessage("global.exception.not-null", new Object[] { "bid" }, Locale.FRANCE));
        }
    }
}
