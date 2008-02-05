/*
+---------------------------------------------------------------------------+
| Openads v2.5                                                              |
| ============                                                              |
|                                                                           |
| Copyright (c) 2003-2008 m3 Media Services Ltd                             |
| For contact details, see: http://www.openads.org/                         |
|                                                                           |
| This program is free software; you can redistribute it and/or modify      |
| it under the terms of the GNU General Public License as published by      |
| the Free Software Foundation; either version 2 of the License, or         |
| (at your option) any later version.                                       |
|                                                                           |
| This program is distributed in the hope that it will be useful,           |
| but WITHOUT ANY WARRANTY; without even the implied warranty of            |
| MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             |
| GNU General Public License for more details.                              |
|                                                                           |
| You should have received a copy of the GNU General Public License         |
| along with this program; if not, write to the Free Software               |
| Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA |
+---------------------------------------------------------------------------+
|  Copyright 2003-2007 Openads Limited                                      |
|                                                                           |
|  Licensed under the Apache License, Version 2.0 (the "License");          |
|  you may not use this file except in compliance with the License.         |
|  You may obtain a copy of the License at                                  |
|                                                                           |
|    http://www.apache.org/licenses/LICENSE-2.0                             |
|                                                                           |
|  Unless required by applicable law or agreed to in writing, software      |
|  distributed under the License is distributed on an "AS IS" BASIS,        |
|  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. |
|  See the License for the specific language governing permissions and      |
|  limitations under the License.                                           |
+---------------------------------------------------------------------------+
$Id:$
*/

package org.openads.campaign;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.openads.utils.DateUtils;
import org.openads.utils.ErrorMessage;
import org.openads.utils.TextUtils;

/**
 * Verify Modify Campaign method
 * 
 * @author <a href="mailto:apetlyovanyy@lohika.com">Andriy Petlyovanyy</a>
 */

public class TestModifyCampaign extends CampaignTestCase {
	private Integer campaignId = null;

	protected void setUp() throws Exception {
		super.setUp();

		campaignId = createCampaign();
	}

	protected void tearDown() throws Exception {
		deleteCampaign(campaignId);

		super.tearDown();
	}

