package com.xiaows.design.adapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/13 18:07
 * @version: v1.0
 */
@Slf4j
public class CustomerHandler extends BaseHandler {

	@Override
	public void onStart() {
		// super.onStart();
		log.info("customer start ...");
	}

	@Override
	public void handler() {
		// super.handler();
		log.info("customer handler ...");
		throw new RuntimeException("customer exception ...");
	}

}
