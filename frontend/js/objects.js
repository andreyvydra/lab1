import $ from 'jquery';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import * as c from './common/constants'
import * as u from './common/utils'
import {PaginationTable} from './common/pagination'
import '../css/main.css';
import 'simple-notify/dist/simple-notify.css'
import {decodeJWT, getCookie, redirectIfAuthenticated, setTokenToCookie} from "./common/utils";
import InfoNotify from "./common/notifications/infoNotify";
import {errorNotifies} from "./common/error";



export function main(url) {

	const decoded  = decodeJWT(getCookie("access_token"));

	$("#login").text(decoded.payload.sub);
	$('#login').click(function () {
		setTokenToCookie('');
		window.location.href = '/login.html';
	});

	new PaginationTable(url);

	var socket = new SockJS(c.baseUrl + '/ws', null, {transports: ['websocket']});
	var stompClient = Stomp.over(socket);

	function connect() {
		stompClient.connect(u.getAuthHeader(), function (frame) {
			console.log('Подключено: ' + frame);

			stompClient.subscribe('/topic/entities', function (messageOutput) {
				alert(messageOutput.body);
			}, u.getAuthHeader());
		}, function(error) {
			console.error('Ошибка подключения:', error);
		});
	}

	connect();

}

export function addObject(url, formData) {
	$.ajax({
		url: c.baseUrl + c.apiUrl + url,
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(formData),
		headers: u.getAuthHeader(),
		success: function (response) {
			new InfoNotify("Объект", "Успешно добавлен!");
		},
		error: function (xhr) {
			if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
			errorNotifies(xhr);
		}
	});
}