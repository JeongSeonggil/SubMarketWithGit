package com.submarket.orderservice.service.impl;

import com.submarket.orderservice.dto.OrderDto;
import com.submarket.orderservice.mapper.impl.OrderMapper;
import com.submarket.orderservice.service.IOrderService;
import com.submarket.orderservice.util.DateUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service(value = "OrderService")
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderMapper orderMapper;

    @Override
    public int insertOrder(OrderDto orderDto) throws Exception {
        log.info(this.getClass().getName() + ".insertOrder Start!");

        orderDto.setOrderId(String.valueOf(UUID.randomUUID()));
        orderDto.setOrderDateDetails(String.valueOf(new Date()));
        orderDto.setOrderDate(DateUtil.getDateTime("yyyyMM"));

        String colNm = "OrderService";
        int res = orderMapper.insertOrder(orderDto, colNm);

        log.info(this.getClass().getName() + "insertOrder End!");
        return res;
    }

    @Override
    public List<OrderDto> findAllOrderByUserId(String userId) throws Exception {
        log.info(this.getClass().getName() + ".findAllOrderByUserId Start!");
        String colNm = "OrderService";

        List<OrderDto> orderDtoList = orderMapper.findOrderInfoByUserId(userId, colNm);


        log.info(this.getClass().getName() + ".findAllOrderByUserId Start!");

        return orderDtoList;
    }

    @Override
    public List<OrderDto> findAllOrderBySellerId(String sellerId) throws Exception {
        log.info(this.getClass().getName() + ".findAllOrderBySellerId Start!");
        String colNm = "OrderService";

        List<OrderDto> orderDtoList = orderMapper.findOrderInfoBySellerId(sellerId, colNm);


        log.info(this.getClass().getName() + ".findAllOrderBySellerId Start!");

        return orderDtoList;

    }

    @Override
    public OrderDto findOneOrderByOrderId(String orderId) throws Exception {
        log.info(this.getClass().getName() + ".findOneOrderByOrderId Start!");
        String colNm = "OrderService";

        OrderDto orderDto = orderMapper.findOrderInfoByOrderId(orderId, colNm);


        log.info(this.getClass().getName() + ".findOneOrderByOrderId Start!");

        return orderDto;
    }
}
