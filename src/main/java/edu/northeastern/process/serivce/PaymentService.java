package edu.northeastern.process.serivce;

/**
 * Created by Jim Z on 11/28/20 23:03
 */
public class PaymentService {
    public enum PayResult {
        SUCCESS,
        FAIL,
    }

    public PayResult makeAPayment(String orderId) {
        // retrieve order and see the order is already paid
        if (isPaid(orderId)) {
            return PayResult.SUCCESS;
        }
        // Approve current order from manager
        if (!approveOrder(orderId)) {
            updateOrder(orderId, "#updated Information");
            if (!approveOrder(orderId)) {
                return PayResult.FAIL;
            }
        }
        //
        if (!processPayment(orderId)) {
            // if is underpayment()

            // if is overcharge
            issueCreditAdjust(orderId);
            if (!processPayment(orderId)) {
                return PayResult.FAIL;
            }
        }
        // default return success
        return PayResult.SUCCESS;
    }

    private boolean isPaid(String orderId) {
        return false;
    }

    private boolean approveOrder(String orderId) {
        return true;
    }

    private boolean updateOrder(String orderId, String information) {
        return true;
    }

    private boolean processPayment(String orderId) {
        return true;
    }

    private boolean issueCreditAdjust(String orderId) {
        return true;
    }
}
