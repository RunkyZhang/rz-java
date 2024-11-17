package com.ww.user.base.interfaces.controller;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ww.common.base.BusinessException;
import com.ww.common.base.dto.RpcResult;

public class ApiFlowLimiting {
    public static RpcResult<String> getConfigLimited(String name, BlockException blockException) {
        String apiPath = blockException.getRule().getResource();
        throw BusinessException.API_FLOW_LIMITING(apiPath, name, blockException);
    }
}
