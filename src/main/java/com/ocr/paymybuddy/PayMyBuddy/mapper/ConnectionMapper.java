package com.ocr.paymybuddy.PayMyBuddy.mapper;

import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.ConnectionDto;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConnectionMapper {

    public List<ConnectionDto> connectionDtoFromConnection(List<UserConnection> userConnections) {

        return userConnections.stream()
                .map(connection -> new ConnectionDto(connection.getToTargeted().getEmail()))
                .collect(Collectors.toList());
    }
}
