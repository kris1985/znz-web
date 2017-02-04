package com.znz.util;

/**
 * Created by Administrator on 2017/1/17.
 */
import com.aliyun.oss.ClientException;
import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.RequestSigner;
import com.aliyun.oss.common.auth.ServiceSignature;
import com.aliyun.oss.common.comm.RequestMessage;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.internal.SignUtils;

public class OSSRequestSigner implements RequestSigner {

    private String httpMethod;

    /* Note that resource path should not have been url-encoded. */
    private String resourcePath;
    private Credentials creds;

    public OSSRequestSigner(String httpMethod, String resourcePath, Credentials creds) {
        this.httpMethod = httpMethod;
        this.resourcePath = resourcePath;
        this.creds = creds;
    }

    @Override
    public void sign(RequestMessage request) throws ClientException {
        String accessKeyId = creds.getAccessKeyId();
        String secretAccessKey = creds.getSecretAccessKey();

        if (accessKeyId.length() > 0 && secretAccessKey.length() > 0) {
            String canonicalString = SignUtils.buildCanonicalString(httpMethod, resourcePath, request, null);
            String signature = ServiceSignature.create().computeSignature(secretAccessKey, canonicalString);
            request.addHeader(OSSHeaders.AUTHORIZATION, OSSUtils.composeRequestAuthorization(accessKeyId, signature));
        }
    }


}