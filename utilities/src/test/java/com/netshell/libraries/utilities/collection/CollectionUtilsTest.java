package com.netshell.libraries.utilities.collection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Abhishek
 * on 7/31/2016.
 */
public class CollectionUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionUtilsTest.class);

    private static final List<Integer> INTEGER_LIST = Arrays.asList(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19
    );

    @Test
    public void testSplitList() throws Exception {
        CollectionUtils.splitListStream(INTEGER_LIST, 13).forEach(l -> LOGGER.info(l.toString()));
    }

    @Test
    public void testBatchStream() throws Exception {
        CollectionUtils.batchStream(INTEGER_LIST, 11).forEach(l -> LOGGER.info(l.toString()));
    }
}