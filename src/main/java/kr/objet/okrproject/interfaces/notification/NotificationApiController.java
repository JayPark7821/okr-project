package kr.objet.okrproject.interfaces.notification;

import kr.objet.okrproject.common.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationApiController {

    private final NotificationFacade notificationFacade;

    @GetMapping
    public ResponseEntity<Response<NotificationDto.Response>> getNotifications(Authorization authorization) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationFacade.getNotifications(userUtils.getUserByEmail(request)));
    }


}
