import '../css/main.css';
import 'simple-notify/dist/simple-notify.css'
import $ from "jquery";
import * as c from './common/constants'
import * as u from './common/utils'
import {errorNotifies} from './common/error'


// Функция для авторизации пользователя
window.loginUser = function(event) {
    event.preventDefault();
    const username = $('#username').val();
    const password = $('#password').val();

    $.ajax({
        url: c.baseUrl + c.authUrl + '/signin',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ username, password }),
        success: function(response) {
            u.setTokenToCookie(response.token);
            u.redirect('object-list.html')
        },
        error: function(error) {
            errorNotifies(error);
        }
    });
};
