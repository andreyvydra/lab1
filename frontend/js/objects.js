import $ from 'jquery';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import * as c from './common/constants'
import * as u from './common/utils'
import {PaginationTable} from './common/pagination'
import '../css/main.css';
import 'simple-notify/dist/simple-notify.css'
import {decodeJWT, getCookie, redirectIfAuthenticated, setTokenToCookie} from "./common/utils";

document.addEventListener('DOMContentLoaded', function() {
	redirectIfAuthenticated();
});

$(document).ready(function() {
	const decoded  = decodeJWT(getCookie("access_token"));

	$("#login").text(decoded.payload.sub);
	$('#login').click(function () {
		setTokenToCookie(''); // Удаляем токен
		window.location.href = '/login.html'; // Перенаправляем на страницу входа
	});

	new PaginationTable("/location");

	var socket = new SockJS(c.baseUrl + '/ws', null, {transports: ['websocket']});
	var stompClient = Stomp.over(socket);

	// Функция для подключения к серверу
	function connect() {
		stompClient.connect(u.getAuthHeader(), function (frame) {
			console.log('Подключено: ' + frame);

			// Подписываемся на тему
			stompClient.subscribe('/topic/entities', function (messageOutput) {
				alert(messageOutput.body);
			}, u.getAuthHeader());
		}, function(error) {
			console.error('Ошибка подключения:', error);
		});
	}

	connect();

})