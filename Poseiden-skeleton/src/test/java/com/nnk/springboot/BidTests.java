package com.nnk.springboot;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BidTests {

	@Autowired
	private BidListRepository bidListRepository;

	private BidListService cut;

	@Test
	public void bidListTest() {
		BidList bid = new BidList("Account Test", "Type Test", 10d);

		// Save
		bid = bidListRepository.save(bid);
		Assert.assertNotNull(bid.getBidListId());
		Assert.assertEquals(bid.getBidQuantity(), 10d, 10d);

		// Update
		bid.setBidQuantity(20d);
		bid = bidListRepository.save(bid);
		Assert.assertEquals(bid.getBidQuantity(), 20d, 20d);

		// Find
		List<BidList> listResult = bidListRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = bid.getBidListId();
		bidListRepository.delete(bid);
		Optional<BidList> bidList = bidListRepository.findById(id);
		Assert.assertFalse(bidList.isPresent());
	}

	@Test
	public void bidListTest_whenNullOrNotExist() {

		// Save
		Assertions.assertThatThrownBy(() -> {
			cut.saveBidList(null);
		}).isInstanceOf(BidListNotFoundException.class).hasMessageContaining("is null");

		// Update
		Assertions.assertThatThrownBy(() -> {
			cut.updateBidList(null);
		}).isInstanceOf(BidListNotFoundException.class).hasMessageContaining("is null");

		// Delete
		Assertions.assertThatThrownBy(() -> {
			cut.deleteBidList(null);
		}).isInstanceOf(BidListNotFoundException.class).hasMessageContaining("is null");
	}

	@Test
	public void bidListTest_whenNotExist() {

		BidList bid = new BidList();
		// Save
		Assertions.assertThatThrownBy(() -> {
			cut.saveBidList(bid);
		}).isInstanceOf(BidListNotFoundException.class).hasMessageContaining("not found");

		// Update
		Assertions.assertThatThrownBy(() -> {
			cut.updateBidList(bid);
		}).isInstanceOf(BidListNotFoundException.class).hasMessageContaining("not found");

		// Delete
		Assertions.assertThatThrownBy(() -> {
			cut.deleteBidList(bid);
		}).isInstanceOf(BidListNotFoundException.class).hasMessageContaining("not found");
	}
}
