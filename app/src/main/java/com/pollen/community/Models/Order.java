package com.pollen.community.Models;

public class Order {

    private long OrderId;
    private int OrderStatus;
    private int Orderprice;
    private String OrderDetail, OrderComment, OrderAddress, UserPhone;


    public Order() {
    }


    public long getOrderId() {
        return OrderId;
    }

    public void setOrderId(long orderId) {
        OrderId = orderId;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public int getOrderprice() {
        return Orderprice;
    }

    public void setOrderprice(int orderprice) {
        Orderprice = orderprice;
    }

    public String getOrderDetail() {
        return OrderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        OrderDetail = orderDetail;
    }

    public String getOrderComment() {
        return OrderComment;
    }

    public void setOrderComment(String orderComment) {
        OrderComment = orderComment;
    }

    public String getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        OrderAddress = orderAddress;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
