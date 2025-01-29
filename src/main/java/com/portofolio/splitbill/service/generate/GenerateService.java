package com.portofolio.splitbill.service.generate;

public interface GenerateService {
    public boolean verifySignature(String secret, String code, String signature);
}
