import {decodeJWT, getAuthHeader, getCookie, redirectIfAuthenticated, setTokenToCookie} from "./utils";
import $ from "jquery";
import * as u from "./utils";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {baseUrl} from "./constants";
import * as c from "./constants";
import ErrorNotify from "./notifications/errorNotify";
import InfoNotify from "./notifications/infoNotify";

export function common(table) {
    const decoded  = decodeJWT(getCookie("access_token"));

    $("#login").text(decoded.payload.sub);
    $('#login').click(function () {
        setTokenToCookie('');
        window.location.href = '/login.html';
    });
    const isAdmin = decoded.payload.role === "ROLE_ADMIN";
    // toggle admin-only nav items
    const importNav = $('nav a[href="./import_history.html"]').closest('li');
    if (importNav.length) importNav.css('display', isAdmin ? 'list-item' : 'none');

    if (isAdmin) {
        $("#admin-requests-button").css("display", "block");
    } else {
        $("#send-application-for-admin").css("display", "block");

        $("#send-application-for-admin").on('click', function () {
            $.ajax({
                url: `${c.baseUrl}${c.apiUrl}/admin-requests`,
                type: "POST",
                headers: getAuthHeader(),
                success: (response) => {
                    new InfoNotify("Заявка успешно создана!")
                },
                error: (xhr) => {
                    if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
                    new ErrorNotify('Ошибка при создании заявки', xhr.responseText);
                },
            });
        })
    }

    connect(table);
}

function connect(table) {
    let socket = new SockJS(baseUrl + '/ws', null, {transports: ['websocket']});
    let stompClient = Stomp.over(socket);

    stompClient.connect(u.getAuthHeader(), function (frame) {
        console.log('Подключено: ' + frame);

        stompClient.subscribe('/topic/entities', function (messageOutput) {
            table.doRequest(
                window.sessionStorage.getItem("page"),
                window.sessionStorage.getItem("size"),
                window.sessionStorage.getItem("sortField"),
                window.sessionStorage.getItem("ascending"),
                window.sessionStorage.getItem("filter")
            );
        }, u.getAuthHeader());
    }, function(error) {
        console.error('Ошибка подключения:', error);
        connect(table);
    });
}
