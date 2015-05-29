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

import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.ServiceContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.bean.BeanUtil;

import yg0r2.datamanipulator.annotation.Handler;
import yg0r2.datamanipulator.annotation.HandlerType;
import yg0r2.datamanipulator.context.RequestContext;
import yg0r2.datamanipulator.displayfield.DisplayFields;
import yg0r2.datamanipulator.displayfield.FieldKeys;
import yg0r2.datamanipulator.handler.BaseHandler;
import yg0r2.datamanipulator.util.GetterUtil;

/**
 * @author Yg0R2
 */
@Handler(type = HandlerType.CONTENT, displayName = "Bookmarks Handler")
public class BookmarkFolderHandler extends BaseHandler {

	public BookmarkFolderHandler() throws Exception {
		super("Bookmark Folder", "bookmark-folder");
	}

	@Override
	public DisplayFields getDisplayFields(long groupId) throws Exception {
		DisplayFields displayFields = new DisplayFields();

		displayFields.addUserMultiSelect(FieldKeys.MULTI_SELECT_USER_LIST);
		displayFields.addInfo(
			getDisplayFieldName(FieldKeys.MULTI_SELECT_USER_LIST));

		displayFields.addSeparator("");

		displayFields.addSelectList(
			getDisplayFieldName(FieldKeys.SELECT_PARENT_LIST),
			_getFolderNameIdPairs(groupId));

		displayFields.addSeparator("");

		displayFields.addLabel(getDisplayFieldName());

		displayFields.addCount(getDisplayFieldName(FieldKeys.INPUT_COUNT));

		displayFields.addUpdateCount(
			getDisplayFieldName(FieldKeys.INPUT_UPDATE_COUNT));

		displayFields.addDepth(getDisplayFieldName(FieldKeys.INPUT_DEPTH));

		displayFields.addSubCount(
			getDisplayFieldName(FieldKeys.INPUT_SUBCOUNT));

		displayFields.addSeparator("");

		displayFields.addAll(
			(new BookmarkEntryHandler()).getDisplayFields(groupId));

		return displayFields;
	}

	@Override
	protected Class<?>[] getAddArgClazzs() {
		return new Class<?>[] {
			Long.TYPE, Long.TYPE, String.class, String.class,
			ServiceContext.class
		};
	}

	@Override
	protected String[] getAddArgNames() {
		return new String[] {
			"userId", "parentFolderId", "name", "description", "serviceContext"
		};
	}

	@Override
	protected Class<?> getAddClass() throws ClassNotFoundException {
		return _getServiceUtilClass();
	}

	@Override
	protected Map<String, Object> getAddEntrySpecifiedArgs(
		RequestContext requestContext) throws Exception {

		return  new HashMap<String, Object>(0);
	}

	@Override
	protected String getAddMethodName() {
		return "addFolder";
	}

	@Override
	protected List<String> getChildHandlerNames() {
		List<String> childHandlerNames = new ArrayList<String>();

		childHandlerNames.add(BookmarkEntryHandler.class.getSimpleName());

		return childHandlerNames;
	}

	@Override
	protected String getClassPKName() {
		return "folderId";
	}

	@Override
	protected String getParentClassPKName() {
		return "parentFolderId";
	}

	@Override
	protected Class<?>[] getUpdateArgClazzs() {
		if (buildNumber < 6200) {
			return new Class<?>[] {
				Long.TYPE, Long.TYPE, String.class, String.class, Boolean.TYPE,
				ServiceContext.class
			};
		}

		return new Class<?>[] {
			Long.TYPE, Long.TYPE, Long.TYPE, String.class, String.class,
			Boolean.TYPE, ServiceContext.class
		};
	}

	@Override
	protected String[] getUpdateArgNames() {
		if (buildNumber < 6200) {
			return new String[] {
				"folderId", "parentFolderId", "name", "description",
				"mergeWithParentFolder", "serviceContext"
			};
		}

		return new String[] {
			"userId", "folderId", "parentFolderId", "name", "description",
			"mergeWithParentFolder", "serviceContext"
		};
	}

	@Override
	protected Class<?> getUpdateClass() throws ClassNotFoundException {
		return _getServiceUtilClass();
	}

	@Override
	protected Map<String, Object> getUpdateEntrySpecifiedArgs(
		Object entry, RequestContext requestContext) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>(1);

		args.put("mergeWithParentFolder", false);

		return args;
	}

	@Override
	protected String getUpdateMethodName() {
		return "updateFolder";
	}

	private List<KeyValuePair> _getFolderNameIdPairs(long groupId)
		throws Exception {

		Class<?> bookmarkFolderClass = _getBookmarkFolderClass();

		Class<?> bookmarkFolderServiceUtil = _getServiceUtilClass();

		Method getFoldersMethod = bookmarkFolderServiceUtil.getDeclaredMethod(
			"getFolders", new Class<?>[] {Long.TYPE, Long.TYPE});

		List<?> folders = (List<?>) getFoldersMethod.invoke(
			bookmarkFolderServiceUtil, new Object[] {groupId, 0L});

		List<KeyValuePair> folderNameIdPairs = new ArrayList<KeyValuePair>();

		folderNameIdPairs.add(new KeyValuePair("", "0"));

		for (Object folder : folders) {
			String folderId = String.valueOf(
				BeanUtil.getDeclaredProperty(folder, getClassPKName()));

			String folderName = (String) BeanUtil.getDeclaredProperty(
				folder, "name");

			try {
				Method method = bookmarkFolderClass.getMethod(
					"getStatus", new Class<?>[0]);

				Object status = method.invoke(
					bookmarkFolderClass, new Object[0]);

				if ((Integer)status != WorkflowConstants.STATUS_APPROVED) {
					continue;
				}
			}
			catch (Exception e) {
			}

			folderNameIdPairs.add(new KeyValuePair(folderName, folderId));
		}

		return folderNameIdPairs;
	}

	private Class<?> _getBookmarkFolderClass() throws ClassNotFoundException {
		String[] classNames = new String[] {
			"com.liferay.portlet.bookmarks.model.BookmarksFolder",
			"com.liferay.bookmarks.model.BookmarksFolder"
		};

		return GetterUtil.getClass(classNames);
	}

	private Class<?> _getServiceUtilClass() throws ClassNotFoundException {
		String[] classNames = new String[] {
			"com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil",
			"com.liferay.bookmarks.service.BookmarksFolderLocalServiceUtil"
		};

		return GetterUtil.getClass(classNames);
	}

}