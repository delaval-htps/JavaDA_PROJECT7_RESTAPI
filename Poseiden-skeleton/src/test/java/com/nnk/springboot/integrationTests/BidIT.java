package com.nnk.springboot.integrationTests;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public class BidIT {

	@Autowired
	private BidListRepository bidListRepository;


	@Test
	
	public void bidListTest() {
		BidList bid = new BidList("Account Test", "Type Test", 10.0);

		// Save
		bid = bidListRepository.save(bid);
		Assert.assertNotNull(bid.getBidListId());
		Assert.assertEquals(10.0, bid.getBidQuantity(), 10.0);

		// Update
		bid.setBidQuantity(10.0);
		bid = bidListRepository.save(bid);
		Assert.assertEquals(10.0, bid.getBidQuantity(), 10.0);

		// Find
		List<BidList> listResult = bidListRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = bid.getBidListId();
		bidListRepository.delete(bid);
		Optional<BidList> bidList = bidListRepository.findById(id);
		Assert.assertFalse(bidList.isPresent());
	}
}
