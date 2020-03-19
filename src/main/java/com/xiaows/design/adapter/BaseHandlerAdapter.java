package com.xiaows.design.adapter;

import lombok.extern.slf4j.Slf4j;

/**
 * 当不需要全部实现接口提供的方法时，先设计一个实现某接口的类，并为该接口中的每个方法提供一个默认实现，
 * 当子类不想使用所有方法的情况下，就可以选择性的覆盖父类方法完成需求。
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/13 18:06
 * @version: v1.0
 */
@Slf4j
public abstract class BaseHandlerAdapter implements Handler {
	public BaseHandlerAdapter() {
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
