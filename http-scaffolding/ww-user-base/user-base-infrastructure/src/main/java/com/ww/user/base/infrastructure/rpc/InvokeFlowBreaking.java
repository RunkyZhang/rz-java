package com.ww.user.base.infrastructure.rpc;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ww.common.base.BusinessException;

public class InvokeFlowBreaking {
    public static String getNameBroken(String name, BlockException blockException) {
        String invokePath = blockException.getRule().getResource();
        throw BusinessException.INVOKE_FLOW_BREAKING(invokePath, name, blockException);
    }
}
