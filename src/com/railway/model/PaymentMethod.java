package com.railway.model;

public class PaymentMethod {
    private int paymentMethodId;
    private String methodName;

    public PaymentMethod() {
    }

    public PaymentMethod(int paymentMethodId, String methodName) {
        this.paymentMethodId = paymentMethodId;
        this.methodName = methodName;
    }

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "paymentMethodId=" + paymentMethodId +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
