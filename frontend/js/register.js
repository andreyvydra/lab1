import '../css/main.css';
import 'simple-notify/dist/simple-notify.css'
import $ from "jquery";
import * as c from './common/constants'
import * as u from './common/utils'
import {errorNotifies} from './common/error'
import {redirectIfAuthenticated} from "./common/utils";
import ErrorNotify from "./common/notifications/errorNotify";


$(document).ready(function() {
    redirectIfAuthenticated();
})

// Функция для авторизации пользователя
window.registerUser = function(event) {

    event.preventDefault();
    const username = $('#username').val();
    const password = $('#password').val();

    $.ajax({
        url: c.baseUrl + c.authUrl + '/signup',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ username, password }),
        success: function(response) {
            if (response.token === "") {
                new ErrorNotify(
                    response.message,
                    ""
                )
            }
            u.setTokenToCookie(response.token);
            redirectIfAuthenticated();
        },
        error: function(error) {
            errorNotifies(error);
        }
    });
};
