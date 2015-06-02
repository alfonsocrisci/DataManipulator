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

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.messageboards.model.MBMessage;

import java.util.ArrayList;
import java.util.List;

import yg0r2.datamanipulator.displayfield.DisplayFields;
import yg0r2.datamanipulator.displayfield.FieldKeys;

/**
 * @author Yg0R2
 */
public class MBThreadHandler extends MBMessageHandled {

	public MBThreadHandler() throws Exception {
		super();

		setDisplayFieldName("message-board-thread");
		setEntryName("Message Board Thread");
	}

	@Override
	public DisplayFields getDisplayFields(long groupId) throws Exception {
		DisplayFields displayFields = new DisplayFields();

		displayFields.addLabel(getDisplayFieldName());

		displayFields.addCount(getDisplayFieldName(FieldKeys.INPUT_COUNT));

		displayFields.addUpdateCount(
			getDisplayFieldName(FieldKeys.INPUT_UPDATE_COUNT));

		displayFields.addSeparator("");

		displayFields.addAll(
			(new MBMessageHandled()).getDisplayFields(groupId));

		return displayFields;
	}

	@Override
	protected List<String> getChildHandlerNames() {
		List<String> childHandlerNames = new ArrayList<String>();

		childHandlerNames.add(MBMessageHandled.class.getSimpleName());

		return childHandlerNames;
	}

	@Override
	protected Object getClassPK(Object entry) {
		if (Validator.isNull(entry)) {
			return 0;
		}

		return ((MBMessage)entry).getThreadId();
	}

	@Override
	protected String getClassPKName() {
		return "threadId";
	}

	@Override
	protected String getParentClassPKName() {
		return "categoryId";
	}

}