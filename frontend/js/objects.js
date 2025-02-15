import $ from 'jquery';
import * as c from './common/constants'
import * as u from './common/utils'
import '../css/main.css';
import 'simple-notify/dist/simple-notify.css'
import {redirectIfAuthenticated} from "./common/utils";
import InfoNotify from "./common/notifications/infoNotify";
import {errorNotifies} from "./common/error";


export function addObject(url, formData) {
	console.log("AddObject", formData)
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

export function putObject(url, id, formData) {
	console.log("PutObject", formData)

	$.ajax({
		url: c.baseUrl + c.apiUrl + url + '/' + id + '/change',
		type: 'PUT',
		contentType: 'application/json',
		data: JSON.stringify(formData),
		headers: u.getAuthHeader(),
		success: function (response) {
			new InfoNotify("Объект", "Успешно обновлен!");
			const modal = $('#update-modal');
			modal.css("display", "none")
		},
		error: function (xhr) {
			if (xhr.status === 403 || xhr.status === 401) redirectIfAuthenticated();
			errorNotifies(xhr);
		}
	});
}