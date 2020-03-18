package com.xiaows.design.adapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/13 18:06
 * @version: v1.0
 */
@Slf4j
public abstract class BaseHandler implements Handler {
	public BaseHandler() {
		this.onStart();
	}

	@Override
	public void onStart() {
		log.info("base on start ...");
	}

	@Override
	public void handler() {
		log.info("base handler ...");
	}

	@Override
	public void onStop() {
		log.info("base on stop ...");
	}

	@Override
	public void caseException(Exception e) {
		log.info("base case exception {}", e.getLocalizedMessage());
	}
}
