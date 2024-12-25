import $ from 'jquery';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import * as c from './common/constants'
import * as u from './common/utils'
import {paginationTable} from './common/pagination'
import '../css/main.css';
import 'simple-notify/dist/simple-notify.css'
import {getAuthHeader} from "./common/utils";
import {errorNotifies} from "./common/error";

$(document).ready(function() {

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

	connect()

})