	/**
	 * Execute test method with error
	 * 
	 * @param params -
	 *            parameters for test method
	 * @param errorMsg -
	 *            true error messages
	 * @throws MalformedURLException
	 */
	private void executeModifyCampaignWithError(Object[] params, String errorMsg)
			throws MalformedURLException {
		try {
			execute(MODIFY_CAMPAIGN_METHOD, params);
			fail(MODIFY_CAMPAIGN_METHOD
					+ " executed successfully, but it shouldn't.");
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE, errorMsg, e
					.getMessage());
		}
	}

	/**
	 * Test method with all required fields and some optional.
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyCampaignAllReqAndSomeOptionalFields()
			throws XmlRpcException, MalformedURLException {
		assertNotNull(advertiserId);
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(CAMPAIGN_ID, campaignId);
		struct.put(ADVERTISER_ID, advertiserId);
		struct.put(CAMPAIGN_NAME, "testCampaign Modified");
		struct.put(START_DATE, DateUtils.MIN_DATE_VALUE);
		struct.put(END_DATE, DateUtils.MAX_DATE_VALUE);
		Object[] params = new Object[] { sessionId, struct };
		final boolean result = (Boolean) execute(MODIFY_CAMPAIGN_METHOD, params);
		assertTrue(result);
	}

	/**
	 * Test method without some required fields.
	 * 
	 * @throws MalformedURLException
	 */
	public void testModifyCampaignWithoutSomeRequiredFields()
			throws MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(CAMPAIGN_NAME, "testCampaign Modified");
		struct.put(END_DATE, DateUtils.MAX_DATE_VALUE);
		Object[] params = new Object[] { sessionId, struct };

		executeModifyCampaignWithError(params, ErrorMessage.getMessage(
				ErrorMessage.FIELD_IN_STRUCTURE_DOES_NOT_EXISTS, CAMPAIGN_ID));
	}

	/**
	 * Test method with fields that has value greater than max.
	 * 
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
	public void testModifyCampaignGreaterThanMaxFieldValueError()
			throws MalformedURLException, XmlRpcException {
		final String strGreaterThan255 = TextUtils.getString(256);

		assertNotNull(campaignId);

		Map<String, Object> struct = new HashMap<String, Object>();

		struct.put(CAMPAIGN_ID, campaignId);
		struct.put(CAMPAIGN_NAME, strGreaterThan255);

		Object[] params = new Object[] { sessionId, struct };

		executeModifyCampaignWithError(params, ErrorMessage
				.getMessage(ErrorMessage.EXCEED_MAXIMUM_LENGTH_OF_FIELD, CAMPAIGN_NAME));
	}

	/**
	 * Test method with fields that has min. allowed values.
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyCampaignMinValues() throws XmlRpcException,
			MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(CAMPAIGN_ID, campaignId);
		struct.put(CAMPAIGN_NAME, "");
		struct.put(START_DATE, DateUtils.MIN_DATE_VALUE);
		struct.put(END_DATE, DateUtils.MIN_DATE_VALUE);
		Object[] params = new Object[] { sessionId, struct };
		final Boolean result = (Boolean) execute(MODIFY_CAMPAIGN_METHOD, params);
		assertTrue(result);
	}

	/**
	 * Test method with fields that has max. allowed values.
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyCampaignMaxValues() throws XmlRpcException,
			MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(CAMPAIGN_ID, campaignId);
		struct.put(CAMPAIGN_NAME, TextUtils.getString(255));
		struct.put(START_DATE, DateUtils.MAX_DATE_VALUE);
		struct.put(END_DATE, DateUtils.MAX_DATE_VALUE);
		Object[] params = new Object[] { sessionId, struct };
		final Boolean result = (Boolean) execute(MODIFY_CAMPAIGN_METHOD, params);
		assertTrue(result);
	}

	/**
	 * Try to modify campaign with unknown advertiser id
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyCampaignUnknownIdError() throws XmlRpcException,
			MalformedURLException {
		final Integer id = createAdvertiser();
		deleteAdvertiser(id);

		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(CAMPAIGN_ID, campaignId);
		struct.put(ADVERTISER_ID, id);
		Object[] params = new Object[] { sessionId, struct };

		executeModifyCampaignWithError(params, ErrorMessage.getMessage(
				ErrorMessage.UNKNOWN_ID_ERROR, ADVERTISER_ID));
	}

	/**
	 * Try to modify campaign with end date that is before start date
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyCampaignDateError() throws XmlRpcException,
			MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(CAMPAIGN_ID, campaignId);
		struct.put(START_DATE, DateUtils.MAX_DATE_VALUE);
		struct.put(END_DATE, DateUtils.MIN_DATE_VALUE);
		Object[] params = new Object[] { sessionId, struct };

		executeModifyCampaignWithError(params,
				ErrorMessage.START_DATE_IS_AFTER_END_DATE);
	}

	/**
	 * Try to modify campaign when the weight is set > 0 for high priority
	 * campaigns
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyCampaignPriorityError() throws XmlRpcException,
			MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(CAMPAIGN_ID, campaignId);
		struct.put(PRIORITY, 8);
		struct.put(WEIGHT, 2);
		Object[] params = new Object[] { sessionId, struct };

		executeModifyCampaignWithError(params,
				ErrorMessage.WEIGHT_COULD_NOT_BE_GREATER_THAN_ZERO);
	}

	/**
	 * Test method with fields that has value of wrong type (error).
	 * 
	 * @throws MalformedURLException
	 */
	public void testModifyCampaignWrongTypeError() throws MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(CAMPAIGN_ID, campaignId);
		Object[] params = new Object[] { sessionId, struct };

		struct.put(ADVERTISER_ID, TextUtils.NOT_INTEGER);
		executeModifyCampaignWithError(params, ErrorMessage.getMessage(
				ErrorMessage.FIELD_IS_NOT_INTEGER, ADVERTISER_ID));

		struct.remove(ADVERTISER_ID);
		struct.put(CAMPAIGN_NAME, TextUtils.NOT_STRING);
		executeModifyCampaignWithError(params, ErrorMessage.getMessage(
				ErrorMessage.FIELD_IS_NOT_STRING, CAMPAIGN_NAME));
	}
}
