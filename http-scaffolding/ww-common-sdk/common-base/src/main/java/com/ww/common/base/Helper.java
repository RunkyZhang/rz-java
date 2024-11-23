package com.ww.common.base;

import com.ww.common.base.dto.RpcResult;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Helper {
    private static List<String> ipV4s = null;

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

    public static String getIpV4() throws SocketException {
        String defaultIpV4 = "127.0.0.1";
        if (null != Helper.ipV4s) {
            return Helper.ipV4s.stream().filter(o -> !o.equals(defaultIpV4)).findFirst().orElse(defaultIpV4);
        }

        List<String> hostAddresses = new ArrayList<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress inetAddress;
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                inetAddress = inetAddresses.nextElement();
                if (inetAddress instanceof Inet4Address) {
                    hostAddresses.add(inetAddress.getHostAddress());
                }
            }
        }
        Helper.ipV4s = hostAddresses;

        return Helper.ipV4s.stream().filter(o -> !o.equals(defaultIpV4)).findFirst().orElse(defaultIpV4);
    }
}
