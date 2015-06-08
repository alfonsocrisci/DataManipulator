/**
 * Copyright (c) 2014-present Yg0R2. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package yg0r2.datamanipulator.thread;

import javax.servlet.http.HttpSession;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionThreadLocal;

import yg0r2.datamanipulator.context.RequestContext;
import yg0r2.datamanipulator.handler.BaseHandler;
/**
 * @author Yg0R2
 */
public class HandlerThread extends Thread {

	public HandlerThread(
		ThreadGroup threadGroup, BaseHandler handler,
		RequestContext requestContext) {

		super(threadGroup, new HandlerThread(handler, requestContext));

		_handler = handler;
		_requestContext = requestContext;
	}

	public HandlerThread(BaseHandler handler, RequestContext requestContext) {
		_handler = handler;
		_requestContext = requestContext;
	}

	@Override
	public void run() {
		HttpSession httpSession = _requestContext.getSession();
		httpSession.setMaxInactiveInterval(-1);

		PermissionThreadLocal.setIndexEnabled(false);
		PermissionThreadLocal.setPermissionChecker(
			_requestContext.getPermissionChecker());

		PrincipalThreadLocal.setName(_requestContext.getUserId());

		PortalSessionThreadLocal.setHttpSession(httpSession);

		String threadName = this.getName();

		long startTime = System.currentTimeMillis();

		_log.info(
			threadName + " start add entries at " + String.valueOf(startTime) +
			StringPool.PERIOD);

		try {
			_handler.proceed(_requestContext);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		long endTime = System.currentTimeMillis();

		_log.info(
			threadName + " finished add entries at " + String.valueOf(endTime) +
			StringPool.PERIOD);

		double took = (endTime - startTime) / 1000;

		_log.info("The whole process took " + String.valueOf(took) + "s.");
	}

	private static Log _log = LogFactoryUtil.getLog(HandlerThread.class);

	private BaseHandler _handler;
	private RequestContext _requestContext;

}