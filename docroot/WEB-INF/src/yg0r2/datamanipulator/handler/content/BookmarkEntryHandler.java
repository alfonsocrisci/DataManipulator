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

package yg0r2.datamanipulator.handler.content;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yg0r2.datamanipulator.context.RequestContext;
import yg0r2.datamanipulator.displayfield.DisplayFields;
import yg0r2.datamanipulator.displayfield.FieldKeys;
import yg0r2.datamanipulator.handler.BaseHandler;

/**
 * @author Yg0R2
 */
public class BookmarkEntryHandler extends BaseHandler {

	public static final String URL = "url";

	public BookmarkEntryHandler() {
		super("Bookmark Entry", "bookmark-entry");
	}

	@Override
	public DisplayFields getDisplayFields(long groupId) throws SystemException {
		DisplayFields displayFields = new DisplayFields();

		displayFields.addLabel(getDisplayFieldName());

		displayFields.addCount(getDisplayFieldName(FieldKeys.INPUT_COUNT));

		displayFields.addUpdateCount(
			getDisplayFieldName(FieldKeys.INPUT_UPDATE_COUNT));

		displayFields.add(
			getDisplayFieldName(URL), FieldKeys.INPUT,
			"http://www.liferay.com");

		return displayFields;
	}

	@Override
	protected Class<?>[] getAddArgClazzs() {
		return new Class<?>[] {
			Long.TYPE, Long.TYPE, Long.TYPE, String.class, String.class,
			String.class, ServiceContext.class
		};
	}

	@Override
	protected String[] getAddArgNames() {
		return new String[] {
			"userId", "groupId", "folderId", "name", "url", "description",
			"serviceContext"
		};
	}

	@Override
	protected Class<?> getAddClass() {
		return BookmarksEntryLocalServiceUtil.class;
	}

	@Override
	protected Map<String, Object> getAddEntrySpecifiedArgs(
		RequestContext requestContext) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>(1);

		args.put(URL, requestContext.getString(getDisplayFieldName(URL)));

		return args;
	}

	@Override
	protected String getAddMethodName() {
		return "addEntry";
	}

	@Override
	protected List<String> getChildHandlerNames() {
		return new ArrayList<String>();
	}

	@Override
	protected Object getClassPK(Object entry) {
		if (Validator.isNull(entry)) {
			return 0;
		}

		return ((BookmarksEntry)entry).getEntryId();
	}

	@Override
	protected String getClassPKName() {
		return "entryId";
	}

	@Override
	protected String getParentClassPKName() {
		return "folderId";
	}

	@Override
	protected Class<?>[] getUpdateArgClazzs() {
		return new Class<?>[] {
			Long.TYPE, Long.TYPE, Long.TYPE, Long.TYPE, String.class,
			String.class, String.class, ServiceContext.class
		};
	}

	@Override
	protected String[] getUpdateArgNames() {
		return new String[] {
			"userId", "entryId", "groupId", "folderId", "name", "url",
			"description", "serviceContext"
		};
	}

	@Override
	protected Class<?> getUpdateClass() {
		return BookmarksEntryLocalServiceUtil.class;
	}

	@Override
	protected Map<String, Object> getUpdateEntrySpecifiedArgs(
		Object entry, RequestContext requestContext) throws Exception {

		return new HashMap<String, Object>(0);
	}

	@Override
	protected String getUpdateMethodName() {
		return "updateEntry";
	}

}