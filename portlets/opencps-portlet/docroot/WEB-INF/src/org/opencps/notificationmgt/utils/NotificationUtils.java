/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.notificationmgt.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.portlet.WindowState;

import org.opencps.backend.message.SendToBackOfficeMsg;
import org.opencps.dossiermgt.search.DossierDisplayTerms;
import org.opencps.notificationmgt.engine.UserNotificationHandler;
import org.opencps.notificationmgt.message.SendNotificationMessage;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.search.PaymentFileDisplayTerms;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.util.MessageBusKeys;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.SendMailUtils;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutFriendlyURLLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserNotificationEventLocalServiceUtil;

/**
 * @author nhanhoang
 */

public class NotificationUtils {

	private static Log _log = LogFactoryUtil.getLog(NotificationUtils.class);

	public static void addUserNotificationEvent(
		SendNotificationMessage message, JSONObject payloadJSON, long userIdDelivery) {

		try {

			ServiceContext serviceContext = new ServiceContext();

			UserNotificationEventLocalServiceUtil.addUserNotificationEvent(
				userIdDelivery, UserNotificationHandler.PORTLET_ID, (new Date()).getTime(), 0,
				payloadJSON.toString(), false, serviceContext);

		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	public static JSONObject createNotification(
		SendNotificationMessage message, String event, String group, long userIdDelivery,
		boolean privatePage) {

		
		JSONObject payloadJSONObject = JSONFactoryUtil.createJSONObject();
		Locale locale = new Locale("vi", "VN");

		try {

			ProcessOrder processOrder =
				ProcessOrderLocalServiceUtil.getProcessOrder(message.getProcessOrderId());

			String title = StringPool.BLANK;
			title = LanguageUtil.get(locale, event) + "[" + processOrder.getDossierId() + "]";

			long plId = 0;

			
			
			_log.info("GROUPPPPPPPPPPPPPPPPPPPPPPPPPP+" + group);
			
			
			
			if (group.equals(NotificationEventKeys.GROUP1)) {
				
				try {
					plId = LayoutLocalServiceUtil.getFriendlyURLLayout(20182, true, group).getPlid();
                }
                catch (Exception e) {
	                // TODO: handle exception
                }
				
				

				payloadJSONObject.put("processOrderId", message.getProcessOrderId());
				

			}
			else if (group.equals(NotificationEventKeys.GROUP2)) {
				try {
					plId = LayoutLocalServiceUtil.getFriendlyURLLayout(20182, false, group).getPlid();
                }
                catch (Exception e) {
	                // TODO: handle exception
                }
				
				

				payloadJSONObject.put("dossierId", message.getDossierId());
				

			}
			else if (group.equals(NotificationEventKeys.GROUP3)) {
				
				try {
					plId = LayoutLocalServiceUtil.getFriendlyURLLayout(20182, true, group).getPlid();
                }
                catch (Exception e) {
	                // TODO: handle exception
                }

				

				payloadJSONObject.put("paymentFileId", message.getPaymentFileId());
				

			}
			else if (group.equals(NotificationEventKeys.GROUP4)) {

				try {
					plId = LayoutLocalServiceUtil.getFriendlyURLLayout(20182, true, group).getPlid();
                }
                catch (Exception e) {
	                // TODO: handle exception
                }


				

				payloadJSONObject.put("paymentFileId", message.getPaymentFileId());
				

			}
			payloadJSONObject.put("userIdDelivery", userIdDelivery);
			payloadJSONObject.put("title", title);
			payloadJSONObject.put("notificationText", message.getNotificationContent());
			payloadJSONObject.put("plId", plId);
			payloadJSONObject.put("friendlyUrl", group);

			

		}
		catch (Exception e) {
			_log.error(e);
		}

		return payloadJSONObject;
	}

	public static void sendEmailNotification(SendNotificationMessage message, String email) {

		String from = StringPool.BLANK;
		String to = StringPool.BLANK;
		String subject = StringPool.BLANK;
		String body = StringPool.BLANK;
		boolean htmlFormat = true;

		Locale locale = new Locale("vi", "VN");

		from = PortletPropsValues.SYSTEM_EMAIL;
		to = email;
		subject = PortletPropsValues.SUBJECT_TO_CUSTOMER;
		body = PortletPropsValues.CONTENT_TO_CUSTOMER;

		body = StringUtil.replace(body, "{receptionNo}", String.valueOf(message.getDossierId()));
		body =
			StringUtil.replace(
				body, "{event}", LanguageUtil.get(locale, message.getNotificationEventName()));

		SendMailUtils.sendEmail(from, to, subject, body, htmlFormat);
	}

	public static void triggerNotfication(SendToBackOfficeMsg message) {

		String event = message.getDossierStatus();
		Message commonMessage = new Message();
		List<SendNotificationMessage> notificationList = new ArrayList<SendNotificationMessage>();

		SendNotificationMessage notification = new SendNotificationMessage();

		if (event.equals(PortletConstants.DOSSIER_STATUS_NEW)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_RECEIVING)) {

			notification.setNotificationEventName(NotificationEventKeys.OFFICIALS.EVENT1);

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_PAYING)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_DENIED)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_RECEIVED)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_PROCESSING)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_CANCELED)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_DONE)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_ARCHIVED)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_ENDED)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_SYSTEM)) {

		}
		if (event.equals(PortletConstants.DOSSIER_STATUS_ERROR)) {
		}

		notificationList.add(notification);

		commonMessage.put(MessageBusKeys.Message.NOTIFICATIONS, notificationList);

		MessageBusUtil.sendMessage(MessageBusKeys.Destination.NOTIFICATIONS, commonMessage);

	}

}