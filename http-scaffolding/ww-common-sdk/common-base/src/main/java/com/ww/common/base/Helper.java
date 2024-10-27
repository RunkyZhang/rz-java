package com.ww.common.base;

import com.ww.common.base.dto.RpcResult;

public class Helper {
    public static <T> T getResultData(RpcResult<T> result, boolean throwing) {
        if (null == result) {
            return null;
        }

        if (RpcResult.SUCCESS_CODE == result.getCode()) {
            return result.getData();
        }

        if (throwing) {
            throw BusinessException.FAILED_INVOKE_RPC_API_WITH_RESULT(result.getCode(), result.getMessage(), result.getDomain());
        }

        return null;
    }
}
