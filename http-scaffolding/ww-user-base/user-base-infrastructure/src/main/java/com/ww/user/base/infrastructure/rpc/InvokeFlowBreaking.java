package com.ww.user.base.infrastructure.rpc;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ww.common.base.BusinessException;
import com.ww.common.base.dto.RpcResult;

public class InvokeFlowBreaking {
    public static RpcResult<String> getNameBroken(String name, BlockException blockException) {
        String invokePath = blockException.getRule().getResource();
        throw BusinessException.INVOKE_FLOW_BREAKING(invokePath, name, blockException);
    }
}
