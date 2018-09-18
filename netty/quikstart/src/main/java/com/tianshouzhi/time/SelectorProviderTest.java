package com.tianshouzhi.time;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;

/**
 * Created by tianshouzhi on 2018/5/5.
 */
public class SelectorProviderTest {
	public static void main(String[] args) throws IOException {
		SelectorProvider provider = SelectorProvider.provider();
		Selector selector1 = provider.openSelector();
		Selector selector2 = provider.openSelector();
        System.out.println(selector1);
        System.out.println(selector2);

        if(null instanceof Exception){
            System.out.println("SelectorProviderTest.main");
        }
    }
}
