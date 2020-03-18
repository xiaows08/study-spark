package com.xiaows.design.adapter;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/13 18:05
 * @version: v1.0
 */
public interface Handler {
	void onStart();

	void handler();

	void caseException(Exception e);

	void onStop();
}
