package com.xiaows.design.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/13 18:07
 * @version: v1.0
 */
public class Model {
	private List<Handler> handlers = new ArrayList<>();

	public Model addHandler(Handler handler) {
		this.handlers.add(handler);
		return this;
	}

	public void start() {
		checkHandler();
		for (Handler handler : this.handlers) {
			try {
				handler.handler();
			} catch (Exception e) {
				handler.caseException(e);
			} finally {
				handler.onStop();
			}
		}
	}

	private void checkHandler() {
		if (this.handlers.size() == 0) {
			throw new RuntimeException("handler must be specified.");
		}
	}

	public static void main(String[] args) {
		new Model()
				.addHandler(new CustomerHandler())
				.start();
	}
}
