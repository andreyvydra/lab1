import {decodeJWT, getCookie, setTokenToCookie} from "./utils";
import $ from "jquery";
import * as u from "./utils";
import SockJS from "sockjs-client";
import * as c from "./constants";
import Stomp from "stompjs";

export function common(table) {
    const decoded  = decodeJWT(getCookie("access_token"));

    $("#login").text(decoded.payload.sub);
    $('#login').click(function () {
        setTokenToCookie('');
        window.location.href = '/login.html';
    });

    connect(table);
}

function connect(table) {
    let socket = new SockJS(c.baseUrl + '/ws', null, {transports: ['websocket']});
    let stompClient = Stomp.over(socket);

    stompClient.connect(u.getAuthHeader(), function (frame) {
        console.log('Подключено: ' + frame);

        stompClient.subscribe('/topic/entities', function (messageOutput) {
            table.doRequest(
                parseInt(window.localStorage.getItem("page")),
                parseInt(window.localStorage.getItem("size"))
            );
        }, u.getAuthHeader());
    }, function(error) {
        console.error('Ошибка подключения:', error);
    });
}
