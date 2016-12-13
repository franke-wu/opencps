package org.opencps.statisticsmgt.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.opencps.statisticsmgt.bean.DossierStatisticsBean;
import org.opencps.statisticsmgt.model.DossiersStatistics;
import org.opencps.statisticsmgt.model.impl.DossiersStatisticsImpl;
import org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil;
import org.opencps.statisticsmgt.service.persistence.DossiersStatisticsFinder;

import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class StatisticsUtil {

	public static final String RECEIVED = "received";
	public static final String FINISHED = "finished";
	public static final String PROCESSING = "processing";

	public static enum StatisticsFieldNumber {
		ReceivedNumber, OntimeNumber, OvertimeNumber, ProcessingNumber, DelayingNumber, RemainingNumber
	}

	/**
	 * @param field
	 * @param delayStatus
	 * @return
	 */
	public static String getFilterCondition(String field, int... delayStatus) {
		String filter = StringPool.BLANK;
		StatisticsFieldNumber fieldNumber = StatisticsFieldNumber
				.valueOf(field);
		switch (fieldNumber) {
		case ReceivedNumber:
			filter = CustomSQLUtil.get(DossiersStatisticsFinder.class.getName()
					+ StringPool.PERIOD + StringPool.OPEN_BRACKET + RECEIVED
					+ StringPool.CLOSE_BRACKET);
			break;
		case OntimeNumber:
			filter = CustomSQLUtil.get(DossiersStatisticsFinder.class.getName()
					+ StringPool.PERIOD + StringPool.OPEN_BRACKET + FINISHED
					+ StringPool.CLOSE_BRACKET);
			break;
		case OvertimeNumber:
			filter = CustomSQLUtil.get(DossiersStatisticsFinder.class.getName()
					+ StringPool.PERIOD + StringPool.OPEN_BRACKET + FINISHED
					+ StringPool.CLOSE_BRACKET);
			break;

		case ProcessingNumber:
			filter = CustomSQLUtil.get(DossiersStatisticsFinder.class.getName()
					+ StringPool.PERIOD + StringPool.OPEN_BRACKET + PROCESSING
					+ StringPool.CLOSE_BRACKET);
			break;
		case DelayingNumber:
			filter = CustomSQLUtil.get(DossiersStatisticsFinder.class.getName()
					+ StringPool.PERIOD + StringPool.OPEN_BRACKET + PROCESSING
					+ StringPool.CLOSE_BRACKET);
			break;

		default:
			break;
		}
		return filter;
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public static String getSetterMethodName(String fieldName) {
		String methodName = "set" + fieldName;
		return methodName;
	}

	/**
	 * @param q
	 * @param columnDataTypes
	 * @param cacheable
	 * @return
	 */
	public static SQLQuery bindingProperties(SQLQuery q,
			String[] columnDataTypes, boolean cacheable) {
		q.setCacheable(cacheable);

		if (columnDataTypes != null) {
			for (int i = 0; i < columnDataTypes.length; i++) {
				String columnName = "COL" + i;
				Type type = getDataType(columnDataTypes[i]);
				q.addScalar(columnName, type);
			}
		}

		return q;
	}

	/**
	 * @param typeLabel
	 * @return
	 */
	public static Type getDataType(String typeLabel) {

		return Type.valueOf(typeLabel.trim());
	}

	/**
	 * @param columnName
	 * @param coulmnDataType
	 * @param field
	 * @param delayStatus
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Method getMethod(String columnName, String coulmnDataType,
			String field, int... delayStatus) throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		String methodName = "set";
		columnName = columnName.trim();
		coulmnDataType = coulmnDataType.trim();

		columnName = columnName.substring(0,
				columnName.indexOf(StringPool.SPACE));

		if (columnName.contains("d.")) {
			String temp = columnName.substring(
					columnName.lastIndexOf(StringPool.PERIOD) + 1,
					columnName.length());
			temp = StringUtil.upperCaseFirstLetter(temp);
			methodName += "Domain" + temp;
		} else if (columnName.contains("g.")) {
			String temp = columnName.substring(
					columnName.lastIndexOf(StringPool.PERIOD) + 1,
					columnName.length());
			temp = StringUtil.upperCaseFirstLetter(temp);
			methodName += "Gov" + temp;

		} else if (columnName
				.contains("count(opencps_processorder.processOrderId)")) {
			methodName = getSetterMethodName(field);
		} else {
			String temp = columnName.substring(
					columnName.lastIndexOf(StringPool.PERIOD) + 1,
					columnName.length());
			temp = StringUtil.upperCaseFirstLetter(temp);
			methodName += temp;
		}

		Class<?> clazz = getClazz(coulmnDataType);

		Method method = null;
		try {
			if (clazz != null) {
				method = DossierStatisticsBean.class.getMethod(methodName,
						clazz);
			} else {
				method = DossierStatisticsBean.class.getMethod(methodName);
			}
		} catch (Exception e) {

		}

		return method;
	}

	/**
	 * @param typeLabel
	 * @return
	 */
	public static Class<?> getClazz(String typeLabel) {
		Class<?> clazz = null;
		Type type = Type.valueOf(typeLabel);
		switch (type) {

		case STRING:
			clazz = String.class;
			break;

		case LONG:
			clazz = long.class;
			break;

		case INTEGER:
			clazz = int.class;
			break;

		case DATE:
			clazz = Date.class;
			break;

		case SHORT:
			clazz = short.class;
			break;

		default:
			break;
		}
		return clazz;
	}

	public static DossierStatisticsBean fakeData(int month, int year, String domainCode, String govCode, String govTreeIndex){
		DossierStatisticsBean dossierStatisticsBean = new DossierStatisticsBean();
		dossierStatisticsBean.setMonth(month);
		dossierStatisticsBean.setYear(year);
		dossierStatisticsBean.setAdministrationLevel(-1);
		dossierStatisticsBean.setDelayingNumber(5);
		dossierStatisticsBean.setDomainItemCode(domainCode);
		dossierStatisticsBean.setGovItemCode(govCode);
		dossierStatisticsBean.setGovTreeIndex(govTreeIndex);
		dossierStatisticsBean.setOntimeNumber(5);
		dossierStatisticsBean.setOvertimeNumber(5);
		dossierStatisticsBean.setProcessingNumber(5);
		dossierStatisticsBean.setReceivedNumber(5);
		return dossierStatisticsBean;
	}

	/**
	 * @param map
	 * @return
	 */
	public static List<DossiersStatistics> getDossiersStatistics(List data) {
		List<DossiersStatistics> dossiersStatistics = new ArrayList<DossiersStatistics>();
		HashMap<String, DossiersStatistics> statisticMap = new HashMap<String, DossiersStatistics>();
		HashMap<String, DossiersStatistics> statisticGroupByDomainMap = new HashMap<String, DossiersStatistics>();
		HashMap<String, DossiersStatistics> statisticGroupByGovMap = new HashMap<String, DossiersStatistics>();
		HashMap<String, DossiersStatistics> statisticGovTreeIndexMap = new HashMap<String, DossiersStatistics>();
		HashMap<String, DossierStatisticsBean> beanMap = new HashMap<String, DossierStatisticsBean>();
		HashMap<String, Integer> remainingNumberMap = new HashMap<String, Integer>();

		if (data != null) {
			_log.info("###################################################Data "
					+ data.size());

			// fake data

			try {
				for (int i = 0; i < data.size(); i++) {
					DossierStatisticsBean statisticsBean = (DossierStatisticsBean) data
							.get(i);

					DossierStatisticsBean statisticsBeanTemp = new DossierStatisticsBean();

					DossiersStatistics dossiersStatisticsTemp = new DossiersStatisticsImpl();

					String key = statisticsBean.getMonth()
							+ StringPool.DASH
							+ statisticsBean.getYear()
							+ StringPool.DASH
							+ (Validator.isNotNull(statisticsBean
									.getGovItemCode()) ? statisticsBean
									.getGovItemCode() : StringPool.BLANK)
							+ StringPool.DASH
							+ (Validator.isNotNull(statisticsBean
									.getDomainItemCode()) ? statisticsBean
									.getDomainItemCode() : StringPool.BLANK)
							+ StringPool.DASH
							+ statisticsBean.getAdministrationLevel();

					_log.info("###################################################Key "
							+ key);

					if (beanMap != null && beanMap.containsKey(key)) {
						statisticsBeanTemp = beanMap.get(key);
					}

					if (statisticMap != null && statisticMap.containsKey(key)) {
						dossiersStatisticsTemp = statisticMap.get(key);
					}

					/*
					 * if (dossiersStatisticsTemp.getAdministrationLevel() >
					 * statisticsBean .getAdministrationLevel()) {
					 * dossiersStatisticsTemp
					 * .setAdministrationLevel(statisticsBean
					 * .getAdministrationLevel()); }
					 */

					// Create Group (domain, gov, index != 0)

					if (statisticsBean.getDelayingNumber() > 0) {
						dossiersStatisticsTemp.setDelayingNumber(statisticsBean
								.getDelayingNumber());

						statisticsBeanTemp.setDelayingNumber(statisticsBean
								.getDelayingNumber());
					}

					if (statisticsBean.getOntimeNumber() > 0) {
						dossiersStatisticsTemp.setOntimeNumber(statisticsBean
								.getOntimeNumber());

						statisticsBeanTemp.setOntimeNumber(statisticsBean
								.getOntimeNumber());
					}

					if (statisticsBean.getOvertimeNumber() > 0) {
						dossiersStatisticsTemp.setOvertimeNumber(statisticsBean
								.getOvertimeNumber());

						statisticsBeanTemp.setOvertimeNumber(statisticsBean
								.getOvertimeNumber());
					}

					if (statisticsBean.getProcessingNumber() > 0) {
						dossiersStatisticsTemp
								.setProcessingNumber(statisticsBean
										.getProcessingNumber());

						statisticsBeanTemp.setProcessingNumber(statisticsBean
								.getProcessingNumber());
					}

					if (statisticsBean.getReceivedNumber() > 0) {
						dossiersStatisticsTemp.setReceivedNumber(statisticsBean
								.getReceivedNumber());

						statisticsBeanTemp.setReceivedNumber(statisticsBean
								.getReceivedNumber());
					}

					if (Validator.isNotNull(statisticsBean.getDomainItemCode())) {
						dossiersStatisticsTemp.setDomainCode(statisticsBean
								.getDomainItemCode());
					}

					if (Validator.isNotNull(statisticsBean.getGovItemCode())) {
						dossiersStatisticsTemp.setGovAgencyCode(statisticsBean
								.getGovItemCode());
					}

					dossiersStatisticsTemp.setMonth(statisticsBean.getMonth());

					dossiersStatisticsTemp.setYear(statisticsBean.getYear());

					dossiersStatisticsTemp
							.setAdministrationLevel(statisticsBean
									.getAdministrationLevel());

					statisticsBeanTemp.setAdministrationLevel(statisticsBean
							.getAdministrationLevel());

					statisticsBeanTemp
							.setGovTreeIndex(Validator.isNotNull(statisticsBean
									.getGovTreeIndex()) ? statisticsBean
									.getGovTreeIndex() : StringPool.BLANK);

					statisticsBeanTemp
							.setDomainTreeIndex(Validator
									.isNotNull(statisticsBean
											.getDomainTreeIndex()) ? statisticsBean
									.getDomainTreeIndex() : StringPool.BLANK);

					_log.info("###################################################Key "
							+ key);

					// System.out.println(statisticsBean.getRemainingNumber());

					int remainingNumber = dossiersStatisticsTemp
							.getProcessingNumber()
							+ dossiersStatisticsTemp.getDelayingNumber()
							+ dossiersStatisticsTemp.getOntimeNumber()
							+ dossiersStatisticsTemp.getOvertimeNumber()
							- dossiersStatisticsTemp.getReceivedNumber();

					_log.info("###################################################remainingNumber "
							+ remainingNumber);

					dossiersStatisticsTemp.setRemainingNumber(remainingNumber);

					String remainingNumberKey = String
							.valueOf(dossiersStatisticsTemp.getMonth())
							+ StringPool.DASH
							+ dossiersStatisticsTemp.getYear();

					remainingNumberMap.put(remainingNumberKey, remainingNumber);

					statisticMap.put(key, dossiersStatisticsTemp);

					beanMap.put(key, statisticsBeanTemp);

				}

				_log.info("###################################################statisticMap "
						+ statisticMap.size());

				_log.info("###################################################beanMap "
						+ beanMap.size());

				// Create Groups (domain, 0, index = 0)

				if (statisticMap != null) {
					for (String key : statisticMap.keySet()) {

						_log.info("###################################################statisticMapKey "
								+ key);

						DossiersStatistics dossiersStatisticsTemp = statisticMap
								.get(key);

						_log.info("###################################################dossiersStatisticsTemp "
								+ dossiersStatisticsTemp.getDomainCode());

						String statisticGroupByDomainKey = dossiersStatisticsTemp
								.getDomainCode()
								+ StringPool.DASH
								+ dossiersStatisticsTemp.getMonth()
								+ StringPool.DASH
								+ dossiersStatisticsTemp.getYear();
						DossiersStatistics dossierStatistics = dossiersStatisticsTemp;

						if (statisticGroupByDomainMap != null
								&& statisticGroupByDomainMap
										.containsKey(statisticGroupByDomainKey)) {

							DossiersStatistics dossierStatisticsGroupByDomain = statisticGroupByDomainMap
									.get(statisticGroupByDomainKey);

							dossierStatistics.setAdministrationLevel(0);
							/*
							 * dossierStatistics
							 * .setDelayingNumber(dossierStatistics
							 * .getDelayingNumber() +
							 * dossierStatisticsGroupByDomain
							 * .getDelayingNumber());
							 */

							dossierStatistics
									.setDelayingNumber(dossierStatisticsGroupByDomain
											.getDelayingNumber());
							dossierStatistics
									.setGovAgencyCode(StringPool.BLANK);
							dossierStatistics.setOntimeNumber(dossierStatistics
									.getOntimeNumber()
									+ dossierStatisticsGroupByDomain
											.getOntimeNumber());

							dossierStatistics
									.setOvertimeNumber(dossierStatistics
											.getOvertimeNumber()
											+ dossierStatisticsGroupByDomain
													.getOvertimeNumber());
							/*
							 * dossierStatistics
							 * .setProcessingNumber(dossierStatistics
							 * .getProcessingNumber() +
							 * dossierStatisticsGroupByDomain
							 * .getProcessingNumber());
							 */

							dossierStatistics
									.setProcessingNumber(dossierStatisticsGroupByDomain
											.getProcessingNumber());

							dossierStatistics
									.setReceivedNumber(dossierStatistics
											.getReceivedNumber()
											+ dossierStatisticsGroupByDomain
													.getReceivedNumber());

							dossierStatistics
									.setRemainingNumber(dossierStatistics
											.getRemainingNumber()
											+ dossierStatisticsGroupByDomain
													.getRemainingNumber());

						}

						statisticGroupByDomainMap.put(
								statisticGroupByDomainKey, dossierStatistics);

						DossierStatisticsBean statisticsBean = beanMap.get(key);

						_log.info("###################################################statisticsBean.getGovTreeIndex() "
								+ statisticsBean.getGovTreeIndex());

						_log.info("###################################################statisticMap.get(key) "
								+ statisticMap.get(key)
										.getAdministrationLevel());

						String statisticGovTreeIndexKey = dossiersStatisticsTemp
								.getMonth()
								+ StringPool.DASH
								+ dossiersStatisticsTemp.getYear()
								+ StringPool.DASH
								+ (statisticsBean.getGovTreeIndex()
										.lastIndexOf(StringPool.PERIOD) + 1 == statisticsBean
										.getGovTreeIndex().length() ? statisticsBean
										.getGovTreeIndex() : statisticsBean
										.getGovTreeIndex() + StringPool.PERIOD);

						statisticGovTreeIndexMap.put(statisticGovTreeIndexKey,
								dossiersStatisticsTemp);

					}
				}

				if (statisticGovTreeIndexMap != null) {
					// Create Groups (domain, gov, index = 0)
					for (String treeIndex : statisticGovTreeIndexMap.keySet()) {
						DossiersStatistics dossierStatistics = statisticGovTreeIndexMap
								.get(treeIndex);
						for (String treeIndexTemp : statisticGovTreeIndexMap
								.keySet()) {
							if (Validator.isNotNull(treeIndexTemp)
									&& treeIndexTemp.contains(treeIndex)
									&& !treeIndexTemp.equals(treeIndex)) {
								DossiersStatistics dossierStatisticsTemp = statisticGovTreeIndexMap
										.get(treeIndex);
								dossierStatistics.setAdministrationLevel(0);
								dossierStatistics
										.setDelayingNumber(dossierStatistics
												.getDelayingNumber()
												+ dossierStatisticsTemp
														.getDelayingNumber());
								// dossierStatistics.setGovAgencyCode(StringPool.BLANK);
								dossierStatistics
										.setOntimeNumber(dossierStatistics
												.getOntimeNumber()
												+ dossierStatisticsTemp
														.getOntimeNumber());

								dossierStatistics
										.setOvertimeNumber(dossierStatistics
												.getOvertimeNumber()
												+ dossierStatisticsTemp
														.getOvertimeNumber());
								dossierStatistics
										.setProcessingNumber(dossierStatistics
												.getProcessingNumber()
												+ dossierStatisticsTemp
														.getProcessingNumber());
								dossierStatistics
										.setReceivedNumber(dossierStatistics
												.getReceivedNumber()
												+ dossierStatisticsTemp
														.getReceivedNumber());

								dossierStatistics
										.setRemainingNumber(dossierStatistics
												.getRemainingNumber()
												+ dossierStatisticsTemp
														.getRemainingNumber());

							}
						}

						String key = dossierStatistics.getMonth()
								+ StringPool.DASH
								+ dossierStatistics.getYear()
								+ StringPool.DASH
								+ (Validator.isNotNull(dossierStatistics
										.getGovAgencyCode()) ? dossierStatistics
										.getGovAgencyCode() : StringPool.BLANK)
								+ StringPool.DASH
								+ (Validator.isNotNull(dossierStatistics
										.getDomainCode()) ? dossierStatistics
										.getDomainCode() : StringPool.BLANK)
								+ StringPool.DASH
								+ dossierStatistics.getAdministrationLevel();
						statisticGroupByGovMap.put(key, dossierStatistics);
					}
				}

				// Insert DB

				for (String key : statisticMap.keySet()) {
					DossiersStatistics dossierStatistics = statisticMap
							.get(key);
					dossierStatistics = DossiersStatisticsLocalServiceUtil
							.addDossiersStatistics(
									dossierStatistics.getGroupId(),
									dossierStatistics.getCompanyId(),
									dossierStatistics.getUserId(),
									dossierStatistics.getRemainingNumber(),
									dossierStatistics.getReceivedNumber(),
									dossierStatistics.getOntimeNumber(),
									dossierStatistics.getOvertimeNumber(),
									dossierStatistics.getProcessingNumber(),
									dossierStatistics.getDelayingNumber(),
									dossierStatistics.getMonth(),
									dossierStatistics.getYear(),
									dossierStatistics.getGovAgencyCode(),
									dossierStatistics.getDomainCode(),
									dossierStatistics.getAdministrationLevel());

					dossiersStatistics.add(dossierStatistics);
				}

				for (String key : statisticGroupByDomainMap.keySet()) {
					DossiersStatistics dossierStatistics = statisticMap
							.get(key);
					dossierStatistics = DossiersStatisticsLocalServiceUtil
							.addDossiersStatistics(
									dossierStatistics.getGroupId(),
									dossierStatistics.getCompanyId(),
									dossierStatistics.getUserId(),
									dossierStatistics.getRemainingNumber(),
									dossierStatistics.getReceivedNumber(),
									dossierStatistics.getOntimeNumber(),
									dossierStatistics.getOvertimeNumber(),
									dossierStatistics.getProcessingNumber(),
									dossierStatistics.getDelayingNumber(),
									dossierStatistics.getMonth(),
									dossierStatistics.getYear(),
									dossierStatistics.getGovAgencyCode(),
									dossierStatistics.getDomainCode(),
									dossierStatistics.getAdministrationLevel());
					dossiersStatistics.add(dossierStatistics);
				}

				for (String key : statisticGroupByGovMap.keySet()) {
					DossiersStatistics dossierStatistics = statisticMap
							.get(key);
					dossierStatistics = DossiersStatisticsLocalServiceUtil
							.addDossiersStatistics(
									dossierStatistics.getGroupId(),
									dossierStatistics.getCompanyId(),
									dossierStatistics.getUserId(),
									dossierStatistics.getRemainingNumber(),
									dossierStatistics.getReceivedNumber(),
									dossierStatistics.getOntimeNumber(),
									dossierStatistics.getOvertimeNumber(),
									dossierStatistics.getProcessingNumber(),
									dossierStatistics.getDelayingNumber(),
									dossierStatistics.getMonth(),
									dossierStatistics.getYear(),
									dossierStatistics.getGovAgencyCode(),
									dossierStatistics.getDomainCode(),
									dossierStatistics.getAdministrationLevel());
					dossiersStatistics.add(dossierStatistics);
				}
			} catch (Exception e) {
				_log.error(e);
			}
		}

		return dossiersStatistics;
	}

	private static Log _log = LogFactoryUtil.getLog(StatisticsUtil.class
			.getName());

	/*
	 * public static void main(String[] args) { String tree1 = "1-2016-100.";
	 * String tree2 = "1-2016-100.102"; System.out.print(tree2.contains(tree1));
	 * System.out.print(tree1.lastIndexOf(StringPool.PERIOD));
	 * System.out.print(tree1.length()); }
	 */
}
