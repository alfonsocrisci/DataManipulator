<%--
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
--%>

<%@ include file="/html/init.jsp" %>

<%
String[] handlerTypes = new String[] {
	yg0r2.datamanipulator.annotation.HandlerType.CONTENT.toString(),
	yg0r2.datamanipulator.annotation.HandlerType.PLUGIN.toString()
};
%>

<%@ include file="/html/base_view.jsp" %>