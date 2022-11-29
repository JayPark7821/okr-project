package kr.objet.okrproject.common.utils;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;

public class EnumLookUpUtil {
	public static <E extends Enum<E>> E lookup(Class<E> e, String id) {
		E result;
		try {
			result = Enum.valueOf(e, id);
		} catch (Exception exception) {
			throw new OkrApplicationException(ErrorCode.INVALID_JOB_DETAIL_FIELD);
		}
		return result;
	}
}