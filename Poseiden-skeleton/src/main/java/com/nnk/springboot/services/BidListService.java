package com.nnk.springboot.services;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service class for {@link BidList}
 */
@Service
@Log4j2
public class BidListService {

    @Autowired
    private BidListRepository bidListRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * return the BidList with the id given in parameter.
     * 
     * @param bidListId the id of research bidlist
     * @return bidlist find by given id if it exists
     * @throws BidListNotFoundException if bidlist not exist
     */
    public BidList findById(int bidListId) {

        Optional<BidList> existedBidList = bidListRepository.findById(bidListId);

        if (existedBidList.isPresent()) {

            log.info(messageSource.getMessage("global.bidlist.find-by-id", new Object[] { bidListId, existedBidList },
                    LocaleContextHolder.getLocale()));
            return existedBidList.get();

        } else {

            throw new BidListNotFoundException(
                    messageSource.getMessage("global.exception.not-found", new Object[] { "bid" }, Locale.FRANCE));
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
     * save a {@link BidList} in db.
     *
     * @param bid the bidlist to save
     * @return the bidlist saved if it was correctly save
     * @throws BidListNotFoundException if bidList is null
     */
    public BidList saveBidList(BidList bid) {

        if (bid != null) {

            BidList savedBidList = bidListRepository.save(bid);
            log.info(messageSource.getMessage("global.bidlist.creation", new Object[] { savedBidList },
                    LocaleContextHolder.getLocale()));
            return savedBidList;

        } else {

            throw new BidListNotFoundException(messageSource.getMessage("global.exception.not-found",
                    new Object[] { "bid" }, LocaleContextHolder.getLocale()));
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

            if (existedBidList.isPresent() && Objects.equals(existedBidList.get().getBidListId(), bid.getBidListId())) {

                BidList savedUpdatedBidList = bidListRepository.save(bid);
                log.info(messageSource.getMessage("global.bidlist.update", new Object[] { savedUpdatedBidList },
                        LocaleContextHolder.getLocale()));
                return savedUpdatedBidList;

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
     * Delete a BidList given in parameter.
     *
     * @param bid the bid to delete.
     * @throws BidListNotFoundException if bid does not exist or if it is null
     */
    public void deleteBidList(BidList bid) {
        if (bid != null && bid.getBidListId() != 0) {

            Optional<BidList> existedBisList = bidListRepository.findById(bid.getBidListId());

            if (existedBisList.isPresent()) {

                log.info(messageSource.getMessage("global.bidlist.delete", new Object[] { existedBisList },
                        LocaleContextHolder.getLocale()));
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
